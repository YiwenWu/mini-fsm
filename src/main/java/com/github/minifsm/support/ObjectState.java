package com.github.minifsm.support;


import com.github.minifsm.ActionResult;
import com.github.minifsm.State;
import com.github.minifsm.StateContext;
import com.github.minifsm.StateMachine;
import com.github.minifsm.StateMachineEventResult;
import com.github.minifsm.model.Message;
import com.github.minifsm.model.TransitionData;
import com.google.common.base.Stopwatch;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import static com.github.minifsm.StateMachineEventResult.ResultType.ACCEPTED;
import static com.github.minifsm.StateMachineEventResult.ResultType.DENIED;
import static com.google.common.collect.Iterables.isEmpty;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ObjectState<S, E> implements State<S, E> {


  private final StateMachine<S, E> stateMachine;
  private final TransitionData<S, E> transition;


  public ObjectState(StateMachine<S, E> stateMachine, TransitionData<S, E> transition) {
    this.stateMachine = stateMachine;
    this.transition = transition;
  }

  @Override
  public StateMachineEventResult<S, E> sendEvent(Message<S, E> event) {
    return transit(event);
  }

  private StateMachineEventResult<S, E> transit(Message<S, E> message) {
    Scheduler scheduler = Schedulers.parallel();
    Stopwatch watch = Stopwatch.createStarted();
    ObjectStateContext<S, E> context = new ObjectStateContext<>(message, stateMachine);
    Boolean isAdopted = executeGuard(context, transition).block();
    if (Boolean.FALSE.equals(isAdopted)) {
      return StateMachineEventResult.of(message, DENIED, new RuntimeException("guard denied"));
    }
    S state = transition.getTarget() != null ? transition.getTarget() : transition.getSource();
    Collection<Function<StateContext<S, E>, Mono<ActionResult>>> actions = transition.getActions();
    if (actions == null || actions.isEmpty()) {
      return StateMachineEventResult.of(message, ACCEPTED, state, 0L, null);
    }
    //one transition can have multiple actions, do the action async parallel
    Flux<ActionResult> actionFlux = Flux.fromIterable(actions)
        .flatMap(action ->
            Mono.defer((Supplier<Mono<ActionResult>>) () -> {
                  Stopwatch stopwatch = Stopwatch.createStarted();
                  Mono<ActionResult> apply = action.apply(context);
                  ActionResult actionResult = apply.block();
                  if (actionResult == null) {
                    actionResult = ActionResult.of();
                  }
                  actionResult.setElapsedTime(stopwatch.elapsed(MILLISECONDS));
                  return Mono.just(actionResult);
                })
                .subscribeOn(scheduler)
                .onErrorResume(e -> {
                  e.printStackTrace();
                  return Mono.just(ActionResult.of(e));
                }).onErrorStop());
    //block all action finished
    List<ActionResult> actionResults;
    try {
      actionResults = actionFlux.collectList().toFuture().get();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    //List<ActionResult> actionResults = actionFlux.collectList().block();
    if (isEmpty(actionResults)) {
      return StateMachineEventResult.of(message, DENIED,
          new RuntimeException("action result is empty"));
    }
    Optional<ActionResult> error = actionResults.stream()
        .filter(action -> action.getThrowable() != null).findAny();
    if (error.isPresent()) {
      return StateMachineEventResult.of(message, DENIED, error.get().getThrowable());
    }
    long elapsed = watch.elapsed(MILLISECONDS);
    return StateMachineEventResult.of(message, ACCEPTED, state, elapsed, actionResults);
  }


  private Mono<Boolean> executeGuard(ObjectStateContext<S, E> context,
      TransitionData<S, E> transition) {
    if (transition.getGuard() == null) {
      return Mono.just(true);
    }
    return transition.getGuard().apply(context)
        .doOnError(Throwable::printStackTrace)
        .onErrorReturn(false);
  }
}

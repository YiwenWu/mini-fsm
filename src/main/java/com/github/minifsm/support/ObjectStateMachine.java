package com.github.minifsm.support;


import org.apache.commons.lang3.tuple.Pair;

import com.github.minifsm.State;
import com.github.minifsm.StateMachine;
import com.github.minifsm.StateMachineEventResult;
import com.github.minifsm.model.Message;
import com.github.minifsm.model.TransitionData;
import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import static com.github.minifsm.StateMachineEventResult.ResultType.DENIED;


public class ObjectStateMachine<S, E> implements StateMachine<S, E> {

  private final String id;
  private final Map<Pair<S, E>, TransitionData<S, E>> transitionDataMap;


  public ObjectStateMachine(String id, Collection<TransitionData<S, E>> transitions) {
    this.id = id;
    this.transitionDataMap = initTransMap(transitions);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public StateMachineEventResult<S, E> sendEvent(Message<S, E> message) {
    return acceptEvent(message);
  }

  @Override
  public List<StateMachineEventResult<S, E>> sendEvents(List<Message<S, E>> messages) {
    return handleEvents(messages, false);
  }

  @Override
  public List<StateMachineEventResult<S, E>> sendEvents(List<Message<S, E>> messages,
      boolean async) {
    return handleEvents(messages, async);
  }

  /**
   * 响应式多Message发送
   *
   * @param messages
   * @param async
   * @return
   */
  private List<StateMachineEventResult<S, E>> handleEvents(List<Message<S, E>> messages,
      boolean async) {
    Scheduler parallel = Schedulers.parallel();
    Flux<StateMachineEventResult<S, E>> resultFlux = Flux.fromIterable(messages)
        .flatMap(message -> {
          Mono<StateMachineEventResult<S, E>> mono =
              Mono.defer(() -> Mono.just(acceptEvent(message)))
                  .onErrorResume(error -> {
                    return Mono.just(StateMachineEventResult.of(message, DENIED, error));
                  });
          return async ? mono.subscribeOn(parallel) : mono;
        });
    return resultFlux.collectList().block();
  }

  /**
   * message执行
   *
   * @param message
   * @return
   */
  private StateMachineEventResult<S, E> acceptEvent(Message<S, E> message) {
    boolean disable = message == null || message.getSource() == null || message.getEvent() == null;
    if (disable) {
      return StateMachineEventResult.of(message, DENIED, new RuntimeException("message is not "
          + "valid"));
    }
    TransitionData<S, E> transition = transitionDataMap.get(Pair.of(message.getSource(),
        message.getEvent()));
    if (transition == null) {
      return StateMachineEventResult.of(message, DENIED, new RuntimeException("can't get "
          + "transition of message"));
    }
    State<S, E> state = new ObjectState<>(this, transition);
    return state.sendEvent(message);
  }

  private Map<Pair<S, E>, TransitionData<S, E>> initTransMap(
      Collection<TransitionData<S, E>> transitions) {
    if (transitions == null) {
      return ImmutableMap.of();
    }
    return transitions.stream()
        .collect(Collectors.toMap(trans -> Pair.of(trans.getSource(), trans.getEvent()),
            trans -> trans));
  }

}

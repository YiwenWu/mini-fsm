package com.github.minifsm.config;


import com.github.minifsm.Action;
import com.github.minifsm.ActionResult;
import com.github.minifsm.Guard;
import com.github.minifsm.StateContext;
import com.github.minifsm.model.TransitionsData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import reactor.core.publisher.Mono;

import static com.github.minifsm.utils.ConfigurerUtils.errorCallingAction;
import static com.github.minifsm.utils.ConfigurerUtils.fromAction;
import static com.github.minifsm.utils.ConfigurerUtils.fromGuard;

public abstract class AbstractTransitionConfigurer<S, E> extends ConfigurerAdapter<
    TransitionsData<S, E>, StateMachineTransitionConfigurer<S, E>,
    StateMachineTransitionBuilder<S, E>> {

  private S source;
  private S target;
  private E event;
  private final Collection<Function<StateContext<S, E>, Mono<ActionResult>>> actions =
      new ArrayList<>();
  private Function<StateContext<S, E>, Mono<Boolean>> guard;


  public void setSource(S source) {
    this.source = source;
  }

  public void setTarget(S target) {
    this.target = target;
  }

  public void setEvent(E event) {
    this.event = event;
  }

  protected void addAction(Action<S, E> action, Action<S, E> error) {
    this.actions.add(fromAction(error != null ? errorCallingAction(action, error) : action));
  }

  public void setGuard(Guard<S, E> guard) {
    this.guard = fromGuard(guard);
  }

  public S getSource() {
    return source;
  }

  public S getTarget() {
    return target;
  }

  public E getEvent() {
    return event;
  }

  public Collection<Function<StateContext<S, E>, Mono<ActionResult>>> getActions() {
    return actions;
  }

  public Function<StateContext<S, E>, Mono<Boolean>> getGuard() {
    return guard;
  }

}

package com.github.minifsm.config;


import com.github.minifsm.Action;
import com.github.minifsm.Guard;

import static com.github.minifsm.model.TransitionKind.INTERNAL;

/**
 * Default implementation of a {@link InternalTransitionConfigurer}.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public class DefaultInternalTransitionConfigurer<S, E> extends AbstractTransitionConfigurer<S, E>
    implements InternalTransitionConfigurer<S, E> {

  @Override
  public void configure(StateMachineTransitionBuilder<S, E> builder)  {
    builder.addTransition(getSource(), getTarget(), getEvent(), getActions(), getGuard(), INTERNAL);
  }

  @Override
  public InternalTransitionConfigurer<S, E> source(S source) {
    setSource(source);
    return this;
  }

  @Override
  public InternalTransitionConfigurer<S, E> event(E event) {
    setEvent(event);
    return this;
  }

  @Override
  public InternalTransitionConfigurer<S, E> action(Action<S, E> action) {
    return action(action, null);
  }

  @Override
  public InternalTransitionConfigurer<S, E> action(Action<S, E> action, Action<S, E> error) {
    addAction(action, error);
    return this;
  }

  @Override
  public InternalTransitionConfigurer<S, E> guard(Guard<S, E> guard) {
    setGuard(guard);
    return this;
  }

}

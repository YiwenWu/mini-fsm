package com.github.minifsm.config;


import com.github.minifsm.Action;
import com.github.minifsm.Guard;

import static com.github.minifsm.model.TransitionKind.EXTERNAL;

/**
 * Default implementation of a {@link InternalTransitionConfigurer}.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public class DefaultExternalTransitionConfigurer<S, E> extends
    AbstractTransitionConfigurer<S, E> implements
    ExternalTransitionConfigurer<S, E> {

  @Override
  public void configure(StateMachineTransitionBuilder<S, E> builder) {
    builder.addTransition(getSource(), getTarget(), getEvent(), getActions(), getGuard(), EXTERNAL);
  }

  @Override
  public ExternalTransitionConfigurer<S, E> target(S target) {
    setTarget(target);
    return this;
  }

  @Override
  public ExternalTransitionConfigurer<S, E> source(S source) {
    setSource(source);
    return this;
  }

  @Override
  public ExternalTransitionConfigurer<S, E> event(E event) {
    setEvent(event);
    return this;
  }

  @Override
  public ExternalTransitionConfigurer<S, E> action(Action<S, E> action) {
    return action(action, null);
  }

  @Override
  public ExternalTransitionConfigurer<S, E> action(Action<S, E> action, Action<S, E> error) {
    addAction(action, error);
    return this;
  }

  @Override
  public ExternalTransitionConfigurer<S, E> guard(Guard<S, E> guard) {
    setGuard(guard);
    return this;
  }
}

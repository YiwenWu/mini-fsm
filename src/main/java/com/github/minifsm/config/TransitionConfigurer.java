package com.github.minifsm.config;

import com.github.minifsm.Action;
import com.github.minifsm.Guard;


/**
 * Base {@code TransitionConfigurer} interface for configuring Transition
 *
 * @param <T> the type of a transition configurer
 * @param <S>
 * @param <E>
 */
public interface TransitionConfigurer<T, S, E> extends
    ConfigurerBuilder<StateMachineTransitionConfigurer<S, E>> {

  /**
   * Specify a source state {@code S} for this Transition
   *
   * @param source the source state {@code S}
   * @return configurer for chaining
   */
  T source(S source);

  /**
   * Specify event {@code E} for this Transition which will be triggered
   * by a event trigger.
   *
   * @param event the event for transition
   * @return configurer for chaining
   */
  T event(E event);


  /**
   * Specify {@link Action} for this Transition
   *
   * @param action the action
   * @return configurer for chaining
   */
  T action(Action<S, E> action);

  /**
   * Specify {@link Action} for this Transition
   *
   * @param action the action
   * @param error action that will be called if any unexpected exception is thrown by the
   *     action.
   * @return configurer for chaining
   */
  T action(Action<S, E> action, Action<S, E> error);


  /**
   * Specify a {@link Guard} for this Transition
   *
   * @param guard the guard
   * @return configurer for chaining
   */
  T guard(Guard<S, E> guard);

}

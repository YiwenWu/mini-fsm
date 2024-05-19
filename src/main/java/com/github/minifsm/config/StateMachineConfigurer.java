package com.github.minifsm.config;


import com.github.minifsm.model.StateMachineConfig;

/**
 * {@link Configurer} exposing configurers for states and transitions.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface StateMachineConfigurer<S, E> extends
    Configurer<StateMachineConfig<S, E>, StateMachineConfigBuilder<S, E>> {

  /**
   * Callback for {@link StateMachineTransitionConfigurer}.
   *
   * @param transitions the {@link StateMachineTransitionConfigurer}
   * @throws Exception if configuration error happens
   */
  void configure(StateMachineTransitionConfigurer<S, E> transitions) throws Exception;
}

package com.github.minifsm.config;


import com.github.minifsm.model.StateMachineConfig;
import com.github.minifsm.model.TransitionsData;

/**
 * {@link Builder} handling all shared builders which effectively contructs
 * full configuration for {@link StateMachineConfig}.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public class StateMachineConfigBuilder<S, E> extends AbstractConfiguredBuilder<
    StateMachineConfig<S, E>, StateMachineConfigBuilder<S, E>, StateMachineConfigBuilder<S, E>> {


  @Override
  protected StateMachineConfig<S, E> performBuild() throws Exception {
    StateMachineTransitionBuilder<?, ?> transitionBuilder =
        getSharedObject(StateMachineTransitionBuilder.class);
    TransitionsData<S, E> transitions = (TransitionsData<S, E>) transitionBuilder.build();
    return new StateMachineConfig<>(transitions);
  }
}

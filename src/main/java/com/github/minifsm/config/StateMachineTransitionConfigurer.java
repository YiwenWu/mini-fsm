package com.github.minifsm.config;

/**
 * Configurer interface exposing different type of transitions.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface StateMachineTransitionConfigurer<S, E> {


    /**
     * Gets a configurer for external transition.
     */
    ExternalTransitionConfigurer<S, E> withExternal();

    /**
     * Gets a configurer for internal transition. Internal transition is used
     * when action needs to be executed without causing a state transition. With
     * internal transition source and target state is always a same and it is
     * identical with self-transition in the absence of state entry and exit
     * actions.
     */
    InternalTransitionConfigurer<S, E> withInternal();

}

package com.github.minifsm.config;

/**
 * interface for configuring external Transition
 *
 * @param <S>
 * @param <E>
 */
public interface ExternalTransitionConfigurer<S, E> extends
        TransitionConfigurer<ExternalTransitionConfigurer<S, E>, S, E> {

    /**
     * Specify a target state {@code S} for this Transition
     *
     * @param target the target state {@code S}
     * @return configurer for chaining
     */
    ExternalTransitionConfigurer<S, E> target(S target);
}

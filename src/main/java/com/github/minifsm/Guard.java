package com.github.minifsm;

/**
 * {@code Guard}s are typically considered as guard conditions which affect the
 * behaviour of a state machine by enabling actions or transitions only when they
 * evaluate to {@code TRUE} and disabling them when they evaluate to
 * {@code FALSE}.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface Guard<S, E> {

    /**
     * Evaluate a guard condition.
     *
     * @param context the state context
     * @return true, if guard evaluation is successful, false otherwise.
     */
    boolean evaluate(StateContext<S, E> context);
}

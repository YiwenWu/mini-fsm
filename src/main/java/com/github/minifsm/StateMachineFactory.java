package com.github.minifsm;

/**
 * {@code StateMachineFactory} is a strategy interface building {@link StateMachine}s.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface StateMachineFactory<S, E> {

    /**
     * Build a new {@link StateMachine} instance.
     *
     * @return a new state machine instance.
     */
    StateMachine<S, E> getStateMachine();

    /**
     * Build a new {@link StateMachine} instance
     * with a given machine id.
     *
     * @param machineId the machine id
     * @return a new state machine instance.
     */
    StateMachine<S, E> getStateMachine(String machineId);

}

package com.github.minifsm.support;


import com.github.minifsm.StateMachine;
import com.github.minifsm.StateMachineFactory;
import com.github.minifsm.model.TransitionsData;

import static java.util.Objects.requireNonNull;

public class ObjectStateMachineFactory<S, E> implements StateMachineFactory<S, E> {

    private final TransitionsData<S, E> transitions;

    public ObjectStateMachineFactory(TransitionsData<S, E> transitions) {
        this.transitions = requireNonNull(transitions);
    }

    @Override
    public StateMachine<S, E> getStateMachine() {
        return getStateMachine(null);
    }

    @Override
    public StateMachine<S, E> getStateMachine(String machineId) {
        return buildMachine(machineId, transitions);
    }

    private StateMachine<S, E> buildMachine(String machineId, TransitionsData<S, E> transitions) {
        return new ObjectStateMachine<>(machineId, transitions.getTransitions());
    }

}

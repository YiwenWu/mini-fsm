package com.github.minifsm.model;

public class StateMachineConfig<S, E> {

    public final TransitionsData<S, E> transitions;

    public StateMachineConfig(TransitionsData<S, E> transitions) {
        this.transitions = transitions;
    }

    public TransitionsData<S, E> getTransitions() {
        return transitions;
    }
}


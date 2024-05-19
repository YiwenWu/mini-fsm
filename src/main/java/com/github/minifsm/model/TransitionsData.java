package com.github.minifsm.model;

import java.util.Collection;

public class TransitionsData<S, E> {

    private final Collection<TransitionData<S, E>> transitions;

    public TransitionsData(Collection<TransitionData<S, E>> transitions) {
        this.transitions = transitions;
    }

    public Collection<TransitionData<S, E>> getTransitions() {
        return transitions;
    }
}

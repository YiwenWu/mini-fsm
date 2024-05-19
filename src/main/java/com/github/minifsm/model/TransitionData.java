package com.github.minifsm.model;


import com.github.minifsm.ActionResult;
import com.github.minifsm.StateContext;

import java.util.Collection;
import java.util.function.Function;
import reactor.core.publisher.Mono;

public class TransitionData<S, E> {

    private final S source;
    private final S target;
    private final E event;
    private final Collection<Function<StateContext<S, E>, Mono<ActionResult>>> actions;
    private final Function<StateContext<S, E>, Mono<Boolean>> guard;
    private final TransitionKind kind;


    /**
     * Instantiates a new transition data.
     *
     * @param source the source
     * @param target the target
     * @param event the event
     * @param actions the actions
     * @param guard the guard
     * @param kind the kind
     */
    public TransitionData(
            S source,
            S target,
            E event,
            Collection<Function<StateContext<S, E>, Mono<ActionResult>>> actions,
            Function<StateContext<S, E>, Mono<Boolean>> guard,
            TransitionKind kind) {
        this.source = source;
        this.target = target;
        this.event = event;
        this.actions = actions;
        this.guard = guard;
        this.kind = kind;
    }

    public S getSource() {
        return source;
    }

    public S getTarget() {
        return target;
    }

    public E getEvent() {
        return event;
    }

    public Collection<Function<StateContext<S, E>, Mono<ActionResult>>> getActions() {
        return actions;
    }

    public Function<StateContext<S, E>, Mono<Boolean>> getGuard() {
        return guard;
    }

    public TransitionKind getKind() {
        return kind;
    }
}

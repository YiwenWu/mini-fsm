package com.github.minifsm.config;


import com.github.minifsm.ActionResult;
import com.github.minifsm.StateContext;
import com.github.minifsm.model.TransitionData;
import com.github.minifsm.model.TransitionKind;
import com.github.minifsm.model.TransitionsData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import reactor.core.publisher.Mono;

public class StateMachineTransitionBuilder<S, E>
        extends AbstractConfiguredBuilder<
    TransitionsData<S, E>, StateMachineTransitionConfigurer<S, E>, StateMachineTransitionBuilder<S, E>>
        implements StateMachineTransitionConfigurer<S, E> {

    private final Collection<TransitionData<S, E>> transitionData = new ArrayList<>();

    @Override
    public ExternalTransitionConfigurer<S, E> withExternal() {
        return apply(new DefaultExternalTransitionConfigurer<>());
    }

    @Override
    public InternalTransitionConfigurer<S, E> withInternal() {
        return apply(new DefaultInternalTransitionConfigurer<>());
    }

    @Override
    protected TransitionsData<S, E> performBuild() {
        return new TransitionsData<>(transitionData);
    }

    /**
     * Adds the transition.
     *
     * @param source the source
     * @param target the target
     * @param event the event
     * @param actions the actions
     * @param guard the guard
     * @param kind the kind
     */
    public void addTransition(
            S source,
            S target,
            E event,
            Collection<Function<StateContext<S, E>, Mono<ActionResult>>> actions,
            Function<StateContext<S, E>, Mono<Boolean>> guard,
            TransitionKind kind) {
        // if rule not given, get it from global
        transitionData.add(new TransitionData<>(source, target, event, actions, guard, kind));
    }

}

package com.github.minifsm;

/**
 * Generic strategy interface used by a state machine to respond
 * events by executing an {@code Action} with a {@link StateContext}.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface Action<S, E> {


  /**
   * Execute action with a {@link StateContext}.
   *
   * @param context the state context
   */
  ActionResult execute(StateContext<S, E> context);
}

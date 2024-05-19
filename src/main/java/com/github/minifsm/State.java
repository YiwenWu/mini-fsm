package com.github.minifsm;


import com.github.minifsm.model.Message;

/**
 * State info
 *
 * @param <S> type of state
 * @param <E> type of event
 */
public interface State<S, E> {

  /**
   * Send an event {@code E} wrapped with a Message to the state and
   * return a {@link StateMachineEventResult} for results.
   *
   * @param event the wrapped event to send
   * @return the state machine event results
   */
  StateMachineEventResult<S, E> sendEvent(Message<S, E> event);

}

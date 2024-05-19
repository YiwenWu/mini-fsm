package com.github.minifsm;


import com.github.minifsm.model.Message;

/**
 * snapshot of where state machine is when this context is passed to various methods.
 *
 * @param <S>
 * @param <E>
 */
public interface StateContext<S, E> {

  /**
   * Gets the event associated with a context. Event may be null if transition
   * is not triggered by a signal.
   *
   * @return the event
   */
  E getEvent();

  /**
   * Gets the message associated with a context. Message may be null if transition
   * is not triggered by a signal.
   *
   * @return the message
   */
  Message<S, E> getMessage();

  /**
   * Gets the state machine.
   *
   * @return the state machine
   */
  StateMachine<S, E> getStateMachine();


  /**
   * Gets the action exception
   *
   * @return
   */
  Throwable getThrowable();

}

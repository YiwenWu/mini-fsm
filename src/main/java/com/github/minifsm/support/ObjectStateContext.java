package com.github.minifsm.support;


import com.github.minifsm.StateContext;
import com.github.minifsm.StateMachine;
import com.github.minifsm.model.Message;

public class ObjectStateContext<S, E> implements StateContext<S, E> {

  private final Message<S, E> message;
  private final StateMachine<S, E> stateMachine;
  private final Throwable throwable;


  public ObjectStateContext(Message<S, E> message, StateMachine<S, E> stateMachine) {
    this(message, stateMachine, null);
  }

  public ObjectStateContext(Message<S, E> message, StateMachine<S, E> stateMachine,
      Throwable throwable) {
    this.message = message;
    this.stateMachine = stateMachine;
    this.throwable = throwable;
  }

  @Override
  public E getEvent() {
    return message != null ? message.getEvent() : null;
  }

  @Override
  public Message<S, E> getMessage() {
    return message;
  }

  @Override
  public StateMachine<S, E> getStateMachine() {
    return stateMachine;
  }

  @Override
  public Throwable getThrowable() {
    return throwable;
  }
}

package com.github.minifsm;


import com.github.minifsm.model.Message;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * Interface defining a result for sending an event to a statemachine.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface StateMachineEventResult<S, E> {

  /**
   * Gets the message.
   *
   * @return the message
   */
  Message<S, E> getMessage();

  /**
   * get action target state
   *
   * @return state
   */
  S getState();


  /**
   * Gets the result type.
   *
   * @return the result type
   */
  ResultType getResultType();

  /**
   * get action elapsedTime of millisecond
   *
   * @return action elapsed time
   */
  Long getElapsedTime();


  /**
   * get the action throwable if action finished with error
   *
   * @return the throwable info
   */
  Throwable getThrowable();


  /**
   * get result extra info
   */
  Collection<ActionResult> getActionResults();


  enum ResultType {
    ACCEPTED,
    DENIED
  }

  static <S, E> StateMachineEventResult<S, E> of(
      Message<S, E> message,
      ResultType resultType) {
    return new DefaultStateMachineEventResult<>(message, resultType,
        requireNonNull(message).getSource(), 0L, null,
        null);
  }

  static <S, E> StateMachineEventResult<S, E> of(
      Message<S, E> message,
      ResultType resultType,
      Throwable throwable) {
    return new DefaultStateMachineEventResult<>(message, resultType,
        requireNonNull(message).getSource(), 0L,
        throwable, null);
  }

  static <S, E> StateMachineEventResult<S, E> of(
      Message<S, E> message,
      ResultType resultType,
      S state,
      Long elapsedTime,
      Collection<ActionResult> actionResults) {
    return new DefaultStateMachineEventResult<>(message, resultType, state, elapsedTime, null,
        actionResults);
  }


  class DefaultStateMachineEventResult<S, E> implements StateMachineEventResult<S, E> {

    private final Message<S, E> message;
    private final ResultType resultType;
    private final S state;
    private final Long elapsedTime;
    private final Throwable throwable;
    private final Collection<ActionResult> actionResults;

    public DefaultStateMachineEventResult(
        Message<S, E> message,
        ResultType resultType,
        S state,
        Long elapsedTime,
        Throwable throwable,
        Collection<ActionResult> actionResults) {
      this.message = message;
      this.resultType = resultType;
      this.state = state;
      this.elapsedTime = elapsedTime;
      this.throwable = throwable;
      this.actionResults = actionResults;
    }

    @Override
    public Message<S, E> getMessage() {
      return message;
    }

    @Override
    public S getState() {
      return state;
    }

    @Override
    public ResultType getResultType() {
      return resultType;
    }

    @Override
    public Long getElapsedTime() {
      return elapsedTime;
    }

    @Override
    public Throwable getThrowable() {
      return throwable;
    }

    @Override
    public Collection<ActionResult> getActionResults() {
      return actionResults;
    }


    @Override
    public String toString() {
      final StringBuffer sb = new StringBuffer("DefaultStateMachineEventResult{");
      sb.append("message=").append(message);
      sb.append(", resultType=").append(resultType);
      sb.append(", state=").append(state);
      sb.append(", elapsedTime=").append(elapsedTime);
      sb.append(", throwable=").append(throwable);
      sb.append(", actionResults=").append(actionResults);
      sb.append('}');
      return sb.toString();
    }
  }


}

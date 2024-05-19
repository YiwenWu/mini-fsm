package com.github.minifsm;


import com.github.minifsm.model.Message;

import java.util.List;

public interface StateMachine<S, E> {

  /**
   * Gets the state machine id.
   *
   * @return
   */
  String getId();

  /**
   * Send an event {@code E} wrapped with a {@link Message} to the region.
   *
   * @param message
   * @return
   */
  StateMachineEventResult<S, E> sendEvent(Message<S, E> message);


  /**
   * send events {@code E} wrapped with a {@link Message} to the region.
   *
   * @param messages
   * @return
   */
  List<StateMachineEventResult<S, E>> sendEvents(List<Message<S, E>> messages);


  /**
   * send events {@code E} wrapped with a {@link Message} to the region.
   *
   * @param messages
   * @param async the events send async
   * @return
   */
  List<StateMachineEventResult<S, E>> sendEvents(List<Message<S, E>> messages, boolean async);
}

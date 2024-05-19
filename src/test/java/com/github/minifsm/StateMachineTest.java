package com.github.minifsm;

import com.github.minifsm.StateMachineEventResult.ResultType;
import com.github.minifsm.config.StateMachineBuilder;
import com.github.minifsm.config.StateMachineBuilder.Builder;
import com.github.minifsm.config.StateMachineTransitionBuilder;
import com.github.minifsm.model.Message;
import com.github.minifsm.model.MessageHeaders;
import com.github.minifsm.model.TransitionData;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class StateMachineTest {

  @Test
  public void testBuilder5() {
    Builder<State, Event> builder = StateMachineBuilder.builder();
    builder.configureTransitions().withInternal()
        .source(State.S1)
        .event(Event.E12)
        .action(context -> {
          sleep(1000);
          Message<State, Event> message = context.getMessage();
          String info = (String) message.getHeaders().get("info");
          System.out.println(info);
          return ActionResult.of(ImmutableMap.of("result", info));
        })
        .and()
        .withInternal()
        .source(State.S1)
        .event(Event.E13)
        .action(context -> {
          throw new RuntimeException("event e13 error");
        }, context -> {
          //error
          System.out.println("drop table back");
          return ActionResult.of(ImmutableMap.of("result", "drop table back"));
        });
    StateMachine<State, Event> stateMachine = builder.build();

    MessageHeaders headers = new MessageHeaders(ImmutableMap.of("info", "123456"));

    Stopwatch watch = Stopwatch.createStarted();
    List<StateMachineEventResult<State, Event>> results = stateMachine.sendEvents(ImmutableList.of(
        Message.of(State.S1, Event.E12, headers),
        Message.of(State.S1, Event.E13, headers)), true);
    assert watch.elapsed(TimeUnit.MILLISECONDS) > 1000;
    assert results.size() == 2;
  }

  @Test
  public void testBuilder4() {
    Builder<State, Event> builder = StateMachineBuilder.builder();
    builder.configureTransitions().withInternal()
        .source(State.S1)
        .event(Event.E12);
    StateMachine<State, Event> stateMachine = builder.build();
    Message<State, Event> message = Message.of(State.S1, Event.E12,
        new MessageHeaders(ImmutableMap.of("info", "123456")));
    StateMachineEventResult<State, Event> result = stateMachine.sendEvent(message);
    assert result.getState() == State.S1;
    assert result.getResultType() == ResultType.ACCEPTED;
  }


  @Test
  public void testBuilder3() {
    Builder<State, Event> builder = StateMachineBuilder.builder();
    builder.configureTransitions().withExternal()
        .source(State.S1)
        .target(State.S2)
        .event(Event.E12);
    StateMachine<State, Event> stateMachine = builder.build();
    Message<State, Event> message = Message.of(State.S1, Event.E12,
        new MessageHeaders(ImmutableMap.of("info", "123456")));
    StateMachineEventResult<State, Event> result = stateMachine.sendEvent(message);
    assert result.getState() == State.S2;
    assert result.getResultType() == ResultType.ACCEPTED;
  }


  /**
   * 相同时间下多个Action并行
   *
   * @throws Exception
   */
  @Test
  public void testBuilder2() {
    Builder<State, Event> builder = StateMachineBuilder.builder();
    StateMachineTransitionBuilder<State, Event> transitionBuilder =
        builder.configureTransitions();
    transitionBuilder.withExternal()
        .source(State.S1)
        .target(State.S2)
        .event(Event.E12)
        .action(action)
        .action(context -> {
          sleep(1000);
          System.out.println("=====action2.2=" + context.getEvent());
          return ActionResult.of();
        }, context -> {
          System.out.println("action failed222222");
          return ActionResult.of();
        })
        .action(context -> {
          sleep(1000);
          throw new RuntimeException("=====action2.1=");
        }, context -> {
          System.out.println("action failed");
          return ActionResult.of();
        })
        .action(context -> {
          sleep(2000);
          throw new RuntimeException("=====action2.3=");
        }, context -> {
          System.out.println("action finished");
          return ActionResult.of();
        })
        .action(context -> {
          System.out.println("=====action3=" + context.getEvent());
          return ActionResult.of();
        })
        .and()
        .withExternal()
        .source(State.S1)
        .target(State.S3)
        .event(Event.E13)
        .and()
        .withExternal()
        .source(State.S2)
        .target(State.S3)
        .event(Event.E23)
        .and()
        .withInternal()
        .source(State.S2)
        .event(Event.E22);
    StateMachine<State, Event> stateMachine = builder.build();
    assert stateMachine.getId() == null;

    Collection<TransitionData<State, Event>> transitions = transitionBuilder.getObject()
        .getTransitions();
    assert transitions.size() == 4;

    Stopwatch watch = Stopwatch.createStarted();
    Message<State, Event> message = Message.of(State.S1, Event.E12,
        new MessageHeaders(ImmutableMap.of("info", "123456")));
    StateMachineEventResult<State, Event> result = stateMachine.sendEvent(message);
    long elapsed = watch.elapsed(TimeUnit.MILLISECONDS);
    assert elapsed > 2000 && elapsed < 3000;
    //System.out.println("cost=" + (System.currentTimeMillis() - start));
  }


  @Test
  public void testBuilder1() {
    Builder<State, Event> builder = StateMachineBuilder.builder();
    StateMachineTransitionBuilder<State, Event> transitionBuilder =
        builder.configureTransitions();
    transitionBuilder.withInternal()
        .source(State.S1)
        .event(Event.E12)
        .action(context -> {
          sleep(2000);
          System.out.println("=====action12=" + context.getEvent());
          return ActionResult.of();
        })
        .and()
        .withInternal()
        .source(State.S1)
        .event(Event.E13)
        .action(context -> {
          sleep(3000);
          System.out.println("=====action22=" + context.getEvent());
          return ActionResult.of();
        })
        .and()
        .withInternal()
        .source(State.S1)
        .event(Event.E14)
        .action(context -> {
          System.out.println("=====action14=" + context.getEvent());
          return ActionResult.of();
        })
        .and()
        .withInternal()
        .source(State.S2)
        .event(Event.E23)
        .action(context -> {
          System.out.println("=====action23=" + context.getEvent());
          return ActionResult.of();
        })
        .and()
        .withInternal()
        .source(State.S2)
        .event(Event.E22);
    StateMachine<State, Event> stateMachine = builder.build();

    MessageHeaders headers = new MessageHeaders(ImmutableMap.of("info", "123456"));
    Message<State, Event> message1 = Message.of(State.S1, Event.E12, headers);
    Message<State, Event> message2 = Message.of(State.S1, Event.E13, headers);
    Message<State, Event> message3 = Message.of(State.S1, Event.E14, headers);
    Stopwatch watch = Stopwatch.createStarted();
    List<StateMachineEventResult<State, Event>> results1 = stateMachine.sendEvents(
        ImmutableList.of(message1, message2, message3));
    assert results1.stream().noneMatch(result -> result.getResultType() == ResultType.DENIED);
    assert watch.elapsed(TimeUnit.MILLISECONDS) > 5000;

    System.out.println("------------Begin Async------------");
    //异步执行Event处理
    watch.reset().start();
    List<StateMachineEventResult<State, Event>> results = stateMachine.sendEvents(
        ImmutableList.of(message1, message2, message3), true);
    assert results.stream().noneMatch(result -> result.getResultType() == ResultType.DENIED);
    long elapsed = watch.elapsed(TimeUnit.MILLISECONDS);
    assert elapsed < 5000 && elapsed > 3000;
  }

  @Test
  public void testBuilder() {
    Builder<State, Event> builder = StateMachineBuilder.builder();
    builder.configureTransitions()
        .withInternal()
        .source(State.S1)
        .event(Event.E1)
        .action(context -> {
          sleep(2000);
          assert context.getEvent() == Event.E1;
          return ActionResult.of();
        })
        .and()
        .withExternal()
        .source(State.S2)
        .target(State.S3)
        .event(Event.E23)
        .action(context -> {
          assert context.getEvent() == Event.E23;
          MessageHeaders headers = context.getMessage().getHeaders();
          String result = ((String) headers.get("info")) + context.getEvent();
          return ActionResult.of(ImmutableMap.of("result", result));
        })
        .and()
        .withInternal()
        .source(State.S1)
        .event(Event.E13)
        .action(
            context -> {
              System.out.println("mock failed case");
              throw new RuntimeException("event e13 error");
            },
            context -> {
              System.out.println("Drop table Rollback");
              return ActionResult.of(ImmutableMap.of("result", "drop table back"));
            })
        .and()
        .withInternal()
        .source(State.S3)
        .event(Event.E22)
        .guard(context -> context.getEvent() == Event.E13)
        .action(context -> ActionResult.of());
    StateMachine<State, Event> stateMachine = builder.build();

    MessageHeaders headers = new MessageHeaders(ImmutableMap.of("info", "123456"));
    final StateMachineEventResult<State, Event> inner =
        stateMachine.sendEvent(Message.of(State.S1, Event.E1, headers));
    assert inner.getResultType() == ResultType.ACCEPTED;
    assert inner.getState() == State.S1;

    final StateMachineEventResult<State, Event> external =
        stateMachine.sendEvent(Message.of(State.S2, Event.E23, headers));
    assert external.getState() == State.S3;
    ActionResult actionResult = external.getActionResults().iterator().next();
    assertEquals("123456E23", actionResult.infos().get("result"));

    List<StateMachineEventResult<State, Event>> results = stateMachine.sendEvents(
        ImmutableList.of(
            Message.of(State.S1, Event.E1, headers),
            Message.of(State.S2, Event.E23, headers)));
    assert results.stream().noneMatch(result -> result.getResultType() == ResultType.DENIED);

    List<StateMachineEventResult<State, Event>> async = stateMachine.sendEvents(
        ImmutableList.of(
            Message.of(State.S1, Event.E1, headers),
            Message.of(State.S2, Event.E23, headers)),
        true);
    assert async.stream().noneMatch(result -> result.getResultType() == ResultType.DENIED);

    final StateMachineEventResult<State, Event> failed =
        stateMachine.sendEvent(Message.of(State.S1, Event.E13, headers));
    assert failed.getResultType() == ResultType.DENIED;

    results = stateMachine.sendEvents(
        ImmutableList.of(
            Message.of(State.S1, Event.E13, headers),
            Message.of(State.S1, Event.E1, headers),
            Message.of(State.S2, Event.E23, headers)));
    assert results.size() == 3;
    assert results.stream()
        .filter(result -> result.getResultType() == ResultType.DENIED).count() == 1;
    assert results.stream()
        .filter(result -> result.getResultType() == ResultType.ACCEPTED).count() == 2;

    results = stateMachine.sendEvents(
        ImmutableList.of(
            Message.of(State.S1, Event.E13, headers),
            Message.of(State.S1, Event.E1, headers),
            Message.of(State.S2, Event.E23, headers)), true);
    assert results.size() == 3;
    assert results.stream()
        .filter(result -> result.getResultType() == ResultType.DENIED).count() == 1;
    assert results.stream()
        .filter(result -> result.getResultType() == ResultType.ACCEPTED).count() == 2;

    StateMachineEventResult<State, Event> guard =
        stateMachine.sendEvent(Message.of(State.S3, Event.E22, headers));
    assert guard.getResultType() == ResultType.DENIED;
  }

  enum State {
    S1, S2, S3
  }

  enum Event {
    E1, E12, E13, E14, E23, E22
  }

  Action<State, Event> action = context -> {
    Message<State, Event> message = context.getMessage();
    Event event = message.getEvent();
    String info = (String) message.getHeaders().get("info");
    //TODO do actions
    System.out.println(info + event);
    Map<String, Object> result = ImmutableMap.of("id", "1,2,3");
    return ActionResult.of(result);
  };


  private void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (Exception e) {

    }
  }

}

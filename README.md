# Lightweight Mini-FSM
更多详情参考：https://cloud.tencent.com/developer/article/2418561

## Background
Based on the design concepts of Cola StateMachine and Spring StateMachine, the design has been simplified and the `ActionResult` returned after the execution of `Action` has been expanded. The execution of Action uses reactive programming, implemented based on Flux and Mono. 
The main interface definitions of the Mini-FSM finite state machine include:

- **StateMachine**: This maintains the context of the state machine.
- **StateMachineEventResult**: The return result of the state machine event transition, including obtaining the current state, Action results, etc.
- **State**: This passes event information and is used for state transition. The core processing of FSM, StateContext refers back to the context of the state machine.
- **Event**: This can be defined based on the enumeration class.
- **Action**: This corresponds to the specific event execution processing.
- **ActionResult**: The return result of event execution, encapsulating the details of the result.
- **Guard**: The condition, indicating whether it is allowed to reach a certain state.
- **Transition**: The transition, indicating from one state to another, including `TransitionKind` type, `TransitionData` data.


The execution characteristics of the Mini-FSM execution framework are:
- **Stateless**: The Statemachine state machine itself is stateless, with no thread safety issues.
- **Lightweight**: Ultra-lightweight framework with only three external dependency packages.
- **Multiple forms**:
  - Execution method: Supports synchronous and asynchronous event triggering.
  - Transition method: Supports internal transition (the current state and the next state remain consistent) and external transition forms.
  - Result return: The triggering of the event allows the return of the Action execution result, and supports obtaining execution time, execution exceptions, and other information.


## Architecture
The implementation of the Mini-FSM framework is as follows: Users trigger the current state transition based on the Event event and return the transition result.
- **sendEvent**: Action accepts the sendEvent request for processing, and encapsulates the transition information for the transit operation.
- **transit**: Based on `TransitionData` processing, 1. Verify execution conditions based on Guard; 2. Trigger execution Apply based on Action and return the execution result.

<img src=https://github.com/YiwenWu/mini-fsm/assets/7956306/e2b66003-f044-459e-9d07-43ba7a4b7d06 width=600/>

Transition types are divided into two categories:
- **EXTERNAL**: External transition, where the Event execution changes the state.
- **INTERNAL**: Internal transition, where the Event execution does not change the state, and the current state and the next state remain consistent. It is a special case of EXTERNAL transition.


---
> 中文文档
## 背景说明
基于Cola StateMachine 和 Spring StateMachine 的设计思想进行简化，扩展了Action执行后的返回结果ActionResult。其中Action的执行使用响应式编程，基于Flux和Mono实现。Mini-FSM有限状态机的主要接口定义包括：
- **StateMachine**：状态机，维护状态机的上下文
- **StateMachineEventResult**：状态机事件转换的返回结果，包括获取当前状态、Action结果等
- **State**：状态，传递事件信息并用于状态转换，FSM核心处理，StateContext 反向引用状态机的上下文
- **Event**：事件，可基于枚举类定义
- **Action**：动作，对应具体事件的执行处理
- **ActionResult**：事件执行的返回结果，封装结果详情
- **Guard**：条件，表示是否允许到达某个状态
- **Transition**：流转，表示从一个状态到另一个状态，包括TransitionKind类型，TransitionData 数据

Mini-FSM执行框架执行特点：
- **无状态**: Statemachine状态机本身是无状态（Stateless）的，无线程安全问题
- **轻量级**: 超轻量级框架，仅三个外部依赖包
- **多形式**：
  - 执行方式：支持同步、异步事件触发
  - 流转方式：支持内部流转(现态与次态保持一致)、外部流转形式
  - 结果返回：事件触发Action执行，允许返回Action执行结果，支持获取执行耗时、执行异常等信息

## 实现架构
Mini-FSM框架实现如下所示：用户基于Event事件触发当前状态转移并返回转移结果
- **sendEvent**：Action接受sendEvent请求处理，封装封装transition信息进行transit操作
- **transit**：基于TransitionData处理，1. 基于Guard校验执行条件；2. 基于Action触发执行Apply并返回执行结果

<img src=https://github.com/YiwenWu/mini-fsm/assets/7956306/f5f2ff3c-bdb6-4434-999c-4fee78c3be57 width=600/>

Transition流转类型分为两类：
- **EXTERNAL**：外部流转，Event执行变更状态
- **INTERNAL**：内部流转，Event执行不变更状态，现态与次态保持一致，是EXTERNAL流转的特例


package com.github.minifsm.config;


import com.github.minifsm.StateMachine;
import com.github.minifsm.StateMachineFactory;
import com.github.minifsm.model.StateMachineConfig;
import com.github.minifsm.model.TransitionsData;
import com.github.minifsm.support.ObjectStateMachineFactory;

/**
 * {@code StateMachineBuilder} provides a builder pattern for
 * {@link StateMachine} using a similar concepts found from a
 * normal annotation based configuration.
 */
public class StateMachineBuilder {

  public static <S, E> Builder<S, E> builder() {
    return new Builder<>(null);
  }


  public static <S, E> Builder<S, E> builder(String machineId) {
    return new Builder<>(machineId);
  }


  public static class Builder<S, E> {

    private final StateMachineConfigBuilder<S, E> builder;
    private final BuilderStateMachineConfigurerAdapter<S, E> adapter;
    private final String machineId;

    public Builder(String machineId) {
      this.adapter = new BuilderStateMachineConfigurerAdapter<>();
      this.builder = new StateMachineConfigBuilder<>();
      this.machineId = machineId;
    }


    public StateMachine<S, E> build() {
      try {
        return createFactory().getStateMachine(machineId);
      } catch (Exception e) {
        throw new RuntimeException("Error building state machine", e);
      }
    }


    private StateMachineFactory<S, E> createFactory() {
      builder.apply(adapter);
      return create(builder);
    }

    /**
     * Configure transitions.
     *
     * @return the state machine transition configurer
     */
    public StateMachineTransitionBuilder<S, E> configureTransitions() {
      return adapter.transitionBuilder;
    }


    private ObjectStateMachineFactory<S, E> create(StateMachineConfigBuilder<S, E> builder) {
      StateMachineConfig<S, E> stateMachineConfig = builder.getOrBuild();
      TransitionsData<S, E> transitions = stateMachineConfig.getTransitions();
      return new ObjectStateMachineFactory<>(transitions);
    }
  }

  private static class BuilderStateMachineConfigurerAdapter<S, E> implements
      StateMachineConfigurer<S, E> {

    private StateMachineTransitionBuilder<S, E> transitionBuilder;


    public BuilderStateMachineConfigurerAdapter() {
      try {
        getStateMachineTransitionBuilder();
      } catch (Exception e) {
        throw new RuntimeException("Error instantiating builder adapter", e);
      }
    }

    @Override
    public void init(StateMachineConfigBuilder<S, E> config) throws Exception {
      config.setSharedObject(StateMachineTransitionBuilder.class,
          getStateMachineTransitionBuilder());
    }

    @Override
    public void configure(StateMachineConfigBuilder<S, E> builder) throws Exception {

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<S, E> transitions) throws Exception {

    }

    public final StateMachineTransitionBuilder<S, E> getStateMachineTransitionBuilder() {
      if (transitionBuilder != null) {
        return transitionBuilder;
      }
      transitionBuilder = new StateMachineTransitionBuilder<>();
      return transitionBuilder;
    }
  }

}

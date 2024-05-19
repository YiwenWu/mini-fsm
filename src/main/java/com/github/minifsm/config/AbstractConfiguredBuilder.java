package com.github.minifsm.config;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A base {@link Builder} that allows {@link Configurer}s to be
 * applied to it. This makes modifying the {@link Builder} a strategy
 * that can be customised and broken up into a number of
 * {@link Configurer} objects that have more specific goals than that
 * of the {@link Builder}.
 * <p>
 *
 * @param <O> The object that this builder returns
 * @param <I> The interface of type B
 * @param <B> The type of this builder (that is returned by the base class)
 */
public abstract class AbstractConfiguredBuilder<O, I, B extends Builder<O>> extends AbstractBuilder<O> {

    /**
     * Configurers which are added to this builder before the configure step
     */
    private final LinkedHashMap<Class<? extends Configurer<O, B>>, List<Configurer<O, B>>> mainConfigurers =
            new LinkedHashMap<>();

    private final Map<Class<Object>, Object> sharedObjects = new HashMap<>();

    /**
     * Current state of this builder
     */
    private BuildState buildState = BuildState.UNBUILT;

    @Override
    protected O doBuild() throws Exception {
        synchronized (mainConfigurers) {
            buildState = BuildState.INITIALIZING_MAINS;
            initMainConfigurers();

            buildState = BuildState.CONFIGURING_MAINS;
            configureMainConfigurers();

            buildState = BuildState.BUILDING;
            O result = performBuild();

            buildState = BuildState.BUILT;
            return result;
        }
    }

    /**
     * Subclasses must implement this method to build the object that is being returned.
     *
     * @return Object build by this builder
     * @throws Exception if error occurred
     */
    protected abstract O performBuild() throws Exception;


    /**
     * Similar to {@link #build()} and {@link #getObject()} but checks the state
     * to determine if {@link #build()} needs to be called first.
     *
     * @return the result of {@link #build()} or {@link #getObject()}. If an
     *         error occurs while building, returns null.
     */
    public O getOrBuild() {
        if (isUnbuilt()) {
            try {
                return build();
            } catch (Exception e) {
                return null;
            }
        } else {
            return getObject();
        }
    }


    /**
     * Applies a {@link AbstractConfiguredBuilder} to this
     * {@link Builder} and invokes
     * {@link ConfigurerAdapter#setBuilder(Builder)}.
     *
     * @param configurer the configurer
     * @param <C> type of AnnotationConfigurer
     * @return Configurer passed as parameter
     * @throws Exception if error occurred
     */
    @SuppressWarnings("unchecked")
    public <C extends ConfigurerAdapter<O, I, B>> C apply(C configurer) {
        requireNonNull(configurer);
        add(configurer);
        configurer.setBuilder((B) this);
        return configurer;
    }


    /**
     * Applies a {@link Configurer} to this {@link Builder}
     * overriding any {@link Configurer} of the exact same class. Note
     * that object hierarchies are not considered.
     *
     * @param configurer the configurer
     * @param <C> type of AnnotationConfigurer
     * @return Configurer passed as parameter
     * @throws Exception if error occurred
     */
    public <C extends Configurer<O, B>> C apply(C configurer) {
        add(configurer);
        return configurer;
    }


    /**
     * Sets an object that is shared by multiple {@link Configurer}.
     *
     * @param sharedType the Class to key the shared object by.
     * @param object the Object to store
     * @param <C> type of share object
     */
    @SuppressWarnings("unchecked")
    public <C> void setSharedObject(Class<C> sharedType, C object) {
        this.sharedObjects.put((Class<Object>) sharedType, object);
    }

    /**
     * Gets a shared Object. Note that object hierarchies are not considered.
     *
     * @param sharedType the type of the shared Object
     * @param <C> type of share object
     * @return the shared Object or null if it is not found
     */
    @SuppressWarnings("unchecked")
    public <C> C getSharedObject(Class<C> sharedType) {
        return (C) this.sharedObjects.get(sharedType);
    }

    private <C extends Configurer<O, B>> void add(C configurer) {
        Class<? extends Configurer<O, B>> clazz = (Class<? extends Configurer<O, B>>) configurer.getClass();
        synchronized (mainConfigurers) {
            List<Configurer<O, B>> configs = this.mainConfigurers.getOrDefault(clazz, new ArrayList<>(1));
            configs.add(configurer);
            this.mainConfigurers.put(clazz, configs);
        }
    }

    private void initMainConfigurers() throws Exception {
        for (Configurer<O, B> configurer : getMainConfigurers()) {
            configurer.init((B) this);
        }
    }

    private void configureMainConfigurers() throws Exception {
        for (Configurer<O, B> configurer : getMainConfigurers()) {
            configurer.configure((B) this);
        }
    }


    private Collection<Configurer<O, B>> getMainConfigurers() {
        List<Configurer<O, B>> result = new ArrayList<>();
        for (List<Configurer<O, B>> configs : this.mainConfigurers.values()) {
            result.addAll(configs);
        }
        return result;
    }

    /**
     * Determines if the object is unbuilt.
     *
     * @return true, if unbuilt else false
     */
    private boolean isUnbuilt() {
        synchronized (mainConfigurers) {
            return buildState == BuildState.UNBUILT;
        }
    }

    enum BuildState {
        /**
         * This is the state before the {@link Builder#build()} is invoked
         */
        UNBUILT(0),

        /**
         * The state from when {@link Builder#build()} is first invoked until
         * all the {@link Configurer#init(Builder)} methods have
         * been invoked.
         */
        INITIALIZING_MAINS(1),

        /**
         * The state from after all main
         * {@link Configurer#init(Builder)}
         * have been invoked until after all the
         * {@link Configurer#configure(Builder)}
         * methods have been invoked.
         */
        CONFIGURING_MAINS(2),

        /**
         * The state from after all post
         * {@link Configurer#init(Builder)}
         * have been invoked until after all the
         * {@link Configurer#configure(Builder)}
         * methods have been invoked.
         */
        CONFIGURING_POSTS(3),

        /**
         * From the point after all the
         * {@link Configurer#configure(Builder)}
         * have completed to just after
         * {@link AbstractConfiguredBuilder#performBuild()}.
         */
        BUILDING(4),

        /**
         * After the object has been completely built.
         */
        BUILT(5);

        private final int order;

        BuildState(int order) {
            this.order = order;
        }

        /**
         * Checks if is initializing.
         *
         * @return true, if is initializing
         */
        public boolean isInitializing() {
            return INITIALIZING_MAINS.order == order;
        }

        /**
         * Determines if the state is CONFIGURING or later
         *
         * @return true, if configured
         */
        public boolean isConfigured() {
            return order >= CONFIGURING_MAINS.order;
        }
    }


}

package com.github.minifsm.config;

/**
 * Allows for configuring an {@link Builder}. All
 * {@link Configurer}s first have their {@link #init(Builder)}
 * method invoked. After all {@link #init(Builder)} methods have been
 * invoked, each {@link #configure(Builder)} method is invoked.
 *
 * @param <O> The object being built by the {@link Builder} B
 * @param <B> The {@link Builder} that builds objects of type O. This is
 *         also the {@link Builder} that is being configured.
 * @see
 */
public interface Configurer<O, B extends Builder<O>> {

    /**
     * Initialise the {@link Builder}. Here only shared state should be
     * created and modified, but not properties on the {@link Builder}
     * used for building the object. This ensures that the
     * {@link #configure(Builder)} method uses the correct shared
     * objects when building.
     *
     * @param builder the builder
     * @throws Exception if error occurred
     */
    void init(B builder) throws Exception;

    /**
     * Configure the {@link Builder} by setting the necessary properties
     * on the {@link Builder}.
     *
     * @param builder the builder
     * @throws Exception if error occurred
     */
    void configure(B builder) throws Exception;

}

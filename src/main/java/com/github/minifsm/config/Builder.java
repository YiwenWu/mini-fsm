package com.github.minifsm.config;


/**
 * Interface for building an {@link Object}.
 *
 * @param <O> The type of the Object being built
 */
public interface Builder<O> {

    /**
     * Builds the object and returns it or null.
     *
     * @return the Object to be built or null if the implementation allows it.
     * @throws Exception if an error occurred when building the Object
     */
    O build() throws Exception;

    /**
     * Gets the object that was built. If it has not been built yet an Exception
     * is thrown.
     *
     * @return the Object that was built
     * @throws Exception if an error occurred when building the Object
     */
    O getObject() throws Exception;


}

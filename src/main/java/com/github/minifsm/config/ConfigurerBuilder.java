package com.github.minifsm.config;


/**
 * Interface for wrapping a return type from Configurer
 *
 * @param <I> The parent return type of the configurer.
 */
public interface ConfigurerBuilder<I> {

    /**
     * Get a parent {@link ConfigurerBuilder} working
     * with a {@link ConfigurerBuilder}.
     *
     * @return The parent {@link ConfigurerBuilder}
     */
    I and();
}

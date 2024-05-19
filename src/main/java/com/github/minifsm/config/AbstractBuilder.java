package com.github.minifsm.config;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractBuilder<O> implements Builder<O> {

    /**
     * Flag tracking build
     */
    private final AtomicBoolean building = new AtomicBoolean();
    /**
     * Built object is stored here
     */
    private O object;

    @Override
    public final O build() throws Exception {
        if (building.compareAndSet(false, true)) {
            object = doBuild();
            return object;
        }
        throw new IllegalStateException("This object has already been built");
    }

    @Override
    public final O getObject() {
        if (!building.get()) {
            throw new IllegalStateException("This object has not been built");
        }
        return object;
    }

    /**
     * Subclasses should implement this to perform the build.
     *
     * @return the object that should be returned by {@link #build()}.
     * @throws Exception if an error occurs
     */
    protected abstract O doBuild() throws Exception;

}

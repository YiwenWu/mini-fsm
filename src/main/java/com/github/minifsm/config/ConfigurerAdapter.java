package com.github.minifsm.config;

public abstract class ConfigurerAdapter<O, I, B extends Builder<O>> implements Configurer<O, B> {

    private B builder;

    @Override
    public void init(B builder) throws Exception {
    }

    @Override
    public void configure(B builder) throws Exception {
    }

    /**
     * Return the {@link Builder} when done using the
     * {@link Configurer}. This is useful for method chaining.
     *
     * @return the {@link Builder}
     */
    @SuppressWarnings("unchecked")
    public I and() {
        // we're either casting to itself or its interface
        return (I) getBuilder();
    }


    /**
     * Sets the {@link Builder} to be used. This is automatically set
     * when using
     * {@link AbstractConfiguredBuilder#apply(ConfigurerAdapter)}
     *
     * @param builder the {@link Builder} to set
     */
    public void setBuilder(B builder) {
        this.builder = builder;
    }

    /**
     * Gets the {@link Builder}. Cannot be null.
     *
     * @return the {@link Builder}
     * @throws IllegalStateException if AnnotationBuilder is null
     */
    private final B getBuilder() {
        if (builder == null) {
            throw new IllegalStateException("annotationBuilder cannot be null");
        }
        return builder;
    }
}

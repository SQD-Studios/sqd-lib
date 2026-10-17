package com.sqdstudios.sqdlib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface Requires {

    Dependency[] value();

    @interface Dependency {
        String value();

        Class<?> neededClass() default Object.class;
    }
}
package net.chamosmp.sqdlib.annotations;

import net.chamosmp.sqdlib.enums.Platform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to show which platforms certain code works in.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE})
public @interface SupportsPlatforms {

    /**
     * The array of platforms certain code supports
     */
    Platform[] value();
}
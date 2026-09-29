package net.chamosmp.sqdlib.annotations.color;

import org.jspecify.annotations.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface SupportsColorSystems {

    @NonNull ColorSystem value();

    enum ColorSystem {
        MINI_MESSAGE,
        CHAT_COLOR
    }
}

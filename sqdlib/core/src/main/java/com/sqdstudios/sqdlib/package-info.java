/**
 * Adventure Platform wide utilities, annotations and more!
 */
@Requires(
        @Requires.Dependency(value = "Jspecify", neededClass = NullMarked.class)
)
@SupportsPlatforms({Platform.ALL})
@SuppressWarnings("unused")
package com.sqdstudios.sqdlib;

import com.sqdstudios.sqdlib.annotations.Requires;
import com.sqdstudios.sqdlib.annotations.SupportsPlatforms;
import com.sqdstudios.sqdlib.enums.Platform;
import org.jspecify.annotations.NullMarked;

package com.sqdstudios.sqdlib.fabric.util;

import com.sqdstudios.sqdlib.exceptions.LoggerNotInitiatedBeforeUsing;
import com.sqdstudios.sqdlib.fabric.FabricMod;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.ApiStatus;

public class LoggerUtil {

    private static final ComponentLogger internalLogger = ComponentLogger.logger("SQDLib");
    private static ComponentLogger logger;

    public LoggerUtil(FabricMod fabricMod) {
        logger = ComponentLogger.logger(fabricMod.getModMetadata().getName());
    }

    public static ComponentLogger getLogger() {
        if (logger == null) {
            throw new LoggerNotInitiatedBeforeUsing("");
        }
        return logger;
    }

    @ApiStatus.Internal
    public static ComponentLogger getInternalLogger() {
        return internalLogger;
    }
}
package dev.moriamap;

import java.util.logging.Logger;

/**
 * Class for accessing the Logger
 */
public class Logging {

    private Logging() {}

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * @return the logger
     */
    public static Logger getLogger() {
        return logger;
    }

}
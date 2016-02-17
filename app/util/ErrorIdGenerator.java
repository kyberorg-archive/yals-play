package util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Generates Error ID for logging and reporting
 *
 * @since 1.0
 */
public class ErrorIdGenerator {

    private static final int LENGTH = 6;

    public static String generate(){
        return RandomStringUtils.randomAlphanumeric(LENGTH);
    }
}

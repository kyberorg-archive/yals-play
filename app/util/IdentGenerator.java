package util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Generates unique combination for shortURL
 *
 * @since 1.0
 */
public class IdentGenerator {

    private static final int LENGTH = 6;
    public static final String VALID_IDENT_PATTERN = "[a-zA-Z]{1,"+LENGTH+"}";


    public static String generateNewIdent() {
        return RandomStringUtils.randomAlphabetic(LENGTH);
    }
}

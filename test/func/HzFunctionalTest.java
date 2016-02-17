package func;

import com.google.gson.Gson;
import org.junit.Ignore;
import org.junit.Test;
import play.test.FunctionalTest;

/**
 * Common for functional tests
 *
 * @since 1.0
 */
public class HzFunctionalTest extends FunctionalTest {
    protected static final String APPLICATION_JSON = "application/json";

    protected static final Gson gson = new Gson();

    @Test
    @Ignore
    public void stub() {
        assertTrue(true);
    }
}

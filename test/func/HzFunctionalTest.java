package func;

import com.google.gson.Gson;
import org.junit.Ignore;
import org.junit.Test;
import play.mvc.Http;
import play.test.FunctionalTest;

/**
 * Common for functional tests
 *
 * @since 1.0
 */
public class HzFunctionalTest extends FunctionalTest {
    //TODO move mimes to internal class
    protected static final String APPLICATION_JSON = "application/json";
    protected static final String TEXT_PLAIN = "text/plain";
    protected static final String IMAGE_X_ICON = "image/x-icon";
    protected static final String TEXT_CSS = "text/css";

    protected static final Gson gson = new Gson();

    @Test
    @Ignore
    public void stub() {
        assertTrue(true);
    }

    public static void assertContentNotEmpty(Http.Response response) {
        assertContentNotEmpty("Content is empty", response);
    }

    public static void assertContentNotEmpty(String message, Http.Response response) {

        boolean isBinaryContent = response.direct != null;

        if(isBinaryContent) {
            //has not NULL binary content
            assertTrue(true);
        } else {
            String body = response.out.toString();
            assertNotNull("No Content", body);
            assertTrue(message, body.trim().length() > 0);
        }
    }
}
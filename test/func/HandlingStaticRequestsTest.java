package func;

import org.junit.Test;
import play.mvc.Http;
import play.mvc.Http.Response;
import util.Paths;

/**
 * Handle Request to Static resources
 *
 * @since 1.0
 */
public class HandlingStaticRequestsTest extends HzFunctionalTest {

    @Test
    public void handlingStraitforwardRequestToCss() {
        Response response = GET(Paths.TEST_CSS);
        assertStatus(Http.StatusCode.OK, response);
        assertContentNotEmpty(response);
        assertContentType(TEXT_CSS, response);
    }

    @Test
    public void handlingRequestToUnknownLocation() {
        Response response = GET(Paths.UNKNOWN_LOCATION);
        assertStatus(Http.StatusCode.NOT_FOUND, response);
        assertContentNotEmpty(response);
    }

    @Test
    public void handlingNotGetRequestToUnknownLocation() {
        Response response = POST(Paths.UNKNOWN_LOCATION);
        assertStatus(Http.StatusCode.NOT_IMPLEMENTED, response);
        assertContentNotEmpty(response);
    }
}

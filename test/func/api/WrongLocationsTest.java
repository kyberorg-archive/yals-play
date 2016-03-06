package func.api;

import func.HzFunctionalTest;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Http.Response;

/**
 * Testing reaction when requesting not implemented yet API paths
 *
 * @since 1.0
 */
public class WrongLocationsTest extends HzFunctionalTest {

    @Test
    public void getApiSlash() {
        Response response = GET("/api/");
        testReaction(response);
    }

    @Test
    public void getApiWithoutEndingSlash() {
        Response response = GET("/api");
        testReaction(response);
    }

    @Test
    public void getSomeUnusedLocation() {
        Response response = GET("/api/habahaba");
        testReaction(response);
    }

    @Test
    public void postApiSlash() {
        Response response = POST("/api/");
        testReaction(response);
    }

    @Test
    public void postApiWithoutEndingSlash() {
        Response response = POST("/api");
        testReaction(response);
    }

    @Test
    public void postToSomeUnusedLocation() {
        Response response = POST("/api/habahaba");
        testReaction(response);
    }

    @Test
    public void putApiSlash() {
        Response response = PUT("/api/", APPLICATION_JSON, "");
        testReaction(response);
    }

    @Test
    public void putApiWithoutEndingSlash() {
        Response response = PUT("/api", APPLICATION_JSON, "");
        testReaction(response);
    }

    @Test
    public void putToSomeUnusedLocation() {
        Response response = PUT("/api/habahaba", APPLICATION_JSON, "");
        testReaction(response);
    }

    @Test
    public void deleteApiSlash() {
        Response response = DELETE("/api/");
        testReaction(response);
    }

    @Test
    public void deleteApiWithoutEndingSlash() {
        Response response = DELETE("/api");
        testReaction(response);
    }

    @Test
    public void deleteToSomeUnusedLocation() {
        Response response = DELETE("/api/habahaba");
        testReaction(response);
    }

    private void testReaction(Response response) {
        assertStatus(Http.StatusCode.NOT_IMPLEMENTED, response);
        assertContentNotEmpty(response);
        assertContentType(APPLICATION_JSON, response);
    }

}

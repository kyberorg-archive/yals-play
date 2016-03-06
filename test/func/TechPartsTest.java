package func;

import org.junit.Test;
import play.mvc.Http.Response;

/**
 * Robots, humans and favicon
 *
 * @since 1.0
 */
public class TechPartsTest extends HzFunctionalTest {

    @Test
    public void robotsTxtIsPresentAndText() {
        Response response = GET("/robots.txt");
        assertIsOk(response);
        assertContentNotEmpty("robots.txt are empty", response);
        assertContentType(TEXT_PLAIN, response);
        assertCharset(play.Play.defaultWebEncoding, response); //UTF-8
    }

    @Test
    public void humansTxtIsPresentAndText() {
        Response response = GET("/humans.txt");
        assertIsOk(response);
        assertContentNotEmpty("humans.txt are empty", response);
        assertContentType(TEXT_PLAIN, response);
        assertCharset(play.Play.defaultWebEncoding, response); //UTF-8
    }

    @Test
    public void faviconIsPresentAndIcon() {
        Response response = GET("/favicon.ico");
        assertIsOk(response);
        assertContentNotEmpty(response);
        assertContentType(IMAGE_X_ICON, response);
    }

}

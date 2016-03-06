package util;

import play.Play;
import play.mvc.Http;

/**
 * Calculates host:port server running at
 *
 * @since 0.0
 */
public class HostHelper {

    public static String getMyHostFromRequest(Http.Request request) {
        boolean hostPresent = request.host != null;
        boolean hostHeaderPresent = requestHasHostHeader(request);
        if(hostPresent) {
            return request.host;
        } else if(hostHeaderPresent) {
            return request.headers.get("Host").value();
        } else {
            //examine configuration
            String host = Play.configuration.getProperty("http.address", "127.0.0.1");
            String port = Play.configuration.getProperty("http.port", "9000");
            return host + ":" + port;
        }
    }

    private static boolean requestHasHostHeader(Http.Request request) {
        if(request.headers == null || request.headers.size() == 0) {
            return false;
        }

        return request.headers.containsKey("Host");
    }

}

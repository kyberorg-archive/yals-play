package controllers;

import com.google.common.base.Strings;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import struct.ErrorJson;

import java.io.File;

/**
 * Catches all unhandled request
 *
 * @since 1.0
 */
public class CatchAll extends Controller {

    public static void handleStatic() {
        String resource = request.path.replaceFirst("/", "");

        Logger.debug("Got a resource to handle: " + resource);
        if(Strings.isNullOrEmpty(resource)) {
            render404static();
        }
        File file = play.Play.getFile("public/" + resource);
        if(file.exists()) {
            response.cacheFor("24h");
            response.status = Http.StatusCode.OK;
            renderBinary(file);
        } else {
            render404static();
        }
    }

    public static void handleUnmappedApiRequests() {
        String method = request.method;
        String resource = request.path;

        Logger.debug("Got unmapped request to API (%s %s ), replying 501 as JSON", method, resource);
        response.status = Http.StatusCode.NOT_IMPLEMENTED;
        renderJSON(ErrorJson.init(("No such method implemented yet")));
    }

    public static void handleElseWhat(String everythingelse) {
        String method = request.method;
        String resource = request.path;

        Logger.debug("Got unmapped request to site (%s %s ), replying as 501", method, resource);
        response.status = Http.StatusCode.NOT_IMPLEMENTED;
        render("errors/501.html");
    }

    private static void render404static() {
        response.status = Http.StatusCode.NOT_FOUND;
        render("errors/404.html");
    }
}

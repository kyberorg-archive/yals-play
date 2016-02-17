package controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import play.Logger;
import play.db.jpa.NoTransaction;
import play.mvc.Controller;
import play.mvc.Http;
import struct.ErrorJson;
import struct.IdentReply;
import util.ErrorIdGenerator;
import util.Paths;

import java.util.Objects;

/**
 * Handles all unmapped requests
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess") //Must be public
public class Slash extends Controller {

    private static String objectRef;
    private static final Gson gson = new Gson();

    @NoTransaction
    public static void index(String ident) {
        Logger.debug("Got ident: " + ident);

        objectRef = ErrorIdGenerator.generate();
        HttpResponse<String> apiResponse;

        try {
            Logger.info("Searching for ident. Ident %s, ObjectRef: %s", ident, objectRef);
            String schema = request.secure ? "https://" : "http://";
            String URL = schema + request.host + Paths.API_GET_LINK + ident;
            Logger.info("Requesting API. URL: %s. ObjectRef: %s", URL, objectRef);
            apiResponse = Unirest.get(URL).asString();
        } catch(Exception e) {
            Logger.error("Exception while searching for link by ident. Ident: %s, ObjectRef: %s", ident, objectRef);
            Logger.error(e, "ObjectRef: %s", objectRef);
            response.status = Http.StatusCode.INTERNAL_ERROR;
            render("errors/500.html", e, objectRef);
            return;
        }

        if(Objects.isNull(apiResponse)) {
            Logger.error("No reply from API. ObjectRef: %s", objectRef);
            render("errors/500.html", objectRef);
            return;
        }

        switch(apiResponse.getStatus()) {
            case 200:
                Logger.info("Got long URL. Redirecting to %s. ObjectRef: %s",
                        extractLongUrl(apiResponse), objectRef);
                redirect(apiResponse);
                break;
            case 400:
                Logger.info("Got malformed request. Reply with 404. ObjectRef: %s", objectRef);
                render404();
                break;
            case 404:
                Logger.info("No corresponding longURL found. Reply with 404. ObjectRef: %s", objectRef);
                render404();
                break;
            case 500:
                Logger.info("Got internal error. Reply with 500. ErrorNo: %s. ObjectRef: %s",
                        extractErrorNo(apiResponse), objectRef);
                render500();
                break;
            default:
                Logger.info("Got unknown status: %d. I don't know how to handle it. " +
                                "Reply with 500. ObjectRef: %s",
                        apiResponse.getStatus(), objectRef);
                render500();
        }
    }

    private static void redirect(HttpResponse<String> response) {
        String longUrl = extractLongUrl(response);
        redirect(longUrl, true);
    }


    private static void render404() {
        render("errors/404.html");
    }

    private static void render500() {
        render("errors/500.html", objectRef);
    }

    private static String extractErrorNo(HttpResponse<String> response) {
        ErrorJson errorJson = gson.fromJson(response.getBody(), ErrorJson.class);
        return errorJson.getErrorId();
    }

    private static String extractLongUrl(HttpResponse<String> response) {
        IdentReply ident = gson.fromJson(response.getBody(), IdentReply.class);
        return ident.getLongUrl();
    }
}

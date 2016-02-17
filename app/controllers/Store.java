package controllers;

import com.google.gson.Gson;
import models.Link;
import play.Logger;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;
import struct.ErrorJson;
import struct.StoreReply;
import util.ErrorIdGenerator;
import util.IdentGenerator;

import java.util.Objects;

/**
 * Does all dirty store job
 *
 * @since 1.0
 */
public class Store extends Controller {

    @Transactional
    public static void store() {
        String body = request.params.get("body");

        boolean isBodyValid = validation.required(body).ok;
        if (!isBodyValid) {
            response.status = Http.StatusCode.BAD_REQUEST;
            renderJSON(ErrorJson.init("Body is empty"));
        }

        Gson gson = new Gson();
        struct.Store storeInput;
        try {
            storeInput = gson.fromJson(body, struct.Store.class);
        } catch (Exception e) {
            response.status = Http.StatusCode.BAD_REQUEST;
            renderJSON(ErrorJson.init("Unable to parse json"));
            return;
        }

        boolean isJsonValid = validation.valid(storeInput).ok;
        if (!isJsonValid) {
            response.status = Http.StatusCode.BAD_REQUEST;
            renderJSON(ErrorJson.init());
            return;
        }

        String ident;
        do {
            ident = IdentGenerator.generateNewIdent();
        } while (isIdentAlreadyExists(ident));

        Link link = Link.init();
        String transactionId = ErrorIdGenerator.generate();
        try {
            Logger.info("Saving new link. Ident: %s URL: %s ObjectRef: %s", ident, storeInput.longUrl, transactionId);
            boolean isStored = link.setIdent(ident).setLongUrl(storeInput.longUrl).validateAndSave();
            if (!isStored) {
                Logger.warn("Link was NOT stored. ObjectRef: " + transactionId);
                response.status = Http.StatusCode.INTERNAL_ERROR;
                renderJSON(ErrorJson.init(Messages.get("public.store.problem"))
                        .errorId(transactionId));
                return;
            }
        } catch (Exception e) {
            Logger.error(e, "Exception while saving Link to db. Ref: %s", transactionId);
            response.status = Http.StatusCode.INTERNAL_ERROR;
            renderJSON(ErrorJson.init(Messages.get("public.store.problem"))
                    .errorId(transactionId));
            return;
        }

        String shortUrl = makeShortUrl(ident);
        StoreReply storeReply = StoreReply.get().setShortUrl(shortUrl);
        response.status = Http.StatusCode.CREATED;
        renderJSON(storeReply);

    }

    private static String makeShortUrl(String ident) {
        if (Objects.nonNull(request.host)) {
            String proto = request.secure ? "https://" : "http://";
            return proto + request.host + "/" + ident;
        } else {
            StringBuilder urlBuilder = new StringBuilder();
            int port = 0;
            if (request.secure) {
                urlBuilder.append("https");
                if (request.port != 443) {
                    port = request.port;
                }
            } else {
                urlBuilder.append("http");
                if (request.port != 80) {
                    port = request.port;
                }
            }
            urlBuilder.append("://");
            urlBuilder.append(request.domain);
            if (port != 0) {
                urlBuilder.append(":").append(port);
            }
            urlBuilder.append("/");
            urlBuilder.append(ident);
            return urlBuilder.toString();
        }

    }

    public static boolean isIdentAlreadyExists(String ident) {
        Link record = Link.find("byIdent", ident).first();
        return Objects.nonNull(record);
    }



}

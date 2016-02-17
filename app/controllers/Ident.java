package controllers;

import models.Link;
import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import struct.ErrorJson;
import struct.IdentReply;
import util.IdentGenerator;

import java.util.Objects;

/**
 * Gets long URL by ident
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess") //Must be public. Used in routing
public class Ident extends Controller {

    @Transactional(readOnly = true)
    public static void getLongUrl(String ident) {

        boolean isIdentValid = validation.required(ident).message("public.ident.absent").ok &&
                validation.match(ident, IdentGenerator.VALID_IDENT_PATTERN).message("public.ident.mismatch").ok;

        if (!isIdentValid) {
            response.status = Http.StatusCode.BAD_REQUEST;
            renderJSON(ErrorJson.init());
            return;
        }
        String longUrl = null;
        try {
            Link link = Link.find("ident", ident).first();
            if(link != null) {
                longUrl = link.getLongUrl();
            }
        } catch (Exception e) {
            String errNo = Integer.toHexString(System.identityHashCode(e)).substring(1);
            Logger.error(e, e.toString(), "");
            response.status = Http.StatusCode.INTERNAL_ERROR;
            renderJSON(ErrorJson.init("Cannot query DB. Report this number to dev: " + errNo));
            return;
        }

        if (Objects.isNull(longUrl)) {
            response.status = Http.StatusCode.NOT_FOUND;
            renderJSON(ErrorJson.init("No link was found by this short URL"));
            return;
        }

        IdentReply reply = IdentReply.init().setLongUrl(longUrl);
        response.status = Http.StatusCode.OK;
        renderJSON(reply);
    }
}

package controllers;

import play.mvc.Controller;

/**
 * Main page
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess") //must be public
public class Front extends Controller {

    public static void index() {
        render();
    }

}
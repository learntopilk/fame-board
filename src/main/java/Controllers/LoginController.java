package Controllers;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session.*;
import Controllers.UserController.*;
import Webserver.Kayttaja;

/**
 *
 * @author Jonse
 */
public class LoginController {

    public static Route handleLoginPost(Request req, Response res, Kayttaja kayt) {

        if (UserController.authenticate(kayt)) {
            System.out.println("Sign-in successfully: " + kayt.getKayttajanimi());
            req.session(true);
            req.session().attribute("loggedOut", false);
            req.session().attribute("currentUser", kayt.getKayttajanimi());
            // Used for displaying welcoming message
            req.session().attribute("fresh", "true");
            res.redirect("/viestit");
        } else {
            System.out.println("Sign in failed: " + kayt.getKayttajanimi());
            res.redirect("/signinfailed");
        }

        return null;
    }

    public static void ensureUserIsLoggedIn(Request req, Response res) {

    }

    public static boolean userIsLogged(Request req) {
        if (req.session().attribute("loggedOut") == "true") {
            return false;
        } else if (req.session().attribute("currentUser") == null) {
            return false;
        }
        return true;
    }

    public static Route handleLogoutPost(Request req, Response res) {
        System.out.println("Logging out user");

        req.session().removeAttribute("currentUser");
        req.session().attribute("loggedOut", "true");
        System.out.println("user logged out.");
        req.session(false);

        res.redirect("/");
        return null;
    }
}

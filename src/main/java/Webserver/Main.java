package Webserver;

import DAOs.*;
import java.sql.SQLException;
import java.util.*;
import spark.*;
import spark.Spark;
import spark.Session;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
//import com.lambdaworks.crypto.SCrypt.*;
//import com.lambdaworks.crypto.SCryptUtil.*;
//import java.io.File;
import Webserver.PwdProcess.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        // Store for test hash
        String thisHash;
        
        // Tämä asettaa herokun portin ympäristömuuttujan määräämäksi,
        // jos ympäristömuuttuja on olemassa. Herokua varten tärkeä!
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        ViestiDao v = new ViestiDao();

        System.out.println("Server starting.");

        /**
         * R E I T I T
         */
        //Kirjautuminen
        Spark.post("/signin", (req, res) -> {
            String testableName = req.queryParams("username");
            String testablePwd = req.queryParams("password");
            System.out.println("user: " + testableName + ", pwd: " + testablePwd);

            String hashedPwd = PwdProcess.hash(testablePwd);
            System.out.println(hashedPwd);

            boolean signedIn = false;
            if (signedIn) {
                req.session(true);
                req.session().attribute("user", "1");
            }
            res.redirect("/viestit");
            return " ";
        });

        Spark.post("/signout", (req, res) -> {
            req.session(false);
            //delete(req.session());
            res.redirect("/");
            return " ";
        });

        Spark.post("/viesti", (req, res) -> {
            String ots = req.queryParams("header");
            String sis = req.queryParams("content");

            if (!v.saveOrUpdate(new Viesti(ots, sis))) {
                return "<h4>Something went wrong when you were trying to save your message!</h4>";
            }

            res.redirect("/viestit");
            return " ";
        });

        // Kaikkien viestien listaus
        Spark.get("/viestit", (req, res) -> {
            HashMap map = new HashMap<>();

            map.put("messages", v.findAll());
            return new ModelAndView(map, "allmessages");
        }, new ThymeleafTemplateEngine());

        Spark.get("/signup", (req, res) -> {
            HashMap map = new HashMap<>();

            // TODO: FIX THIS
            if (req.session() != null) {

            }
            return new ModelAndView(map, "signup");
        }, new ThymeleafTemplateEngine());

        Spark.post("/signup", (req, res) -> {
            //TODO: create new user

            // TODO
            String testableName = req.queryParams("username");
            String testablePwd = req.queryParams("password");
            System.out.println("user: " + testableName + ", pwd: " + testablePwd);

            String hashedPwd = PwdProcess.hash(testablePwd);
            //thisHash = hashedPwd;
            System.out.println(hashedPwd);

            return " ";
        });

        // "Catch-all" -reitti
        Spark.get("*", (req, res) -> {
            HashMap map = new HashMap<>();
            //map.put("raaka_aineet", raakaaineet);
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

    }

}

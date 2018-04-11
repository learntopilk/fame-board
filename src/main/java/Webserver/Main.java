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
import com.lambdaworks.crypto.SCryptUtil;

public class Main {

    public static void main(String[] args) throws SQLException {
        // Store for test hash

        List<String> l = new ArrayList<>();

        // Tämä asettaa herokun portin ympäristömuuttujan määräämäksi,
        // jos ympäristömuuttuja on olemassa. Herokua varten tärkeä!
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        ViestiDao v = new ViestiDao();
        KayttajaDao k = new KayttajaDao();

        System.out.println("Server starting.");

        /**
         * R E I T I T
         */
        //Kirjautuminen
        Spark.get("/login", (req, res) -> {
            HashMap map = new HashMap();
            return new ModelAndView(map, "signin");
        }, new ThymeleafTemplateEngine());

        Spark.post("/signin", (req, res) -> {
            String name = req.queryParams("username");
            String pwd = req.queryParams("password");
            //System.out.println("user: " + testableName + ", pwd: " + testablePwd);

            //String hashedPwd = PwdProcess.hash(testablePwd);
            //System.out.println(hashedPwd);
            Kayttaja kayt = k.findOne(name);

            if (kayt == null) {
                return ("<h4>Käyttäjänimi tai salasana on väärä! <br/></h4><a href=\"/\">Etusivulle</a>");
            }

            if (SCryptUtil.check(pwd, kayt.getSalasana())) {
                System.out.println("Sign-in successful!");
                req.session(true);
                req.session().attribute("user", kayt.getKayttajanimi());

            } else {
                System.out.println("Sign-in failed");
                return ("<h4>Salasana lienee väärä! <br/></h4><a href=\"/\">Etusivulle</a>");
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

        //
        //
        // Kaikkien viestien listaus
        Spark.get("/viestit", (req, res) -> {
            HashMap map = new HashMap<>();

            map.put("messages", v.findAll());
            return new ModelAndView(map, "allmessages");
        }, new ThymeleafTemplateEngine());

        //
        //
        // Käyttäjän luominen
        Spark.get("/signup", (req, res) -> {
            HashMap map = new HashMap<>();

            // TODO: FIX THIS
            if (req.session() != null) {

            }
            return new ModelAndView(map, "signup");
        }, new ThymeleafTemplateEngine());

        Spark.post("/signup", (req, res) -> {

            String Name = req.queryParams("username");
            String Pwd = req.queryParams("password");
            System.out.println("user: " + Name + ", pwd: " + Pwd);

            //Onko käyttäjänimi uniikki?
            if (k.findOne(Name) != null) {
                return ("<h4>Käyttäjänimi on jo olemassa! <br/>Kokeile toista käyttäjänimeä.</h4><a href=\"/\">Etusivulle</a>");
                //res.redirect("/signup");
            }

            String hashedPwd = PwdProcess.hash(Pwd);
            Kayttaja kay = new Kayttaja(Name, hashedPwd);
            // Onnistuuko tallennus?
            if (k.saveOrUpdate(kay)) {
                System.out.println("Added user: " + kay.getKayttajanimi());
                req.session(true);
                req.session().attribute("user", Name);
            } else {
                return ("<h4>Käyttäjän luominen ei onnistunut! <br/>Ota yhteyttä järjestelmänvalvojaan.</h4><a href=\"/\">Etusivulle</a>");
            }

            System.out.println(hashedPwd);
            res.redirect("/viestit");

            return " ";
        });

        //
        // "Catch-all" -reitti
        Spark.get("*", (req, res) -> {
            HashMap map = new HashMap<>();
            //map.put("raaka_aineet", raakaaineet);
            System.out.println(req.session().attributes());
            Set<String> attr = req.session().attributes();
            /*
            if (req.session().attribute("user")) {
                System.out.println("user attribute for session: " + req.session().attribute("user"));
            } else {
                System.out.println("No request session username found.");
            }*/
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

    }

}

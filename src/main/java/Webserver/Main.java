package Webserver;

import DAOs.*;
import java.sql.SQLException;
import java.util.*;
import spark.*;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import Controllers.LoginController;
import Controllers.ToolbarController;
import static spark.Spark.staticFileLocation;

public class Main {

    public static ViestiDao v;
    public static KayttajaDao k;
    //public static KeskusteluDao kesk;

    public static void main(String[] args) throws SQLException {
        
        // Init database connectivity
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        k = new KayttajaDao();
        //k.reset(); // REsetting Viesti for now
        //kesk = new KeskusteluDao();
        v = new ViestiDao();
        
        HashMap<String, String> profilePics = new HashMap<>();
        profilePics.put("kekkonen", "kekkonen.jpeg");
        profilePics.put("Some guy", "guy.jpg");
        
        System.out.println("Server starting.");
        
        staticFileLocation("/public");
        
        /**
         * R E I T I T
         */
        //Kirjautuminen
        Spark.get("/login", (req, res) -> {
            HashMap map = new HashMap();
            if(LoginController.userIsLogged(req)) {
                res.redirect("/profile");
            }
            map.put("toolbar", ToolbarController.parseToolbar(LoginController.userIsLogged(req)));
            return new ModelAndView(map, "signin");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/signin", (req, res) -> {
            String name = req.queryParams("username");
            String pwd = req.queryParams("password");
            Kayttaja kayt = new Kayttaja(name, pwd);
            //System.out.println("user: " + testableName + ", pwd: " + testablePwd);

            LoginController.handleLoginPost(req, res, kayt);

            return " ";
        });

        // HACK
        Spark.get("/signout", (req, res) -> {
            System.out.println("Signing out");
            LoginController.handleLogoutPost(req, res);
            return " ";
        });

        Spark.post("/viesti", (req, res) -> {

            String ots = req.queryParams("header");
            String sis = req.queryParams("content");
            String uri = req.queryParams("url");
            
            // Tsekataan pituusrajat
            // TODO: Muuta literaaleiksi ja vie tarkistus omaan moduuliinsa
            if (ots.length() > 160) {
                return "<h4>Otsikko liian pitkä!</h4>";
            } 
            if (sis.length() > 3000) {
                return "<h4>Message too long!</h4>";
            }
            if (uri.length() > 300) {
                return "<h4>Image URL too long!</h4>";
            }
            
            // TODO: Laita järjestelmä tarkistamaan, osoittaako URL kuvaan
            

            //TODO: Luo tätä varten oma aliohjelma
            // Jos kirjautunut sisään tallennetaan viesti ja päivitetään näkymä
            if (LoginController.userIsLogged(req)) {
                // Saving the message
                Viesti viesti = new Viesti(ots, sis);
                if (uri != null && uri.length() > 4) {
                    viesti.setKuvanURL(uri);
                } else {
                    viesti.setKuvanURL("./kekkonen.jpeg");
                }
                viesti.setLuoja(req.session().attribute("currentUser"));
                if (!v.saveOrUpdate(viesti)) {
                    return "<h4>Something went wrong when you were trying to save your message!</h4>";
                }
                // ja ei kun katsomaan viestejä!
                res.redirect("/viestit");

            } else {
                /* Jos ei kirjautunut sisään, ohjataan kirjautumaan ja tallennetaan kirjoitettu viesti
                    sessioattribuutiksi, jotta se voidaan laittaa viestinkirjoitussivulle, kun on kirjauduttu sisään
                 */
                req.session().attribute("ots", ots);
                req.session().attribute("sis", sis);
                res.redirect("/login");
            }
            return " ";

        });

        //
        // Kaikkien viestien listaus
        Spark.get("/viestit", (req, res) -> {
            HashMap map = new HashMap<>();

            if (req.session().attribute("fresh") == "true") {
                req.session().removeAttribute("fresh");
                String name = req.session().attribute("currentUser");
                String message = "Welcome, " + name + "!";
                map.put("welcome", message);
            }

            // If the user tried to post a message but was not logged in, we may have saved
            // the message so they are spared the work
            if (req.session().attribute("ots") != null) {
                // This could also be made persistent across multiple reloads, but just after login for now
                map.put("edelOts", req.session().attribute("ots"));
                map.put("edelSis", req.session().attribute("sis"));

                req.session().removeAttribute("ots");
                req.session().removeAttribute("sis");
            } else {

            }

            map.put("messages", v.findAll());
            map.put("toolbar", ToolbarController.parseToolbar(LoginController.userIsLogged(req)));
            return new ModelAndView(map, "allmessages");
        }, new ThymeleafTemplateEngine());

        //
        // Käyttäjän luominen
        Spark.get("/signup", (req, res) -> {
            HashMap map = new HashMap<>();

            // TODO: FIX THIS
            if (req.session() != null) {

            }
            map.put("toolbar", ToolbarController.parseToolbar(LoginController.userIsLogged(req)));
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

            // CHANGE THIS IN THE DATABASE
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
        
        Spark.get("/profile", (req, res) -> {
            HashMap map = new HashMap();
            if (LoginController.userIsLogged(req)) {
                
                List<Viesti> lis = new ArrayList<>();
                // Sorting is not working
                lis = v.findAllByUsername(req.session().attribute("currentUser"));
                Collections.sort(lis, (a, b) -> {
                    return b.compareTo(a);
                });
                
                map.put("viestit", lis);
                map.put("kayttaja", k.findOne(req.session().attribute("currentUser")));
                map.put("toolbar", ToolbarController.parseToolbar(LoginController.userIsLogged(req)));
                return new ModelAndView(map, "profile");
            } else {
                res.redirect("/login");
                return null;
            }
        }, new ThymeleafTemplateEngine());

        // "Catch-all" -reitti
        Spark.get("*", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("toolbar", ToolbarController.parseToolbar(LoginController.userIsLogged(req)));
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

    }

}

package Webserver;

import DAOs.*;
import java.sql.SQLException;
import java.util.*;
import spark.*;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
//import java.io.File;


public class Main {

    public static void main(String[] args) throws SQLException {

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
        
        Spark.post("/viesti", (req, res) -> {
            
            
        
        
        return " ";
        });
        
        Spark.get("/viestit", (req, res) -> {
        HashMap map = new HashMap<>();
            
        return new ModelAndView(map, "allmessages");
        }, new ThymeleafTemplateEngine());

        // "Catch-all" -reitti
        Spark.get("*", (req, res) -> {
            HashMap map = new HashMap<>();
            //map.put("raaka_aineet", raakaaineet);
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

    }

}

package Webserver;

import DAOs.*;
import java.sql.SQLException;
import java.util.*;
import spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.List;
//import spark.ModelAndView;
//import spark.TemplateEngine;
//import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws SQLException {

        AnnosDao annosDao = new AnnosDao();
        //annosDao.saveOrUpdate(new Annos(-1, "jauhelihakeitto", "kiehauta vesi, lisää ainekset"));
        //Annos keitto = annosDao.findByName("jauhelihakeitto");
        //System.out.println(annosDao.findByName("jauhelihakeitto"));
        //System.out.println(annosDao.findOne(new Annos(-2, "jauhelihakeitto", "")));
        //System.out.println("etsi kaikki:" + annosDao.findAll().toString());

        RaakaaineDao raakaDao = new RaakaaineDao();
        //raakaDao.saveOrUpdate(new Raaka_aine(0, "soijarouhe", "g", "ruskea soijarouhe, toimii siinä missä jauhelihakin"));
        //raakaDao.saveOrUpdate(new Raaka_aine(0, "jauheliha", "g", "jauheliha, toimii siinä missä soijarouhekin"));
        AnnosRaakaaineDao annosaineDao = new AnnosRaakaaineDao();
        //Integer annosid, Integer raakaaineid, Integer jarjestys, Integer maara, String ohje)
        //annosaineDao.saveOrUpdate(new AnnosRaakaaine(keitto.getId(), jauhelihat.get(0).getId(), 1, 400, "Paista ja lisää liemeen"));

        // Tämä asettaa herokun portin ympäristömuuttujan määräämäksi,
        // jos ympäristömuuttuja on olemassa. Herokua varten tärkeä!
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        System.out.println("Server starting.");

        // Välimuistissa oleva lisättävä resepti:
        //List<AnnosRaakaaine> reseptinCache = new ArrayList();
        HashMap<String, ArrayList<AnnosRaakaaine>> reseptinMapCache = new HashMap<>();
        /**
         * R E I T I T
         */
        // Tästä alkavat "oikeat" eli tuotantoreitit
        Spark.get("/raaka_aineet", (req, res) -> {

            HashMap map = new HashMap<>();
            List<Raaka_aine> raaka_aineet = new ArrayList<>();
            raaka_aineet = raakaDao.findAll();
            raaka_aineet.forEach(a -> {
                System.out.println("id:" + a.getId());
            });
            map.put("raaka_aineet", raaka_aineet);

            return new ModelAndView(map, "raaka-aine");
        }, new ThymeleafTemplateEngine());

        Spark.post("/raaka_aineet", (req, res) -> {
            // Lisätään raaka-aine tietokantaan

            String raakisNimi = req.queryParams("nimi");
            String raakisMittis = req.queryParams("mittayksikko");
            String raakisKuvaus = req.queryParams("kuvaus");

            Raaka_aine r = new Raaka_aine();
            r.setNimi(raakisNimi);
            r.setKuvaus(raakisKuvaus);
            r.setMittayksikko(raakisMittis);

            raakaDao.saveOrUpdate(r);

            res.redirect("/raaka_aineet");
            return " ";
        });

        Spark.post("/raaka_aineet/delete/:id", (req, res) -> {

            String id = req.params(":id");
            try {
                Raaka_aine dummy = new Raaka_aine();
                dummy.setId(Integer.parseInt(id));
                raakaDao.delete(dummy);
            } catch (Exception e) {
                System.out.println("Raaka-aineen delete: Yritettiin muuntaa id integeriksi siinä onnistumatta");
            }

            res.redirect("/raaka_aineet");
            return " ";
        });

        Spark.get("/reseptit", (req, res) -> {

            HashMap map = new HashMap<>();
            List<Annos> reseptit = new ArrayList<>();
            reseptit = annosDao.findAll();
            for (Annos annos : reseptit) {
                List<AnnosRaakaaine> aineet = new ArrayList<>();
                aineet.addAll(annosaineDao.findAll(annos.getId()));
                annos.setRaakaaineet(aineet);
            }
            map.put("reseptit", reseptit);

            return new ModelAndView(map, "reseptit");
        }, new ThymeleafTemplateEngine());

        Spark.post("/resepti/:id", (req, res) -> {

            HashMap map = new HashMap<>();
            String id = req.params(":id");
            Annos annos = new Annos();
            List<AnnosRaakaaine> aineet = new ArrayList<>();
            try {
                annos = (Annos) annosDao.findById(Integer.parseInt(id));
                System.out.println("valmistusohje-rlog: " + annos.getValmistusohje());
                aineet.addAll(annosaineDao.findAll(annos.getId()));
                annos.setRaakaaineet(aineet);
            } catch (Exception e) {
                System.out.println("Reseptin haku: Yritettiin muuntaa id integeriksi siinä onnistumatta");
            }
            map.put("resepti", annos);
            map.put("aineet", aineet);
            return new ModelAndView(map, "resepti");
        }, new ThymeleafTemplateEngine());

        Spark.post("/reseptit/delete/:id", (req, res) -> {

            String id = req.params(":id");
            try {
                int dummy = Integer.parseInt(id);
                annosDao.delete(dummy, annosaineDao);
            } catch (Exception e) {
                System.out.println("Reseptin delete: Yritettiin muuntaa id integeriksi siinä onnistumatta");
            }

            res.redirect("/reseptit");
            return " ";
        });

        Spark.get("/annosraakaaine", (req, res) -> {

            HashMap map = new HashMap<>();
            List<Integer> ARs = new ArrayList<>();
            ARs = annosaineDao.findAll();

            map.put("annosraakaaineet", ARs);
            return new ModelAndView(map, "annosraakaaine");
        }, new ThymeleafTemplateEngine());

        Spark.get("/lisaaresepti", (req, res) -> {

            String sessio = req.session().id();
            System.out.println("sessio:" + sessio);
            ArrayList<AnnosRaakaaine> reseptinCache = new ArrayList<>();
            if (!reseptinMapCache.isEmpty()) {
                if (reseptinMapCache.containsKey(sessio)) {
                    reseptinCache = reseptinMapCache.get(sessio);
                }
            }

            HashMap map = new HashMap<>();
            List<Raaka_aine> a = raakaDao.findAll();

            map.put("edelliset", reseptinCache);
            map.put("raaka_aineet", a);
            return new ModelAndView(map, "lisaaresepti");
        }, new ThymeleafTemplateEngine());

        Spark.post("/raaka_ainereseptiin", (req, res) -> {

            String sessio = req.session().id();
            System.out.println("sessio:" + sessio);
            ArrayList<AnnosRaakaaine> reseptinCache = new ArrayList<>();

            // JOS on painettu painiketta "valmis"
            if (req.queryParams("end") != null) {

                /* Kommentoitu pois, jotta sessiokohtainen cache
                if (reseptinCache.size() < 1) {
                    res.redirect("/lisaaresepti");
                    return " ";
                }
                 */
                if (!reseptinMapCache.isEmpty()) {
                    if (!reseptinMapCache.containsKey(sessio)) {
                        res.redirect("/lisaaresepti");
                        return " ";
                    }
                }
                reseptinCache = reseptinMapCache.get(sessio);

                System.out.println("Starting recipe saving procedure.");

                String nimi = req.queryParams("resNimi");

                String kuvaus = req.queryParams("resKuvaus");
                System.out.println("Kalasteltu kuvaus: " + kuvaus);

                Annos a = new Annos();
                a.setNimi(nimi);
                a.setValmistusohje(kuvaus);

                System.out.println("objektiin injektoitu kuvaus: " + a.getValmistusohje());
                annosDao.saveOrUpdate(a);

                Annos currentAnnos = annosDao.findOne(a);

                for (int i = 0; i < reseptinCache.size(); i++) {
                    AnnosRaakaaine r = reseptinCache.get(i);
                    AnnosRaakaaine ar = new AnnosRaakaaine();
                    ar.setAnnosId(currentAnnos.getId());
                    ar.setRaakaaineId(r.getRaakaaineId());
                    ar.setOhje(r.getOhje());
                    ar.setMaara(r.getMaara());
                    ar.setJarjestys(i);

                    try {
                        annosaineDao.saveOrUpdate(ar);
                    } catch (SQLException e) {
                        System.out.println("Jokin meni pieleen reseptia tallennettaessa: " + e);
                    }

                }

                System.out.println("Ending recipe saving procedure.");

                reseptinMapCache.remove(sessio);
                //reseptinCache.clear();
                res.redirect("/reseptit");
                return " ";

            } else {
                // reseptinCache:een tallentaminen
                System.out.println("Starting recipe updating procedure.");
                System.out.println(req.queryParams());
                System.out.println(req.queryParams("raakaaine"));

                AnnosRaakaaine r = new AnnosRaakaaine();
                r.setOhje(req.queryParams("ohje"));
                System.out.println("ohje: " + r.getOhje());
                int maara = 0;
                try {
                    maara = Integer.parseInt(req.queryParams("maara"));
                } catch (NumberFormatException e) {
                    maara = 0;
                }

                //Nimi talteen:
                String nimi = req.queryParams("resNimi");

                r.setMaara(maara);
                // Väliaikainen raaka-aineolio etsintää varten
                Raaka_aine raak = new Raaka_aine();
                int id = Integer.parseInt(req.queryParams("raakaaine"));
                raak.setId(id);

                Raaka_aine realRaak = raakaDao.findOne(id);
                r.setRaakaaineenNimi(realRaak.getNimi());
                r.setRaakaaineId(id);
                r.setRaakaaineenMittayksikko(realRaak.getMittayksikko());

                System.out.println("Ending recipe updating procedure.");
                if (!reseptinMapCache.isEmpty()) {
                    if (reseptinMapCache.containsKey(sessio)) {
                        reseptinCache = reseptinMapCache.get(sessio);
                    }
                }
                reseptinCache.add(r);
                reseptinMapCache.put(sessio, reseptinCache);
                res.redirect("/lisaaresepti");
            }
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

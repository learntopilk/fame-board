package DAOs;

import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import static Webserver.Database.getConnection;

import Webserver.Viesti;
import java.util.ArrayList;

public class ViestiDao implements Dao {

    public ViestiDao() throws SQLException {

        try {
            Connection conn = getConnection();

            //PreparedStatement tableCreator = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Viesti("
            PreparedStatement tableCreator = conn.prepareStatement("CREATE TABLE Viesti("
                    + " id SERIAL PRIMARY KEY, "
                    + " otsikko varchar(160),"
                    + " sisalto varchar(3000), "
                    // CHANGE THIS: Long instead of date
                    + " luomisaika bigint,"
                    + " url_kuva varchar(300), "
                    + " kayttajanNimi varchar(40),"
                    //+ " keskustelu_id integer,"
                    + " kayttaja_id integer,"
                    //+ " FOREIGN KEY(keskustelu_id) REFERENCES Keskustelu(id),"
                    + " FOREIGN KEY(kayttaja_id) REFERENCES Kayttaja(id))");

            tableCreator.executeUpdate();
            tableCreator.close();
            System.out.println("ViestiDao initialized.");

        } catch (Exception e) {
            System.out.println("Problem creating or initializing ViestiDao: " + e);
        }

    }

    @Override
    public Viesti findOne(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List findAllByKeskustelu(int keskusteluId) {
        List<Viesti> viestit = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement getter = conn.prepareStatement("SELECT * FROM Viesti WHERE keskustelu_id = ?");
            getter.setInt(0, keskusteluId);

            ResultSet rs = getter.executeQuery();

            // Viesti(String otsikko, String sisalto, int id, long time)
            if (rs.next()) {
                do {
                    Viesti v = new Viesti(rs.getString("otsikko"), rs.getString("sisalto"), rs.getInt("id"), rs.getLong("luomisaika"));
                    String url = rs.getString("url_kuva");
                    if (url != null && url.length() > 1) {
                        v.setKuvanURL(url);
                    } else {
                        v.setKuvanURL("guy.jpg");
                    }
                    //v.setKuvanURL("kekkonen.jpeg");
                    v.setId(rs.getInt("id"));
                    v.setKeskustelu_id(keskusteluId);
                    v.setLuoja(rs.getString("kayttajanNimi"));
                    v.setKayttajaId(rs.getInt("kayttaja_id"));

                    viestit.add(v);

                } while (rs.next());

            }

        } catch (Exception e) {
            System.out.println("Something went wrong in findAllByKeskustelu: " + e);
        }

        return viestit;
    }

    @Override
    public List findAll() throws SQLException {
        List<Viesti> a = new ArrayList<>();

        try {
            Connection conn = getConnection();

            PreparedStatement getter = conn.prepareStatement("SELECT * FROM Viesti;");

            ResultSet rs = getter.executeQuery();

            while (rs.next()) {
                String ots = rs.getString("otsikko");
                String sis = rs.getString("sisalto");
                String tek = rs.getString("kayttajanNimi");
                Viesti v = new Viesti(ots, sis);
                v.setLuoja(tek);
                v.setId(rs.getInt("id"));
                v.setPaiva(rs.getLong("luomisaika"));
                
                String url = rs.getString("url_kuva");
                if (url != null && url.length() > 4) {
                    if (url.contains("https://")) {
                        v.setKuvanURL(url);
                    } else {
                        v.setKuvanURL("./" + url);
                    }
                } else {
                    v.setKuvanURL("./kekkonen.jpeg");
                }
                a.add(v);
                
            }

            rs.close();
            getter.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Problem saving message: " + e);
        }

        return a;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List findAllByUsername(String username) {
        List<Viesti> l = new ArrayList<>();
        try {
            Connection conn = getConnection();

            PreparedStatement prep = conn.prepareStatement("SELECT * FROM Viesti WHERE kayttajanNimi=?");
            prep.setString(1, username);

            ResultSet rs = prep.executeQuery();

            while (rs.next()) {
                String ots = rs.getString("otsikko");
                String sis = rs.getString("sisalto");
                String tek = rs.getString("kayttajanNimi");
                Viesti v = new Viesti(ots, sis);
                v.setLuoja(tek);
                v.setId(rs.getInt("id"));
                v.setPaiva(rs.getLong("luomisaika"));
                
                String url = rs.getString("url_kuva");
                System.out.println(url);
                if (url != null && url.length() > 4) {
                    System.out.println(url.contains("https://"));
                    System.out.println(url.startsWith("http"));
                    
                    if (url.contains("https://") || url.startsWith("http")) {
                        v.setKuvanURL(url);
                    } else {
                        v.setKuvanURL("./" + url);
                        System.out.println();
                    }
                } else {
                    v.setKuvanURL("./kekkonen.jpeg");
                }
                l.add(v);
                
            }
        } catch (Exception e) {
            System.out.println("Error in findAllByUsername: " + e);
        }
        return l;
    }

    public List findAllByUserId(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveOrUpdate(Object viesti) throws SQLException {
        try {
            Viesti v = (Viesti) viesti;
            Connection conn = getConnection();

            PreparedStatement saver = conn.prepareStatement("INSERT INTO Viesti("
                    + "otsikko, "
                    + "sisalto, "
                    + "luomisaika, "
                    + "url_kuva, "
                    + "kayttajanNimi,"
                    // + "keskustelu_id,"
                    + "kayttaja_id) "
                    + "VALUES(?,?,?,?,?,?)");

            saver.setString(1, v.getOtsikko());
            saver.setString(2, v.getSisalto());
            saver.setLong(3, v.getLuomisaika());
            saver.setString(4, v.getKuvanURL());

            saver.setString(5, v.getLuoja());
            //saver.setInt(6, v.getKeskustelu_id());
            saver.setInt(6, v.getKayttajaId()); // would be 7th if we had a keskustelu to refer to

            saver.executeUpdate();

            saver.close();
            conn.close();

            System.out.println("Saved message with title " + v.getOtsikko());
            return true;

        } catch (Exception e) {
            System.out.println("Problem in saveOrUpdate in ViestiDao.java: " + e);
            return false;
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

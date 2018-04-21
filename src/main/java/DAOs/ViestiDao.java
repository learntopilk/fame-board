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

            PreparedStatement tableCreator = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Viesti("
                    + " id SERIAL PRIMARY KEY, "
                    + " otsikko varchar(160),"
                    + " sisalto varchar(3000), "
                    // CHANGE THIS: Long instead of date
                    + " luomisaika bigint,"
                    + " url_kuva varchar(300), "
                    + " kayttajanNimi varchar(40),"
                    + " keskustelu_id integer,"
                    + " kayttaja_id integer,"
                    + " FOREIGN KEY(keskustelu_id) REFERENCES Keskustelu(id),"
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
                    if (url.length() > 0) {
                        v.setKuvanURL(url);
                    }
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
        throw new UnsupportedOperationException("Not supported yet.");
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
                    + "keskustelu_id,"
                    + "kayttaja_id) "
                    + "VALUES(?,?,?,?,?,?,?)");

            saver.setString(1, v.getOtsikko());
            saver.setString(2, v.getSisalto());
            saver.setLong(3, v.getLuomisaika());
            saver.setString(4, v.getKuvanURL());
            
            saver.setString(5, v.getLuoja());
            saver.setInt(6, v.getKeskustelu_id());
            saver.setInt(7, v.getKayttajaId());

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

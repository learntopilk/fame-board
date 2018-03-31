
package DAOs;

import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import static Webserver.Database.getConnection;

import Webserver.Kayttaja;


public class KayttajaDao implements Dao {
    
    public KayttajaDao(){
        try {
            Connection conn = getConnection();
            
            PreparedStatement init = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Kayttaja("
                    + "id SERIAL PRIMARY KEY,"
                    + "kayttajatunnus varchar(30),"
                    + "salasana varchar(50),"
                    + "luomisaika bigint)");
            
            init.executeUpdate();
            init.close();
            conn.close();
            
        } catch (Exception e){
            System.out.println("Issue starting up KayttajaDao: " + e);
        }
    }

    @Override
    public Kayttaja findOne(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Kayttaja findOne(String s) throws SQLException {
            Kayttaja k;
        try {
            Connection conn = getConnection();
            PreparedStatement finder = conn.prepareStatement("SELECT * FROM Kayttaja WHERE kayttajatunnus=?");
            finder.setString(0, s);
            
            ResultSet rs = finder.executeQuery();
            
            if (rs.next()) {
                k = new Kayttaja(s, rs.getString("salasana"));
                k.setLuomisaika(rs.getLong("luomisaika"));
            } else {
                k = null;
            }
            
            
            rs.close();
            finder.close();
            conn.close();
                    
        } catch (SQLException e) {
            System.out.println("Exception in findOne in KayttajaDao.java: " + e);
            k = null;
        }
        return k;
    }

    @Override
    public List findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean saveOrUpdate(Object obj) throws SQLException {
        
        try {
            Kayttaja kayt = (Kayttaja) (obj);
            Connection conn = getConnection();
            
            PreparedStatement saver = conn.prepareStatement("INSERT INTO Kayttaja(kayttajatunnus, salasana, luomisaika) "
                    + "VALUES (?,?,?)");
            
            saver.setString(0, kayt.getKayttajanimi());
            saver.setString(1, kayt.getSalasana());
            saver.setLong(2, System.currentTimeMillis());
            
            saver.execute();
            
            saver.close();
            conn.close();
            
            return true;
        }
        catch (Exception e){
           System.out.println("Exception saving user in kayttajadao: " + e);
           return false;
        }
    }

    @Override
    public void delete(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

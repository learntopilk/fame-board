package DAOs;

import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import static Webserver.Database.getConnection;

import Webserver.Viesti;

public class ViestiDao implements Dao {

    @Override
    public Viesti findOne(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List findAllByUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List findAllByUserId(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveOrUpdate(Object viesti) throws SQLException {
        Connection conn = getConnection();
        
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

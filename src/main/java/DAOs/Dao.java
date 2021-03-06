package DAOs;

import java.util.*;
import java.sql.*;

public interface Dao<T, K> {

    T findOne(K key) throws SQLException;
    
    //T findByString(String s) throws SQLException;

    List<T> findAll() throws SQLException;

    Boolean saveOrUpdate(T object) throws SQLException;

    void delete(K key) throws SQLException;

}


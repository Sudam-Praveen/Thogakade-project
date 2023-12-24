package DAO.util;

import DB.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    private static CrudUtil crudUtil;
    private  Connection connection;
    private CrudUtil() throws SQLException, ClassNotFoundException {
          connection = DBConnection.getInstance().getConnection();

    }
    public static CrudUtil getCrudUtil() throws SQLException, ClassNotFoundException {
        return crudUtil != null ? crudUtil : (crudUtil=new CrudUtil());
    }
    public  <T>T execute(String sql,Object...args) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = connection.prepareStatement(sql);
        if(sql.startsWith("SELECT") || sql.startsWith("select")){
            ResultSet resultSet = pstm.executeQuery();
            return (T)resultSet;
        }
        for (int i = 0; i < args.length; i++) {
            pstm.setObject((i+1),args[i]);
        }
        return (T)(Boolean)(pstm.executeUpdate()>0);
    }
}

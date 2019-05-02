package com.data;

import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceConnection {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DataSourceConnection.class);
    Connection connection = null;


    public Connection getOracleConnection(PropertiesPojo pojo){
        try {
            // Load the Oracle JDBC driver
            String OracledriverName = pojo.getOracledriverName();
            // Register class
            Class.forName(OracledriverName);
            // Create a connection to the database
            String url = pojo.getOracleurl() + pojo.getOracleserverName() + ":" + pojo.getOracleserverPort() + ":" + pojo.getOraclesid();
            String username = pojo.getOracleusername();
            String password = pojo.getOraclepassword();
            connection = DriverManager.getConnection(url, username, password);
            logger.info("Successfully Connected to the Oracle database!");
        } catch (ClassNotFoundException e) {
            logger.error("Could not find the oracle database driver \n" + e.getMessage());
        } catch (SQLException ex) {
            logger.error("Could not connect to the oracle database \n" + ex.getMessage());
        }

        return connection;
    }

    public Connection getSybaseConnection(PropertiesPojo pojo) {
        try{
            Properties props = new Properties();
            props.put("user", pojo.getSybaseuser());
            props.put("password", pojo.getSybasepassword());
            props.put("charset", pojo.getSybasecharset());
            // Load the Oracle JDBC driver
            String SybasedriverName = pojo.getSybasedriver();
            // Register class
            Class.forName(SybasedriverName);
            // Create a connection to the database
            String dburl = String.valueOf(String.valueOf(new StringBuffer(pojo.getSybaseurl()).append(pojo.getSybasehost()).append(":").append(pojo.getSybaseport())))+"/" + pojo.getSybasedbname();
            connection = DriverManager.getConnection(dburl, props);
            logger.info("Successfully Connected to the Sybase database!");
        } catch (ClassNotFoundException e) {
            logger.error("Could not find the sybase database driver \n" + e.getMessage());
        } catch (SQLException ex) {
            logger.error("Could not connect to the sybase database \n" + ex.getMessage());
        }

        return connection;
    }

    public void closeConnection(){
        if(connection != null){
            try{
                connection.close();
                logger.info("Connection successfully closed.");
            }catch (Exception ex){
                logger.error("Could not close connection. \n" + ex.getMessage());
            }

        }
    }
}

package PaymentEmail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class loadProps {

    String DEFAULT_DATABASE_USER;
    String DEFAULT_DATABASE_PASSWORD;
    String DEFAULT_DATABASE_SERVER;
    String DEFAULT_DATABASE_DRIVER;
    String DEFAULT_DATABASE_PORT;
    String DEFAULT_DATABASE_CHARSET;
    String DEFAULT_DATABASE_URL;
    String DEFAULT_DATABASE_DBNAME;
    String DEFAULT_APP_MODE;
    String DEFAULT_SEND_TO_DEV;
    String DEFAULT_BEN_DEV;
    private Connection con;
    boolean bConnected = false;


    public loadProps() {

             try{

                    Properties prop = ProperLoader.getProps();

                    this.DEFAULT_DATABASE_USER = prop.getProperty("database.user");
                    this.DEFAULT_DATABASE_PASSWORD = prop.getProperty("database.password");
                    this.DEFAULT_DATABASE_SERVER = prop.getProperty("database.host");
                    this.DEFAULT_DATABASE_DRIVER = prop.getProperty("database.driver");
                    this.DEFAULT_DATABASE_PORT = prop.getProperty("database.port");
                    this.DEFAULT_DATABASE_CHARSET = prop.getProperty("database.charset");
                    this.DEFAULT_DATABASE_DBNAME = prop.getProperty("database.dbname");
                    this.DEFAULT_APP_MODE = prop.getProperty("app.mode");
                    this.DEFAULT_BEN_DEV = prop.getProperty("mail.dev.ben");
                    this.DEFAULT_SEND_TO_DEV = prop.getProperty("mail.dev.sendto");
                    this.DEFAULT_DATABASE_URL = prop.getProperty("database.url");

                    //in.close();
                }
            catch (Exception e) {
                    throw new RuntimeException("Could not read the notifications.config file.\r\n".concat(String.valueOf(String.valueOf(e.toString()))));
                }

                try
                {
                    Class.forName(this.DEFAULT_DATABASE_DRIVER);
                }
                catch (ClassNotFoundException e) {
                    System.err.print("ClassNotFoundException: ");
                    System.err.println(e.getMessage());
                    Logger.getLogger( "DBConnect Constructor:  ".concat(String.valueOf(String.valueOf(e.toString()))));
                }

                try
                {
                    Properties props = new Properties();

                    String hostName = this.DEFAULT_DATABASE_SERVER;
                    new Integer(this.DEFAULT_DATABASE_PORT);
                    int port = Integer.parseInt(this.DEFAULT_DATABASE_PORT);
                    String url = this.DEFAULT_DATABASE_URL;

                    System.out.println("Connecting...");
                    props.put("user", this.DEFAULT_DATABASE_USER);
                    props.put("password", this.DEFAULT_DATABASE_PASSWORD);
                    props.put("charset", this.DEFAULT_DATABASE_CHARSET);

                    System.out.println("Host...".concat(String.valueOf(String.valueOf(hostName))));
                    System.out.println("Port...".concat(String.valueOf(String.valueOf(port))));

                    this.DEFAULT_DATABASE_SERVER = String.valueOf(String.valueOf(new StringBuffer(url).append(hostName).append(":").append(port)))+"/" + DEFAULT_DATABASE_DBNAME;
                    this.con = DriverManager.getConnection(this.DEFAULT_DATABASE_SERVER, props);
                    this.bConnected = true;

                    System.out.println(" Connection " + this.con);
                    System.out.println("Database Connection successful");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    Logger.getLogger("DBConnect Constructor: (com.sybase.jdbc2.jdbc.SybDriver) ".concat(String.valueOf(String.valueOf(e.toString()))));
                }

    }

    public  Connection getInstance() {

        if(this.bConnected){
            return this.con;
        }
         return null;
    }
}

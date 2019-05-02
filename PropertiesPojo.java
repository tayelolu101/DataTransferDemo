package com.data;

import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class PropertiesPojo {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PropertiesPojo.class);

    public String getOracledriverName() {
        return oracledriverName;
    }

    public void setOracledriverName(String oracledriverName) {
        this.oracledriverName = oracledriverName;
    }

    public String getOracleserverName() {
        return oracleserverName;
    }

    public void setOracleserverName(String oracleserverName) {
        this.oracleserverName = oracleserverName;
    }

    public int getOracleserverPort() {
        return oracleserverPort;
    }

    public void setOracleserverPort(int oracleserverPort) {
        this.oracleserverPort = oracleserverPort;
    }

    public String getOraclesid() {
        return oraclesid;
    }

    public void setOraclesid(String oraclesid) {
        this.oraclesid = oraclesid;
    }

    public String getOracleusername() {
        return oracleusername;
    }

    public void setOracleusername(String oracleusername) {
        this.oracleusername = oracleusername;
    }

    public String getOracleurl() {
        return oracleurl;
    }

    public void setOracleurl(String oracleurl) {
        this.oracleurl = oracleurl;
    }

    public String getOraclepassword() {
        return oraclepassword;
    }

    public void setOraclepassword(String oraclepassword) {
        this.oraclepassword = oraclepassword;
    }

    public String getOracledbname() {
        return oracledbname;
    }

    public void setOracledbname(String oracledbname) {
        this.oracledbname = oracledbname;
    }

    public String getSybasehost() {
        return sybasehost;
    }

    public void setSybasehost(String sybasehost) {
        this.sybasehost = sybasehost;
    }

    public int getSybaseport() {
        return sybaseport;
    }

    public void setSybaseport(int sybaseport) {
        this.sybaseport = sybaseport;
    }

    public String getSybasedbname() {
        return sybasedbname;
    }

    public void setSybasedbname(String sybasedbname) {
        this.sybasedbname = sybasedbname;
    }

    public String getSybasecharset() {
        return sybasecharset;
    }

    public void setSybasecharset(String sybasecharset) {
        this.sybasecharset = sybasecharset;
    }

    public String getSybasedriver() {
        return sybasedriver;
    }

    public void setSybasedriver(String sybasedriver) {
        this.sybasedriver = sybasedriver;
    }

    public String getSybaseurl() {
        return sybaseurl;
    }

    public void setSybaseurl(String sybaseurl) {
        this.sybaseurl = sybaseurl;
    }

    public String getSybaseuser() {
        return sybaseuser;
    }

    public void setSybaseuser(String sybaseuser) {
        this.sybaseuser = sybaseuser;
    }

    public String getSybasepassword() {
        return sybasepassword;
    }

    public void setSybasepassword(String sybasepassword) {
        this.sybasepassword = sybasepassword;
    }


    public String getOraclequery() {
        return oraclequery;
    }

    public void setOraclequery(String oraclequery) {
        this.oraclequery = oraclequery;
    }

    public String getSybasequery() {
        return sybasequery;
    }

    public void setSybasequery(String sybasequery) {
        this.sybasequery = sybasequery;
    }

    private String oracledriverName;
    private String oracleserverName;
    private int oracleserverPort;
    private String oraclesid;
    private String oracleusername;
    private String oracleurl;
    private String oraclepassword;
    private String oracledbname;
    private String oraclequery;
    private String sybasehost;
    private int sybaseport;
    private String sybasedbname;
    private String sybasecharset;
    private String sybasedriver;
    private String sybaseurl;
    private String sybaseuser;
    private String sybasepassword;
    private String sybasequery;




    public PropertiesPojo getPropDetails(){

        PropertiesPojo propPojo = new PropertiesPojo();
        File configFile = null;
        Properties properties = null;
        FileInputStream in = null;
        String workingDir = System.getProperty("user.dir");
        String fileConfigLocation = workingDir  + "\\" + "FileProperties.config";

        try{
            if (properties == null) {
                properties  = new Properties();

                try {
                    System.out.println("====== 1 ==========");
                    ClassLoader classLoader = PropertiesPojo.class.getClassLoader();
                    System.out.println(fileConfigLocation);
                    configFile = new File(fileConfigLocation);
                    if (!configFile.exists()) {
                        System.out.println("====== 2 ==========");
                        configFile = new File(classLoader.getResource("FileProperties.config").getFile());
                    }
                    in = new FileInputStream(configFile);
                    properties.load(in);

                    propPojo.setOracledbname(properties.getProperty("oracle.dbname"));
                    propPojo.setOracledriverName(properties.getProperty("oracle.driverName"));
                    propPojo.setOraclepassword(properties.getProperty("oracle.password"));
                    propPojo.setOracleserverName(properties.getProperty("oracle.serverName"));
                    propPojo.setOracleserverPort(Integer.parseInt(properties.getProperty("oracle.serverPort")));
                    propPojo.setOraclesid(properties.getProperty("oracle.sid"));
                    propPojo.setOracleurl(properties.getProperty("oracle.url"));
                    propPojo.setOracleusername(properties.getProperty("oracle.username"));
                    propPojo.setOraclequery(properties.getProperty("oracle.query"));
                    propPojo.setSybasecharset(properties.getProperty("sybase.charset"));
                    propPojo.setSybasedbname(properties.getProperty("sybase.dbname"));
                    propPojo.setSybasedriver(properties.getProperty("sybase.driver"));
                    propPojo.setSybasehost(properties.getProperty("sybase.host"));
                    propPojo.setSybasepassword(properties.getProperty("sybase.password"));
                    propPojo.setSybaseport(Integer.parseInt(properties.getProperty("sybase.port")));
                    propPojo.setSybaseurl(properties.getProperty("sybase.url"));
                    propPojo.setSybaseuser(properties.getProperty("sybase.user"));
                    propPojo.setSybasequery(properties.getProperty("sybase.query"));

                    logger.info("Done getting properties.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Problem loading config file.");
                    return null;
                }
            }

        }catch (Exception ex){
           ex.printStackTrace();
           logger.error("An Error reading properties file.");
           if(in != null){
               try{
                   in.close();
               }catch (Exception x){
                   x.printStackTrace();
               }
           }
        }
        return propPojo;
    }
}

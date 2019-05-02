package PaymentEmail;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ProperLoader {

    private static Properties properties = null;
    private static String workingDir = System.getProperty("user.dir");
    private static  String fileConfigLocation = workingDir + "\\" + "notification.config";
    public static Properties getProps(){

        if(properties == null){
            Properties prop = new Properties();

            try{

                System.out.println("====== 1 ==========");
                ClassLoader classLoader = ProperLoader.class.getClassLoader();
                System.out.println(fileConfigLocation);
                File configFile = new File(fileConfigLocation);
               // System.out.println("====== 2 ==========");
                if(!configFile.exists()){
                    System.out.println("====== 3 ==========");
                    configFile = new File(classLoader.getResource("notification.config").getFile());
                }
                //System.out.println("====== 4 and half ==========");
                System.out.println(" =================> Using config file in  " + configFile.getAbsolutePath() + " <=====================");
               // System.out.println("====== 4 and half half==========");
                FileInputStream in = new FileInputStream(configFile);
               // System.out.println("====== 4 and half half half==========");
                prop.load(in);
                //System.out.println("====== 4 ==========");
                properties = prop;
                //System.out.println("====== 5 ==========");
                //return prop;
            }catch (Exception ex){
                ex.printStackTrace();
                Logger.getLogger("Problem loading config file");
                return null;
            }
        }
        return properties;

    }
}

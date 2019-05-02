package com.data;

import org.quartz.Job;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ExecuteDataMigration implements Job{

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ExecuteDataMigration.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Connection con1 = null;
        Connection con2 = null;
        try{

            PropertiesPojo ppjo = new PropertiesPojo();
            PropertiesPojo property = ppjo.getPropDetails();
            logger.info("Done getting details of properties.");

            DataTransferDetails dtd = new DataTransferDetails();
            DataSourceConnection dataSourceConnection = new DataSourceConnection();

            con1 = dataSourceConnection.getSybaseConnection(property);
            logger.info("Done getting sybase connection.");
            List<TableColumnPojo> sybaseData = dtd.getSybasedetails(property, con1);
            dataSourceConnection.closeConnection();
            logger.info("Done getting data from sybase.");

            if(sybaseData != null && sybaseData.size() > 0) {
                con2 = dataSourceConnection.getOracleConnection(property);
                logger.info("Done getting oracle connection.");
                dtd.insertDetailsIntoOracle(sybaseData, property, con2);
                dataSourceConnection.closeConnection();
                logger.info("Done inserting data into oracle.");
            }
        }catch (Exception ex){
            logger.error("Error while executing migration... \n" + ex.getMessage());
        }finally {
            if(con1 != null){
                try{
                    con1.close();
                }catch (Exception ex) {
                    logger.error("An Error closing sybase connection.... \n" + ex.getMessage());
                }
            }
            if(con2 != null){
                try{
                    con2.close();
                }catch (Exception ex) {
                    logger.error("An Error closing oracle connection. \n" + ex.getMessage());
                }
            }
        }
        logger.info("==================================================");

    }

}

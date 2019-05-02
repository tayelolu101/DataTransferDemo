package com.data;

import org.apache.log4j.BasicConfigurator;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class DataMain {

    private static Scheduler scheduler = null;
    private static final Logger logger = LoggerFactory.getLogger(DataMain.class);

    public static void main(String[] args) {
        try {
            Properties quartzProperties = new Properties();
            quartzProperties.put("org.quartz.threadPool.threadCount", "1");
            BasicConfigurator.configure();
            JobDetail job = newJob(ExecuteDataMigration.class).withIdentity("ExecuteDataMigrationTrigger", "Group").build();
            Trigger trigger;
            trigger = newTrigger().withIdentity("ExecuteDataMigrationTrigger", "Group")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *")).build();
            // Setup the Job and Trigger with Scheduler & schedule jobs
            scheduler = new StdSchedulerFactory(quartzProperties).getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        }catch (Exception ex){
            logger.error("An error occurred during the migration process.");
        }

    }
}

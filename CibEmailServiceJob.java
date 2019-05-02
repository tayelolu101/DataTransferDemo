package PaymentEmail;

import org.apache.log4j.BasicConfigurator;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;


public class CibEmailServiceJob {

    private static Scheduler scheduler = null;
    private static final Logger logger = LoggerFactory.getLogger(CibEmailServiceJob.class);

    public static void main(String[] args) throws Exception{

        Properties props =  ProperLoader.getProps();

        Properties quartzProperties = new Properties();
        quartzProperties.put("org.quartz.threadPool.threadCount", "1");

        BasicConfigurator.configure();
        JobDetail job =  newJob(EmailJob.class).withIdentity("CronQuartzJob", "Group").build();
        String cronTimer = props.getProperty("CronTimer");

        logger.info("Cron timer used :: " + cronTimer);

        Trigger trigger;
        trigger =  newTrigger().withIdentity("EmailNotificationTrigger", "Group")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronTimer)).build();

        // Setup the Job and Trigger with Scheduler & schedule jobs
        scheduler = new StdSchedulerFactory(quartzProperties).getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);

    }
}



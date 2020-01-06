package toolkit.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import toolkit.datagrid.DataGridClient;

public class SchedulerExecutor {

    public SchedulerFactory schedulerFactory;
    public Scheduler scheduler;

    public SchedulerExecutor(DataGridClient dataGridClient) {

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {

            Scheduler scheduler = schedulerFactory.getScheduler();
            SchedulerContext context = scheduler.getContext();
            context.put("datagridClient", dataGridClient);

            JobDetail job = JobBuilder.newJob(SimpleJob.class)
                                      .withIdentity("myJob", "group1")
                                      .usingJobData("jobSays", "Hello World!")
                                      .usingJobData("myFloatValue", 3.141f)
                                      .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                                            .withIdentity("myTrigger", "group1")
                                            .startNow()
                                            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever())
                                            .build();

            scheduler.scheduleJob(job, trigger);
            scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}

package org.gaussian.amplifix.toolkit.scheduler;

//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;


//public class SimpleJob implements Job {
public class SimpleJob {
    //public DataGridClient dataGridClient;

    public SimpleJob() {
        //this.dataGridClient = new DataGridClient();
    }

//    public void execute(JobExecutionContext context) throws JobExecutionException {
//
//        System.out.println("This is a quartz job!");
//        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
//        String jobSays = dataMap.getString("jobSays");
//        float myFloatValue = dataMap.getFloat("myFloatValue");
//        System.out.println("Job says: " + jobSays + ", and val is: " + myFloatValue);
//
//        try {
//            SchedulerContext schedulerContext = context.getScheduler().getContext();
//            DataGridClient dataGridClient = (DataGridClient) schedulerContext.get("datagridClient");
//            IMap data = dataGridClient.getMap("data");
//            System.out.println("Data grid value: " + data.get("drop"));
//        } catch (SchedulerException e) {
//            // TODO
//        }
//    }

}

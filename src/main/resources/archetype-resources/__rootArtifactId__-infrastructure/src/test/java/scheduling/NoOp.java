package ${package}.scheduling;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * A job must an implemention of org.quartz.Job:
 * ethier inherit QuartzJobBean or adapt with DelegatingJob when using spring.
 *
 * @author iMinusMinus
 * @date 2020-04-12
 */
public class NoOp extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //NO-OP
    }

}
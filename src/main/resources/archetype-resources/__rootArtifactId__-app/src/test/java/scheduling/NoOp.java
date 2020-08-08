package ${package}.scheduling;

#if($framework.contains('quartz'))
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
#end

/**
 * A job must an implemention of org.quartz.Job:
 * ethier inherit QuartzJobBean or adapt with DelegatingJob when using spring.
 *
 * @author iMinusMinus
 * @date 2020-04-12
 */
public class NoOp #if($framework.contains('quartz'))extends QuartzJobBean#{end} {

#if($framework.contains('quartz'))
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //NO-OP
    }
#end

}
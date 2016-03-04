import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

/**
 * Created by Mahmood on 3/4/2016.
 */
class MainJob : Job {

    @Throws(JobExecutionException::class)
    override fun execute(jobExecutionContext: JobExecutionContext) {

    }
}

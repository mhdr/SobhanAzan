import javazoom.jl.player.Player
import org.joda.time.DateTime
import org.quartz.JobBuilder
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import java.io.FileInputStream
import java.sql.DriverManager

/**
 * Created by Mahmood on 3/2/2016.
 */

fun main(args: Array<String>) {

    //playAzan()

    val job = JobBuilder.newJob(MainJob::class.java).withIdentity("Main", "Main").build()

    val trigger = TriggerBuilder.newTrigger().withIdentity("Main", "Main").startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(1).repeatForever()).build()

    val scheduler = StdSchedulerFactory().scheduler
    scheduler.start()
    scheduler.scheduleJob(job, trigger)

    val azan = getNextAzan()

    println(azan.toString())
}

private fun getNextAzan(): Azan? {
    val today = DateTime()
    val todayStr = today.toString("yyyy-MM-dd")

    val connection = DriverManager.getConnection("jdbc:sqlite:Azan.db")

    val statement = connection.createStatement()

    val rs = statement.executeQuery("SELECT * FROM Times;")
    var azan: Azan? = null

    while (rs.next()) {
        val azanId = rs.getInt("AzanId")
        val azanDateTime = rs.getString("AzanDateTime")
        val azanType = rs.getInt("AzanType")

        if (azanDateTime.startsWith(todayStr) && azanType == 2) {
            azan = Azan(azanId, azanDateTime, azanType)
        }
    }

    rs.close()
    statement.close()
    connection.close()
    return azan
}

private fun playAzan(){
    val file= FileInputStream("Azan1.mp3")

    val player= Player(file)
    player.play()
}

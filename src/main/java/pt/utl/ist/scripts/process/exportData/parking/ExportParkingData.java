package pt.utl.ist.scripts.process.exportData.parking;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import net.sourceforge.fenixedu.domain.reports.ParkingDataReportFile;

@Task(englishTitle = "ExportParkingData")
public class ExportParkingData extends CronTask {

    @Override
    public void runTask() {
        taskLog("Start ExportParkingData");

        new ParkingDataReportFile();

        taskLog("The end");
    }

}

package pt.utl.ist.scripts.process.exportData.parking;

import net.sourceforge.fenixedu.domain.reports.ParkingDataReportFile;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;

@Task(englishTitle = "ExportParkingData")
public class ExportParkingData extends CronTask {

    @Override
    public void runTask() {
        taskLog("Start ExportParkingData");

        new ParkingDataReportFile();

        taskLog("The end");
    }

}

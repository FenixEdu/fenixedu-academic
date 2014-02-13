package pt.utl.ist.scripts.process.exportData.parking;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.ist.fenix.domain.reports.ParkingDataReportFile;

@Task(englishTitle = "ExportParkingData")
public class ExportParkingData extends CronTask {

    @Override
    public void runTask() {
        new ParkingDataReportFile();
    }

}

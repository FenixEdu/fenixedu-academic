package pt.utl.ist.scripts.process.exportData.parking;

import net.sourceforge.fenixedu.domain.reports.ParkingDataReportFile;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

@Task(englishTitle = "ExportParkingData")
public class ExportParkingData extends CronTask {

    @Override
    public void runTask() {
        new ParkingDataReportFile();
    }

}

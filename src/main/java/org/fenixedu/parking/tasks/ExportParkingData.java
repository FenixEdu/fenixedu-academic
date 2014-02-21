package org.fenixedu.parking.tasks;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.parking.domain.reports.ParkingDataReportFile;

@Task(englishTitle = "ExportParkingData")
public class ExportParkingData extends CronTask {

    @Override
    public void runTask() {
        new ParkingDataReportFile();
    }

}

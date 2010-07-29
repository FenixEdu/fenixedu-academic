package net.sourceforge.fenixedu.domain.accounting.report;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;

public class GratuityReportQueueJobLaunchService {

    @Service
    public static GratuityReportQueueJob launchJob(final ExecutionYear executionYear) {
	return new GratuityReportQueueJob(executionYear);
    }

}

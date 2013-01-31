package net.sourceforge.fenixedu.domain.accounting.report;

import pt.ist.fenixWebFramework.services.Service;

public class GratuityReportQueueJobLaunchService {

	@Service
	public static GratuityReportQueueJob launchJob(final GratuityReportBean bean) {
		return new GratuityReportQueueJob(bean.getType(), bean.getExecutionYear(), bean.getBeginDate(), bean.getEndDate());
	}

}

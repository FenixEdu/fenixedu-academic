package net.sourceforge.fenixedu.domain.accounting.report;

import pt.ist.fenixframework.Atomic;

public class GratuityReportQueueJobLaunchService {

    @Atomic
    public static GratuityReportQueueJob launchJob(final GratuityReportBean bean) {
        return new GratuityReportQueueJob(bean.getType(), bean.getExecutionYear(), bean.getBeginDate(), bean.getEndDate());
    }

}

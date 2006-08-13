package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.Date;

import net.sourceforge.fenixedu.domain.EnrolmentPeriod;

public class InfoEnrolmentPeriod extends InfoObject implements Serializable {

    private Date endDate;
    private Date startDate;
    private InfoDegreeCurricularPlan infoDegreeCurricularPlan;
    private InfoExecutionPeriod infoExecutionPeriod;

    public void copyFromDomain(final EnrolmentPeriod enrolmentPeriod) {
        super.copyFromDomain(enrolmentPeriod);
        if (enrolmentPeriod != null) {
            setEndDate(enrolmentPeriod.getEndDate());
            setStartDate(enrolmentPeriod.getStartDate());
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan.newInfoFromDomain(
                    enrolmentPeriod.getDegreeCurricularPlan()));
        }
    }

    public static InfoEnrolmentPeriod newInfoFromDomain(final EnrolmentPeriod enrolmentPeriod) {
        InfoEnrolmentPeriod infoExecutionPeriod = null;
        if (enrolmentPeriod != null) {
            infoExecutionPeriod = new InfoEnrolmentPeriod();
            infoExecutionPeriod.copyFromDomain(enrolmentPeriod);
        }
        return infoExecutionPeriod;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return infoDegreeCurricularPlan;
    }

    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
        this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }

    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

}

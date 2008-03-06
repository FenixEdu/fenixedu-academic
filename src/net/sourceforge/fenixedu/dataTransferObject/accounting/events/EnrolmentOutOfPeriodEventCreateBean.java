package net.sourceforge.fenixedu.dataTransferObject.accounting.events;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class EnrolmentOutOfPeriodEventCreateBean extends AccountingEventCreateBean {

    /**
     * 
     */
    private static final long serialVersionUID = -7857479858097958524L;

    private Integer numberOfDelayDays;

    public EnrolmentOutOfPeriodEventCreateBean(StudentCurricularPlan studentCurricularPlan) {
	super(studentCurricularPlan);
    }

    public Integer getNumberOfDelayDays() {
	return numberOfDelayDays;
    }

    public void setNumberOfDelayDays(Integer numberOfDelayDays) {
	this.numberOfDelayDays = numberOfDelayDays;
    }

}

/*
 * Created on 2004/08/24
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Luis Cruz
 */
public class EnrolmentPeriodInClasses extends EnrolmentPeriod implements IEnrolmentPeriodInClasses {

    protected String ojbConcreteClass;

    public EnrolmentPeriodInClasses() {
        ojbConcreteClass = EnrolmentPeriodInClasses.class.getName();
    }

    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    public void setOjbConcreteClass(String ojbConcreteClass) {
        this.ojbConcreteClass = ojbConcreteClass;
    }

}
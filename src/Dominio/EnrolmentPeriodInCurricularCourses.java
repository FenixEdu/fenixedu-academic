/*
 * Created on 2004/08/24
 *
 */
package Dominio;

/**
 * @author Luis Cruz
 */
public class EnrolmentPeriodInCurricularCourses extends EnrolmentPeriod implements
        IEnrolmentPeriodInCurricularCourses {

    protected String ojbConcreteClass;

    public EnrolmentPeriodInCurricularCourses() {
        ojbConcreteClass = EnrolmentPeriodInCurricularCourses.class.getName();
    }

    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    public void setOjbConcreteClass(String ojbConcreteClass) {
        this.ojbConcreteClass = ojbConcreteClass;
    }

}
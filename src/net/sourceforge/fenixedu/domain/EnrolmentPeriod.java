/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author jpvl
 */
public abstract class EnrolmentPeriod extends EnrolmentPeriod_Base {

    private IDegreeCurricularPlan degreeCurricularPlan;

    public EnrolmentPeriod() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IEnrolmentPeriodInCurricularCourses#setDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
     */
    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    /**
     * @return
     */
    public IDegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

}
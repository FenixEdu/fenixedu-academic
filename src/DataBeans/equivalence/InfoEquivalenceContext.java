package DataBeans.equivalence;

import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.InfoStudentCurricularPlan;

public class InfoEquivalenceContext extends DataTranferObject {
    private List infoEnrolmentsToGiveEquivalence;

    private List infoCurricularCoursesToGetEquivalence;

    private InfoStudentCurricularPlan infoStudentCurricularPlan;

    private List chosenInfoEnrollmentGradesToGiveEquivalence;

    private List chosenInfoCurricularCourseGradesToGetEquivalence;

    public InfoEquivalenceContext() {
    }

    /**
     * @return
     */
    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
        return infoStudentCurricularPlan;
    }

    /**
     * @param plan
     */
    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan plan) {
        infoStudentCurricularPlan = plan;
    }

    /**
     * @return
     */
    public List getInfoEnrolmentsToGiveEquivalence() {
        return infoEnrolmentsToGiveEquivalence;
    }

    /**
     * @param list
     */
    public void setInfoEnrolmentsToGiveEquivalence(List list) {
        infoEnrolmentsToGiveEquivalence = list;
    }

    /**
     * @return
     */
    public List getChosenInfoEnrollmentGradesToGiveEquivalence() {
        return chosenInfoEnrollmentGradesToGiveEquivalence;
    }

    /**
     * @param list
     */
    public void setChosenInfoEnrollmentGradesToGiveEquivalence(List list) {
        chosenInfoEnrollmentGradesToGiveEquivalence = list;
    }

    /**
     * @return Returns the chosenInfoCurricularCourseGradesToGetEquivalence.
     */
    public List getChosenInfoCurricularCourseGradesToGetEquivalence() {
        return chosenInfoCurricularCourseGradesToGetEquivalence;
    }

    /**
     * @param chosenInfoCurricularCourseGradesToGetEquivalence
     *            The chosenInfoCurricularCourseGradesToGetEquivalence to set.
     */
    public void setChosenInfoCurricularCourseGradesToGetEquivalence(
            List chosenInfoCurricularCoursesToGetEquivalence) {
        this.chosenInfoCurricularCourseGradesToGetEquivalence = chosenInfoCurricularCoursesToGetEquivalence;
    }

    /**
     * @return Returns the infoCurricularCoursesToGetEquivalence.
     */
    public List getInfoCurricularCoursesToGetEquivalence() {
        return infoCurricularCoursesToGetEquivalence;
    }

    /**
     * @param infoCurricularCoursesToGetEquivalence
     *            The infoCurricularCoursesToGetEquivalence to set.
     */
    public void setInfoCurricularCoursesToGetEquivalence(List infoCurricularCoursesToGetEquivalence) {
        this.infoCurricularCoursesToGetEquivalence = infoCurricularCoursesToGetEquivalence;
    }

    /**
     * @return Returns the infoEnrollmentsFromEquivalences.
     */
    public List getInfoEnrollmentsFromEquivalences() {
        return infoEnrolmentsToGiveEquivalence;
    }

    /**
     * @param infoEnrollmentsFromEquivalences
     *            The infoEnrollmentsFromEquivalences to set.
     */
    public void setInfoEnrollmentsFromEquivalences(List infoEnrollmentsFromEquivalences) {
        this.infoEnrolmentsToGiveEquivalence = infoEnrollmentsFromEquivalences;
    }

}
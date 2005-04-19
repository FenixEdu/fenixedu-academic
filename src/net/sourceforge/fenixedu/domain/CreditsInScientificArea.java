package net.sourceforge.fenixedu.domain;

/**
 * @author David Santos Jan 14, 2004
 */

public class CreditsInScientificArea extends CreditsInScientificArea_Base {
    private IStudentCurricularPlan studentCurricularPlan;

    private IScientificArea scientificArea;

    private IEnrolment enrolment;

    public CreditsInScientificArea() {
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ICreditsInScientificArea) {
            ICreditsInScientificArea creditsInSpecificScientificArea = (ICreditsInScientificArea) obj;
            result = this.getEnrolment().equals(creditsInSpecificScientificArea.getEnrolment())
                    && this.getStudentCurricularPlan().equals(
                            creditsInSpecificScientificArea.getStudentCurricularPlan())
                    && this.getScientificArea().equals(
                            creditsInSpecificScientificArea.getScientificArea());
        }
        return result;
    }

    public String toString() {
        String result = "scientificArea: [" + this.getScientificArea().getName();
        result += "] student: [" + this.getStudentCurricularPlan().getStudent().getNumber().toString();
        result += "] course: [" + this.getEnrolment().getCurricularCourse().getName() + "]";
        return result;
    }

    /**
     * @return Returns the enrolment.
     */
    public IEnrolment getEnrolment() {
        return enrolment;
    }

    /**
     * @param enrolment
     *            The enrolment to set.
     */
    public void setEnrolment(IEnrolment enrolment) {
        this.enrolment = enrolment;
    }


    /**
     * @return Returns the scientificArea.
     */
    public IScientificArea getScientificArea() {
        return scientificArea;
    }

    /**
     * @param scientificArea
     *            The scientificArea to set.
     */
    public void setScientificArea(IScientificArea scientificArea) {
        this.scientificArea = scientificArea;
    }


    /**
     * @return Returns the studentCurricularPlan.
     */
    public IStudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    /**
     * @param studentCurricularPlan
     *            The studentCurricularPlan to set.
     */
    public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }


}
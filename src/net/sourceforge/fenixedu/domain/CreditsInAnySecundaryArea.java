package net.sourceforge.fenixedu.domain;

/**
 * @author David Santos Jan 14, 2004
 */

public class CreditsInAnySecundaryArea extends CreditsInAnySecundaryArea_Base {

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ICreditsInAnySecundaryArea) {
            ICreditsInAnySecundaryArea creditsInSpecificScientificArea = (ICreditsInAnySecundaryArea) obj;
            result = this.getEnrolment().equals(creditsInSpecificScientificArea.getEnrolment())
                    && this.getStudentCurricularPlan().equals(
                            creditsInSpecificScientificArea.getStudentCurricularPlan());
        }
        return result;
    }

    public String toString() {
        String result = "student: ["
                + this.getStudentCurricularPlan().getStudent().getNumber().toString();
        result += "] course: [" + this.getEnrolment().getCurricularCourse().getName() + "]";
        return result;
    }

}
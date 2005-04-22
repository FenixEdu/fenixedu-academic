package net.sourceforge.fenixedu.domain;

/**
 * @author David Santos Jan 14, 2004
 */

public class CreditsInScientificArea extends CreditsInScientificArea_Base {

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
}
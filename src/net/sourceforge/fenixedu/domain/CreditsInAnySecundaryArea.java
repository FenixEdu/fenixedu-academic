package net.sourceforge.fenixedu.domain;

/**
 * @author David Santos Jan 14, 2004
 */

public class CreditsInAnySecundaryArea extends CreditsInAnySecundaryArea_Base {

    public boolean equals(Object obj) {
        if (obj instanceof ICreditsInAnySecundaryArea) {
            final ICreditsInAnySecundaryArea creditsInAnySecundaryArea = (ICreditsInAnySecundaryArea) obj;
            return this.getIdInternal().equals(creditsInAnySecundaryArea.getIdInternal());
        }
        return false;
    }

    public String toString() {
        String result = "student: ["
                + this.getStudentCurricularPlan().getStudent().getNumber().toString();
        result += "] course: [" + this.getEnrolment().getCurricularCourse().getName() + "]";
        return result;
    }

}

package net.sourceforge.fenixedu.domain;

/**
 * @author David Santos Jan 14, 2004
 */

public class CreditsInScientificArea extends CreditsInScientificArea_Base {

    public CreditsInScientificArea() {
    }

    public boolean equals(Object obj) {
        if (obj instanceof ICreditsInScientificArea) {
            final ICreditsInScientificArea creditsInScientificArea = (ICreditsInScientificArea) obj;
            return this.getIdInternal().equals(creditsInScientificArea.getIdInternal());
        }
        return false;
    }

    public String toString() {
        String result = "scientificArea: [" + this.getScientificArea().getName();
        result += "] student: [" + this.getStudentCurricularPlan().getStudent().getNumber().toString();
        result += "] course: [" + this.getEnrolment().getCurricularCourse().getName() + "]";
        return result;
    }
}

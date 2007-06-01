package net.sourceforge.fenixedu.dataTransferObject.equivalence;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;

public class InfoEnrollmentGrade extends DataTranferObject {
    private InfoEnrolment infoEnrollment;

    private String gradeValue;

    public InfoEnrollmentGrade() {
    }

    /**
     * @return
     */
    public String getGradeValue() {
        return gradeValue;
    }

    /**
     * @param string
     */
    public void setGradeValue(String string) {
        gradeValue = string;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "infoEnrollment = " + this.infoEnrollment + "; ";
        result += "grade = " + this.gradeValue + "]\n";
        return result;
    }

    /**
     * @return Returns the infoEnrollment.
     */
    public InfoEnrolment getInfoEnrollment() {
        return infoEnrollment;
    }

    /**
     * @param infoEnrollment
     *            The infoEnrollment to set.
     */
    public void setInfoEnrollment(InfoEnrolment infoEnrollment) {
        this.infoEnrollment = infoEnrollment;
    }
}
package net.sourceforge.fenixedu.dataTransferObject.equivalence;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;

public class InfoEnrollmentGrade extends DataTranferObject {
    private InfoEnrolment infoEnrollment;

    private String grade;

    public InfoEnrollmentGrade() {
    }

    /**
     * @return
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param string
     */
    public void setGrade(String string) {
        grade = string;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "infoEnrollment = " + this.infoEnrollment + "; ";
        result += "grade = " + this.grade + "]\n";
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
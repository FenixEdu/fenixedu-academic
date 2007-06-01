package net.sourceforge.fenixedu.dataTransferObject.equivalence;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;

public class InfoCurricularCourseGrade extends DataTranferObject {
    private InfoCurricularCourse infoCurricularCourse;

    private String gradeValue;

    /**
     * @return
     */
    public InfoCurricularCourse getInfoCurricularCourse() {
        return infoCurricularCourse;
    }

    /**
     * @param infoCurricularCourse
     */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

    public InfoCurricularCourseGrade() {
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
        result += "infoCurricularCourse = " + this.infoCurricularCourse + "; ";
        result += "grade = " + this.gradeValue + "]\n";
        return result;
    }
}
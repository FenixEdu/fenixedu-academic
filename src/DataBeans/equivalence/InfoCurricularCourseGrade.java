package DataBeans.equivalence;

import DataBeans.DataTranferObject;
import DataBeans.InfoCurricularCourse;

public class InfoCurricularCourseGrade extends DataTranferObject {
    private InfoCurricularCourse infoCurricularCourse;

    private String grade;

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
        result += "infoCurricularCourse = " + this.infoCurricularCourse + "; ";
        result += "grade = " + this.grade + "]\n";
        return result;
    }
}
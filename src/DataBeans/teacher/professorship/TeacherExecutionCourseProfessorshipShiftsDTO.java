/*
 * Created on Nov 22, 2003 by jpvl
 *  
 */
package DataBeans.teacher.professorship;

import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;

/**
 * @author jpvl
 */
public class TeacherExecutionCourseProfessorshipShiftsDTO extends DataTranferObject {
    private InfoExecutionCourse infoExecutionCourse;

    private List infoShiftPercentageList;

    private InfoTeacher infoTeacher;

    /**
     * @return Returns the infoExecutionCourse.
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return this.infoExecutionCourse;
    }

    /**
     * @return Returns the infoShiftPercentage.
     */
    public List getInfoShiftPercentageList() {
        return this.infoShiftPercentageList;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return this.infoTeacher;
    }

    /**
     * @param infoExecutionCourse
     *            The infoExecutionCourse to set.
     */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * @param infoShiftPercentage
     *            The infoShiftPercentage to set.
     */
    public void setInfoShiftPercentageList(List infoShiftPercentage) {
        this.infoShiftPercentageList = infoShiftPercentage;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

}
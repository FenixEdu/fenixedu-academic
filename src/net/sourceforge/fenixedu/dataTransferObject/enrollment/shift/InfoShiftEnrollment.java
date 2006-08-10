/*
 * Created on 10/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.enrollment.shift;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;

/**
 * This class keeps all information usefull for shift enrollment use case
 * 
 * @author Tânia Pousão
 */
public class InfoShiftEnrollment extends DataTranferObject {

    /**
     * Execution courses that student attends
     */
    private List infoAttendingCourses;

    /**
     * Shifts that the student are enrolling
     */
    private List infoShiftEnrollment;

    /**
     * Number of courses whith shift's enrollment
     */
    private Integer numberCourseWithShiftEnrollment;

    /**
     * Execution courses that belongs to execution degree selected
     */
    private List infoExecutionCoursesList;

    /**
     * Execution degree selected
     */
    private InfoExecutionDegree infoExecutionDegree;

    /**
     * List with Execution degrees labels and values to display in the select
     * box. Witch label has degree's name concat with degree curricular plan
     * name because it's necessary to distinct two execution degree of the same
     * degree.
     */
    private List infoExecutionDegreesLabelsList;

    /**
     * Execution degrees present.
     */
    private List infoExecutionDegreesList;

    /**
     * Registration enrolling
     */
    private InfoStudent infoStudent;
    
    /**
     * number of courses in wich isn't enrolled in all shifts
     */
    private Integer numberCourseUnenrolledShifts; 

    /**
     * @return Returns the numberCourseWithShiftEnrollment.
     */
    public Integer getNumberCourseWithShiftEnrollment() {
        return numberCourseWithShiftEnrollment;
    }

    /**
     * @param numberCourseWithShiftEnrollment
     *            The numberCourseWithShiftEnrollment to set.
     */
    public void setNumberCourseWithShiftEnrollment(Integer numberCourseWithShiftEnrollment) {
        this.numberCourseWithShiftEnrollment = numberCourseWithShiftEnrollment;
    }

    /**
     * @return Returns the infoShiftEnrollment.
     */
    public List getInfoShiftEnrollment() {
        return infoShiftEnrollment;
    }

    /**
     * @param infoShiftEnrollment
     *            The infoShiftEnrollment to set.
     */
    public void setInfoShiftEnrollment(List infoShiftEnrollment) {
        this.infoShiftEnrollment = infoShiftEnrollment;
    }

    /**
     * @return Returns the infoAttendingCourses.
     */
    public List getInfoAttendingCourses() {
        return infoAttendingCourses;
    }

    /**
     * @return Returns the infoExecutionCoursesList.
     */
    public List getInfoExecutionCoursesList() {
        return infoExecutionCoursesList;
    }

    /**
     * @return Returns the infoExecutionDegree.
     */
    public InfoExecutionDegree getInfoExecutionDegree() {
        return infoExecutionDegree;
    }

    /**
     * @return Returns the infoExecutionDegreesLabelsList.
     */
    public List getInfoExecutionDegreesLabelsList() {
        return infoExecutionDegreesLabelsList;
    }

    /**
     * @return Returns the infoExecutionDegreesList.
     */
    public List getInfoExecutionDegreesList() {
        return infoExecutionDegreesList;
    }

    /**
     * @return Returns the infoStudent.
     */
    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    /**
     * @param infoAttendingCourses
     *            The infoAttendingCourses to set.
     */
    public void setInfoAttendingCourses(List infoAttendingCourses) {
        this.infoAttendingCourses = infoAttendingCourses;
    }

    /**
     * @param infoExecutionCoursesList
     *            The infoExecutionCoursesList to set.
     */
    public void setInfoExecutionCoursesList(List infoExecutionCoursesList) {
        this.infoExecutionCoursesList = infoExecutionCoursesList;
    }

    /**
     * @param infoExecutionDegree
     *            The infoExecutionDegree to set.
     */
    public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
        this.infoExecutionDegree = infoExecutionDegree;
    }

    /**
     * @param infoExecutionDegreesLabelsList
     *            The infoExecutionDegreesLabelsList to set.
     */
    public void setInfoExecutionDegreesLabelsList(List infoExecutionDegreesLabelsList) {
        this.infoExecutionDegreesLabelsList = infoExecutionDegreesLabelsList;
    }

    /**
     * @param infoExecutionDegreesList
     *            The infoExecutionDegreesList to set.
     */
    public void setInfoExecutionDegreesList(List infoExecutionDegreesList) {
        this.infoExecutionDegreesList = infoExecutionDegreesList;
    }

    /**
     * @param infoStudent
     *            The infoStudent to set.
     */
    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append("[InfoShiftEnrollment ");
        string.append("\ninfoStudent: ");
        string.append(infoStudent.getIdInternal());

        string.append("\ninfoExecutionDegreesList: ");
        if (infoExecutionDegreesList != null) {
            string.append(infoExecutionDegreesList.size());
        } else {
            string.append("null");
        }

        string.append("\ninfoExecutionDegreesLabelsList: ");
        if (infoExecutionDegreesLabelsList != null) {
            string.append(infoExecutionDegreesLabelsList.size());
        } else {
            string.append("null");
        }

        string.append("\ninfoExecutionDegree: ");
        string.append(infoExecutionDegree.getIdInternal());

        string.append("\ninfoExecutionCoursesList: ");
        if (infoExecutionCoursesList != null) {
            string.append(infoExecutionCoursesList.size());
        } else {
            string.append("null");
        }

        string.append("\ninfoAttendingCourses: ");
        if (infoAttendingCourses != null) {
            string.append(infoAttendingCourses.size());
        } else {
            string.append("null");
        }

        string.append("\ninfoShiftEnrollment: ");
        if (infoShiftEnrollment != null) {
            string.append(infoShiftEnrollment.size());
        } else {
            string.append("null");
        }
        string.append(" ]");

        return string.toString();
    }

    public Integer getNumberCourseUnenrolledShifts() {
        return numberCourseUnenrolledShifts;
    }

    public void setNumberCourseUnenrolledShifts(Integer numberCourseUnenrolledShifts) {
        this.numberCourseUnenrolledShifts = numberCourseUnenrolledShifts;
    }

}
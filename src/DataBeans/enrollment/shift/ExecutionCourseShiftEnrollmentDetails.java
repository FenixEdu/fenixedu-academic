/*
 * Created on 11/Fev/2004
 */
package DataBeans.enrollment.shift;

import java.util.ArrayList;
import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.InfoExecutionCourse;

/**
 * Contains that about shifts of an execution course for shiftEnrollment
 * 
 * @author jpvl
 */
public class ExecutionCourseShiftEnrollmentDetails extends DataTranferObject {

    private InfoExecutionCourse infoExecutionCourse;

    /**
     * @see ShiftEnrollmentDetails
     */
    private List shiftEnrollmentDetailsList;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ExecutionCourseShiftEnrollmentDetails) {
            ExecutionCourseShiftEnrollmentDetails details = (ExecutionCourseShiftEnrollmentDetails) obj;
            resultado = getInfoExecutionCourse().equals(details.getInfoExecutionCourse());
        }

        return resultado;
    }

    /**
     * @return Returns the infoExecutionCourse.
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * @return Returns the shiftDetailsEnrollmentList.
     */
    public List getShiftEnrollmentDetailsList() {
        return shiftEnrollmentDetailsList;
    }

    /**
     * @param infoExecutionCourse
     *            The infoExecutionCourse to set.
     */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * @param shiftDetailsEnrollmentList
     *            The shiftDetailsEnrollmentList to set.
     */
    public void setShiftEnrollmentDetailsList(List shiftDetailsEnrollmentList) {
        this.shiftEnrollmentDetailsList = shiftDetailsEnrollmentList;
    }

    /**
     * @param shiftEnrollmentDetails2
     */
    public void addShiftEnrollmentDetails(ShiftEnrollmentDetails shiftEnrollmentDetails) {
        if (this.shiftEnrollmentDetailsList == null) {
            this.shiftEnrollmentDetailsList = new ArrayList();
            this.shiftEnrollmentDetailsList.add(shiftEnrollmentDetails);
        } else {
            if (!this.shiftEnrollmentDetailsList.contains(shiftEnrollmentDetails)) {
                this.shiftEnrollmentDetailsList.add(shiftEnrollmentDetails);
            }
        }
    }

}
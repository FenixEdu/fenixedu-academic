/*
 * Created on 11/Fev/2004
 */
package DataBeans.enrollment.shift;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;

/**
 * Contains that about shifts of an execution course for shiftEnrollment
 * 
 * @author jpvl
 */
public class ExecutionCourseShiftEnrollmentDetails
{
    private InfoExecutionCourse infoExecutionCourse;

    /**
     * @see ShiftEnrollmentDetails
     */
    private List shiftEnrollmentDetailsList;

    /**
     * @return Returns the infoExecutionCourse.
     */
    public InfoExecutionCourse getInfoExecutionCourse()
    {
        return infoExecutionCourse;
    }

    /**
     * @return Returns the shiftDetailsEnrollmentList.
     */
    public List getShiftEnrollmentDetailsList()
    {
        return shiftEnrollmentDetailsList;
    }

    /**
     * @param infoExecutionCourse
     *            The infoExecutionCourse to set.
     */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse)
    {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * @param shiftDetailsEnrollmentList
     *            The shiftDetailsEnrollmentList to set.
     */
    public void setShiftEnrollmentDetailsList(List shiftDetailsEnrollmentList)
    {
        this.shiftEnrollmentDetailsList = shiftDetailsEnrollmentList;
    }

    /**
     * @param shiftEnrollmentDetails2
     */
    public void addShiftEnrollmentDetails(ShiftEnrollmentDetails shiftEnrollmentDetails)
    {
        if (this.shiftEnrollmentDetailsList == null)
        {
            this.shiftEnrollmentDetailsList = new ArrayList();
        }
        this.shiftEnrollmentDetailsList.add(shiftEnrollmentDetails);
    }

}

package DataBeans;

import java.util.List;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class InfoExecutionCourseOccupancy extends InfoObject {

    private InfoExecutionCourse infoExecutionCourse;

    // Note: This will always be null when putting to request.
    //       The ShiftsInGroups will contain all the information arranjed
    private List infoShifts;

    private List shiftsInGroups;

    public InfoExecutionCourseOccupancy() {
    }

    /**
     * @return
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * @param infoExecutionCourse
     */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * @return
     */
    public List getInfoShifts() {
        return infoShifts;
    }

    /**
     * @param infoShifts
     */
    public void setInfoShifts(List infoShifts) {
        this.infoShifts = infoShifts;
    }

    /**
     * @return
     */
    public List getShiftsInGroups() {
        return shiftsInGroups;
    }

    /**
     * @param shiftsInGroups
     */
    public void setShiftsInGroups(List shiftsInGroups) {
        this.shiftsInGroups = shiftsInGroups;
    }

    public String toString() {
        String result = "[InfoExecutionCourseOccupancy ";
        result += "infoExecutionCourse" + this.infoExecutionCourse + ";";
        result += "infoShifts" + this.infoShifts + ";";
        result += "shiftsInGroups" + this.shiftsInGroups + "]";
        return result;
    }

}
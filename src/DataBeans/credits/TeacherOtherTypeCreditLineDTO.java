/*
 * Created on 29/Fev/2004
 */
package DataBeans.credits;

import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;

/**
 * @author jpvl
 */
public class TeacherOtherTypeCreditLineDTO extends DataTranferObject {
    private InfoExecutionPeriod infoExecutionPeriod;

    private InfoTeacher infoTeacher;

    private List creditLines;

    /**
     * @return Returns the creditLines.
     */
    public List getCreditLines() {
        return creditLines;
    }

    /**
     * @param creditLines
     *            The creditLines to set.
     */
    public void setCreditLines(List creditLines) {
        this.creditLines = creditLines;
    }

    /**
     * @return Returns the infoExecutionPeriod.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }

    /**
     * @param infoExecutionPeriod
     *            The infoExecutionPeriod to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

}
/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package DataBeans.degree.finalProject;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoObject;
import DataBeans.InfoStudent;
import DataBeans.InfoTeacher;

/**
 * @author jpvl
 */
public class InfoTeacherDegreeFinalProjectStudent extends InfoObject
{
    private InfoExecutionPeriod infoExecutionPeriod;
    private InfoStudent infoStudent;
    private InfoTeacher infoTeacher;
    private Double percentage;
    /**
	 * @return Returns the infoExecutionYear.
	 */
    public InfoExecutionPeriod getInfoExecutionPeriod()
    {
        return this.infoExecutionPeriod;
    }

    /**
	 * @return Returns the infoStudent.
	 */
    public InfoStudent getInfoStudent()
    {
        return this.infoStudent;
    }

    /**
	 * @return Returns the infoTeacher.
	 */
    public InfoTeacher getInfoTeacher()
    {
        return this.infoTeacher;
    }

    /**
     * @return Returns the percentage.
     */
    public Double getPercentage()
    {
        return this.percentage;
    }

    /**
	 * @param infoExecutionYear
	 *                   The infoExecutionYear to set.
	 */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod)
    {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
	 * @param infoStudent
	 *                   The infoStudent to set.
	 */
    public void setInfoStudent(InfoStudent infoStudent)
    {
        this.infoStudent = infoStudent;
    }

    /**
	 * @param infoTeacher
	 *                   The infoTeacher to set.
	 */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }
    /**
     * @param percentage The percentage to set.
     */
    public void setPercentage(Double percentage)
    {
        this.percentage = percentage;
    }
}

/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package DataBeans.degree.finalProject;

import java.util.List;

import DataBeans.InfoExecutionYear;
import DataBeans.InfoTeacher;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectStudentsDTO
{
    private InfoExecutionYear infoExecutionYear;
    private InfoTeacher infoTeacher;
    private List infoTeacherDegreeFinalProjectStudentList;

    /**
     * @return Returns the infoExecutionYear.
     */
    public InfoExecutionYear getInfoExecutionYear()
    {
        return this.infoExecutionYear;
    }
    /**
	 * @return Returns the infoTeacher.
	 */
    public InfoTeacher getInfoTeacher()
    {
        return this.infoTeacher;
    }

    /**
	 * @return Returns the infoTeacherDegreeFinalProjectStudentList.
	 */
    public List getInfoTeacherDegreeFinalProjectStudentList()
    {
        return this.infoTeacherDegreeFinalProjectStudentList;
    }

    /**
     * @param infoExecutionYear The infoExecutionYear to set.
     */
    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear)
    {
        this.infoExecutionYear = infoExecutionYear;
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
	 * @param infoTeacherDegreeFinalProjectStudentList
	 *                   The infoTeacherDegreeFinalProjectStudentList to set.
	 */
    public void setInfoTeacherDegreeFinalProjectStudentList(List infoTeacherDegreeFinalProjectStudentList)
    {
        this.infoTeacherDegreeFinalProjectStudentList = infoTeacherDegreeFinalProjectStudentList;
    }

}

/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package DataBeans.teacher.workTime;

import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;

/**
 * @author jpvl
 */
public class TeacherInstitutionWorkingTimeDTO extends DataTranferObject
{
    private InfoExecutionPeriod infoExecutionPeriod;
    private InfoTeacher infoTeacher;
    private List infoTeacherInstitutionWorkTimeList;
    /**
	 * @return Returns the infoExecutionPeriod.
	 */
    public InfoExecutionPeriod getInfoExecutionPeriod()
    {
        return this.infoExecutionPeriod;
    }

    /**
	 * @return Returns the infoTeacher.
	 */
    public InfoTeacher getInfoTeacher()
    {
        return this.infoTeacher;
    }

    /**
	 * @return Returns the infoTeacherInstitutionWorkTimeList.
	 */
    public List getInfoTeacherInstitutionWorkTimeList()
    {
        return this.infoTeacherInstitutionWorkTimeList;
    }

    /**
	 * @param infoExecutionPeriod
	 *                   The infoExecutionPeriod to set.
	 */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod)
    {
        this.infoExecutionPeriod = infoExecutionPeriod;
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
	 * @param infoTeacherInstitutionWorkTimeList
	 *                   The infoTeacherInstitutionWorkTimeList to set.
	 */
    public void setInfoTeacherInstitutionWorkTimeList(List infoTeacherInstitutionWorkTimeList)
    {
        this.infoTeacherInstitutionWorkTimeList = infoTeacherInstitutionWorkTimeList;
    }

}

/*
 * Created on Nov 19, 2003 by jpvl
 *  
 */
package DataBeans.credits;

import java.io.Serializable;
import java.util.List;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;

/**
 * @author jpvl
 */
public class TeacherCreditsSheetDTO implements Serializable
{

    private List infoCreditLineList;

    private InfoExecutionPeriod infoExecutionPeriod;

    private List infoMasterDegreeProfessorships;
    private List infoShiftProfessorshipList;
    private List infoSupportLessonList;
    private InfoTeacher infoTeacher;
    private List infoTeacherDegreeFinalProjectStudentList;

    private List infoTeacherInstitutionWorkingTimeList;

    /**
	 * @return Returns the infoCreditLineList.
	 */
    public List getInfoCreditLineList()
    {
        return this.infoCreditLineList;
    }

    /**
	 * @return Returns the infoExecutionPeriod.
	 */
    public InfoExecutionPeriod getInfoExecutionPeriod()
    {
        return this.infoExecutionPeriod;
    }

    /**
	 * @return Returns the infoMasterDegreeProfessorships.
	 */
    public List getInfoMasterDegreeProfessorships()
    {
        return this.infoMasterDegreeProfessorships;
    }

    /**
	 * @return Returns the infoShiftProfessorshipList.
	 */
    public List getInfoShiftProfessorshipList()
    {
        return this.infoShiftProfessorshipList;
    }
    /**
	 * @return Returns the infoSupportLessonList.
	 */
    public List getInfoSupportLessonList()
    {
        return this.infoSupportLessonList;
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
	 * @return Returns the infoTeacherInstitutionWorkingTimeList.
	 */
    public List getInfoTeacherInstitutionWorkingTimeList()
    {
        return this.infoTeacherInstitutionWorkingTimeList;
    }

    /**
	 * @param infoCreditLineList
	 *                   The infoCreditLineList to set.
	 */
    public void setInfoCreditLineList(List infoCreditLineList)
    {
        this.infoCreditLineList = infoCreditLineList;
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
	 * @param infoMasterDegreeProfessorships
	 *                   The infoMasterDegreeProfessorships to set.
	 */
    public void setInfoMasterDegreeProfessorships(List infoMasterDegreeProfessorships)
    {
        this.infoMasterDegreeProfessorships = infoMasterDegreeProfessorships;
    }

    /**
	 * @param infoShiftProfessorshipList
	 *                   The infoShiftProfessorshipList to set.
	 */
    public void setInfoShiftProfessorshipList(List infoShiftProfessorshipList)
    {
        this.infoShiftProfessorshipList = infoShiftProfessorshipList;
    }
    /**
	 * @param infoSupportLessonList
	 *                   The infoSupportLessonList to set.
	 */
    public void setInfoSupportLessonList(List infoSupportLessonList)
    {
        this.infoSupportLessonList = infoSupportLessonList;
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
    /**
	 * @param infoTeacherInstitutionWorkingTimeList
	 *                   The infoTeacherInstitutionWorkingTimeList to set.
	 */
    public void setInfoTeacherInstitutionWorkingTimeList(List infoTeacherInstitutionWorkingTimeList)
    {
        this.infoTeacherInstitutionWorkingTimeList = infoTeacherInstitutionWorkingTimeList;
    }
}
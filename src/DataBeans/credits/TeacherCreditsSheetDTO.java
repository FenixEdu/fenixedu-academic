/*
 * Created on Nov 19, 2003 by jpvl
 *  
 */
package DataBeans.credits;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import DataBeans.teacher.professorship.InfoSupportLesson;

/**
 * @author jpvl
 */
public class TeacherCreditsSheetDTO implements Serializable
{

    private List infoCreditLineList;

    private InfoExecutionPeriod infoExecutionPeriod;

    private List infoMasterDegreeProfessorships;

    private List infoProfessorshipList;
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
	 * @return Returns the infoProfessorshipList.
	 */
    public List getInfoProfessorshipList()
    {
        return this.infoProfessorshipList;
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
	 * @param infoProfessorshipList
	 *                   The infoProfessorshipList to set.
	 */
    public void setInfoProfessorshipList(List infoProfessorshipList)
    {
        this.infoProfessorshipList = infoProfessorshipList;
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

    public List getExecutionCourseSupportLessons(final String executionCourseCode)
    {
        List executionCourseSupportLessons = (List) CollectionUtils.select(this
                .getInfoSupportLessonList(), new Predicate()
        {

            public boolean evaluate(Object input)
            {
                InfoSupportLesson infoSupportLesson = (InfoSupportLesson) input;
                return infoSupportLesson.getInfoProfessorship().getInfoExecutionCourse().getSigla()
                        .equals(executionCourseCode);
            }
        });
        return executionCourseSupportLessons;
    }

    public List getExecutionCourseShiftProfessorship (final String executionCourseCode)
	{
		List executionCourseShiftProfessorships = (List) CollectionUtils.select(this
				.getInfoShiftProfessorshipList(), new Predicate()
		{

			public boolean evaluate(Object input)
			{
				InfoShiftProfessorship infoShiftProfessorship = (InfoShiftProfessorship) input;
				return infoShiftProfessorship.getInfoProfessorship().getInfoExecutionCourse().getSigla()
						.equals(executionCourseCode);
			}
		});
		return executionCourseShiftProfessorships;
	}
    
}
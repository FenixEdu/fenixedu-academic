package ServidorAplicacao.strategy.enrolment.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolment;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoObject;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.IEnrollment;

/**
 * @author David Santos
 * Jan 27, 2004
 */

public final class InfoStudentEnrolmentContext extends InfoObject
{
    //deprecated
	private InfoExecutionPeriod infoExecutionPeriod;
	private List finalInfoCurricularCoursesWhereStudentCanBeEnrolled;
	private Integer creditsInSecundaryArea;
	private Integer creditsInSpecializationArea;

	//not deprecated
	private List studentCurrentSemesterInfoEnrollments;
	private List curricularCourses2Enroll;
	private InfoStudentCurricularPlan infoStudentCurricularPlan;
	
	
	public InfoStudentEnrolmentContext() {}

	
	/**
	 * @return Returns the studentCurrentSemesterInfoEnrollments.
	 */
	public List getStudentInfoEnrollmentsWithStateEnrolled()
	{
		return studentCurrentSemesterInfoEnrollments;
	}

	/**
	 * @param studentCurrentSemesterInfoEnrollments The studentCurrentSemesterInfoEnrollments to set.
	 */
	public void setStudentInfoEnrollmentsWithStateEnrolled(List studentInfoEnrollmentsWithStateEnrolled)
	{
		this.studentCurrentSemesterInfoEnrollments = studentInfoEnrollmentsWithStateEnrolled;
	}
	
	/**
	 * @return Returns the finalInfoCurricularCoursesWhereStudentCanBeEnrolled.
	 */
	public List getFinalInfoCurricularCoursesWhereStudentCanBeEnrolled()
	{
		return finalInfoCurricularCoursesWhereStudentCanBeEnrolled;
	}

	/**
	 * @param finalInfoCurricularCoursesWhereStudentCanBeEnrolled The finalInfoCurricularCoursesWhereStudentCanBeEnrolled to set.
	 */
	public void setFinalInfoCurricularCoursesWhereStudentCanBeEnrolled(List finalInfoCurricularCoursesWhereStudentCanBeEnrolled)
	{
		this.finalInfoCurricularCoursesWhereStudentCanBeEnrolled = finalInfoCurricularCoursesWhereStudentCanBeEnrolled;
	}

	/**
	 * @return Returns the infoExecutionPeriod.
	 */
	public InfoExecutionPeriod getInfoExecutionPeriod()
	{
		return infoExecutionPeriod;
	}

	/**
	 * @param infoExecutionPeriod The infoExecutionPeriod to set.
	 */
	public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod)
	{
		this.infoExecutionPeriod = infoExecutionPeriod;
	}

	/**
	 * @return Returns the infoStudentCurricularPlan.
	 */
	public InfoStudentCurricularPlan getInfoStudentCurricularPlan()
	{
		return infoStudentCurricularPlan;
	}

	/**
	 * @param infoStudentCurricularPlan The infoStudentCurricularPlan to set.
	 */
	public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan)
	{
		this.infoStudentCurricularPlan = infoStudentCurricularPlan;
	}

	/**
	 * @return Returns the studentCurrentSemesterInfoEnrollments.
	 */
	public List getStudentCurrentSemesterInfoEnrollments()
	{
		return studentCurrentSemesterInfoEnrollments;
	}

	/**
	 * @param studentCurrentSemesterInfoEnrollments The studentCurrentSemesterInfoEnrollments to set.
	 */
	public void setStudentCurrentSemesterInfoEnrollments(List studentCurrentSemesterInfoEnrollments)
	{
		this.studentCurrentSemesterInfoEnrollments = studentCurrentSemesterInfoEnrollments;
	}

	/**
	 * @return Returns the creditsInSecundaryArea.
	 */
	public Integer getCreditsInSecundaryArea()
	{
		return creditsInSecundaryArea;
	}

	/**
	 * @param creditsInSecundaryArea The creditsInSecundaryArea to set.
	 */
	public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea)
	{
		this.creditsInSecundaryArea = creditsInSecundaryArea;
	}

	/**
	 * @return Returns the creditsInSpecializationArea.
	 */
	public Integer getCreditsInSpecializationArea()
	{
		return creditsInSpecializationArea;
	}

	/**
	 * @param creditsInSpecializationArea The creditsInSpecializationArea to set.
	 */
	public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea)
	{
		this.creditsInSpecializationArea = creditsInSpecializationArea;
	}

	/**
	 * @param infoStudentEnrolmentContext
	 * @return StudentEnrolmentContext
	 */
	public static InfoStudentEnrolmentContext cloneStudentEnrolmentContextToInfoStudentEnrolmentContext(
		StudentEnrolmentContext studentEnrolmentContext)
	{
		Iterator iterator = studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled().iterator();
		List finalInfoCurricularCoursesWhereStudentCanBeEnrolled = new ArrayList();
		while (iterator.hasNext())
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
			InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
			finalInfoCurricularCoursesWhereStudentCanBeEnrolled.add(infoCurricularCourse);
		}

		iterator = studentEnrolmentContext.getStudentCurrentSemesterEnrollments().iterator();
		List studentCurrentSemesterInfoEnrollments = new ArrayList();
		while (iterator.hasNext())
		{
			IEnrollment enrolment = (IEnrollment) iterator.next();
			InfoEnrolment infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(enrolment);
			studentCurrentSemesterInfoEnrollments.add(infoEnrolment);
		}
		
		InfoStudentCurricularPlan infoStudentCurricularPlan =
			Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentEnrolmentContext.getStudentCurricularPlan());

		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(studentEnrolmentContext.getExecutionPeriod());

		InfoStudentEnrolmentContext infoStudentEnrolmentContext = new InfoStudentEnrolmentContext();
		infoStudentEnrolmentContext.setFinalInfoCurricularCoursesWhereStudentCanBeEnrolled(
			finalInfoCurricularCoursesWhereStudentCanBeEnrolled);
		infoStudentEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);
		infoStudentEnrolmentContext.setInfoStudentCurricularPlan(infoStudentCurricularPlan);
		infoStudentEnrolmentContext.setStudentCurrentSemesterInfoEnrollments(studentCurrentSemesterInfoEnrollments);
		infoStudentEnrolmentContext.setCreditsInSecundaryArea(studentEnrolmentContext.getCreditsInSecundaryArea());
		infoStudentEnrolmentContext.setCreditsInSpecializationArea(studentEnrolmentContext.getCreditsInSpecializationArea());
		
		return infoStudentEnrolmentContext;
	}

    /**
     * @return Returns the curricularCourses2Enroll.
     */
    public List getCurricularCourses2Enroll() {
        return curricularCourses2Enroll;
    }
    /**
     * @param curricularCourses2Enroll The curricularCourses2Enroll to set.
     */
    public void setCurricularCourses2Enroll(List curricularCourses2Enroll) {
        this.curricularCourses2Enroll = curricularCourses2Enroll;
    }
}
package ServidorAplicacao.strategy.enrolment.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoBranch;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolment;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;

/**
 * @author David Santos
 * Jan 27, 2004
 */

public final class InfoStudentEnrolmentContext
{
	private InfoExecutionPeriod infoExecutionPeriod;
	private InfoStudentCurricularPlan infoStudentCurricularPlan;
	private List studentCurrentSemesterInfoEnrollments;
	private List finalInfoCurricularCoursesWhereStudentCanBeEnrolled;
	private List infoAreas;

	public InfoStudentEnrolmentContext() {}

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
	 * @return Returns the infoAreas.
	 */
	public List getInfoAreas()
	{
		return infoAreas;
	}

	/**
	 * @param infoAreas The infoAreas to set.
	 */
	public void setInfoAreas(List infoAreas)
	{
		this.infoAreas = infoAreas;
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
			IEnrolment enrolment = (IEnrolment) iterator.next();
			InfoEnrolment infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(enrolment);
			studentCurrentSemesterInfoEnrollments.add(infoEnrolment);
		}
		
		iterator = studentEnrolmentContext.getAreas().iterator();
		List infoAreas = new ArrayList();
		while (iterator.hasNext())
		{
			IBranch branch = (IBranch) iterator.next();
			InfoBranch infoBranch = Cloner.copyIBranch2InfoBranch(branch);
			infoAreas.add(infoBranch);
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
		infoStudentEnrolmentContext.setInfoAreas(infoAreas);
		
		return infoStudentEnrolmentContext;
	}
}
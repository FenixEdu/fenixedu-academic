package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolment;
import DataBeans.InfoObject;
import DataBeans.teacher.credits.InfoCredits;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.BothAreasAreTheSameServiceException;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Fernanda Quitério 27/Jan/2004
 *  
 */
public class CurricularCoursesEnrollmentDispatchAction extends DispatchAction
{

	public ActionForward prepareEnrollmentChooseStudent(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		return mapping.findForward("prepareEnrollmentChooseStudent");
	}

	public ActionForward prepareEnrollmentChooseCurricularCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		ActionErrors errors = new ActionErrors();
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;

		Integer studentNumber = new Integer((String) enrollmentForm.get("studentNumber"));

		InfoStudentEnrolmentContext infoStudentEnrolmentContext = null;
		Object[] args = { studentNumber };
		try
		{
			infoStudentEnrolmentContext =
				(InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(
					userView,
					"ShowAvailableCurricularCourses",
					args);

		}
		catch (ExistingServiceException e)
		{
			if (e.getMessage().equals("student"))
			{
				errors.add("student", new ActionError("error.no.student.in.database", studentNumber));
			}
			else if (e.getMessage().equals("studentCurricularPlan"))
			{
				errors.add(
					"studentCurricularPlan",
					new ActionError("error.student.curricularPlan.nonExistent"));
			}
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		catch (FenixServiceException e)
		{
			if (e.getMessage().equals("degree"))
			{
				errors.add("degree", new ActionError("error.student.degreeCurricularPlan.LEEC"));
				saveErrors(request, errors);
				return mapping.getInputForward();
			}
			throw new FenixActionException(e);
		}

		Integer[] enrolledInArray =
			buildArrayForForm(infoStudentEnrolmentContext.getStudentCurrentSemesterInfoEnrollments());
		enrollmentForm.set("enrolledCurricularCoursesBefore", enrolledInArray);
		enrollmentForm.set("enrolledCurricularCoursesAfter", enrolledInArray);
		//		Integer[] curricularCoursesToEnroll =
		// buildArrayForForm(infoStudentEnrolmentContext.getFinalInfoCurricularCoursesWhereStudentCanBeEnrolled());
		//		enrollmentForm.set("unenrolledCurricularCoursesToDisable", curricularCoursesToEnroll);

		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);

		return mapping.findForward("prepareEnrollmentChooseCurricularCourses");
	}

	private Integer[] buildArrayForForm(List listToTransform)
	{
		List newList = new ArrayList();
		newList = (List) CollectionUtils.collect(listToTransform, new Transformer()
		{
			public Object transform(Object arg0)
			{
				InfoObject infoObject = (InfoObject) arg0;
				return infoObject.getIdInternal();
			}
		});
		Integer[] array = new Integer[newList.size()];
		for (int i = 0; i < array.length; i++)
		{
			array[i] = (Integer) newList.get(i);
		}
		return array;
	}

	public ActionForward prepareEnrollmentPrepareChooseAreas(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		ActionErrors errors = new ActionErrors();
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;

		Integer studentNumber = Integer.valueOf(request.getParameter("studentNumber"));
		enrollmentForm.set("studentNumber", studentNumber.toString());

		String specialization = request.getParameter("specializationArea");
		String secondary = request.getParameter("secondaryArea");

		maintainEnrollmentState(request, studentNumber);

		List infoBranches = null;
		Object[] args = { studentNumber };
		try
		{
			infoBranches =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadSpecializationAndSecundaryAreasByStudent",
					args);

		}
		catch (ExistingServiceException e)
		{
			if (e.getMessage().equals("student"))
			{
				errors.add("student", new ActionError("error.no.student.in.database", studentNumber));
			}
			else if (e.getMessage().equals("studentCurricularPlan"))
			{
				errors.add(
					"studentCurricularPlan",
					new ActionError("error.student.curricularPlan.nonExistent"));
			}
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException();
		}
		if (specialization != null
			&& specialization.length() > 0
			&& secondary != null
			&& secondary.length() > 0)
		{
			enrollmentForm.set("specializationArea", Integer.valueOf(specialization));
			enrollmentForm.set("secondaryArea", Integer.valueOf(secondary));
		}
		request.setAttribute("infoBranches", infoBranches);
		return mapping.findForward("prepareEnrollmentChooseAreas");
	}

	private void maintainEnrollmentState(HttpServletRequest request, Integer studentNumber)
	{
		String executionPeriod = request.getParameter("executionPeriod");
		String executionYear = request.getParameter("executionYear");
		String studentName = request.getParameter("studentName");
		String studentCurricularPlanId = request.getParameter("studentCurricularPlanId");

		request.setAttribute("executionPeriod", executionPeriod);
		request.setAttribute("executionYear", executionYear);
		request.setAttribute("studentName", studentName);
		request.setAttribute("studentNumber", studentNumber.toString());
		request.setAttribute("studentCurricularPlanId", studentCurricularPlanId);
	}

	public ActionForward prepareEnrollmentChooseAreas(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		ActionErrors errors = new ActionErrors();
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
		Integer specializationArea = (Integer) enrollmentForm.get("specializationArea");
		Integer secondaryArea = (Integer) enrollmentForm.get("secondaryArea");
		Integer studentCurricularPlanId =
			Integer.valueOf(request.getParameter("studentCurricularPlanId"));
		Integer studentNumber = Integer.valueOf((String) enrollmentForm.get("studentNumber"));

		Object[] args = { studentCurricularPlanId, specializationArea, secondaryArea };
		try
		{
			ServiceManagerServiceFactory.executeService(userView, "WriteStudentAreas", args);

		}
		catch (BothAreasAreTheSameServiceException e)
		{
			errors.add("bothAreas", new ActionError("error.student.enrollment.AreasNotEqual"));
			maintainEnrollmentState(request, studentNumber);
			saveErrors(request, errors);
			return mapping.findForward("prepareChooseAreas");
		}
		catch (ExistingServiceException e)
		{
			errors.add(
				"studentCurricularPlan",
				new ActionError("error.student.curricularPlan.nonExistent"));
			maintainEnrollmentState(request, studentNumber);
			saveErrors(request, errors);
			return mapping.findForward("prepareChooseAreas");
		}
		catch (InvalidArgumentsServiceException e)
		{
			errors.add("areas", new ActionError("error.areas.choose"));
			maintainEnrollmentState(request, studentNumber);
			saveErrors(request, errors);
			return mapping.findForward("prepareChooseAreas");
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException();
		}

		return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
	}

	public ActionForward enrollInCurricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
		Integer[] curricularCoursesToEnroll =
			(Integer[]) enrollmentForm.get("unenrolledCurricularCourses");

		List toEnroll = Arrays.asList(curricularCoursesToEnroll);
		Integer studentCurricularPlanId =
			Integer.valueOf(request.getParameter("studentCurricularPlanId"));

		if (toEnroll.size() > 0)
		{
			Object[] args = { studentCurricularPlanId, toEnroll.get(0), null };
			try
			{
				ServiceManagerServiceFactory.executeService(userView, "WriteEnrolment", args);
			}
			catch (FenixServiceException e)
			{
				throw new FenixActionException(e);
			}
		}
		return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
	}

	public ActionForward unenrollFromCurricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
		Integer[] enrolledCurricularCoursesBefore =
			(Integer[]) enrollmentForm.get("enrolledCurricularCoursesBefore");
		Integer[] enrolledCurricularCoursesAfter =
			(Integer[]) enrollmentForm.get("enrolledCurricularCoursesAfter");

		List enrollmentsBefore = Arrays.asList(enrolledCurricularCoursesBefore);
		List enrollmentsAfter = Arrays.asList(enrolledCurricularCoursesAfter);
		List toUnenroll = (List) CollectionUtils.subtract(enrollmentsBefore, enrollmentsAfter);
		if (toUnenroll.size() > 0)
		{
			Object[] args = {(Integer) toUnenroll.get(0)};
			try
			{
				ServiceManagerServiceFactory.executeService(userView, "DeleteEnrolment", args);
			}
			catch (FenixServiceException e)
			{
				throw new FenixActionException(e);
			}
		}
		return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
	}
}
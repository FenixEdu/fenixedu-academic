/*
 * Created on 10/Fev/2004
 *  
 */
package ServidorApresentacao.Action.student;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.LookupDispatchAction;

import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class ShiftStudentEnrolmentManagerLookupDispatchAction extends LookupDispatchAction
{
	public ActionForward addCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println("-->addCourses");

		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		//Read data from form
		DynaActionForm enrollmentForm = (DynaActionForm) form;
		Integer wantedCourse = (Integer) enrollmentForm.get("wantedCourse");
		System.out.println("-->wantedCourse: " + wantedCourse);

		Integer studentNumber = (Integer) enrollmentForm.get("studentNumber");
		System.out.println("-->studentNumber: " + studentNumber);

		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setDegreeType(TipoCurso.LICENCIATURA_OBJ);
		infoStudent.setNumber(studentNumber);

		//Add course
		Object[] args = { infoStudent, wantedCourse };
		Boolean result = Boolean.FALSE;
		try
		{
			result =
				(Boolean) ServiceManagerServiceFactory.executeService(
					userView,
					"WriteStudentAttendingCourse",
					args);
		}
		catch (FenixServiceException exception)
		{
			exception.printStackTrace();
			if (exception.getMessage().endsWith("reachedAteendsLimit"))
			{
				errors.add("error", new ActionMessage("message.maximum.number.curricular.courses.to.enroll", new Integer(8)));
			}
			else
			{
				errors.add("error", new ActionMessage("errors.impossible.operation"));
			}

			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (result.equals(Boolean.FALSE))
		{
			System.out.println("-->FALSE");
			errors.add("error", new ActionMessage("errors.impossible.operation"));
			saveErrors(request, errors);

			return mapping.getInputForward();
		}

		return mapping.findForward("start");
	}

	public ActionForward removeCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println("-->removeCourses");

		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		//Read data from form
		DynaActionForm enrollmentForm = (DynaActionForm) form;
		Integer removedCourse = (Integer) enrollmentForm.get("removedCourse");
		System.out.println("-->removedCourse: " + removedCourse);

		Integer studentNumber = (Integer) enrollmentForm.get("studentNumber");
		System.out.println("-->studentNumber: " + studentNumber);

		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setDegreeType(TipoCurso.LICENCIATURA_OBJ);
		infoStudent.setNumber(studentNumber);

		//Remove course
		Object[] args = { infoStudent, removedCourse };
		Boolean result = Boolean.FALSE;
		try
		{
			result =
				(Boolean) ServiceManagerServiceFactory.executeService(
					userView,
					"DeleteStudentAttendingCourse",
					args);
		}
		catch (FenixServiceException exception)
		{ 
			exception.printStackTrace();
			if (exception.getMessage().endsWith("alreadyEnrolledInGroup"))
			{
				System.out.println("-->alreadyEnrolledInGroup");
				errors.add("error", new ActionMessage("errors.student.already.enroled.in.group"));
			}
			else if (exception.getMessage().endsWith("alreadyEnrolled"))
			{
				System.out.println("-->alreadyEnrolled");
				errors.add("error", new ActionMessage("errors.student.already.enroled"));
			}
			else if (exception.getMessage().endsWith("alreadyEnrolledInShift"))
			{
				System.out.println("-->alreadyEnrolledInShift");
				errors.add("error", new ActionMessage("errors.student.already.enroled.in.shift"));
			}
			else
			{
				errors.add("error", new ActionMessage("errors.impossible.operation"));
			}

			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (result.equals(Boolean.FALSE))
		{
			System.out.println("-->FALSE");
			errors.add("error", new ActionMessage("errors.impossible.operation"));
			saveErrors(request, errors);

			return mapping.getInputForward();
		}

		return mapping.findForward("start");
	}

	public ActionForward proceedToShiftEnrolment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{

		System.out.println("-->proceedToShiftEnrolment");
		return mapping.findForward("");
	}

	public ActionForward exitEnrollment(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		return mapping.findForward("studentFirstPage");
	}

	protected Map getKeyMethodMap()
	{
		Map map = new HashMap();
		map.put("button.addCourse", "addCourses");
		map.put("button.removeCourse", "removeCourses");
		map.put("button.continue.enrolment", "proceedToShiftEnrolment");
		map.put("button.exit.enrollment", "exitEnrollment");
		return map;
	}

}

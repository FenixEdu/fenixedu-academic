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

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoStudent;
import DataBeans.enrollment.shift.InfoClassEnrollmentDetails;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.commons.TransactionalLookupDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixTransactionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class ShiftStudentEnrolmentManagerLookupDispatchAction extends TransactionalLookupDispatchAction
{
	public ActionForward addCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws FenixTransactionException
	{
		super.validateToken(request, form, mapping, "error.transaction.enrollment");
		
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		//Read data from form
		DynaActionForm enrollmentForm = (DynaActionForm) form;
		Integer wantedCourse = (Integer) enrollmentForm.get("wantedCourse");

		Integer studentId = (Integer) enrollmentForm.get("studentId");

		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setIdInternal(studentId);

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
				errors.add(
					"error",
					new ActionError(
						"message.maximum.number.curricular.courses.to.enroll",
						new Integer(8)));
			}
			else
			{
				errors.add("error", new ActionError("errors.impossible.operation"));
			}

			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (result.equals(Boolean.FALSE))
		{
			errors.add("error", new ActionError("errors.impossible.operation"));
			saveErrors(request, errors);

			return mapping.getInputForward();
		}
		
		return mapping.findForward("chooseCourses");
	}

	public ActionForward removeCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws FenixTransactionException
	{
		super.validateToken(request, form, mapping, "error.transaction.enrollment");
		
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		//Read data from form
		DynaActionForm enrollmentForm = (DynaActionForm) form;
		Integer removedCourse = (Integer) enrollmentForm.get("removedCourse");
		
		Integer studentId = (Integer) enrollmentForm.get("studentId");
		
		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setIdInternal(studentId);

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
				errors.add("error", new ActionError("errors.student.already.enroled.in.group"));
			}
			else if (exception.getMessage().endsWith("alreadyEnrolled"))
			{
				errors.add("error", new ActionError("errors.student.already.enroled"));
			}
			else if (exception.getMessage().endsWith("alreadyEnrolledInShift"))
			{
				errors.add("error", new ActionError("errors.student.already.enroled.in.shift"));
			}
			else
			{
				errors.add("error", new ActionError("errors.impossible.operation"));
			}

			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (result.equals(Boolean.FALSE))
		{
			errors.add("error", new ActionError("errors.impossible.operation"));
			saveErrors(request, errors);

			return mapping.getInputForward();
		}

		return mapping.findForward("chooseCourses");
	}

	public ActionForward proceedToShiftEnrolment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		System.out.println("-->proceedToShiftEnrolment");

		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		//Read data from form
		DynaActionForm enrollmentForm = (DynaActionForm) form;
		Integer studentId = (Integer) enrollmentForm.get("studentId");
		System.out.println("-->studentId: " + studentId);

		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setIdInternal(studentId);

		Object[] args = { infoStudent };
		InfoClassEnrollmentDetails infoClassEnrollmentDetails = null;
		try
		{
			infoClassEnrollmentDetails =
				(InfoClassEnrollmentDetails) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadClassShiftEnrollmentDetails",
					args);
		}
		catch (FenixServiceException exception)
		{
			exception.printStackTrace();
			errors.add("error", new ActionError("errors.impossible.operation"));

			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if(infoClassEnrollmentDetails == null)	{
			errors.add("error", new ActionError("errors.impossible.operation"));

			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		
		request.setAttribute("infoClassEnrollmentDetails", infoClassEnrollmentDetails);

		System.out.println("---------------------------------------------------");
		System.out.println(infoClassEnrollmentDetails);
		System.out.println("---------------------------------------------------");
		
		
		return mapping.findForward("");
	}

	public ActionForward exitEnrollment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
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

/*
 * Created on 16/Mai/2003
 * 
 *  
 */
package ServidorApresentacao.Action.student.enrollment;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoStudent;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import DataBeans.enrollment.shift.InfoShiftEnrollment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.ExecutionDegreesFormat;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author tdi-dev (bruno) Modified by Tânia Pousão
 *  
 */
public class ShiftStudentEnrolmentManagerDispatchAction extends TransactionalDispatchAction
{
	public ActionForward start(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		super.createToken(request);

		return chooseCourses(mapping, form, request, response);
	}

	public ActionForward chooseCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();
		IUserView userView = SessionUtils.getUserView(request);

		//TODO:FIXME:THIS IS JUST A TEMPORARY BYPASS TO PREVENT 1ST YEAR STUDENTS FROM ENROLLING IN
		// SHIFTS
		if ((new Integer(userView.getUtilizador().substring(1))).intValue() > 53227)
		{
			errors.add(
				"notAuthorizedShiftEnrollment",
				new ActionError("error.notAuthorized.ShiftEnrollment"));
			saveErrors(request, errors);
			return mapping.findForward("studentFirstPage");
		}

		DynaActionForm enrolmentForm = (DynaActionForm) form;
		Integer executionDegreeIdChosen = (Integer) enrolmentForm.get("degree");

		String studentNumber = obtainStudentNumber(request, userView);

		InfoShiftEnrollment infoShiftEnrollment = null;
		Object[] args = { Integer.valueOf(studentNumber), executionDegreeIdChosen };
		try
		{
			infoShiftEnrollment =
				(InfoShiftEnrollment) ServiceManagerServiceFactory.executeService(
					userView,
					"PrepareInfoShiftEnrollmentByUsername",
					args);
		}
		catch (FenixServiceException serviceException)
		{
			serviceException.printStackTrace();
			errors.add("error", new ActionError(serviceException.getMessage()));
			saveErrors(request, errors);
			return mapping.findForward("studentFirstPage");
		}

		//inicialize the form with the degree chosen and student number
		enrolmentForm.set("degree", infoShiftEnrollment.getInfoExecutionDegree().getIdInternal());
		enrolmentForm.set("studentId", infoShiftEnrollment.getInfoStudent().getIdInternal());

		request.setAttribute("infoShiftEnrollment", infoShiftEnrollment);

		if (infoShiftEnrollment.getInfoShiftEnrollment() != null
			&& infoShiftEnrollment.getInfoShiftEnrollment().size() > 0)
		{
			return mapping.findForward("showShiftsEnrollment");
		}
		else
		{
			//order degree's list and format them names
			if (infoShiftEnrollment.getInfoExecutionDegreesList() != null
				&& infoShiftEnrollment.getInfoExecutionDegreesList().size() > 0)
			{
				Collections.sort(
					infoShiftEnrollment.getInfoExecutionDegreesList(),
					new ComparatorByNameForInfoExecutionDegree());
				infoShiftEnrollment.setInfoExecutionDegreesLabelsList(
					ExecutionDegreesFormat.buildExecutionDegreeLabelValueBean(
						infoShiftEnrollment.getInfoExecutionDegreesList()));
			}

			return mapping.findForward("selectCourses");
		}
	}

	private String obtainStudentNumber(HttpServletRequest request, IUserView userView) throws FenixActionException
	{
		String studentNumber = getStudent(request);
		if (studentNumber == null)
		{
			InfoStudent infoStudent = null;
			try
			{
				Object args[] = { userView.getUtilizador()};
				infoStudent =
					(InfoStudent) ServiceManagerServiceFactory.executeService(
						userView,
						"ReadStudentByUsername",
						args);
			}
			catch (FenixServiceException e)
			{
				throw new FenixActionException(e);
			}
			studentNumber = infoStudent.getNumber().toString();
		}
		return studentNumber;
	}

	private String getStudent(HttpServletRequest request)
	{
		String studentNumber = request.getParameter("studentNumber");
		if (studentNumber == null)
		{
			studentNumber = (String) request.getAttribute("studentNumber");
		}
		return studentNumber;
	}

}

/*
 * Created on 16/Mai/2003
 * 
 *  
 */
package ServidorApresentacao.Action.student;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import DataBeans.enrollment.shift.InfoShiftEnrollment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.ExecutionDegreesFormat;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author tdi-dev (bruno)
 * Modified by Tânia Pousão
 * 
 * This class is used to group shifts by 'main types' (such as classes or courses) and then subdivide by
 * shift types
 */
public class ShiftStudentEnrolmentManagerDispatchAction extends TransactionalDispatchAction
{	
	public ActionForward start(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	{
		super.createToken(request);
		
		return chooseCourses(mapping, form, request, response);
	}
	
	public ActionForward chooseCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
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
		
		InfoShiftEnrollment infoShiftEnrollment = null;
		Object[] args = { userView.getUtilizador(), executionDegreeIdChosen };
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
		
		//inicialize the form with the degree chosen and student number
		enrolmentForm.set("degree", infoShiftEnrollment.getInfoExecutionDegree().getIdInternal());
		enrolmentForm.set("studentId", infoShiftEnrollment.getInfoStudent().getIdInternal());

		request.setAttribute("infoShiftEnrollment", infoShiftEnrollment);
		
		return mapping.findForward("selectCourses");
	}

}

/*
 * Created on 2/Fev/2004
 *  
 */
package ServidorApresentacao.Action.coordinator;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class TutorManagementDispatchAction extends FenixDispatchAction
{
	public ActionForward prepareChooseTutor(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		String executionDegreeId = request.getParameter("executionDegreeId");
		request.setAttribute("executionDegreeId", executionDegreeId);

		//This code is temporary, it just verify if the coordinator logged is a LEEC's coordinator
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Object[] args = { Integer.valueOf(executionDegreeId), userView.getUtilizador(), "LEEC" };
		Boolean authorized = Boolean.FALSE;
		try
		{
			authorized =
				(Boolean) ServiceManagerServiceFactory.executeService(
					userView,
					"UserCoordinatorByExecutionDegree",
					args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			errors.add("error", new ActionError("error.tutor.impossibleOperation"));
			saveErrors(request, errors);
			return mapping.findForward("notAuthorized");
		}
		if (authorized.booleanValue() == false)
		{
			errors.add("notAuthorized", new ActionError("error.tutor.notAuthorized.LEEC"));
			saveErrors(request, errors);

			return mapping.findForward("notAuthorized");
		}

		return mapping.findForward("chooseTutor");
	}

	public ActionForward readTutor(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		HttpSession httpSession = request.getSession();
		IUserView userView = (IUserView) httpSession.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm tutorForm = (DynaActionForm) actionForm;
		Integer tutorNumber = Integer.valueOf((String) tutorForm.get("tutorNumber"));
		request.setAttribute("tutorNumber", tutorNumber);

		Integer executionDegreeId = Integer.valueOf((String) tutorForm.get("executionDegreeId"));
		request.setAttribute("executionDegreeId", executionDegreeId);

		Object[] args = { executionDegreeId, tutorNumber };
		List infoStudentsOfTutor = null;
		try
		{
			infoStudentsOfTutor =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadStudentsByTutor",
					args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			errors.add("error", new ActionError(e.getMessage(), tutorNumber));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (infoStudentsOfTutor != null && infoStudentsOfTutor.size() >= 0)
		{
			//order list by number
			Collections.sort(infoStudentsOfTutor, new BeanComparator("infoStudent.number"));
			request.setAttribute("studentsOfTutor", infoStudentsOfTutor);
		}
		else
		{
			//read teacher
			Object[] args2 = { tutorNumber };
			InfoTeacher infoTeacher = null;
			try
			{
				infoTeacher =
					(InfoTeacher) ServiceManagerServiceFactory.executeService(
						userView,
						"ReadTeacherByNumber",
						args2);
			}
			catch (FenixServiceException e)
			{
				e.printStackTrace();
				errors.add("error", new ActionError(e.getMessage(), tutorNumber));
				saveErrors(request, errors);
			}
			if (infoTeacher != null)
			{
				request.setAttribute("infoTeacher", infoTeacher);
			}
		}

		cleanForm(tutorForm);

		return mapping.findForward("showStudentsByTutor");
	}

	/**
	 * @param tutorForm
	 */
	private void cleanForm(DynaActionForm tutorForm)
	{
		tutorForm.set("studentNumber", null);
		tutorForm.set("studentNumberFirst", null);
		tutorForm.set("studentNumberSecond", null);
	}
}

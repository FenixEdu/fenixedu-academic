/*
 * Created on 2/Fev/2004
 *  
 */
package ServidorApresentacao.Action.coordinator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

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
		System.out.println("-->prepareChooseTutor...");

		String executionDegreeId = request.getParameter("executionDegreeId");
		System.out.println("executionDegreeId: " + executionDegreeId);
		request.setAttribute("executionDegreeId", executionDegreeId);

		return mapping.findForward("chooseTutor");
	}

	public ActionForward readTutor(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		System.out.println("-->readTutor...");

		ActionErrors errors = new ActionErrors();

		HttpSession httpSession = request.getSession();
		IUserView userView = (IUserView) httpSession.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm tutorForm = (DynaActionForm) actionForm;
		Integer tutorNumber = (Integer) tutorForm.get("tutorNumber");
		System.out.println("tutorNumber: " + tutorNumber);
		request.setAttribute("tutorNumber", tutorNumber);

		Integer executionDegreeId = (Integer) tutorForm.get("executionDegreeId");
		System.out.println("executionDegreeId: " + executionDegreeId);
		request.setAttribute("executionDegreeId", executionDegreeId);

		Object[] args = { tutorNumber };
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
			mapping.getInputForward();
		}
		request.setAttribute("studentsOfTutor", infoStudentsOfTutor);

		return mapping.findForward("showStudentsByTutor");
	}
}

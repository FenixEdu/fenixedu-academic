/*
 * Created on 3/Fev/2004
 *  
 */
package ServidorApresentacao.Action.coordinator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.apache.struts.actions.LookupDispatchAction;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class TutorManagementLookupDispatchAction extends LookupDispatchAction
{

	public ActionForward insertTutorShipWithOneStudent(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm tutorForm = (DynaActionForm) actionForm;
		Integer tutorNumber = (Integer) tutorForm.get("tutorNumber");
		request.setAttribute("tutorNumber", tutorNumber);

		Integer executionDegreeId = (Integer) tutorForm.get("executionDegreeId");
		request.setAttribute("executionDegreeId", executionDegreeId);

		Integer studentNumber = (Integer) tutorForm.get("studentNumber");

		Object[] args = { tutorNumber, studentNumber };

		try
		{
			ServiceManagerServiceFactory.executeService(userView, "InsertTutorShipWithOneStudent", args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			errors.add("errors", new ActionError(e.getMessage()));
		}

		return mapping.findForward("confirmation");
	}

	public ActionForward insertTutorShipWithManyStudent(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm tutorForm = (DynaActionForm) actionForm;
		Integer tutorNumber = (Integer) tutorForm.get("tutorNumber");
		request.setAttribute("tutorNumber", tutorNumber);

		Integer executionDegreeId = (Integer) tutorForm.get("executionDegreeId");
		request.setAttribute("executionDegreeId", executionDegreeId);

		Integer studentNumberFirst = (Integer) tutorForm.get("studentNumberFirst");
		Integer studentNumberSecond = (Integer) tutorForm.get("studentNumberSecond");

		Object[] args = { tutorNumber, studentNumberFirst, studentNumberSecond };

		try
		{
			ServiceManagerServiceFactory.executeService(
				userView,
				"InsertTutorShipWithManyStudent",
				args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			errors.add("errors", new ActionError(e.getMessage()));
		}

		return mapping.findForward("confirmation");
	}

	public ActionForward deleteTutorShip(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm tutorForm = (DynaActionForm) actionForm;
		Integer tutorNumber = (Integer) tutorForm.get("tutorNumber");
		request.setAttribute("tutorNumber", tutorNumber);

		Integer executionDegreeId = (Integer) tutorForm.get("executionDegreeId");
		request.setAttribute("executionDegreeId", executionDegreeId);

		Integer[] deletedTutors = (Integer[]) tutorForm.get("deletedTutorsIds");
		List deletedTutorsList = Arrays.asList(deletedTutors);
		Object[] args = { tutorNumber, deletedTutorsList };

		try
		{
			ServiceManagerServiceFactory.executeService(userView, "DeleteTutorShip", args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			errors.add("errors", new ActionError(e.getMessage()));
		}

		return mapping.findForward("confirmation");
	}

	public ActionForward cancel(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		return mapping.findForward("cancel");
	}

	protected Map getKeyMethodMap()
	{
		Map map = new HashMap();
		map.put("button.coordinator.tutor.associateOneStudent", "insertTutorShipWithOneStudent");
		map.put("button.coordinator.tutor.associateManyStudent", "insertTutorShipWithManyStudent");
		map.put("button.coordinator.tutor.remove", "deleteTutorShip");
		map.put("button.cancel", "cancel");
		return map;
	}

}

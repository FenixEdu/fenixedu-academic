/*
 * Created on 5/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class DeleteExecutionDegreesAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest request, 
								 HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		DynaActionForm deleteForm = (DynaActionForm) form;

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		List executionDegreesIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));
		Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
		Object args[] = { executionDegreesIds };
		GestorServicos manager = GestorServicos.manager();
		List errorsList = new ArrayList();

		try {
				errorsList = (List) manager.executar(userView, "DeleteExecutionDegreesService", args);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		
		if (!errorsList.isEmpty()) {
				ActionErrors actionErrors = new ActionErrors();
				ActionError error = null;
				Iterator iter = errorsList.iterator();
				while (iter.hasNext()) {
						// Create an ACTION_ERROR for each EXECUTION_DEGREE
						error = new ActionError("errors.invalid.delete.not.empty.execution.degree", iter.next());
						actionErrors.add("errors.invalid.delete.not.empty.execution.degree", error);
				}
				saveErrors(request, actionErrors);
		}
		return mapping.findForward("readDegreeCurricularPlan");
	}

}
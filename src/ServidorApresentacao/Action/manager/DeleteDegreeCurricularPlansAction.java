/*
 * Created on 31/Jul/2003
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

public class DeleteDegreeCurricularPlansAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest request, 
								 HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		DynaActionForm deleteForm = (DynaActionForm) form;

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		List degreeCurricularPlansIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));
		Integer degreeId = new Integer(request.getParameter("degreeId"));
		Object args[] = { degreeCurricularPlansIds };
		GestorServicos manager = GestorServicos.manager();
		List errorNames = new ArrayList();

		try {
				errorNames = (List) manager.executar(userView, "DeleteDegreeCurricularPlansService", args);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		
		if (!errorNames.isEmpty()) {
				ActionErrors actionErrors = new ActionErrors();
				Iterator namesIter = errorNames.iterator();
				ActionError error = null;
				while (namesIter.hasNext()) {
						// Create an ACTION_ERROR for each DEGREE_CURRICULAR_PLAN
						error = new ActionError("errors.invalid.delete.not.empty.degree.curricular.plan", (String) namesIter.next());
						actionErrors.add("errors.invalid.delete.not.empty.degree.curricular.plan", error);
				}
				saveErrors(request, actionErrors);
		}
		request.setAttribute("degreeId", degreeId);
		return mapping.findForward("readDegree");
	}

}
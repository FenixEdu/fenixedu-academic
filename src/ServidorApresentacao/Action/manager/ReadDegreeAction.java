/*
 * Created on 15/Mai/2003
 *
 */
package ServidorApresentacao.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class ReadDegreeAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer degreeId = new Integer(request.getParameter("degreeId"));
		Object args[] = { degreeId };

		GestorServicos manager = GestorServicos.manager();
		InfoDegree degree = null;

		try {
				degree = (InfoDegree) manager.executar(userView, "ReadDegree", args);
				
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("message.nonExistingDegree", "", e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		// in case the degree really exists
		List degreeCurricularPlans = null;

		try {
				degreeCurricularPlans = (List) manager.executar(userView, "ReadDegreeCurricularPlansByDegree", args);
				
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		Collections.sort(degreeCurricularPlans);
		request.setAttribute("infoDegree", degree);
		request.setAttribute("lista de planos curriculares", degreeCurricularPlans);
		return mapping.findForward("viewDegree");
	}
}

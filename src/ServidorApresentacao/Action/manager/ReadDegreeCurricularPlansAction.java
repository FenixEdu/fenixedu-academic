/*
 * Created on 29/Jul/2003
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

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlansAction extends FenixAction {

		public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
		
			HttpSession session = getSession(request);
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
		
			try {		
					List degreeCurricularPlans = null;
					GestorServicos serviceManager = GestorServicos.manager();
					degreeCurricularPlans = (List) serviceManager.executar(
								userView,
								"ReadDegreeCurricularPlansService",
								null);
		
					Collections.sort(degreeCurricularPlans);
					request.setAttribute(SessionConstants.INFO_DEGREE_CURRICULAR_PLANS_LIST, degreeCurricularPlans);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			return mapping.findForward("readDegreeCurricularPlans");
		}

}
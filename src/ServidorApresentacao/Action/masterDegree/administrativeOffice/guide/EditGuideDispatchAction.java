
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoGuide;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;
import Util.PaymentType;
import Util.SituationOfGuide;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class EditGuideDispatchAction extends DispatchAction {

	public ActionForward prepareEditSituation(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm editGuideForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			// Get the Information
			Integer guideNumber = new Integer((String) request.getParameter("number"));
			Integer guideYear = new Integer((String) request.getParameter("year"));
			Integer guideVersion = new Integer((String) request.getParameter("version"));
					
			Object args[] = { guideNumber, guideYear , guideVersion };

			InfoGuide infoGuide = null;
			try {
				infoGuide = (InfoGuide) serviceManager.executar(userView, "ChooseGuide", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Versão da Guia", e);
			}

			editGuideForm.set("paymentDateDay", Data.OPTION_DEFAULT.toString());
			editGuideForm.set("paymentDateMonth", Data.OPTION_DEFAULT.toString());
			editGuideForm.set( "paymentDateYear", Data.OPTION_DEFAULT.toString());
			request.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
			request.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
			request.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());
			session.setAttribute(SessionConstants.PAYMENT_TYPE, PaymentType.toArrayList());
			request.setAttribute(SessionConstants.GUIDE, infoGuide);
			request.setAttribute(SessionConstants.GUIDE_SITUATION_LIST, SituationOfGuide.toArrayList());
					
			return mapping.findForward("EditReady");
		  } else
			throw new Exception();   

	}
		

	  
}

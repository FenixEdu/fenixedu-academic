
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoGuide;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.exceptions.NonValidChangeActionException;
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
			session.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
			session.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
			session.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());
			session.setAttribute(SessionConstants.PAYMENT_TYPE, PaymentType.toArrayList());
			session.setAttribute(SessionConstants.GUIDE, infoGuide);
			session.setAttribute(SessionConstants.GUIDE_SITUATION_LIST, SituationOfGuide.toArrayList());
				
			return mapping.findForward("EditReady");
		  } else
			throw new Exception();   
	}
		

	public ActionForward editGuideSituation(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm editGuideForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			Integer guideYear = new Integer((String) request.getParameter("year"));
			Integer guideNumber = new Integer((String) request.getParameter("number"));
			Integer guideVersion = new Integer((String) request.getParameter("version"));
			
			String remarks = (String) request.getParameter("remarks");
			String situationOfGuide = (String) request.getParameter("guideSituation");
			Integer paymentDateYear = new Integer((String) request.getParameter("paymentDateYear"));
			Integer paymentDateMonth = new Integer((String) request.getParameter("paymentDateMonth"));
			Integer paymentDateDay = new Integer((String) request.getParameter("paymentDateDay"));
			String paymentType = (String) request.getParameter("paymentType");
			
			// Final form Check

			ActionErrors actionErrors = new ActionErrors();
			if ((situationOfGuide.equals(SituationOfGuide.PAYED_STRING)) && (
					(paymentDateYear.equals(new Integer(-1))) ||
					(paymentDateMonth.equals(new Integer(-1))) ||
					(paymentDateDay.equals(new Integer(-1))))) {
				
				ActionError actionError = new ActionError("error.required.paymentDate");
				actionErrors.add("UnNecessary1", actionError);
			}
	
			if ((situationOfGuide.equals(SituationOfGuide.PAYED_STRING)) && (paymentType.equals(PaymentType.DEFAULT_STRING))){
				ActionError actionError = new ActionError("error.required.paymentType");
				actionErrors.add("UnNecessary2", actionError);
			}
			if (actionErrors.size() != 0) {
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
		
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(paymentDateYear.intValue(), paymentDateMonth.intValue(), paymentDateDay.intValue());
			
			Object args[] = {guideNumber, guideYear, guideVersion, calendar.getTime(), remarks, situationOfGuide, paymentType};
			
			try {
				serviceManager.executar(userView, "ChangeGuideSituation", args);
			} catch (NonValidChangeServiceException e) {
				throw new NonValidChangeActionException(e);
			}

			session.removeAttribute(SessionConstants.GUIDE);
			session.removeAttribute(SessionConstants.MONTH_DAYS_KEY);
			session.removeAttribute(SessionConstants.MONTH_LIST_KEY);
			session.removeAttribute(SessionConstants.YEARS_KEY);
			session.removeAttribute(SessionConstants.PAYMENT_TYPE);
			session.removeAttribute(SessionConstants.GUIDE_SITUATION_LIST);
			
			
			return mapping.findForward("SituationChanged");			

		} else
			throw new Exception();   
	}
		
		  
}

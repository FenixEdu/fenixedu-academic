/*
 * Created on 30/Jun/2003
 *
 * 
 */
package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoShift;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *
 * 30/Jun/2003
 * fenix-branch
 * ServidorApresentacao.Action.sop
 * 
 */
public class AddShiftsToClassesDA extends FenixDispatchAction {

	public ActionForward showClasses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			
			IUserView userView =
				(IUserView) sessao.getAttribute(SessionConstants.U_VIEW);
			Integer keyExecutionPeriod = null;
			String keyExecutionPeriodString = (String) request.getAttribute("objectCode");
			if (keyExecutionPeriodString!=null){
				 keyExecutionPeriod = new Integer(keyExecutionPeriodString);	
			}else {
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) sessao.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
				 keyExecutionPeriod = infoExecutionPeriod.getIdInternal();
			}
			
			Object argsAdicionarAula[] = { keyExecutionPeriod};
			SiteView siteView = null;
			
				try {
					siteView =
						 (SiteView) ServiceUtils.executeService(
							userView,
							"ReadAllClasses",
							argsAdicionarAula);
				} catch (FenixServiceException e) {
					throw new FenixActionException(e);
				}
			
			request.setAttribute("siteView",siteView);
			return mapping.findForward("showClasses");
		} else
			throw new FenixActionException();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
	public ActionForward addShiftToClasses(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
			HttpSession sessao = request.getSession(false);
			if (sessao != null) {
				DynaActionForm classesForm = (DynaActionForm) form;	
				IUserView userView =
					(IUserView) sessao.getAttribute(SessionConstants.U_VIEW);
				
				String[] classesList =  (String[]) classesForm.get("classesList");				
				InfoShift infoShift = (InfoShift) sessao.getAttribute("infoTurno");
				Integer keyShift = infoShift.getIdInternal();
				Object argsAdicionarAula[] = {keyShift, classesList};
				
					try {
						 ServiceUtils.executeService(
								userView,
								"AddShiftToClasses",
								argsAdicionarAula);
					} catch (FenixServiceException e) {
						throw new FenixActionException(e);
					}
			
			
				return mapping.findForward("sucess");
			} else
				throw new FenixActionException();
			// nao ocorre... pedido passa pelo filtro Autorizacao
		}
}

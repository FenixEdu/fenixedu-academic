/*
 * VisualizeApplicationInfoAction.java
 *
 * 
 * Created on 07 de Dezembro de 2002, 11:16
 *
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorApresentacao.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoPerson;
import ServidorAplicacao.IUserView;

import ServidorApresentacao.Action.sop.utils.SessionConstants;

import framework.factory.ServiceManagerServiceFactory;

public class VisualizePersonalInfoAction
	extends ServidorApresentacao.Action.base.FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoPerson infoPerson = null;

		Object args[] = new Object[1];
		args[0] = userView;

		infoPerson =
			(InfoPerson) ServiceManagerServiceFactory.executeService(
				userView,
				"ReadPersonByUsername",
				args);

		request.removeAttribute("personalInfo");

		request.setAttribute("personalInfo", infoPerson);

		return mapping.findForward("Success");
	}

}

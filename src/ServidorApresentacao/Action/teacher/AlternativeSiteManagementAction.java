/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AlternativeSiteManagementAction extends FenixDispatchAction {

	public ActionForward management(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession();
		String alternativeSite =
			(String) session.getAttribute(SessionConstants.ALTERNATIVE_SITE);
		String mail = (String) session.getAttribute(SessionConstants.MAIL);
		DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;
		alternativeSiteForm.set("siteAddress", alternativeSite);
		alternativeSiteForm.set("mail", mail);		
		return mapping.findForward("editAlternativeSite");
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();

		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		InfoSite infoSiteNew = new InfoSite();

		infoSiteNew.setInfoExecutionCourse(infoSite.getInfoExecutionCourse());
		String alternativeSite =
			(String) alternativeSiteForm.get("siteAddress");
		String mail = (String) alternativeSiteForm.get("mail");
		infoSiteNew.setAlternativeSite(alternativeSite);
		infoSiteNew.setMail(mail);

		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { infoSite, infoSiteNew };
		GestorServicos gestor = GestorServicos.manager();
		try {
			System.out.println(
				"resultado servico: "
					+ gestor.executar(userView, "EditSite", args));
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		session.setAttribute(
			SessionConstants.ALTERNATIVE_SITE,
			alternativeSite);
		session.setAttribute(SessionConstants.MAIL, mail);
		session.setAttribute("alternativeSiteForm", alternativeSiteForm);

		return mapping.findForward("editAlternativeSite");
	}
}
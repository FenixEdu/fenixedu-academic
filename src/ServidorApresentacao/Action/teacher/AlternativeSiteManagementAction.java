/*
 * Created on 26/Mar/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoSite;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;
/**
 * @author PTRLV
 *
 */
public class AlternativeSiteManagementAction extends FenixDispatchAction {

	public ActionForward management(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		session.removeAttribute(SessionConstants.INFO_SECTION);
		InfoSite site = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		String alternativeSite =site.getAlternativeSite();
		String mail = site.getMail();
		String initialStatement = site.getMail();
		String introduction = site.getIntroduction();
		DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;
		alternativeSiteForm.set("siteAddress", alternativeSite);
		alternativeSiteForm.set("mail", mail);	
		alternativeSiteForm.set("initialStatement",initialStatement);
		alternativeSiteForm.set("introduction",introduction);	
		return mapping.findForward("editAlternativeSite");
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession(false);
		session.removeAttribute(SessionConstants.INFO_SECTION);
		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		InfoSite infoSiteNew = new InfoSite();

		infoSiteNew.setInfoExecutionCourse(infoSite.getInfoExecutionCourse());
		String alternativeSite =
			(String) alternativeSiteForm.get("siteAddress");
		String mail = (String) alternativeSiteForm.get("mail");
		String initialStatement = (String) alternativeSiteForm.get("initialStatement");
		String introduction = (String) alternativeSiteForm.get("introduction");
		infoSiteNew.setAlternativeSite(alternativeSite);
		infoSiteNew.setMail(mail);
		infoSiteNew.setInitialStatement(initialStatement);
		infoSiteNew.setIntroduction(introduction);

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Object args[] = { infoSite, infoSiteNew };
		try {
			ServiceManagerServiceFactory.executeService(userView, "EditSite", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		session.setAttribute(SessionConstants.INFO_SITE,infoSiteNew);
		session.setAttribute("alternativeSiteForm", alternativeSiteForm);

		return mapping.findForward("viewSite");
	}
}
/*
 * Created on 26/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author PTRLV
 *  
 */
public class AlternativeSiteManagementAction extends FenixDispatchAction {

    public ActionForward management(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        InfoSite site = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
        String alternativeSite = site.getAlternativeSite();
        String mail = site.getMail();
        String initialStatement = site.getMail();
        String introduction = site.getIntroduction();
        DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;
        alternativeSiteForm.set("siteAddress", alternativeSite);
        alternativeSiteForm.set("mail", mail);
        alternativeSiteForm.set("initialStatement", initialStatement);
        alternativeSiteForm.set("introduction", introduction);
        return mapping.findForward("editAlternativeSite");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        String alternativeSite = (String) alternativeSiteForm.get("siteAddress");
        String mail = (String) alternativeSiteForm.get("mail");
        String initialStatement = (String) alternativeSiteForm.get("initialStatement");
        String introduction = (String) alternativeSiteForm.get("introduction");

        IUserView userView = getUserView(request);
        Object args[] = { infoSite, alternativeSite, mail, initialStatement, introduction };
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditSite", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        session.setAttribute(SessionConstants.INFO_SITE, InfoSite.newInfoFromDomain(ExecutionCourseSite.readExecutionCourseSiteByOID(infoSite.getIdInternal())));
        session.setAttribute("alternativeSiteForm", alternativeSiteForm);

        return mapping.findForward("viewSite");
    }
}
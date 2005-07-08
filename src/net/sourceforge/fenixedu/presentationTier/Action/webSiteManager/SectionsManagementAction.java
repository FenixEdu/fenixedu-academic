package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * @author Fernanda Quitério 24/Set/2003
 *  
 */
public class SectionsManagementAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();

        InfoWebSite webSite = null;
        Integer objectCode = null;
        if (request.getParameter("objectCode") != null) {
            objectCode = Integer.valueOf(request.getParameter("objectCode"));
        }
        try {
            Object args[] = { objectCode };
            webSite = (InfoWebSite) ServiceUtils.executeService(userView, "ReadWebSiteByObjectCode",
                    args);
        } catch (NotAuthorizedException e) {
            errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
        } catch (NonExistingServiceException e) {
            errors.add("website", new ActionError("error.impossibleReadWebsite"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        request.setAttribute("infoWebSite", webSite);

        return mapping.findForward("sectionsMenu");
    }

    public ActionForward getSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();

        InfoWebSite infoWebSite = null;
        Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
        try {
            Object args[] = { objectCode };
            infoWebSite = (InfoWebSite) ServiceUtils.executeService(userView,
                    "ReadWebSiteBySectionCode", args);
        } catch (NonExistingServiceException e) {
            errors.add("website", new ActionError("error.impossibleReadWebsite"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        request.setAttribute("infoWebSite", infoWebSite);
        request.setAttribute("objectCode", request.getParameter("objectCode"));

        return mapping.findForward("sectionPage");
    }

    public ActionForward prepareSectionsConfiguration(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();

        InfoWebSite infoWebSite = null;
        Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
        try {
            Object args[] = { objectCode };
            infoWebSite = (InfoWebSite) ServiceUtils.executeService(userView, "ReadWebSiteByObjectCode",
                    args);
        } catch (NonExistingServiceException e) {
            errors.add("website", new ActionError("error.impossibleReadWebsite"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        request.setAttribute("infoWebSite", infoWebSite);
        request.setAttribute("objectCode", request.getParameter("objectCode"));

        return mapping.findForward("sectionsConfiguration");
    }

    public ActionForward configureSections(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();

        Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
        InfoWebSite infoWebSite = fillInfoWebSite(objectCode, form, request, errors);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        try {
            Object args[] = { infoWebSite };
            ServiceUtils.executeService(userView, "ConfigureWebSiteSections", args);
        } catch (NotAuthorizedException e) {
            errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
        } catch (NonExistingServiceException e) {

            if (e.getMessage().equals("website")) {
                errors.add("website", new ActionError("error.impossibleReadWebsite"));
            } else if (e.getMessage().equals("websiteSection")) {
                errors.add("websiteSection", new ActionError("error.impossibleReadSection"));
            }
        } catch (ExistingServiceException e) {
            errors.add("websiteSection", new ActionError("error.sectionAlreadyExists", e.getMessage()));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        // update all sections in server with new configuration
        try {
            Object args[] = { null, null };
            ServiceUtils.executeService(userView, "SendWebSiteSectionFileToServer", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return prepare(mapping, form, request, response);
    }

    /**
     * @param objectCode
     * @param form
     * @return
     */
    private InfoWebSite fillInfoWebSite(Integer objectCode, ActionForm form, HttpServletRequest request,
            ActionErrors errors) {
        InfoWebSite infoWebSite = new InfoWebSite();

        infoWebSite.setIdInternal(objectCode);

        Integer sectionsSize = Integer.valueOf(request.getParameter("sectionsSize"));

        List sections = new ArrayList(sectionsSize.intValue());

        for (int i = 0; i < sectionsSize.intValue(); i++) {
            InfoWebSiteSection infoWebSiteSection = new InfoWebSiteSection();

            if (request.getParameter("section[" + i + "].idInternal") == null
                    || request.getParameter("section[" + i + "].idInternal").length() == 0
                    || request.getParameter("section[" + i + "].name") == null
                    || request.getParameter("section[" + i + "].name").length() == 0
                    || request.getParameter("section[" + i + "].ftpName") == null
                    || request.getParameter("section[" + i + "].ftpName").length() == 0
                    || request.getParameter("section[" + i + "].whatToSort") == null
                    || request.getParameter("section[" + i + "].whatToSort").length() == 0
                    || request.getParameter("section[" + i + "].sortingOrder") == null
                    || request.getParameter("section[" + i + "].sortingOrder").length() == 0
                    || request.getParameter("section[" + i + "].size") == null
                    || request.getParameter("section[" + i + "].size").length() == 0
                    || request.getParameter("section[" + i + "].excerptSize") == null
                    || request.getParameter("section[" + i + "].excerptSize").length() == 0) {
                errors.add("allFields", new ActionError("error.webSiteSection.configuration.allFields"));
                return null;
            }

            MessageResources messages = getResources(request);
            try {
                Integer.valueOf(request.getParameter("section[" + i + "].size"));
            } catch (NumberFormatException e) {
                errors.add("integer", new ActionError("errors.integer", messages
                        .getMessage("label.posts.number")));
                return null;
            }
            try {
                Integer.valueOf(request.getParameter("section[" + i + "].excerptSize"));
            } catch (NumberFormatException e) {
                errors.add("integer", new ActionError("errors.integer", messages
                        .getMessage("label.excerpt.size")));
                return null;
            }
            infoWebSiteSection.setIdInternal(Integer.valueOf(request.getParameter("section[" + i
                    + "].idInternal")));
            infoWebSiteSection.setName(request.getParameter("section[" + i + "].name"));
            infoWebSiteSection.setFtpName(request.getParameter("section[" + i + "].ftpName"));
            infoWebSiteSection.setWhatToSort(request.getParameter("section[" + i + "].whatToSort"));
            infoWebSiteSection.setSortingOrder(request.getParameter("section[" + i + "].sortingOrder"));
            infoWebSiteSection.setSize(Integer.valueOf(request.getParameter("section[" + i + "].size")));
            infoWebSiteSection.setExcerptSize(Integer.valueOf(request.getParameter("section[" + i
                    + "].excerptSize")));

            sections.add(infoWebSiteSection);
        }
        infoWebSite.setSections(sections);

        return infoWebSite;
    }

}
/*
 * Created on 17/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author PTRLV
 *  
 */
public class BibliographicReferenceManagerDispatchAction extends FenixDispatchAction {

    public ActionForward createBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        DynaActionForm insertBibliographicReferenceForm = (DynaActionForm) form;

        String title = (String) insertBibliographicReferenceForm.get("title");
        String authors = (String) insertBibliographicReferenceForm.get("authors");
        String reference = (String) insertBibliographicReferenceForm.get("reference");
        String year = (String) insertBibliographicReferenceForm.get("year");
        String optionalStr = (String) insertBibliographicReferenceForm.get("optional");
        Boolean optional;

        if (optionalStr.equals(this.getResources(request).getMessage("message.optional"))) {
            optional = new Boolean(true);
        } else {
            optional = new Boolean(false);
        }

        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        InfoExecutionCourse infoExecutionCourse = infoSite.getInfoExecutionCourse();

        IUserView userView = getUserView(request);
        Object args[] = { infoExecutionCourse, title, authors, reference, year, optional };
        try {
            ServiceManagerServiceFactory.executeService(userView, "InsertBibliographicReference", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Object args1[] = { infoExecutionCourse, null };
        List references = null;
        try {
            references = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadBibliographicReference", args1);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        session.setAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST, references);
        return mapping.findForward("bibliographyManagement");
    }

    public ActionForward editBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        DynaActionForm editBibliographicReferenceForm = (DynaActionForm) form;

        String title = (String) editBibliographicReferenceForm.get("title");
        String authors = (String) editBibliographicReferenceForm.get("authors");
        String reference = (String) editBibliographicReferenceForm.get("reference");
        String year = (String) editBibliographicReferenceForm.get("year");
        String optionalStr = (String) editBibliographicReferenceForm.get("optional");

        Boolean optional;

        if (optionalStr.equals("opcional")) {
            optional = new Boolean(true);
        } else {
            optional = new Boolean(false);
        }

        InfoBibliographicReference infoBibliographicReferenceNew = new InfoBibliographicReference(title,
                authors, reference, year, optional);

        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        InfoExecutionCourse infoExecutionCourse = infoSite.getInfoExecutionCourse();

        InfoBibliographicReference infoBibliographicReference = (InfoBibliographicReference) session
                .getAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE);

        Object args[] = { infoExecutionCourse, infoBibliographicReference, infoBibliographicReferenceNew };

        IUserView userView = getUserView(request);
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditBibliographicReference", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Object args1[] = { infoExecutionCourse, null };
        List references = null;
        try {
            references = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadBibliographicReference", args1);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        session.setAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST, references);

        return mapping.findForward("bibliographyManagement");
    }

    public ActionForward deleteBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        InfoExecutionCourse infoExecutionCourse = infoSite.getInfoExecutionCourse();

        List bibliographicReferences = (ArrayList) session
                .getAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST);

        String infoBiblioRefIndex = request.getParameter("index");

        Integer index = new Integer(infoBiblioRefIndex);

        InfoBibliographicReference infoBibliographicReference = (InfoBibliographicReference) bibliographicReferences
                .get(index.intValue());

        IUserView userView = getUserView(request);
        Object args[] = { infoExecutionCourse, infoBibliographicReference };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteBibliographicReference", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Object args1[] = { infoExecutionCourse, null };
        List references = null;
        try {
            references = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadBibliographicReference", args1);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        session.setAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST, references);
        return mapping.findForward("bibliographyManagement");
    }

    public ActionForward viewBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        InfoExecutionCourse infoExecutionCourse = infoSite.getInfoExecutionCourse();

        IUserView userView = getUserView(request);
        Object args[] = { infoExecutionCourse, null };
        List references = null;
        try {
            references = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadBibliographicReference", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        session.setAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST, references);

        if (references.size() == 0) {
            session.removeAttribute("bibliographicReferenceForm");
            return mapping.findForward("editBibliographicReference");
        }

        return mapping.findForward("bibliographyManagement");
    }

    public ActionForward prepareEditBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        DynaValidatorForm referenceForm = (DynaValidatorForm) form;

        String index = request.getParameter("index");

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        List referenceList = null;
        if (index != null) {
            session.setAttribute("edit", "Editar");
            Integer indexInt = new Integer(index);
            referenceList = (ArrayList) session
                    .getAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST);

            InfoBibliographicReference infoBibRef = (InfoBibliographicReference) referenceList
                    .get(indexInt.intValue());

            session.setAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE, infoBibRef);

            referenceForm.set("title", infoBibRef.getTitle());
            referenceForm.set("authors", infoBibRef.getAuthors());
            referenceForm.set("reference", infoBibRef.getReference());
            referenceForm.set("year", infoBibRef.getYear());

            if (infoBibRef.getOptional().equals(new Boolean(true)))
                referenceForm.set("optional", this.getResources(request).getMessage("message.optional"));
            else
                referenceForm.set("optional", this.getResources(request).getMessage(
                        "message.recommended"));
        } else {
            session.removeAttribute("edit");
            session.removeAttribute("bibliographicReferenceForm");
        }

        return mapping.findForward("editBibliographicReference");
    }

    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.FenixLookupDispatchAction#getKeyMethodMap()
     */
    /*
     * protected Map getKeyMethodMap() { Map map = new HashMap();
     * map.put("button.insert", "prepareEditBibliographicReference");
     * map.put("button.confirmInsert", "createBibliographicReference");
     * map.put("button.delete", "deleteBibliographicReference");
     * map.put("button.edit", "prepareEditBibliographicReference");
     * map.put("button.confirmEdit", "editBibliographicReference");
     * map.put("button.view", "viewBibliographicReference"); return map; }
     */
}
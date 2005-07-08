/*
 * Created on 18/Nov/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.publication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSiteAttributes;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author TJBF & PFON
 */
public class ReadPublicationAttributesAction extends FenixAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        Integer publicationTypeId = (Integer) dynaForm.get("infoPublicationTypeId");

        String typePublication = (String) dynaForm.get("typePublication");

        Integer idTeacher = (Integer) dynaForm.get("teacherId");

        String[] list = (String[]) dynaForm.get("authorsIds");
        List newList = Arrays.asList(list);
        List authorsIds = new ArrayList();
        authorsIds.addAll(newList);

        IUserView userView = SessionUtils.getUserView(request);

        if (session != null) {

            Object[] args = { userView.getUtilizador(), publicationTypeId };
            List requiredAttributes = (List) ServiceUtils.executeService(userView,
                    "ReadRequiredAttributes", args);

            List nonRequiredAttributes = (List) ServiceUtils.executeService(userView,
                    "ReadNonRequiredAttributes", args);

            List subTypeList = (List) ServiceUtils.executeService(userView, "ReadPublicationSubtypes",
                    args);

            List formatList = (List) ServiceUtils.executeService(userView, "ReadPublicationFormats",
                    args);

            List monthList = (List) ServiceUtils.executeService(userView, "ReadPublicationMonths", args);

            List scopeList = (List) ServiceUtils.executeService(userView, "ReadPublicationScopes", args);

            List infoAuthors = readInfoAuthors(authorsIds, userView);

            InfoSiteAttributes bodyComponent = new InfoSiteAttributes();
            bodyComponent.setInfoRequiredAttributes(requiredAttributes);
            bodyComponent.setInfoNonRequiredAttributes(nonRequiredAttributes);

            SiteView siteView = new SiteView(bodyComponent);

            Object argPubType[] = { userView.getUtilizador() };
            List infoPublicationTypes = (List) ServiceUtils.executeService(userView,
                    "ReadPublicationTypes", argPubType);
            request.setAttribute("publicationTypesList", infoPublicationTypes);

            request.setAttribute("infoAuthorsList", infoAuthors);
            request.setAttribute("siteView", siteView);
            request.setAttribute("subTypeList", subTypeList);
            request.setAttribute("formatList", formatList);
            request.setAttribute("monthList", monthList);
            request.setAttribute("scopeList", scopeList);
            dynaForm.set("infoPublicationTypeId", publicationTypeId);
            dynaForm.set("typePublication", typePublication);
            dynaForm.set("teacherId", idTeacher);

        }
        ActionForward actionForward = null;

        if (typePublication.equalsIgnoreCase(PublicationConstants.DIDATIC_STRING)) {
            actionForward = mapping.findForward("show-publicationDidatic-attributes-form");
        } else {
            actionForward = mapping.findForward("show-publicationCientific-attributes-form");
        }
        return actionForward;
    }

    public List readInfoAuthors(List authorsIds, IUserView userView) throws FenixFilterException, FenixServiceException {

        List newAuthorsIds = new ArrayList();
        Iterator iteratorIds = authorsIds.iterator();

        while (iteratorIds.hasNext()) {
            String idString = (String) iteratorIds.next();
            newAuthorsIds.add(new Integer(idString));
        }

        Object[] args = { newAuthorsIds };
        List authors = (List) ServiceUtils.executeService(userView, "ReadAuthorsToInsert", args);

        List infoAuthors = (List) CollectionUtils.collect(authors, new Transformer() {
            public Object transform(Object o) {
                IAuthor author = (IAuthor) o;
                return Cloner.copyIAuthor2InfoAuthor(author);
            }
        });
        return infoAuthors;
    }
}
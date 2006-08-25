package net.sourceforge.fenixedu.presentationTier.Action.publication;

import java.util.ArrayList;
import java.util.Arrays;
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
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ReadPublicationAttributesAction extends FenixAction {

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

            Object[] args = { publicationTypeId };
            
            List requiredAttributes = (List) ServiceUtils.executeService(userView, "ReadRequiredAttributes", args);
            List nonRequiredAttributes = (List) ServiceUtils.executeService(userView, "ReadNonRequiredAttributes", args);
            List subTypeList = (List) ServiceUtils.executeService(userView, "ReadPublicationSubtypesByPublicationType", args);
            List formatList = (List) ServiceUtils.executeService(userView, "ReadAllPublicationFormats", args);
            List monthList = (List) ServiceUtils.executeService(userView, "ReadPublicationMonths", args);
            List scopeList = (List) ServiceUtils.executeService(userView, "ReadPublicationScopes", args);

            List infoAuthors = readInfoAuthors(authorsIds, userView);

            InfoSiteAttributes bodyComponent = new InfoSiteAttributes();
            bodyComponent.setInfoRequiredAttributes(requiredAttributes);
            bodyComponent.setInfoNonRequiredAttributes(nonRequiredAttributes);

            SiteView siteView = new SiteView(bodyComponent);

            Object argPubType[] = { userView.getUtilizador() };
            List infoPublicationTypes = (List) ServiceUtils.executeService(userView,
                    "ReadAllPublicationTypes", argPubType);
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

    public List readInfoAuthors(List<String> authorsIds, IUserView userView) throws FenixFilterException, FenixServiceException {
	final List<Integer> newAuthorsIds = new ArrayList<Integer>(authorsIds.size());
	for (final String idString : authorsIds) {
	    newAuthorsIds.add(Integer.valueOf(idString));
	}
        return (List) ServiceUtils.executeService(userView, "ReadPersonsByIDs", new Object[] { newAuthorsIds });
    }
}
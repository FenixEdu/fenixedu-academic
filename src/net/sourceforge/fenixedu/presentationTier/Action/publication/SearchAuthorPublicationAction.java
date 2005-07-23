/*
 * Created on May 31, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.presentationTier.Action.publication;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
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
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class SearchAuthorPublicationAction extends FenixDispatchAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        IUserView userView = SessionUtils.getUserView(request);

        Integer publicationTypeId = new Integer(request.getParameter("infoPublicationTypeId"));

        String typePublication = (String) dynaForm.get("typePublication");

        Integer idTeacher = (Integer) dynaForm.get("teacherId");

        String[] list = (String[]) dynaForm.get("authorsIds");
        List newList = Arrays.asList(list);
        List authorsIds = new ArrayList();
        authorsIds.addAll(newList);

        ActionForward actionForward = null;

        Object args[] = { publicationTypeId };
        IPublicationType publicationType = (IPublicationType) ServiceUtils.executeService(userView,
                "ReadPublicationType", args);

        if (session != null) {

            List infoAuthors = readInfoAuthors(authorsIds, userView);

            request.setAttribute("infoAuthorsList", infoAuthors);
            dynaForm.set("infoPublicationTypeId", publicationTypeId);
            dynaForm.set("typePublication", typePublication);
            dynaForm.set("teacherId", idTeacher);

            if (publicationType.getPublicationType().equalsIgnoreCase("Unstructured"))
                actionForward = mapping.findForward("show-attributes");
            else
                actionForward = mapping.findForward("show-search-author-form");
        }

        return actionForward;
    }


    private List readInfoAuthors(List authorsIds, IUserView userView) throws FenixServiceException, FenixFilterException {

        List newAuthorsIds = new ArrayList();
        Iterator iteratorIds = authorsIds.iterator();

        while (iteratorIds.hasNext()) {
            String idString = (String) iteratorIds.next();
            newAuthorsIds.add(new Integer(idString));
        }

        Object[] args = { newAuthorsIds };
        List authors = (List) ServiceUtils.executeService(userView, "ReadPersonsByIDs", args);

        List infoAuthors = (List) CollectionUtils.collect(authors, new Transformer() {
            public Object transform(Object o) {
                IPerson author = (IPerson) o;
                InfoPerson infoPerson = new InfoPerson();
                infoPerson.copyFromDomain(author);
                return infoPerson;
            }
        });
        return infoAuthors;
    }

}
/*
 * Created on 21-Oct-2004
 *
 * @author Carlos Pereira & Francisco Passos
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.publication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.research.result.Publication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Carlos Pereira & Francisco Passos
 * 
 */
public class DeletePublicationAction extends FenixDispatchAction {

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
	final Publication publication = getPublication(request);
	if (publication != null) {
	    Object[] args = { publication.getIdInternal() };
	    executeService(request, "DeletePublication", args);
	    request.setAttribute("msg", "message.publications.managementDeleted");
	}
	return mapping.findForward("deleted");
    }

    protected Publication getPublication(final HttpServletRequest request) {
	final Integer publicationID = Integer.valueOf(request.getParameter("idInternal"));
	return (Publication) rootDomainObject.readResultByOID(publicationID);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward("cancelled");
    }

}
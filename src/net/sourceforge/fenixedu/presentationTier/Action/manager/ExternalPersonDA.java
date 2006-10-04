package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person.AnyPersonSearchBean;
import net.sourceforge.fenixedu.domain.Person.ExternalPersonBeanFactoryCreator;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExternalPersonDA extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	AnyPersonSearchBean anyPersonSearchBean = (AnyPersonSearchBean) getRenderedObject();
	if (anyPersonSearchBean == null) {
	    anyPersonSearchBean = new AnyPersonSearchBean();
	}
        final String name = request.getParameter("name");
        if (isSpecified(name)) {
            anyPersonSearchBean.setName(name);
        }
	request.setAttribute("anyPersonSearchBean", anyPersonSearchBean);
	
    	return mapping.findForward("showSearch");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	ExternalPersonBeanFactoryCreator externalPersonBean = (ExternalPersonBeanFactoryCreator) getRenderedObject();
	if (externalPersonBean == null) {
	    externalPersonBean = new ExternalPersonBeanFactoryCreator();
	}
	setRequestParameters(request, externalPersonBean);
	request.setAttribute("externalPersonBean", externalPersonBean);
	
    	return mapping.findForward("showCreateForm");
    }

    private void setRequestParameters(final HttpServletRequest request, final ExternalPersonBeanFactoryCreator externalPersonBean) {
	final String name = request.getParameter("name");
	if (isSpecified(name)) {
	    externalPersonBean.setName(name);
	}
	final String idDocumentType = request.getParameter("idDocumentType");
	if (isSpecified(idDocumentType)) {
	    externalPersonBean.setIdDocumentType(IDDocumentType.valueOf(idDocumentType));
	}
	final String documentIdNumber = request.getParameter("documentIdNumber");
	if (isSpecified(documentIdNumber)) {
	    externalPersonBean.setDocumentIdNumber(documentIdNumber);
	}
    }

    private boolean isSpecified(final String string) {
	return string != null && string.length() > 0;
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
	final Person person = (Person) executeFactoryMethod(request);
	request.setAttribute("person", person);
	RenderUtils.invalidateViewState();
    	return mapping.findForward("showCreatedPerson");
    }

    public ActionForward createExternalPersonAndParkingParty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        create(mapping, form, request, response);
        return createParkingParty(mapping, form, request, response);
    }


    public ActionForward createParkingParty(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String personIDString = request.getParameter("personID");
        final Integer personID = personIDString == null ? null : Integer.valueOf(personIDString);
        final Person person;
        if (personID != null) {
            person = (Person) rootDomainObject.readPartyByOID(personID);
        } else {
            person = (Person) request.getAttribute("person");
        }
        if (person != null) {
            final Object[] args = { person };
            executeService(request, "CreateParkingParty", args);
            final ActionForward actionForward = new ActionForward("/parking.do?plateNumber=&partyID=" + person.getIdInternal() + "&method=showParkingPartyRequests");
            return actionForward;
        } else {
            throw new Error("error.no.person.specified");
        }
    }

}

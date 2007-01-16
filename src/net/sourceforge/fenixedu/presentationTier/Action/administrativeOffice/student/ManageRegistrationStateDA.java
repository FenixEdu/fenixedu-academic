/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationStateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateDeleter;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ManageRegistrationStateDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final Registration registration = getAndTransportRegistration(request);
	request.setAttribute("registrationStateBean", new RegistrationStateCreator(registration));
	return mapping.findForward("showRegistrationStates");
    }

    public ActionForward createNewState(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	try {
	    executeFactoryMethod();
	    addActionMessage(request, "message.success.state.edit");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}

	final Registration registration = ((RegistrationStateBean) getRenderedObject())
		.getRegistration();
	request.setAttribute("registration", registration);
	request.setAttribute("registrationStateBean", new RegistrationStateCreator(registration));
	return mapping.findForward("showRegistrationStates");
    }

    public ActionForward deleteState(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	try {
	    executeFactoryMethod(new RegistrationStateDeleter(Integer.valueOf(request
		    .getParameter("registrationStateId"))));
	    addActionMessage(request, "message.success.state.delete");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}

	return prepare(mapping, actionForm, request, response);
    }

    private Registration getAndTransportRegistration(final HttpServletRequest request) {
	final Registration registration = rootDomainObject.readRegistrationByOID(getIntegerFromRequest(
		request, "registrationId"));
	request.setAttribute("registration", registration);
	return registration;
    }

}

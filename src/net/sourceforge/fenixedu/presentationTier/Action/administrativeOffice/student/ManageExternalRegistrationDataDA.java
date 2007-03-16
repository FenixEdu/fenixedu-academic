/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.ExternalRegistrationDataFactoryExecutor.ExternalRegistrationDataEditor;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ManageExternalRegistrationDataDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final ExternalRegistrationDataEditor bean;
	if (getRenderedObject() != null) {
	    bean = (ExternalRegistrationDataEditor) getRenderedObject();
	    request.setAttribute("registration", bean.getExternalRegistrationData().getRegistration());
	} else {
	    bean = new ExternalRegistrationDataEditor(getAndTransportRegistration(request)
		    .getExternalRegistrationData());
	}
	request.setAttribute("externalRegistrationDataBean", bean);

	return mapping.findForward("manageExternalRegistrationData");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	executeFactoryMethod();
	return prepare(mapping, actionForm, request, response);
    }

    private Registration getAndTransportRegistration(final HttpServletRequest request) {
	final Registration registration = rootDomainObject.readRegistrationByOID(getIntegerFromRequest(
		request, "registrationId"));
	request.setAttribute("registration", registration);
	return registration;
    }

}

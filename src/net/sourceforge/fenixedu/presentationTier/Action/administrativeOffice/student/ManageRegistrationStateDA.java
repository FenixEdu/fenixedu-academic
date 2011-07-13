package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.DeleteRegistrationActualInfoFactoryExecutor;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationStateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateDeleter;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/manageRegistrationState", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "showRegistrationStates", path = "/academicAdminOffice/student/registration/manageRegistrationState.jsp"),
	@Forward(name = "deleteActualInfoConfirm", path = "/academicAdminOffice/student/registration/deleteRegistrationActualInfo.jsp") })
public class ManageRegistrationStateDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Registration registration = getAndTransportRegistration(request);
	request.setAttribute("registrationStateBean", new RegistrationStateCreator(registration));
	return mapping.findForward("showRegistrationStates");
    }

    public ActionForward createNewState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    final RegistrationStateCreator creator = (RegistrationStateCreator) getFactoryObject();
	    creator.setResponsible(AccessControl.getPerson());
	    executeFactoryMethod(creator);

	    addActionMessage(request, "message.success.state.edit");
	} catch (DomainExceptionWithLabelFormatter e) {
	    addActionMessage(request, e.getKey(), solveLabelFormatterArgs(request, e.getLabelFormatterArgs()));
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}

	final Registration registration = ((RegistrationStateBean) getRenderedObject()).getRegistration();
	request.setAttribute("registration", registration);
	request.setAttribute("registrationStateBean", new RegistrationStateCreator(registration));
	return mapping.findForward("showRegistrationStates");
    }

    public ActionForward deleteState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeFactoryMethod(new RegistrationStateDeleter(Integer.valueOf(request.getParameter("registrationStateId"))));
	    addActionMessage(request, "message.success.state.delete");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}

	return prepare(mapping, actionForm, request, response);
    }

    public ActionForward deleteActualInfoConfirm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	getAndTransportRegistration(request);
	request.setAttribute("executionYear", ExecutionYear.readCurrentExecutionYear().getYear());
	return mapping.findForward("deleteActualInfoConfirm");
    }

    public ActionForward deleteActualInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	Registration registration = getAndTransportRegistration(request);

	try {
	    executeFactoryMethod(new DeleteRegistrationActualInfoFactoryExecutor(registration));
	    addActionMessage(request, "message.success.deleteActualInfo");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}

	return prepare(mapping, actionForm, request, response);
    }

    private Registration getAndTransportRegistration(final HttpServletRequest request) {
	final Registration registration = rootDomainObject
		.readRegistrationByOID(getIntegerFromRequest(request, "registrationId"));
	request.setAttribute("registration", registration);
	return registration;
    }
}

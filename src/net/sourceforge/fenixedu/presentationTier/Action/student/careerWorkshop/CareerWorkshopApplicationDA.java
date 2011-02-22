package net.sourceforge.fenixedu.presentationTier.Action.student.careerWorkshop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplication;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmation;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmationEvent;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopSessions;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopThemes;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

@Mapping(path="/careerWorkshopApplication", module="student")
@Forwards({@Forward(name="careerWorkshop", path="/student/careerWorkshop/careerWorkshop.jsp"), 
    @Forward(name="careerWorkshopApplicationForm", path="/student/careerWorkshop/careerWorkshopApplicationForm.jsp"),
    @Forward(name="careerWorkshopConfirmationForm", path="/student/careerWorkshop/careerWorkshopConfirmationForm.jsp")})
public class CareerWorkshopApplicationDA extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	final Student student = getLoggedStudent(request);
	CareerWorkshopApplicationEvent openApplicationEvent = CareerWorkshopApplicationEvent.getActualEvent();
	List<CareerWorkshopApplicationEvent> openApplicationEvents = new ArrayList<CareerWorkshopApplicationEvent>();
	if(openApplicationEvent != null) {
	    openApplicationEvents.add(openApplicationEvent);
	}
	List<CareerWorkshopConfirmationEvent> pendingForConfirmation = student.getApplicationsWaitingForConfirmation();
	request.setAttribute("openApplicationEvents", openApplicationEvents);
	request.setAttribute("pendingForConfirmation", pendingForConfirmation);
	return actionMapping.findForward("careerWorkshop");
    }
    
    public ActionForward presentApplication(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	final Student student = getLoggedStudent(request);
	final String eventExternalId = request.getParameter("eventId");
	CareerWorkshopApplicationEvent event = AbstractDomainObject.fromExternalId(eventExternalId);
	CareerWorkshopApplication application = retrieveThisWorkshopApplication(student, event);
	request.setAttribute("application", application);
	return actionMapping.findForward("careerWorkshopApplicationForm");
    }

    public ActionForward submitApplication(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	boolean comingFromSubmission = true;
	CareerWorkshopApplication application = getDomainObject(request, "applicationExternalId");
	String[] sessionPreferences = new String[CareerWorkshopSessions.values().length];
	for(CareerWorkshopSessions careerWorkshopSessions : CareerWorkshopSessions.values()) {
	    sessionPreferences[careerWorkshopSessions.ordinal()] = (String)getFromRequest(request, careerWorkshopSessions.name());
	}
	application.setSessionPreferences(sessionPreferences);
	
	String[] themePreferences = new String[CareerWorkshopThemes.values().length];
	for(CareerWorkshopThemes careerWorkshopThemes : CareerWorkshopThemes.values()) {
	    themePreferences[careerWorkshopThemes.ordinal()] = (String)getFromRequest(request, careerWorkshopThemes.name());
	}
	application.setThemePreferences(themePreferences);
	
	application.sealApplication();
	
	request.setAttribute("comingFromSubmission", comingFromSubmission);

	request.setAttribute("application", application);
	return actionMapping.findForward("careerWorkshopApplicationForm");
    }
    
    public ActionForward presentConfirmation(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	final Student student = getLoggedStudent(request);
	final String eventExternalId = request.getParameter("eventId");
	CareerWorkshopConfirmationEvent event = AbstractDomainObject.fromExternalId(eventExternalId);
	CareerWorkshopApplication application = retrieveThisWorkshopApplication(student, event.getCareerWorkshopApplicationEvent());
	CareerWorkshopConfirmation confirmation = retrieveThisWorskhopApplicationConfirmation(student, event, application);
	
	request.setAttribute("confirmationForm", confirmation);
	request.setAttribute("confirmationBean", new CareerWorkshopConfirmationBean(confirmation.getExternalId()));
	return actionMapping.findForward("careerWorkshopConfirmationForm");
    
    }
    
    public ActionForward acceptConfirmation(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	CareerWorkshopConfirmationBean bean = getRenderedObject("confirmationBean");
	CareerWorkshopConfirmation confirmation = AbstractDomainObject.fromExternalId(bean.getExternalId());
	confirmation.setConfirmationCode(bean.getConfirmationCode());
	confirmation.setConfirmation(true);
	confirmation.sealConfirmation();
	
	return prepare(actionMapping, actionForm, request, response);
    
    }
    
    public ActionForward declineConfirmation(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	
	return prepare(actionMapping, actionForm, request, response);
    
    }
    
    private Student getLoggedStudent(final HttpServletRequest request) {
	return getLoggedPerson(request).getStudent();
    }
    
    @Service
    private CareerWorkshopApplication retrieveThisWorkshopApplication(Student student, CareerWorkshopApplicationEvent event) {
	for(CareerWorkshopApplication application : student.getCareerWorkshopApplications()) {
	    if(application.getCareerWorkshopApplicationEvent() == event)
		return application;
	}
	return new CareerWorkshopApplication(student, event);
    }
    
    @Service
    private CareerWorkshopConfirmation retrieveThisWorskhopApplicationConfirmation(Student student, CareerWorkshopConfirmationEvent confirmationEvent, CareerWorkshopApplication application) {
	for(CareerWorkshopConfirmation confirmation : student.getCareerWorkshopConfirmations()) {
	    if(confirmation.getCareerWorkshopConfirmationEvent() == confirmationEvent)
		return confirmation;
	}
	return new CareerWorkshopConfirmation(student, confirmationEvent, application);
    }

}

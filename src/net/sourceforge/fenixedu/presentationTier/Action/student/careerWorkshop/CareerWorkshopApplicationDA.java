package net.sourceforge.fenixedu.presentationTier.Action.student.careerWorkshop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplication;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopSessions;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopThemes;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

@Mapping(path="/careerWorkshopApplication", module="student")
@Forwards({@Forward(name="careerWorkshop", path="/student/careerWorkshop/careerWorkshop.jsp")})
public class CareerWorkshopApplicationDA extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	final Student student = getLoggedStudent(request);
	CareerWorkshopApplicationEvent event = CareerWorkshopApplicationEvent.getActualEvent();
	CareerWorkshopApplication application = retrieveThisWorkshopApplication(student, event);
	return prepare(actionMapping, request, application);
    }

    public ActionForward prepare(final ActionMapping actionMapping, final HttpServletRequest request, final CareerWorkshopApplication application) throws Exception {
	request.setAttribute("application", application);
	return actionMapping.findForward("careerWorkshop");
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

	return prepare(actionMapping, request, application);
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

}

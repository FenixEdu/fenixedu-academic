package net.sourceforge.fenixedu.presentationTier.Action.student.careerWorkshop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplication;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmation;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmationEvent;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopSessions;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopThemes;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/careerWorkshopApplication", module = "student")
@Forwards({
        @Forward(name = "careerWorkshop", path = "/student/careerWorkshop/careerWorkshop.jsp", tileProperties = @Tile(
                title = "private.student.participate.istcareerworkshops")),
        @Forward(name = "careerWorkshopApplicationForm", path = "/student/careerWorkshop/careerWorkshopApplicationForm.jsp",
                tileProperties = @Tile(title = "private.student.participate.istcareerworkshops")),
        @Forward(name = "careerWorkshopConfirmationForm", path = "/student/careerWorkshop/careerWorkshopConfirmationForm.jsp",
                tileProperties = @Tile(title = "private.student.participate.istcareerworkshops")) })
public class CareerWorkshopApplicationDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Student student = getLoggedStudent(request);
        CareerWorkshopApplicationEvent openApplicationEvent = CareerWorkshopApplicationEvent.getActualEvent();
        List<CareerWorkshopApplicationEvent> openApplicationEvents = new ArrayList<CareerWorkshopApplicationEvent>();
        if (openApplicationEvent != null) {
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
        CareerWorkshopApplicationEvent event = FenixFramework.getDomainObject(eventExternalId);
        CareerWorkshopApplication application = retrieveThisWorkshopApplication(student, event);
        request.setAttribute("application", application);
        return actionMapping.findForward("careerWorkshopApplicationForm");
    }

    public ActionForward submitApplication(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CareerWorkshopApplication application = getDomainObject(request, "applicationExternalId");

        if (isApplicationPeriodExpired(application)) {
            addActionMessage("error", request, "error.careerWorkshops.applicationPeriodExpired");
            return prepare(actionMapping, actionForm, request, response);
        }

        boolean comingFromSubmission = true;
        String[] sessionPreferences = new String[CareerWorkshopSessions.values().length];
        for (CareerWorkshopSessions careerWorkshopSessions : CareerWorkshopSessions.values()) {
            sessionPreferences[careerWorkshopSessions.ordinal()] =
                    (String) getFromRequest(request, careerWorkshopSessions.name());
        }
        application.setSessionPreferences(sessionPreferences);

        String[] themePreferences = new String[CareerWorkshopThemes.values().length];
        for (CareerWorkshopThemes careerWorkshopThemes : CareerWorkshopThemes.values()) {
            themePreferences[careerWorkshopThemes.ordinal()] = (String) getFromRequest(request, careerWorkshopThemes.name());
        }
        application.setThemePreferences(themePreferences);

        application.sealApplication();

        request.setAttribute("comingFromSubmission", comingFromSubmission);

        request.setAttribute("application", application);
        return actionMapping.findForward("careerWorkshopApplicationForm");
    }

    private boolean isApplicationPeriodExpired(CareerWorkshopApplication application) {
        DateTime thisInstant = new DateTime();
        return thisInstant.isAfter(application.getCareerWorkshopApplicationEvent().getEndDate());
    }

    public ActionForward presentConfirmation(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Student student = getLoggedStudent(request);
        final String eventExternalId = request.getParameter("eventId");
        CareerWorkshopConfirmationEvent event = FenixFramework.getDomainObject(eventExternalId);
        CareerWorkshopApplication application =
                retrieveThisWorkshopApplication(student, event.getCareerWorkshopApplicationEvent());
        CareerWorkshopConfirmation confirmation = retrieveThisWorskhopApplicationConfirmation(student, event, application);

        request.setAttribute("confirmationForm", confirmation);
        request.setAttribute("confirmationBean", new CareerWorkshopConfirmationBean(confirmation.getExternalId()));
        return actionMapping.findForward("careerWorkshopConfirmationForm");

    }

    public ActionForward acceptConfirmation(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CareerWorkshopConfirmationBean bean = getRenderedObject("confirmationBean");
        CareerWorkshopConfirmation confirmation = FenixFramework.getDomainObject(bean.getExternalId());
        if (isConfirmationPeriodExpired(confirmation)) {
            addActionMessage("error", request, "error.careerWorkshops.confirmationPeriodExpired");
            return prepare(actionMapping, actionForm, request, response);
        }
        confirmation.setConfirmationCode(bean.getConfirmationCode());
        confirmation.setConfirmation(true);
        confirmation.sealConfirmation();

        addActionMessage("success", request, "message.careerWorkshops.confirmationSuccessful");

        return prepare(actionMapping, actionForm, request, response);

    }

    private boolean isConfirmationPeriodExpired(CareerWorkshopConfirmation confirmation) {
        DateTime thisInstant = new DateTime();
        return thisInstant.isAfter(confirmation.getCareerWorkshopConfirmationEvent().getEndDate());
    }

    public ActionForward declineConfirmation(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return prepare(actionMapping, actionForm, request, response);

    }

    private Student getLoggedStudent(final HttpServletRequest request) {
        return getLoggedPerson(request).getStudent();
    }

    @Atomic
    private CareerWorkshopApplication retrieveThisWorkshopApplication(Student student, CareerWorkshopApplicationEvent event) {
        for (CareerWorkshopApplication application : student.getCareerWorkshopApplications()) {
            if (application.getCareerWorkshopApplicationEvent() == event) {
                return application;
            }
        }
        return new CareerWorkshopApplication(student, event);
    }

    @Atomic
    private CareerWorkshopConfirmation retrieveThisWorskhopApplicationConfirmation(Student student,
            CareerWorkshopConfirmationEvent confirmationEvent, CareerWorkshopApplication application) {
        for (CareerWorkshopConfirmation confirmation : student.getCareerWorkshopConfirmations()) {
            if (confirmation.getCareerWorkshopConfirmationEvent() == confirmationEvent) {
                return confirmation;
            }
        }
        return new CareerWorkshopConfirmation(student, confirmationEvent, application);
    }

}

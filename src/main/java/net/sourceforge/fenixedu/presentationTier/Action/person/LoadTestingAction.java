package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FakeEnrollment;
import net.sourceforge.fenixedu.domain.FakeShift;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "person", path = "/loadTesting", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "loadTesting", path = "/person/loadTesting.jsp"),
        @Forward(name = "manageFakeEnrollments", path = "/person/manageFakeEnrollments.jsp"),
        @Forward(name = "viewAFewRandomFakeShifts", path = "/person/viewAFewRandomFakeShifts.jsp"),
        @Forward(name = "viewFakeShift", path = "/person/viewFakeShift.jsp"),
        @Forward(name = "manageFakeShifts", path = "/person/manageFakeShifts.jsp") })
public class LoadTestingAction extends FenixDispatchAction {

    public ActionForward loadTesting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ArrayList<Degree> allDegreesShuffled = new ArrayList<Degree>(Degree.readBolonhaDegrees());
        Collections.shuffle(allDegreesShuffled);

        Degree randomDegree = allDegreesShuffled.get(0);

        ExecutionSemester lastSemester = ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod();

        request.setAttribute("degreeID", randomDegree.getExternalId());
        request.setAttribute("degreeOID", randomDegree.getExternalId());
        request.setAttribute("degreeCurricularPlanID", randomDegree.getActiveDegreeCurricularPlans().get(0).getExternalId());
        request.setAttribute("executionPeriodOID", lastSemester.getExternalId());

        return mapping.findForward("loadTesting");
    }

    public ActionForward manageFakeEnrollments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("manageFakeEnrollments");
    }

    public ActionForward viewAFewRandomFakeShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("fakeShifts", FakeShift.readAFewRandomFakeShifts());
        return mapping.findForward("viewAFewRandomFakeShifts");
    }

    public ActionForward viewFakeShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("fakeShift", AbstractDomainObject.fromExternalId(request.getParameter("fakeShift")));
        return mapping.findForward("viewFakeShift");
    }

    public ActionForward manageFakeShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("fakeShifts", RootDomainObject.getInstance().getFakeShifts());
        return mapping.findForward("manageFakeShifts");
    }

    @Service
    public ActionForward createFakeEnrollment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Person person = AccessControl.getPerson();
        person.addFakeEnrollment(new FakeEnrollment(person, person.getName()));
        return mapping.findForward("manageFakeEnrollments");
    }

    @Service
    public ActionForward resetFakeEnrollments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Person person = AccessControl.getPerson();
        for (FakeEnrollment fakeEnrollment : person.getFakeEnrollment()) {
            fakeEnrollment.delete();
        }
        return mapping.findForward("manageFakeEnrollments");
    }

    @Service
    public ActionForward createFakeShiftEnrollment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        FakeShift fakeShift = AbstractDomainObject.fromExternalId(request.getParameter("fakeShift"));
        try {
            fakeShift.enroll();
        } catch (DomainException ex) {
            addActionMessage(request, ex.getMessage());
        }

        request.setAttribute("fakeShift", fakeShift);
        return mapping.findForward("viewFakeShift");
    }

    @Service
    public ActionForward resetFakeShiftEnrollments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        FakeShift fakeShift = AbstractDomainObject.fromExternalId(request.getParameter("fakeShift"));
        fakeShift.resetCurrentUserEnrollments();

        request.setAttribute("fakeShift", fakeShift);
        return mapping.findForward("viewFakeShift");
    }

    @Service
    public ActionForward importFakeShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        FakeShift.importFromLastSemesterShifts();
        request.setAttribute("fakeShifts", RootDomainObject.getInstance().getFakeShifts());
        return mapping.findForward("manageFakeShifts");
    }

    @Service
    public ActionForward deleteFakeShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        FakeShift.deleteAllFakeShifts();
        request.setAttribute("fakeShifts", RootDomainObject.getInstance().getFakeShifts());
        return mapping.findForward("manageFakeShifts");
    }
}

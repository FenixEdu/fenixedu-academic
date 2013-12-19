package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.CorrectTestGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeleteTestGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.FinishTestGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.PublishGrades;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.PublishTestGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.UnpublishTestGroup;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.tests.NewTest;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * 
 * @author jpmsi, lmam
 * 
 */

@Mapping(module = "teacher", path = "/tests/tests", input = "/tests/tests.do?method=manageTests", scope = "request",
        parameter = "method")
@Forwards(value = {
        @Forward(name = "deleteTestGroup", path = "/teacher/tests/tests/deleteTestGroup.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp")),
        @Forward(name = "correctTestGroup", path = "/teacher/tests/tests/correctTestGroup.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp")),
        @Forward(name = "correctTestByPerson", path = "/teacher/tests/tests/correctTestByPerson.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp")),
        @Forward(name = "manageTests", path = "/teacher/tests/tests/manageTests.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp")),
        @Forward(name = "viewTest", path = "/teacher/tests/tests/viewTest.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp")) })
public class TestsManagementAction extends FenixDispatchAction {

    public ActionForward manageTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        ExecutionCourse executionCourse = getDomainObject(request, "oid");

        Teacher teacher = getPerson(request).getTeacher();

        List<NewTestGroup> testGroups = new ArrayList<NewTestGroup>();

        for (NewTestGroup testGroup : teacher.getTestGroups()) {
            if (testGroup.getExecutionCourse().equals(executionCourse)) {
                testGroups.add(testGroup);
            }
        }

        request.setAttribute("testGroups", testGroups);
        request.setAttribute("executionCourse", executionCourse);

        return mapping.findForward("manageTests");
    }

    public ActionForward viewTestGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestGroup testGroup = getDomainObject(request, "oid");

        request.setAttribute("oid", testGroup.getOrderedTests().iterator().next().getExternalId());
        request.setAttribute("executionCourse", testGroup.getExecutionCourse());

        return this.viewTest(mapping, form, request, response);
    }

    public ActionForward viewTest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {

        NewTest test = getDomainObject(request, "oid");

        request.setAttribute("test", test);
        request.setAttribute("executionCourse", test.getTestGroup().getExecutionCourse());

        return mapping.findForward("viewTest");
    }

    public ActionForward publishTestGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestGroup testGroup = getDomainObject(request, "oid");

        PublishTestGroup.run(testGroup);

        request.setAttribute("oid", testGroup.getExecutionCourse().getExternalId());

        return this.manageTests(mapping, form, request, response);
    }

    public ActionForward unpublishTestGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestGroup testGroup = getDomainObject(request, "oid");

        UnpublishTestGroup.run(testGroup);

        request.setAttribute("oid", testGroup.getExecutionCourse().getExternalId());

        return this.manageTests(mapping, form, request, response);
    }

    public ActionForward prepareDeleteTestGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestGroup testGroup = getDomainObject(request, "oid");

        request.setAttribute("testGroup", testGroup);
        request.setAttribute("executionCourse", testGroup.getExecutionCourse());

        return mapping.findForward("deleteTestGroup");
    }

    public ActionForward deleteTestGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestGroup testGroup = getDomainObject(request, "oid");

        request.setAttribute("oid", testGroup.getExecutionCourse().getExternalId());

        DeleteTestGroup.run(testGroup);

        return this.manageTests(mapping, form, request, response);
    }

    public ActionForward finishTestGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestGroup testGroup = getDomainObject(request, "oid");

        FinishTestGroup.run(testGroup);

        request.setAttribute("oid", testGroup.getExecutionCourse().getExternalId());

        return this.manageTests(mapping, form, request, response);
    }

    public ActionForward publishGrades(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestGroup testGroup = getDomainObject(request, "oid");

        PublishGrades.run(testGroup);

        request.setAttribute("oid", testGroup.getExecutionCourse().getExternalId());

        return this.manageTests(mapping, form, request, response);
    }

    public ActionForward correctTestGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestGroup testGroup = getDomainObject(request, "oid");

        CorrectTestGroup.run(testGroup);

        List<CorrectTestBean> uncorrectedByPerson = new ArrayList<CorrectTestBean>();
        List<CorrectTestBean> correctedByPerson = new ArrayList<CorrectTestBean>();

        for (NewTest test : testGroup.getTests()) {
            for (Person person : test.getPersons()) {
                if (test.getAllUncorrectedQuestionsCount(person) == 0) {
                    correctedByPerson.add(new CorrectTestBean(test, person));
                } else {
                    uncorrectedByPerson.add(new CorrectTestBean(test, person));
                }
            }
        }

        request.setAttribute("testGroup", testGroup);
        request.setAttribute("uncorrectedByPerson", uncorrectedByPerson);
        request.setAttribute("correctedByPerson", correctedByPerson);
        request.setAttribute("executionCourse", testGroup.getExecutionCourse());

        return mapping.findForward("correctTestGroup");
    }

    public ActionForward correctByPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        Integer personId = getCodeFromRequest(request, "personId");
        Integer testGroupId = getCodeFromRequest(request, "testGroupId");

        NewTestGroup testGroup = getDomainObject(request, "testGroupId");
        Person person = getDomainObject(request, "personId");

        NewTest test = testGroup.getTest(person);

        request.setAttribute("test", test);
        request.setAttribute("person", person);
        request.setAttribute("executionCourse", testGroup.getExecutionCourse());

        return mapping.findForward("correctTestByPerson");
    }

    private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
        Integer code = null;
        Object objectCode = request.getAttribute(codeString);

        if (objectCode != null) {
            if (objectCode instanceof String) {
                code = new Integer((String) objectCode);
            } else if (objectCode instanceof Integer) {
                code = (Integer) objectCode;
            }
        } else {
            String thisCodeString = request.getParameter(codeString);
            if (thisCodeString != null) {
                code = new Integer(thisCodeString);
            }
        }

        return code;
    }

    private Person getPerson(HttpServletRequest request) {
        User userView = getUserView(request);

        return userView.getPerson();
    }

    private Object getMetaObject(String key) {
        IViewState viewState = RenderUtils.getViewState(key);

        if (viewState == null) {
            return null;
        }

        return viewState.getMetaObject().getObject();
    }

    private void createMessage(HttpServletRequest request, String name, String key) {
        ActionMessages messages = getMessages(request);
        messages.add(name, new ActionMessage(key, true));
        saveMessages(request, messages);
    }

}
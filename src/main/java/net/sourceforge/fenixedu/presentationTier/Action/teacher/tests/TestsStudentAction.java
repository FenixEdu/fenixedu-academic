package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeleteAnswer;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.GetStudentTest;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.GiveUpQuestion;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.tests.AtomicQuestionState;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewTest;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author jpmsi, lmam
 * 
 */

@Mapping(module = "student", path = "/tests/tests", input = "/tests/tests.do?method=viewTests", scope = "request",
        parameter = "method")
@Forwards(value = { @Forward(name = "viewTests", path = "/student/tests/viewTests.jsp"),
        @Forward(name = "viewTest", path = "/student/tests/viewTest.jsp") })
public class TestsStudentAction extends FenixDispatchAction {

    public static final Map<AtomicQuestionState, String> stateClasses = new HashMap<AtomicQuestionState, String>();

    static {
        stateClasses.put(AtomicQuestionState.ANSWERABLE, "qsnotanswered");
        stateClasses.put(AtomicQuestionState.ANSWERED, "qsanswered");
        stateClasses.put(AtomicQuestionState.GIVEN_UP, "qsgivenup");
    }

    public ActionForward viewTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        List<NewTestGroup> publishedTestGroups = new ArrayList<NewTestGroup>();
        List<NewTestGroup> finishedTestGroups = new ArrayList<NewTestGroup>();

        for (Registration registration : getPerson(request).getStudents()) {
            publishedTestGroups.addAll(registration.getPublishedTestGroups());
        }

        for (Registration registration : getPerson(request).getStudents()) {
            finishedTestGroups.addAll(registration.getFinishedTestGroups());
        }

        request.setAttribute("publishedTestGroups", publishedTestGroups);
        request.setAttribute("finishedTestGroups", finishedTestGroups);

        return mapping.findForward("viewTests");
    }

    public ActionForward viewTest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, ExcepcaoPersistencia {
        Integer testGroupId = getCodeFromRequest(request, "oid");

        NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);

        NewTest test = GetStudentTest.run(getPerson(request), testGroup);

        request.setAttribute("test", test);
        request.setAttribute("stateClasses", stateClasses);

        return mapping.findForward("viewTest");
    }

    public ActionForward deleteAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, ExcepcaoPersistencia {
        Integer atomicQuestionId = getCodeFromRequest(request, "oid");

        NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) rootDomainObject.readNewTestElementByOID(atomicQuestionId);

        NewTestGroup testGroup = atomicQuestion.getTest().getTestGroup();

        DeleteAnswer.run(atomicQuestion);

        request.setAttribute("oid", testGroup.getExternalId());

        return this.viewTest(mapping, form, request, response);
    }

    public ActionForward giveUpQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, ExcepcaoPersistencia {
        Integer atomicQuestionId = getCodeFromRequest(request, "oid");

        NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) rootDomainObject.readNewTestElementByOID(atomicQuestionId);

        NewTestGroup testGroup = atomicQuestion.getTest().getTestGroup();

        GiveUpQuestion.run(atomicQuestion);

        request.setAttribute("oid", testGroup.getExternalId());

        return this.viewTest(mapping, form, request, response);
    }

    public ActionForward answerQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, ExcepcaoPersistencia {
        return this.viewTest(mapping, form, request, response);
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
        IUserView userView = getUserView(request);

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
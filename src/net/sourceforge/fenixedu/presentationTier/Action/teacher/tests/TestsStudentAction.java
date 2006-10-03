package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.tests.AtomicQuestionState;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewTest;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * 
 * @author jpmsi, lmam
 * 
 */

public class TestsStudentAction extends FenixDispatchAction {

	public static final Map<AtomicQuestionState, String> stateClasses = new HashMap<AtomicQuestionState, String>();

	static {
		stateClasses.put(AtomicQuestionState.ANSWERABLE, "qsnotanswered");
		stateClasses.put(AtomicQuestionState.ANSWERED, "qsanswered");
		stateClasses.put(AtomicQuestionState.GIVEN_UP, "qsgivenup");
	}

	public ActionForward viewTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
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

	public ActionForward viewTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		Integer testGroupId = getCodeFromRequest(request, "oid");

		NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);

		NewTest test = (NewTest) ServiceUtils.executeService(getUserView(request), "GetStudentTest",
				new Object[] { getPerson(request), testGroup });

		request.setAttribute("test", test);
		request.setAttribute("stateClasses", stateClasses);

		return mapping.findForward("viewTest");
	}

	public ActionForward deleteAnswer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer atomicQuestionId = getCodeFromRequest(request, "oid");

		NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) rootDomainObject
				.readNewTestElementByOID(atomicQuestionId);

		NewTestGroup testGroup = atomicQuestion.getTest().getTestGroup();

		ServiceUtils.executeService(getUserView(request), "DeleteAnswer",
				new Object[] { atomicQuestion });

		request.setAttribute("oid", testGroup.getIdInternal());

		return this.viewTest(mapping, form, request, response);
	}

	public ActionForward giveUpQuestion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer atomicQuestionId = getCodeFromRequest(request, "oid");

		NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) rootDomainObject
				.readNewTestElementByOID(atomicQuestionId);

		NewTestGroup testGroup = atomicQuestion.getTest().getTestGroup();

		ServiceUtils.executeService(getUserView(request), "GiveUpQuestion",
				new Object[] { atomicQuestion });

		request.setAttribute("oid", testGroup.getIdInternal());

		return this.viewTest(mapping, form, request, response);
	}

	public ActionForward answerQuestion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		return this.viewTest(mapping, form, request, response);
	}

	private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
		Integer code = null;
		Object objectCode = request.getAttribute(codeString);

		if (objectCode != null) {
			if (objectCode instanceof String)
				code = new Integer((String) objectCode);
			else if (objectCode instanceof Integer)
				code = (Integer) objectCode;
		} else {
			String thisCodeString = request.getParameter(codeString);
			if (thisCodeString != null)
				code = new Integer(thisCodeString);
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
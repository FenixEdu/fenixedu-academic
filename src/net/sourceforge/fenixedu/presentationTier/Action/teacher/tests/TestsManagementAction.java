package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import net.sourceforge.fenixedu.domain.tests.NewTest;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
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

public class TestsManagementAction extends FenixDispatchAction {

	public ActionForward manageTests(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer executionCourseId = getCodeFromRequest(request, "oid");
		
		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
		
		Teacher teacher = getPerson(request).getTeacher();
		
		List<NewTestGroup> testGroups = new ArrayList<NewTestGroup>();
		
		for(NewTestGroup testGroup : teacher.getTestGroups()) {
			if(testGroup.getExecutionCourse().equals(executionCourse)) {
				testGroups.add(testGroup);
			}
		}
		
		request.setAttribute("testGroups", testGroups);
		request.setAttribute("executionCourse", executionCourse);
		
		return mapping.findForward("manageTests");
	}

	public ActionForward viewTestGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer testGroupId = getCodeFromRequest(request, "oid");

		NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);
		
		request.setAttribute("oid", testGroup.getOrderedTests().get(0).getIdInternal());
		request.setAttribute("executionCourse", testGroup.getExecutionCourse());

		return this.viewTest(mapping, form, request, response);
	}

	public ActionForward viewTest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer testId = getCodeFromRequest(request, "oid");

		NewTest test = (NewTest) rootDomainObject.readNewTestElementByOID(testId);
		
		request.setAttribute("test", test);
		request.setAttribute("executionCourse", test.getTestGroup().getExecutionCourse());

		return mapping.findForward("viewTest");
	}

	public ActionForward publishTestGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer testGroupId = getCodeFromRequest(request, "oid");

		NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);
		
		ServiceUtils.executeService(getUserView(request), "PublishTestGroup", new Object[] {
			testGroup });
		
		request.setAttribute("oid", testGroup.getExecutionCourse().getIdInternal());

		return this.manageTests(mapping, form, request, response);
	}

	public ActionForward unpublishTestGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer testGroupId = getCodeFromRequest(request, "oid");

		NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);
		
		ServiceUtils.executeService(getUserView(request), "UnpublishTestGroup", new Object[] {
			testGroup });
		
		request.setAttribute("oid", testGroup.getExecutionCourse().getIdInternal());

		return this.manageTests(mapping, form, request, response);
	}

	public ActionForward prepareDeleteTestGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer testGroupId = getCodeFromRequest(request, "oid");

		NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);
		
		request.setAttribute("testGroup", testGroup);
		request.setAttribute("executionCourse", testGroup.getExecutionCourse());

		return mapping.findForward("deleteTestGroup");
	}

	public ActionForward deleteTestGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer testGroupId = getCodeFromRequest(request, "oid");

		NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);
		
		request.setAttribute("oid", testGroup.getExecutionCourse().getIdInternal());
		
		ServiceUtils.executeService(getUserView(request), "DeleteTestGroup", new Object[] {
			testGroup });

		return this.manageTests(mapping, form, request, response);
	}

	public ActionForward finishTestGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer testGroupId = getCodeFromRequest(request, "oid");

		NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);
		
		ServiceUtils.executeService(getUserView(request), "FinishTestGroup", new Object[] {
			testGroup });
		
		request.setAttribute("oid", testGroup.getExecutionCourse().getIdInternal());

		return this.manageTests(mapping, form, request, response);
	}

	public ActionForward publishGrades(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer testGroupId = getCodeFromRequest(request, "oid");

		NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);
		
		ServiceUtils.executeService(getUserView(request), "PublishGrades", new Object[] {
			testGroup });
		
		request.setAttribute("oid", testGroup.getExecutionCourse().getIdInternal());

		return this.manageTests(mapping, form, request, response);
	}

	public ActionForward correctTestGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer testGroupId = getCodeFromRequest(request, "oid");

		NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);
		
		ServiceUtils.executeService(getUserView(request), "CorrectTestGroup", new Object[] {
			testGroup });
		
		List<CorrectTestBean> uncorrectedByPerson = new ArrayList<CorrectTestBean>();
		List<CorrectTestBean> correctedByPerson = new ArrayList<CorrectTestBean>();
		
		for(NewTest test : testGroup.getTests()) {
			for(Person person : test.getPersons()) {
				if(test.getAllUncorrectedQuestionsCount(person) == 0) {
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

	public ActionForward correctByPerson(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer personId = getCodeFromRequest(request, "personId");
		Integer testGroupId = getCodeFromRequest(request, "testGroupId");

		NewTestGroup testGroup = rootDomainObject.readNewTestGroupByOID(testGroupId);
		Person person = (Person) rootDomainObject.readPartyByOID(personId);
		
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
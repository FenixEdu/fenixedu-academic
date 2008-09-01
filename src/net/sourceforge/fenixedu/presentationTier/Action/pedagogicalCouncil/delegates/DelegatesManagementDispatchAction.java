package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.delegates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.delegates.DelegateBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class DelegatesManagementDispatchAction extends FenixDispatchAction {

    @Override
    protected Object getFromRequest(HttpServletRequest request, String id) {
	if (RenderUtils.getViewState(id) != null)
	    return RenderUtils.getViewState(id).getMetaObject().getObject();
	else if (request.getParameter(id) != null)
	    return request.getParameter(id);
	else
	    return request.getAttribute(id);
    }

    /*
     * Degree Delegates Group Management
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	DelegateBean bean = (DelegateBean) getFromRequest(request, "delegateBean");

	if (bean == null) {
	    bean = new DelegateBean();
	    bean.setDegreeType(DegreeType.BOLONHA_DEGREE);
	    bean.setDegree(getDefaultDegreeGivenDegreeType(DegreeType.BOLONHA_DEGREE));
	} else {
	    RenderUtils.invalidateViewState("delegateBean");
	    bean.setDegree(getDefaultDegreeGivenDegreeType(bean.getDegreeType()));
	}

	request.setAttribute("delegateBean", bean);
	request.setAttribute("currentExecutionYear", currentExecutionYear);
	return prepareViewDelegates(mapping, actionForm, request, response);
    }

    public ActionForward prepareSelectDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	DelegateBean bean = (DelegateBean) getFromRequest(request, "delegateBean");
	RenderUtils.invalidateViewState("delegateBean");

	if (bean.getDegree() == null) {
	    bean.setDegree(getDefaultDegreeGivenDegreeType(bean.getDegreeType()));
	}

	request.setAttribute("delegateBean", bean);
	request.setAttribute("currentExecutionYear", currentExecutionYear);
	return prepareViewDelegates(mapping, actionForm, request, response);
    }

    public ActionForward prepareViewDelegates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	DelegateBean bean = (DelegateBean) getFromRequest(request, "delegateBean");

	final Degree degree = bean.getDegree();
	final DegreeType degreeType = bean.getDegreeType();

	List<DelegateBean> delegates = new ArrayList<DelegateBean>();

	if (degreeType.equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
	    DelegateBean delegateBean = getDelegateBean(degree, FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE);
	    delegates.add(delegateBean);
	}

	if (degreeType.isSecondCycle()) {
	    DelegateBean delegateBean = getDelegateBean(degree, FunctionType.DELEGATE_OF_MASTER_DEGREE);
	    delegates.add(delegateBean);
	}

	if (degreeType.isFirstCycle()) {
	    DelegateBean delegateBean = getDelegateBean(degree, FunctionType.DELEGATE_OF_DEGREE);
	    delegates.add(delegateBean);
	}

	delegates.addAll(getYearDelegateBeans(degree));

	request.setAttribute("delegates", delegates);
	request.setAttribute("delegateBean", bean);
	request.setAttribute("currentExecutionYear", currentExecutionYear);
	return mapping.findForward("createEditDelegates");
    }

    public ActionForward prepareAddDelegate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer degreeOID = Integer.parseInt(request.getParameter("selectedDegree"));
	final Degree degree = rootDomainObject.readDegreeByOID(degreeOID);

	final String delegateType = request.getParameter("delegateType");
	final FunctionType delegateFunctionType = FunctionType.valueOf(delegateType);

	DelegateBean bean = getInitializedBean(degree);
	bean.setDelegateType(delegateFunctionType);

	if (delegateFunctionType.equals(FunctionType.DELEGATE_OF_YEAR)) {
	    final Integer year = Integer.parseInt(request.getParameter("selectedYear"));
	    final CurricularYear curricularYear = CurricularYear.readByYear(year);
	    bean.setCurricularYear(curricularYear);
	    request.setAttribute("newYearDelegateBean", bean);
	} else {
	    request.setAttribute("newDelegateBean", bean);
	}

	request.setAttribute("delegateBean", bean);
	return prepareViewDelegates(mapping, actionForm, request, response);
    }

    public ActionForward addYearDelegate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DelegateBean bean = (DelegateBean) getFromRequest(request, "newYearDelegateBean");

	Object[] args = null;
	if (bean != null) { /* From Delegates management main page */
	    final Integer studentNumber = bean.getStudentNumber();
	    final Student student = Student.readStudentByNumber(studentNumber);

	    if (student == null) {
		addActionMessage(request, "error.delegates.studentNotFound");
		RenderUtils.invalidateViewState("delegateBean");
		request.setAttribute("delegateBean", bean);
		return prepareViewDelegates(mapping, actionForm, request, response);
	    }

	    args = new Object[] { student, bean.getCurricularYear(), bean.getDegree() };
	} else { /* From selected delegate election voting results */
	    final Integer electionOID = Integer.parseInt(request.getParameter("selectedElection"));
	    final YearDelegateElection election = (YearDelegateElection) rootDomainObject.readDelegateElectionByOID(electionOID);
	    final Integer studentOID = Integer.parseInt(request.getParameter("selectedStudent"));
	    final Student student = rootDomainObject.readStudentByOID(studentOID);

	    bean = getInitializedBean(election.getDegree());
	    args = new Object[] { student, election };
	}

	return createDelegate(mapping, actionForm, request, response, args, bean, "prepareViewDelegates");
    }

    public ActionForward addDelegate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DelegateBean bean = (DelegateBean) getFromRequest(request, "newDelegateBean");

	final Integer studentNumber = bean.getStudentNumber();
	final Student student = Student.readStudentByNumber(studentNumber);

	if (student == null) {
	    addActionMessage(request, "error.delegates.studentNotFound");
	    RenderUtils.invalidateViewState("delegateBean");
	    request.setAttribute("delegateBean", bean);
	    return prepareViewDelegates(mapping, actionForm, request, response);
	}

	final Degree degree = bean.getDegree();
	final FunctionType functionType = bean.getDelegateType();

	return createDelegate(mapping, actionForm, request, response, new Object[] { student, degree, functionType }, bean,
		"prepareViewDelegates");
    }

    private ActionForward createDelegate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, Object[] args, DelegateBean bean, String forwardTo) throws Exception {

	try {
	    executeService(request, "AddNewDelegate", args);
	} catch (FenixServiceException ex) {
	    addActionMessage(request, ex.getMessage(), ex.getArgs());
	}

	RenderUtils.invalidateViewState("delegateBean");
	request.setAttribute("delegateBean", bean);
	return mapping.findForward(forwardTo);
    }

    public ActionForward removeDelegate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Integer delegateOID = Integer.parseInt(request.getParameter("selectedDelegate"));
	final Student student = rootDomainObject.readStudentByOID(delegateOID);

	Object[] args = null;

	if (request.getParameter("delegateType") != null) {
	    final String delegateType = request.getParameter("delegateType");
	    final FunctionType delegateFunctionType = FunctionType.valueOf(delegateType);

	    args = new Object[] { student, delegateFunctionType };
	} else {
	    args = new Object[] { student };
	}

	try {
	    executeService(request, "RemoveDelegate", args);
	} catch (FenixServiceException ex) {
	    addActionMessage(request, ex.getMessage(), ex.getArgs());
	}

	DelegateBean bean = getInitializedBean(student.getLastActiveRegistration().getDegree());

	request.setAttribute("delegateBean", bean);
	return prepareViewDelegates(mapping, actionForm, request, response);
    }

    public ActionForward prepareViewResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	Integer degreeOID = Integer.parseInt(request.getParameter("selectedDegree"));
	final Degree degree = rootDomainObject.readDegreeByOID(degreeOID);

	Integer year = Integer.parseInt(request.getParameter("selectedYear"));
	final CurricularYear curricularYear = CurricularYear.readByYear(year);

	DelegateBean bean = getInitializedBean(degree);

	DelegateElection election = degree.getYearDelegateElectionWithLastCandidacyPeriod(currentExecutionYear, curricularYear);
	if (election == null) {
	    addActionMessage(request, "error.delegates.noElection");
	    request.setAttribute("delegateBean", bean);
	    return prepareViewDelegates(mapping, actionForm, request, response);
	}

	request.setAttribute("selectedVotingPeriod", election.getIdInternal().toString());
	return mapping.findForward("showPossibleDelegates");
    }

    public ActionForward prepareChangeDelegate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Integer delegateOID = Integer.parseInt(request.getParameter("selectedDelegate"));
	final Student delegate = (Student) rootDomainObject.readStudentByOID(delegateOID);

	DelegateElection election = delegate.getLastElectedDelegateElection();

	request.setAttribute("selectedVotingPeriod", election.getIdInternal().toString());
	return mapping.findForward("showPossibleDelegates");
    }

    public ActionForward goBackToViewDelegates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Integer electionOID = Integer.parseInt(request.getParameter("selectedElection"));
	final DelegateElection election = rootDomainObject.readDelegateElectionByOID(electionOID);

	DelegateBean delegateBean = getInitializedBean(election.getDegree());
	request.setAttribute("delegateBean", delegateBean);

	return prepareViewDelegates(mapping, actionForm, request, response);
    }

    /*
     * GGAE Delegates Management
     */
    public ActionForward prepareViewGGAEDelegates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	List<DelegateBean> ggaeDelegates = getGGAEDelegateBeans();
	Collections.sort(ggaeDelegates, new BeanComparator("ggaeDelegateFunction.name"));

	request.setAttribute("ggaeDelegates", ggaeDelegates);
	request.setAttribute("currentExecutionYear", currentExecutionYear);
	return mapping.findForward("createEditGGAEDelegates");
    }

    public ActionForward prepareAddGGAEDelegate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	request.setAttribute("currentExecutionYear", currentExecutionYear);

	DelegateBean bean = (DelegateBean) getFromRequest(request, "delegateBean");
	if (bean == null) {
	    final Integer functionId = Integer.parseInt(request.getParameter("selectedGgaeFunction"));
	    final Function function = (Function) rootDomainObject.readAccountabilityTypeByOID(functionId);

	    bean = new DelegateBean();
	    bean.setGgaeDelegateFunction(function);

	    request.setAttribute("choosePersonBean", bean);
	    return prepareViewGGAEDelegates(mapping, actionForm, request, response);
	}

	if (bean.getPersonUsername() == null) {
	    request.setAttribute("chooseStudentBean", bean);
	    return prepareViewGGAEDelegates(mapping, actionForm, request, response);
	}

	Person person = Person.readPersonByUsername(bean.getPersonUsername());

	if (person != null) {
	    bean.setGgaeDelegate(person);
	    RenderUtils.invalidateViewState("delegateBean");
	    request.setAttribute("confirmPersonBean", bean);
	} else {
	    addActionMessage(request, "error.delegates.personNotFound");
	    RenderUtils.invalidateViewState("delegateBean");
	    request.setAttribute("choosePersonBean", bean);
	}
	return prepareViewGGAEDelegates(mapping, actionForm, request, response);
    }

    public ActionForward addGGAEDelegate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DelegateBean bean = (DelegateBean) getFromRequest(request, "delegateBean");

	final Person person = bean.getGgaeDelegate();

	final Function delegateFunction = bean.getGgaeDelegateFunction();
	return createDelegate(mapping, actionForm, request, response, new Object[] { person, delegateFunction }, bean,
		"prepareViewGGAEDelegates");
    }

    public ActionForward removeGGAEDelegate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Integer delegateOID = Integer.parseInt(request.getParameter("selectedDelegate"));
	final Person person = (Person) rootDomainObject.readPartyByOID(delegateOID);

	final Integer functionId = Integer.parseInt(request.getParameter("selectedGgaeFunction"));
	final Function function = (Function) rootDomainObject.readAccountabilityTypeByOID(functionId);

	try {
	    executeService(request, "RemoveDelegate", new Object[] { person, function });
	} catch (FenixServiceException ex) {
	    addActionMessage(request, ex.getMessage(), ex.getArgs());
	}

	return prepareViewGGAEDelegates(mapping, actionForm, request, response);
    }

    /*
     * AUXILIARY METHODS
     */

    /* Delegates from given degree (not year delegates) */
    private DelegateBean getDelegateBean(Degree degree, FunctionType functionType) {
	DelegateBean delegateBean = getInitializedBean(degree);
	delegateBean.setDelegateType(functionType);
	List<Student> delegates = degree.getAllActiveDelegatesByFunctionType(functionType, delegateBean.getExecutionYear());
	if (!delegates.isEmpty()) {
	    delegateBean.setDelegate(delegates.get(0));
	}
	return delegateBean;
    }

    private List<DelegateBean> getYearDelegateBeans(Degree degree) {
	List<DelegateBean> yearDelegates = new ArrayList<DelegateBean>();
	for (int i = 1; i <= degree.getDegreeType().getYears(); i++) {
	    final CurricularYear curricularYear = CurricularYear.readByYear(i);
	    final Student student = degree.getActiveYearDelegateByCurricularYear(curricularYear); // can
	    // be
	    // null
	    // if
	    // doesn
	    // 't
	    // exist
	    final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	    final DelegateElection election = degree
		    .getYearDelegateElectionWithLastCandidacyPeriod(executionYear, curricularYear);

	    DelegateBean delegateBean = getInitializedBean(degree);
	    delegateBean.setDelegate(student);
	    delegateBean.setDelegateType(FunctionType.DELEGATE_OF_YEAR);
	    delegateBean.setCurricularYear(curricularYear);
	    delegateBean.setDelegateElection(election);
	    yearDelegates.add(delegateBean);
	}
	return yearDelegates;
    }

    private DelegateBean getInitializedBean(final Degree degree) {
	DelegateBean bean = new DelegateBean();
	bean.setDegreeType(degree.getDegreeType());
	bean.setDegree(degree);
	return bean;
    }

    private Degree getDefaultDegreeGivenDegreeType(DegreeType degreeType) {
	List<Degree> degrees = Degree.readAllByDegreeType(degreeType);
	return degrees.get(0);
    }

    private List<DelegateBean> getGGAEDelegateBeans() {
	final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	List<DelegateBean> result = new ArrayList<DelegateBean>();
	Set<Function> functions = Function.readAllActiveFunctionsByType(FunctionType.DELEGATE_OF_GGAE);
	for (Function function : functions) {
	    if (function.getActivePersonFunctionsStartingIn(executionYear).isEmpty()) {
		DelegateBean bean = new DelegateBean();
		bean.setDelegateType(FunctionType.DELEGATE_OF_GGAE);
		bean.setGgaeDelegateFunction(function);
		result.add(bean);
	    } else {
		for (PersonFunction personFunction : function.getActivePersonFunctionsStartingIn(executionYear)) {
		    DelegateBean bean = new DelegateBean();
		    bean.setGgaeDelegate(personFunction.getPerson());
		    bean.setDelegateType(FunctionType.DELEGATE_OF_GGAE);
		    bean.setGgaeDelegateFunction(function);
		    result.add(bean);
		}
	    }
	}
	return result;
    }
}

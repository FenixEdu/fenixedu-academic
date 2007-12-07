package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.DataOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearIntervalBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.RequestUtils;

import pt.utl.ist.fenix.tools.image.TextPngCreator;

public class ViewHomepageDA extends SiteVisualizationDA {

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
	Homepage homepage = (Homepage) request.getAttribute("homepage");
	if (homepage == null) {
	    return null;
	}

	try {
	    return RequestUtils.absoluteURL(request,
		    "/homepage/" + homepage.getPerson().getUser().getUserUId()).toString();
	} catch (MalformedURLException e) {
	    return null;
	}
    }

    public ActionForward show(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Homepage homepage = getHomepage(request);
	
	if (homepage == null || !homepage.getActivated().booleanValue()) {
	    final ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("homepage.not.found"));
	    saveMessages(request, actionMessages);

	    return list(mapping, actionForm, request, response);
	} else {
	    SortedSet<Attends> personAttendsSortedByExecutionCourseName = new TreeSet<Attends>(
		    Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
	    personAttendsSortedByExecutionCourseName.addAll(homepage.getPerson().getCurrentAttends());

	    request.setAttribute("personAttends", personAttendsSortedByExecutionCourseName);
	    request.setAttribute("homepage", homepage);

	    return mapping.findForward("view-homepage");
	}
    }

    public ActionForward notFound(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward("not-found-homepage");
    }

    public ActionForward list(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final SortedMap<String, SortedSet<Homepage>> homepages = new TreeMap<String, SortedSet<Homepage>>();
	for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
	    homepages.put("" + ((char) i), new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME));
	}

	for (final Homepage homepage : Homepage.getAllHomepages()) {
	    if (homepage.getActivated().booleanValue()) {
		final String key = homepage.getOwnersName().substring(0, 1);
		final SortedSet<Homepage> sortedSet;
		if (homepages.containsKey(key)) {
		    sortedSet = homepages.get(key);
		    sortedSet.add(homepage);
		}
	    }
	}

	request.setAttribute("homepages", homepages);

	final String selectedPage = request.getParameter("selectedPage");
	if (selectedPage != null) {
	    request.setAttribute("selectedPage", selectedPage);
	} else {
	    request.setAttribute("selectedPage", "");
	}

	return mapping.findForward("list-homepages");
    }

    public ActionForward listTeachers(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final SortedMap<Unit, SortedSet<Homepage>> homepages = new TreeMap<Unit, SortedSet<Homepage>>(
		Unit.COMPARATOR_BY_NAME_AND_ID);
	for (final Teacher teacher : rootDomainObject.getTeachersSet()) {
	    final Person person = teacher.getPerson();
	    final Employee employee = person.getEmployee();
	    if (employee != null) {
		final Contract contract = employee.getCurrentWorkingContract();
		if (contract != null) {
		    final Unit unit = contract.getWorkingUnit();
		    final SortedSet<Homepage> unitHomepages;
		    if (homepages.containsKey(unit)) {
			unitHomepages = homepages.get(unit);
		    } else {
			unitHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
			homepages.put(unit, unitHomepages);
		    }
		    final Homepage homepage = person.getHomepage();
		    if (homepage != null && homepage.getActivated().booleanValue()) {
			unitHomepages.add(homepage);
		    }
		}
	    }
	}
	request.setAttribute("homepages", homepages);

	final String selectedPage = request.getParameter("selectedPage");
	if (selectedPage != null) {
	    request.setAttribute("selectedPage", selectedPage);
	}

	return mapping.findForward("list-homepages-teachers");
    }

    public ActionForward listEmployees(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final SortedMap<Unit, SortedSet<Homepage>> homepages = new TreeMap<Unit, SortedSet<Homepage>>(
		Unit.COMPARATOR_BY_NAME_AND_ID);
	for (final Employee employee : rootDomainObject.getEmployeesSet()) {
	    final Person person = employee.getPerson();
	    if (person != null) {
		final Teacher teacher = person.getTeacher();
		if (teacher == null) {
		    final Contract contract = employee.getCurrentWorkingContract();
		    if (contract != null) {
			final Unit unit = contract.getWorkingUnit();
			final SortedSet<Homepage> unitHomepages;
			if (homepages.containsKey(unit)) {
			    unitHomepages = homepages.get(unit);
			} else {
			    unitHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
			    homepages.put(unit, unitHomepages);
			}
			final Homepage homepage = person.getHomepage();
			if (homepage != null && homepage.getActivated().booleanValue()) {
			    unitHomepages.add(homepage);
			}
		    }
		}
	    }
	}
	request.setAttribute("homepages", homepages);

	final String selectedPage = request.getParameter("selectedPage");
	if (selectedPage != null) {
	    request.setAttribute("selectedPage", selectedPage);
	}

	return mapping.findForward("list-homepages-employees");
    }

    public ActionForward listStudents(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final SortedMap<Degree, SortedSet<Homepage>> homepages = new TreeMap<Degree, SortedSet<Homepage>>(
		Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	for (final Registration registration : rootDomainObject.getRegistrationsSet()) {
	    final StudentCurricularPlan studentCurricularPlan = registration
		    .getActiveStudentCurricularPlan();
	    if (studentCurricularPlan != null) {
		final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan
			.getDegreeCurricularPlan();
		final Degree degree = degreeCurricularPlan.getDegree();
		final Person person = registration.getPerson();
		final SortedSet<Homepage> degreeHomepages;
		if (homepages.containsKey(degree)) {
		    degreeHomepages = homepages.get(degree);
		} else {
		    degreeHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
		    homepages.put(degree, degreeHomepages);
		}
		final Homepage homepage = person.getHomepage();
		if (homepage != null && homepage.getActivated().booleanValue()) {
		    degreeHomepages.add(homepage);
		}
	    }
	}
	request.setAttribute("homepages", homepages);

	final String selectedPage = request.getParameter("selectedPage");
	if (selectedPage != null) {
	    request.setAttribute("selectedPage", selectedPage);
	}

	return mapping.findForward("list-homepages-students");
    }

    public ActionForward listAlumni(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final SortedMap<Degree, SortedSet<Homepage>> homepages = new TreeMap<Degree, SortedSet<Homepage>>(
		Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	for (final Registration registration : rootDomainObject.getRegistrationsSet()) {

	    if (registration.getActiveState().getStateType().equals(RegistrationStateType.CONCLUDED)) {

		final Degree degree = registration.getActiveStudentCurricularPlan()
			.getDegreeCurricularPlan().getDegree();

		final SortedSet<Homepage> degreeHomepages;
		if (homepages.containsKey(degree)) {
		    degreeHomepages = homepages.get(degree);
		} else {
		    degreeHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
		    homepages.put(degree, degreeHomepages);
		}

		final Homepage homepage = registration.getPerson().getHomepage();
		if (homepage != null && homepage.getActivated()) {
		    degreeHomepages.add(homepage);
		}
	    }

	}

	request.setAttribute("homepages", homepages);

	final String selectedPage = request.getParameter("selectedPage");
	if (selectedPage != null) {
	    request.setAttribute("selectedPage", selectedPage);
	}

	return mapping.findForward("list-homepages-alumni");
    }

    public ActionForward emailPng(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final String email = getEmailString(request);
	if (StringUtils.isNotEmpty(email)) {
	    final byte[] pngFile = TextPngCreator.createPng("arial", 12, "000000", email);
	    response.setContentType("image/png");
	    response.getOutputStream().write(pngFile);
	    response.getOutputStream().close();
	}
	return null;
    }

    public ActionForward stats(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("homepages", Homepage.getAllHomepages());
	return mapping.findForward("homepage-stats");
    }

    private String getEmailString(final HttpServletRequest request) {
	
	Homepage homepage = getHomepage(request);
	final Person person = homepage.getPerson();
	if (person != null && person.getHomepage() != null
		&& person.getHomepage().getActivated().booleanValue()
		&& person.getHomepage().getShowEmail().booleanValue()) {
	    return person.getEmail();

	}
	return "";
    }

    public ActionForward retrievePhoto(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final Homepage homepage = getHomepage(request);
	if (homepage != null && homepage.getShowPhoto() != null
		&& homepage.getShowPhoto().booleanValue()) {
	    final Person person = homepage.getPerson();
	    final FileEntry personalPhoto = person.getPersonalPhoto();

	    if (personalPhoto != null) {
		response.setContentType(personalPhoto.getContentType().getMimeType());
		DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		dos.write(personalPhoto.getContents());
		dos.close();
	    }
	}

	return null;
    }

    public ActionForward showPublications(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Homepage homepage = getHomepage(request);
	IViewState viewState = RenderUtils.getViewState("executionYearIntervalBean");

	ExecutionYearIntervalBean bean;
	if (viewState != null) {
	    bean = (ExecutionYearIntervalBean) viewState.getMetaObject().getObject();
	} else {
	    bean = generateSearchBean();
	}

	request.setAttribute("executionYearIntervalBean", bean);

	setPublicationsInRequest(request, bean, homepage.getPerson());

	return mapping.findForward("showPublications");
    }

    public ActionForward showPatents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Homepage homepage = getHomepage(request);

	List<ResearchResultPatent> patents = homepage.getPerson().getResearchResultPatents();
	Collections.sort(patents, new BeanComparator("approvalYear"));
	request.setAttribute("patents", patents);
	return mapping.findForward("showPatents");
    }

    public ActionForward showInterests(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Homepage homepage = getHomepage(request);

	request.setAttribute("interests", homepage.getPerson().getResearchInterests());
	return mapping.findForward("showInterests");
    }

    public ActionForward showParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Homepage homepage = getHomepage(request);
	setParticipationsInRequest(request, homepage.getPerson());

	return mapping.findForward("showParticipations");

    }

    public ActionForward showPrizes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Homepage homepage = getHomepage(request);

	request.setAttribute("prizes", homepage.getPerson().getPrizes());

	return mapping.findForward("showPrizes");
    }

    private void setPublicationsInRequest(HttpServletRequest request, ExecutionYearIntervalBean bean,
	    Person person) {

	ExecutionYear firstExecutionYear = bean.getFirstExecutionYear();
	ExecutionYear finalExecutionYear = bean.getFinalExecutionYear();
	ResultPublicationType resultPublicationType = bean.getPublicationType();

	if (resultPublicationType == null) {
	    request.setAttribute("books", ResearchResultPublication.sort(person.getBooks(
		    firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("national-articles", ResearchResultPublication.sort(person.getArticles(
		    ScopeType.NATIONAL, firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("international-articles", ResearchResultPublication.sort(person
		    .getArticles(ScopeType.INTERNATIONAL, firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("national-inproceedings", ResearchResultPublication.sort(person
		    .getInproceedings(ScopeType.NATIONAL, firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("international-inproceedings", ResearchResultPublication.sort(person
		    .getInproceedings(ScopeType.INTERNATIONAL, firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("proceedings", ResearchResultPublication.sort(person.getProceedings(
		    firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("theses", ResearchResultPublication.sort(person.getTheses(
		    firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("manuals", ResearchResultPublication.sort(person.getManuals(
		    firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("technicalReports", ResearchResultPublication.sort(person
		    .getTechnicalReports(firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("otherPublications", ResearchResultPublication.sort(person
		    .getOtherPublications(firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("unstructureds", ResearchResultPublication.sort(person
		    .getUnstructureds(firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("inbooks", ResearchResultPublication.sort(person.getInbooks(
		    firstExecutionYear, finalExecutionYear)));
	} else {
	    switch (resultPublicationType) {
	    case Article:
		request.setAttribute("articles", ResearchResultPublication.sort(person.getArticles(
			firstExecutionYear, finalExecutionYear)));
		break;
	    case Book:
		request.setAttribute("books", ResearchResultPublication.sort(person.getBooks(
			firstExecutionYear, finalExecutionYear)));
		break;
	    case BookPart:
		request.setAttribute("inbooks", ResearchResultPublication.sort(person.getInbooks(
			firstExecutionYear, finalExecutionYear)));
		break;
	    case Inproceedings:
		request.setAttribute("inproceedings", ResearchResultPublication.sort(person
			.getInproceedings(firstExecutionYear, finalExecutionYear)));
		break;
	    case Manual:
		request.setAttribute("manuals", ResearchResultPublication.sort(person.getManuals(
			firstExecutionYear, finalExecutionYear)));
		break;
	    case OtherPublication:
		request.setAttribute("otherPublications", ResearchResultPublication.sort(person
			.getOtherPublications(firstExecutionYear, finalExecutionYear)));
		break;
	    case Proceedings:
		request.setAttribute("proceedings", ResearchResultPublication.sort(person
			.getProceedings(firstExecutionYear, finalExecutionYear)));
		break;
	    case TechnicalReport:
		request.setAttribute("technicalReports", ResearchResultPublication.sort(person
			.getTechnicalReports(firstExecutionYear, finalExecutionYear)));
		break;
	    case Thesis:
		request.setAttribute("theses", ResearchResultPublication.sort(person.getTheses(
			firstExecutionYear, finalExecutionYear)));
		break;
	    }
	}

	request.setAttribute("person", getLoggedPerson(request));
    }

    private void setParticipationsInRequest(HttpServletRequest request, Person person) {
	request.setAttribute("national-events", new ArrayList<ResearchEvent>(person
		.getAssociatedEvents(ScopeType.NATIONAL)));
	request.setAttribute("international-events", new ArrayList<ResearchEvent>(person
		.getAssociatedEvents(ScopeType.INTERNATIONAL)));
	request.setAttribute("international-eventEditions", new ArrayList<EventEdition>(person
		.getAssociatedEventEditions(ScopeType.INTERNATIONAL)));
	request.setAttribute("national-eventEditions", new ArrayList<EventEdition>(person
		.getAssociatedEventEditions(ScopeType.NATIONAL)));
	request.setAttribute("national-journals", new ArrayList<ScientificJournal>(person
		.getAssociatedScientificJournals(ScopeType.NATIONAL)));
	request.setAttribute("international-journals", new ArrayList<ScientificJournal>(person
		.getAssociatedScientificJournals(ScopeType.INTERNATIONAL)));
	request.setAttribute("cooperations", new ArrayList<Cooperation>(person
		.getAssociatedCooperations()));
	request.setAttribute("national-issues", new ArrayList<JournalIssue>(person
		.getAssociatedJournalIssues(ScopeType.NATIONAL)));
	request.setAttribute("international-issues", new ArrayList<JournalIssue>(person
		.getAssociatedJournalIssues(ScopeType.INTERNATIONAL)));
    }

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return show(mapping, form, request, response);
    }

    protected Homepage getHomepage(HttpServletRequest request) {
	FunctionalityContext context = AbstractFunctionalityContext.getCurrentContext(request);
	Container container = null;
	if (context != null) {
	    container = (Container) context.getLastContentInPath(Homepage.class);
	}
	if(container instanceof Homepage) {
	    return (Homepage) container;    
	}
	else {
	    String homepageID = request.getParameter("homepageID");
	    return (Homepage) RootDomainObject.getInstance().readContentByOID(Integer.valueOf(homepageID));
	}
	
    }

}
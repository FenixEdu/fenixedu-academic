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

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.RequestUtils;

import pt.utl.ist.fenix.tools.image.TextPngCreator;

public class ViewHomepageDA extends SiteVisualizationDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String homepageIDString = request.getParameter("homepageID");

	if (homepageIDString != null) {
	    Integer homepageID = Integer.valueOf(homepageIDString);

	    DomainObject possibleHomepage = readDomainObject(request, Homepage.class, homepageID);
	    if (possibleHomepage instanceof Homepage) {
		Homepage homepage = (Homepage) possibleHomepage;

		if (homepage.getActivated() != null && homepage.getActivated()) {
		    request.setAttribute("homepage", homepage);
		}
	    }
	}

	return super.execute(mapping, actionForm, request, response);
    }

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
	final String homepageIDString = request.getParameter("homepageID");
	final Integer homepageID = Integer.valueOf(homepageIDString);

	final Homepage homepage;
	DomainObject possibleHomepage = readDomainObject(request, Homepage.class, homepageID);
	if (possibleHomepage instanceof Homepage) {
	    homepage = (Homepage) possibleHomepage;
	} else {
	    homepage = null;
	}

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

    public ActionForward notFound(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
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
		final String key = homepage.getName().substring(0, 1);
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
	    final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
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

    public ActionForward listAlumni(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final SortedMap<Degree, SortedSet<Homepage>> homepages = new TreeMap<Degree, SortedSet<Homepage>>(
		Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	for (final Registration registration : rootDomainObject.getRegistrationsSet()) {

	    if (registration.getActiveState().getStateType().equals(RegistrationStateType.CONCLUDED)) {

		final Degree degree = registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan()
			.getDegree();

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

    public ActionForward emailPng(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final String email = getEmailString(request);
	final byte[] pngFile = TextPngCreator.createPng("arial", 12, "000000", email);
	response.setContentType("image/png");
	response.getOutputStream().write(pngFile);
	response.getOutputStream().close();
	return null;
    }

    public ActionForward stats(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("homepages", Homepage.getAllHomepages());
	return mapping.findForward("homepage-stats");
    }

    private String getEmailString(final HttpServletRequest request) {
	final String homepageId = request.getParameter("homepageID");
	Homepage homepage = (Homepage) RootDomainObject.readDomainObjectByOID(Homepage.class, Integer
		.valueOf(homepageId));
	final Person person = homepage.getPerson();
	if (person != null && person.getHomepage() != null
		&& person.getHomepage().getActivated().booleanValue()
		&& person.getHomepage().getShowEmail().booleanValue()) {
	    return person.getEmail();

	}
	return "";
    }

    public ActionForward retrievePhoto(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final String homepageIDString = request.getParameter("homepageID");
	final Integer homepageID = Integer.valueOf(homepageIDString);
	final Homepage homepage = (Homepage) readDomainObject(request, Homepage.class, homepageID);
	if (homepage != null && homepage.getShowPhoto() != null && homepage.getShowPhoto().booleanValue()) {
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

    public ActionForward showPublications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final String homepageId = request.getParameter("homepageID");
	Homepage homepage = (Homepage) RootDomainObject.readDomainObjectByOID(Homepage.class, Integer
		.valueOf(homepageId));

	setPublicationsInRequest(request, homepage.getPerson());

	return mapping.findForward("showPublications");
    }

    public ActionForward showPatents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final String homepageId = request.getParameter("homepageID");
	Homepage homepage = (Homepage) RootDomainObject.readDomainObjectByOID(Homepage.class, Integer
		.valueOf(homepageId));

	List<ResearchResultPatent> patents = homepage.getPerson().getResearchResultPatents();
	Collections.sort(patents, new BeanComparator("approvalYear"));
	request.setAttribute("patents", patents);
	return mapping.findForward("showPatents");
    }

    public ActionForward showInterests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final String homepageId = request.getParameter("homepageID");
	Homepage homepage = (Homepage) RootDomainObject.readDomainObjectByOID(Homepage.class, Integer
		.valueOf(homepageId));

	request.setAttribute("interests", homepage.getPerson().getResearchInterests());
	return mapping.findForward("showInterests");
    }

    public ActionForward showParticipations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	final String homepageId = request.getParameter("homepageID");
	Homepage homepage = (Homepage) RootDomainObject.readDomainObjectByOID(Homepage.class, Integer
		.valueOf(homepageId));

	setParticipationsInRequest(request,homepage.getPerson());
	
	return mapping.findForward("showParticipations");
	
    }
    
    private void setPublicationsInRequest(HttpServletRequest request, Person person) {

	request.setAttribute("books", ResearchResultPublication.sort(person.getBooks()));
	request.setAttribute("national-articles", ResearchResultPublication.sort(person
		.getArticles(ScopeType.NATIONAL)));
	request.setAttribute("international-articles", ResearchResultPublication.sort(person
		.getArticles(ScopeType.INTERNATIONAL)));
	request.setAttribute("national-inproceedings", ResearchResultPublication.sort(person
		.getInproceedings(ScopeType.NATIONAL)));
	request.setAttribute("international-inproceedings", ResearchResultPublication.sort(person
		.getInproceedings(ScopeType.INTERNATIONAL)));
	request.setAttribute("proceedings", ResearchResultPublication.sort(person.getProceedings()));
	request.setAttribute("theses", ResearchResultPublication.sort(person.getTheses()));
	request.setAttribute("manuals", ResearchResultPublication.sort(person.getManuals()));
	request
		.setAttribute("technicalReports", ResearchResultPublication
			.sort(person.getTechnicalReports()));
	request.setAttribute("otherPublications", ResearchResultPublication.sort(person
		.getOtherPublications()));
	request.setAttribute("unstructureds", ResearchResultPublication.sort(person.getUnstructureds()));
	request.setAttribute("inbooks", ResearchResultPublication.sort(person.getInbooks()));

	request.setAttribute("person", getLoggedPerson(request));
    }

    private void setParticipationsInRequest(HttpServletRequest request, Person person) {
	request.setAttribute("national-events", new ArrayList<Event>(person
		.getAssociatedEvents(ScopeType.NATIONAL)));
	request.setAttribute("international-events", new ArrayList<Event>(person
		.getAssociatedEvents(ScopeType.INTERNATIONAL)));
	request.setAttribute("international-eventEditions", new ArrayList<EventEdition>(person
		.getAssociatedEventEditions(ScopeType.INTERNATIONAL)));
	request.setAttribute("national-eventEditions", new ArrayList<EventEdition>(person
		.getAssociatedEventEditions(ScopeType.NATIONAL)));
	request.setAttribute("national-journals", new ArrayList<ScientificJournal>(person
		.getAssociatedScientificJournals(ScopeType.NATIONAL)));
	request.setAttribute("international-journals", new ArrayList<ScientificJournal>(person
		.getAssociatedScientificJournals(ScopeType.INTERNATIONAL)));
	request.setAttribute("cooperations", new ArrayList<Cooperation>(person.getAssociatedCooperations()));
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

}
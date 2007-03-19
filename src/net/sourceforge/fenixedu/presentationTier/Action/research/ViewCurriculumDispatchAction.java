package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearIntervalBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityLocationType;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewCurriculumDispatchAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String personId = request.getParameter("personOID");

	final Person person = ((personId != null && personId.length() > 0) ? (Person) RootDomainObject
		.readDomainObjectByOID(Person.class, Integer.valueOf(personId)) : getLoggedPerson(request));

	request.setAttribute("person", person);

	ExecutionYearIntervalBean bean = retrieveExecutionYearBeanFromRequest(request);

	if (bean.getFirstExecutionYear() != null || bean.getFinalExecutionYear() != null) {
	    putInformationOnRequestForGivenExecutionYear(bean.getFirstExecutionYear(), bean
		    .getFinalExecutionYear(), person, request);
	} else {
	    putAllInformationOnRequest(person, request);
	}

	final List<ResearchInterest> researchInterests = person.getResearchInterests();
	request.setAttribute("researchInterests", researchInterests);

	return mapping.findForward("Success");
    }

    private ExecutionYearIntervalBean retrieveExecutionYearBeanFromRequest(HttpServletRequest request) {
	IViewState viewState = RenderUtils.getViewState("executionYearIntervalBean");
	ExecutionYearIntervalBean bean = (viewState != null) ? (ExecutionYearIntervalBean) viewState.getMetaObject()
		.getObject() : new ExecutionYearIntervalBean();
	request.setAttribute("executionYearIntervalBean", bean);
	return bean;
    }

    private void putInformationOnRequestForGivenExecutionYear(ExecutionYear firstExecutionYear,
	    ExecutionYear finaltExecutionYear, Person person, HttpServletRequest request) {

	Set<Advise> final_works = new HashSet<Advise>();
	Set<MasterDegreeThesisDataVersion> guidances = new HashSet<MasterDegreeThesisDataVersion>();
	Set<ExecutionCourse> lectures = new HashSet<ExecutionCourse>();
	Set<PersonFunction> functions = new HashSet<PersonFunction>();
	Set<ResearchResultPublication> books = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> localArticles = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> nationalArticles = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> internationalArticles = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> inproceedings = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> proceedings = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> thesis = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> manuals = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> technicalReports = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> otherPublication = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> unstructured = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> bookParts = new HashSet<ResearchResultPublication>();
	Set<ResearchResultPublication> resultPublications = new HashSet<ResearchResultPublication>();

	if (firstExecutionYear == null) {
	    firstExecutionYear = ExecutionYear.readFirstExecutionYear();
	}
	if (finaltExecutionYear == null) {
	    finaltExecutionYear = ExecutionYear.readLastExecutionYear();
	}

	ExecutionYear stoppageYear = finaltExecutionYear.getNextExecutionYear();
	ExecutionYear iteratorYear = firstExecutionYear;
	Teacher teacher = person.getTeacher();

	while (iteratorYear != stoppageYear) {

	    if (teacher != null) {
		final_works.addAll(teacher.getAdvisesByAdviseTypeAndExecutionYear(
			AdviseType.FINAL_WORK_DEGREE, iteratorYear));

		guidances.addAll(teacher.getGuidedMasterDegreeThesisByExecutionYear(iteratorYear));
		lectures.addAll(teacher.getLecturedExecutionCoursesByExecutionYear(iteratorYear));
	    }

	    functions.addAll(person.getPersonFuntions(iteratorYear.getBeginDateYearMonthDay(), iteratorYear
		    .getEndDateYearMonthDay()));
	    resultPublications.addAll(person.getResearchResultPublicationsByExecutionYear(iteratorYear));
	    books.addAll(person.getBooks(iteratorYear));
	    nationalArticles.addAll(person.getArticles(ResearchActivityLocationType.NATIONAL, iteratorYear));
	    internationalArticles.addAll(person.getArticles(ResearchActivityLocationType.INTERNATIONAL,
		    iteratorYear));
	    inproceedings.addAll(person.getInproceedings(iteratorYear));
	    proceedings.addAll(person.getProceedings(iteratorYear));
	    thesis.addAll(person.getTheses(iteratorYear));
	    manuals.addAll(person.getManuals(iteratorYear));
	    technicalReports.addAll(person.getTechnicalReports(iteratorYear));
	    otherPublication.addAll(person.getOtherPublications(iteratorYear));
	    unstructured.addAll(person.getUnstructureds(iteratorYear));
	    bookParts.addAll(person.getInbooks(iteratorYear));

	    iteratorYear = iteratorYear.getNextExecutionYear();
	}

	List<PersonFunction> functionsList = new ArrayList<PersonFunction>(functions);
	Collections.sort(functionsList, new ReverseComparator(new BeanComparator("beginDateInDateType")));
	request.setAttribute("functions", functionsList);
	List<Advise> final_worksList = new ArrayList<Advise>(final_works);
	Collections.sort(final_worksList, new BeanComparator("student.number"));
	request.setAttribute("final_works", final_worksList);
	request.setAttribute("guidances", guidances);
	request.setAttribute("lectures", lectures);
	request.setAttribute("resultPublications", resultPublications);
	request.setAttribute("books", books);
	request.setAttribute("local-articles", localArticles);
	request.setAttribute("national-articles", nationalArticles);
	request.setAttribute("international-articles", internationalArticles);
	request.setAttribute("inproceedings", inproceedings);
	request.setAttribute("proceedings", proceedings);
	request.setAttribute("theses", thesis);
	request.setAttribute("manuals", manuals);
	request.setAttribute("technicalReports", technicalReports);
	request.setAttribute("otherPublications", otherPublication);
	request.setAttribute("unstructureds", unstructured);
	request.setAttribute("inbooks", bookParts);

    }

    private void putAllInformationOnRequest(Person person, HttpServletRequest request) {
	if (person.hasTeacher()) {
	    Teacher teacher = person.getTeacher();

	    List<Advise> final_works = new ArrayList<Advise>(teacher
		    .getAdvisesByAdviseType(AdviseType.FINAL_WORK_DEGREE));
	    Collections.sort(final_works, new ReverseComparator(new BeanComparator("startExecutionPeriod")));

	    request.setAttribute("final_works", final_works);
	    request.setAttribute("guidances", teacher.getAllGuidedMasterDegreeThesis());
	    request.setAttribute("lectures", teacher.getAllLecturedExecutionCourses());
	}

	List<PersonFunction> functions = new ArrayList<PersonFunction>(person.getPersonFunctions());
	Collections.sort(functions, new ReverseComparator(new BeanComparator("beginDateInDateType")));
	request.setAttribute("functions", functions);
	final List<ResearchResultPublication> resultPublications = person.getResearchResultPublications();
	request.setAttribute("resultPublications", resultPublications);

	request.setAttribute("books", person.getBooks());
	request.setAttribute("national-articles", person.getArticles(ResearchActivityLocationType.NATIONAL));
	request.setAttribute("international-articles", person
		.getArticles(ResearchActivityLocationType.INTERNATIONAL));
	request.setAttribute("inproceedings", person.getInproceedings());
	request.setAttribute("proceedings", person.getProceedings());
	request.setAttribute("theses", person.getTheses());
	request.setAttribute("manuals", person.getManuals());
	request.setAttribute("technicalReports", person.getTechnicalReports());
	request.setAttribute("otherPublications", person.getOtherPublications());
	request.setAttribute("unstructureds", person.getUnstructureds());
	request.setAttribute("inbooks", person.getInbooks());

	request.setAttribute("resultPatents", person.getResearchResultPatents());
    }

}
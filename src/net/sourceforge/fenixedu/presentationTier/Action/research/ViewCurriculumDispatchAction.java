package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearIntervalBean;
import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/viewCurriculum", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "Success", path = "/researcher/viewCurriculum.jsp", tileProperties = @Tile(
		title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.curriculum")) })
public class ViewCurriculumDispatchAction extends FenixAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String personId = request.getParameter("personOID");

		final Person person =
				((personId != null && personId.length() > 0) ? (Person) RootDomainObject.readDomainObjectByOID(Person.class,
						Integer.valueOf(personId)) : getLoggedPerson(request));

		request.setAttribute("person", person);

		ExecutionYearIntervalBean bean = retrieveExecutionYearBeanFromRequest(request);

		ExecutionYear firstExecutionYear = bean.getFirstExecutionYear();
		ExecutionYear finalExecutionYear = bean.getFinalExecutionYear();

		ExecutionYear firstOfAll = ExecutionYear.readFirstExecutionYear();

		if (firstExecutionYear == null) {
			firstExecutionYear = firstOfAll;
		}
		if (finalExecutionYear == null || finalExecutionYear.isBefore(firstExecutionYear)) {
			finalExecutionYear = ExecutionYear.readLastExecutionYear();
		}

		if (person.getResearchResultPublications().size() > 100
				&& (finalExecutionYear.getBeginCivilYear() - firstExecutionYear.getBeginCivilYear()) > 5
				&& firstExecutionYear == firstOfAll) {
			firstExecutionYear =
					finalExecutionYear.getPreviousExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear()
							.getPreviousExecutionYear().getPreviousExecutionYear();
		}

		bean.setFinalExecutionYear(finalExecutionYear);
		bean.setFirstExecutionYear(firstExecutionYear);

		putInformationOnRequestForGivenExecutionYear(firstExecutionYear, finalExecutionYear, person, request);

		final List<ResearchInterest> researchInterests = person.getResearchInterests();
		request.setAttribute("researchInterests", researchInterests);

		return mapping.findForward("Success");
	}

	private ExecutionYearIntervalBean retrieveExecutionYearBeanFromRequest(HttpServletRequest request) {
		IViewState viewState = RenderUtils.getViewState("executionYearIntervalBean");
		ExecutionYearIntervalBean bean =
				(viewState != null) ? (ExecutionYearIntervalBean) viewState.getMetaObject().getObject() : new ExecutionYearIntervalBean();
		request.setAttribute("executionYearIntervalBean", bean);
		RenderUtils.invalidateViewState("executionYearIntervalBean");
		return bean;
	}

	private void putInformationOnRequestForGivenExecutionYear(ExecutionYear firstExecutionYear,
			ExecutionYear finaltExecutionYear, Person person, HttpServletRequest request) {

		Set<Advise> final_works = new HashSet<Advise>();
		Set<MasterDegreeThesisDataVersion> guidances = new HashSet<MasterDegreeThesisDataVersion>();
		SortedSet<ExecutionCourse> lectures =
				new TreeSet<ExecutionCourse>(new ReverseComparator(
						ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME));
		Set<Thesis> orientedThesis = new HashSet<Thesis>();
		Set<PersonFunction> functions = new HashSet<PersonFunction>();
		Set<ResearchResultPublication> books = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> localArticles = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> nationalArticles = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> internationalArticles = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> nationalInproceedings = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> internationalInproceedings = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> proceedings = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> thesis = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> manuals = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> technicalReports = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> otherPublication = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> unstructured = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> bookParts = new HashSet<ResearchResultPublication>();
		Set<ResearchResultPublication> resultPublications = new HashSet<ResearchResultPublication>();
		Set<Prize> prizes = new HashSet<Prize>();
		SortedSet<Career> career = new TreeSet<Career>(Career.CAREER_DATE_COMPARATOR);

		ExecutionYear stoppageYear = finaltExecutionYear.getNextExecutionYear();
		ExecutionYear iteratorYear = firstExecutionYear;
		Teacher teacher = person.getTeacher();

		while (iteratorYear != stoppageYear) {

			if (teacher != null) {
				final_works.addAll(teacher.getAdvisesByAdviseTypeAndExecutionYear(AdviseType.FINAL_WORK_DEGREE, iteratorYear));

				guidances.addAll(teacher.getGuidedMasterDegreeThesisByExecutionYear(iteratorYear));
				lectures.addAll(teacher.getLecturedExecutionCoursesByExecutionYear(iteratorYear));
			}

			orientedThesis.addAll(person.getOrientedOrCoorientedThesis(iteratorYear));

			functions.addAll(person.getPersonFuntions(iteratorYear.getBeginDateYearMonthDay(),
					iteratorYear.getEndDateYearMonthDay()));
			resultPublications.addAll(person.getResearchResultPublicationsByExecutionYear(iteratorYear));
			books.addAll(person.getBooks(iteratorYear));
			nationalArticles.addAll(person.getArticles(ScopeType.NATIONAL, iteratorYear));
			internationalArticles.addAll(person.getArticles(ScopeType.INTERNATIONAL, iteratorYear));
			nationalInproceedings.addAll(person.getInproceedings(ScopeType.NATIONAL, iteratorYear));
			internationalInproceedings.addAll(person.getInproceedings(ScopeType.INTERNATIONAL, iteratorYear));
			proceedings.addAll(person.getProceedings(iteratorYear));
			thesis.addAll(person.getTheses(iteratorYear));
			manuals.addAll(person.getManuals(iteratorYear));
			technicalReports.addAll(person.getTechnicalReports(iteratorYear));
			otherPublication.addAll(person.getOtherPublications(iteratorYear));
			unstructured.addAll(person.getUnstructureds(iteratorYear));
			bookParts.addAll(person.getInbooks(iteratorYear));
			prizes.addAll(person.getPrizes(iteratorYear));
			iteratorYear = iteratorYear.getNextExecutionYear();
		}

		career.addAll(person.getCareersByTypeAndInterval(CareerType.PROFESSIONAL, new Interval(firstExecutionYear
				.getBeginDateYearMonthDay().toDateTimeAtMidnight(), finaltExecutionYear.getEndDateYearMonthDay()
				.toDateTimeAtMidnight())));

		List<PersonFunction> functionsList = new ArrayList<PersonFunction>(functions);
		Collections.sort(functionsList, new ReverseComparator(new BeanComparator("beginDateInDateType")));
		request.setAttribute("functions", functionsList);
		List<Advise> final_worksList = new ArrayList<Advise>(final_works);
		Collections.sort(final_worksList, new BeanComparator("student.number"));

		request.setAttribute("final_works", final_worksList);
		request.setAttribute("guidances", guidances);
		request.setAttribute("lectures", lectures);
		request.setAttribute("orientedThesis", orientedThesis);
		if (!(guidances.isEmpty() && orientedThesis.isEmpty())) {
			request.setAttribute("secondCycleThesis", true);
		}
		request.setAttribute("resultPublications", resultPublications);
		request.setAttribute("books", ResearchResultPublication.sort(books));
		request.setAttribute("local-articles", ResearchResultPublication.sort(localArticles));
		request.setAttribute("national-articles", ResearchResultPublication.sort(nationalArticles));
		request.setAttribute("international-articles", ResearchResultPublication.sort(internationalArticles));
		request.setAttribute("national-inproceedings", ResearchResultPublication.sort(nationalInproceedings));
		request.setAttribute("international-inproceedings", ResearchResultPublication.sort(internationalInproceedings));
		request.setAttribute("proceedings", ResearchResultPublication.sort(proceedings));
		request.setAttribute("theses", ResearchResultPublication.sort(thesis));
		request.setAttribute("manuals", ResearchResultPublication.sort(manuals));
		request.setAttribute("technicalReports", ResearchResultPublication.sort(technicalReports));
		request.setAttribute("otherPublications", ResearchResultPublication.sort(otherPublication));
		request.setAttribute("unstructureds", unstructured);
		request.setAttribute("inbooks", ResearchResultPublication.sort(bookParts));
		request.setAttribute(
				"national-events",
				new ArrayList<ResearchEvent>(person.getAssociatedEvents(ScopeType.NATIONAL, firstExecutionYear,
						finaltExecutionYear)));
		request.setAttribute(
				"international-events",
				new ArrayList<ResearchEvent>(person.getAssociatedEvents(ScopeType.INTERNATIONAL, firstExecutionYear,
						finaltExecutionYear)));
		request.setAttribute(
				"international-eventEditions",
				new ArrayList<EventEdition>(person.getAssociatedEventEditions(ScopeType.INTERNATIONAL, firstExecutionYear,
						finaltExecutionYear)));
		request.setAttribute(
				"national-eventEditions",
				new ArrayList<EventEdition>(person.getAssociatedEventEditions(ScopeType.NATIONAL, firstExecutionYear,
						finaltExecutionYear)));
		request.setAttribute(
				"national-journals",
				new ArrayList<ScientificJournal>(person.getAssociatedScientificJournals(ScopeType.NATIONAL, firstExecutionYear,
						finaltExecutionYear)));
		request.setAttribute(
				"international-journals",
				new ArrayList<ScientificJournal>(person.getAssociatedScientificJournals(ScopeType.INTERNATIONAL,
						firstExecutionYear, finaltExecutionYear)));
		request.setAttribute("cooperations",
				new ArrayList<Cooperation>(person.getAssociatedCooperations(firstExecutionYear, finaltExecutionYear)));
		request.setAttribute(
				"national-issues",
				new ArrayList<JournalIssue>(person.getAssociatedJournalIssues(ScopeType.NATIONAL, firstExecutionYear,
						finaltExecutionYear)));
		request.setAttribute(
				"international-issues",
				new ArrayList<JournalIssue>(person.getAssociatedJournalIssues(ScopeType.INTERNATIONAL, firstExecutionYear,
						finaltExecutionYear)));
		request.setAttribute("participations", person.getParticipations());
		request.setAttribute("prizes", prizes);
		request.setAttribute("career", career);
	}
}
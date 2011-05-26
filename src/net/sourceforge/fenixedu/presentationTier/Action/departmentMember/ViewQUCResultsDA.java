package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CompetenceCourseResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentExecutionSemester;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentTeacherDetailsBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentTeacherResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentUCResultsBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeGroupsResumeResult;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public abstract class ViewQUCResultsDA extends FenixDispatchAction {

    public ActionForward resumeResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DepartmentUnit departmentUnit = getDepartmentUnit(request);
	ExecutionSemester executionSemester = getExecutionSemester(request);

	List<CompetenceCourseResultsResume> competenceCoursesToAudit = new ArrayList<CompetenceCourseResultsResume>();
	for (ScientificAreaUnit scientificAreaUnit : departmentUnit.getScientificAreaUnits()) {
	    for (CompetenceCourseGroupUnit competenceCourseGroupUnit : scientificAreaUnit.getCompetenceCourseGroupUnits()) {
		for (CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
		    CompetenceCourseResultsResume competenceCourseResultsResume = null;
		    for (ExecutionCourse executionCourse : competenceCourse
			    .getExecutionCoursesByExecutionPeriod(executionSemester)) {
			if (executionCourse.isAvailableForInquiries()) {
			    for (ExecutionDegree executionDegree : executionCourse.getExecutionDegrees()) {
				CurricularCourseResumeResult courseResumeResult = new CurricularCourseResumeResult(
					executionCourse, executionDegree, "label.inquiry.execution", executionDegree.getDegree()
						.getSigla(), AccessControl.getPerson(),
					ResultPersonCategory.DEPARTMENT_PRESIDENT, false, true, true, getShowAllComments(), true);
				if (courseResumeResult.getResultBlocks().size() > 1) {
				    if (executionCourse.getForAudit(executionDegree) != null) {
					if (competenceCourseResultsResume == null) {
					    competenceCourseResultsResume = new CompetenceCourseResultsResume(competenceCourse);
					    competenceCoursesToAudit.add(competenceCourseResultsResume);
					}
					competenceCourseResultsResume.addCurricularCourseResumeResult(courseResumeResult);
				    }
				}
			    }
			}
		    }
		}
	    }
	}
	Collections.sort(competenceCoursesToAudit, new BeanComparator("competenceCourse.name"));

	List<DepartmentTeacherResultsResume> teachersResumeToImprove = getDepartmentTeachersResume(departmentUnit,
		executionSemester, false, true);
	Collections.sort(teachersResumeToImprove, new BeanComparator("teacher.person.name"));

	request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
	request.setAttribute("teachersResumeToImprove", teachersResumeToImprove);
	request.setAttribute("nothingToImprove", competenceCoursesToAudit.isEmpty() && teachersResumeToImprove.isEmpty());
	return mapping.findForward("viewResumeResults");
    }

    protected abstract DepartmentUnit getDepartmentUnit(HttpServletRequest request);

    public ActionForward competenceResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DepartmentUnit departmentUnit = getDepartmentUnit(request);
	ExecutionSemester executionSemester = getExecutionSemester(request);

	Map<ScientificAreaUnit, List<CompetenceCourseResultsResume>> competenceCoursesByScientificArea = new TreeMap<ScientificAreaUnit, List<CompetenceCourseResultsResume>>(
		new BeanComparator("name"));
	for (ScientificAreaUnit scientificAreaUnit : departmentUnit.getScientificAreaUnits()) {
	    for (CompetenceCourseGroupUnit competenceCourseGroupUnit : scientificAreaUnit.getCompetenceCourseGroupUnits()) {
		for (CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
		    CompetenceCourseResultsResume competenceCourseResultsResume = null;
		    for (ExecutionCourse executionCourse : competenceCourse
			    .getExecutionCoursesByExecutionPeriod(executionSemester)) {
			if (executionCourse.isAvailableForInquiries()) {
			    for (ExecutionDegree executionDegree : executionCourse.getExecutionDegrees()) {
				CurricularCourseResumeResult courseResumeResult = new CurricularCourseResumeResult(
					executionCourse, executionDegree, "label.inquiry.execution", executionDegree.getDegree()
						.getSigla(), AccessControl.getPerson(),
					ResultPersonCategory.DEPARTMENT_PRESIDENT, false, true, false, getShowAllComments(), true);
				if (courseResumeResult.getResultBlocks().size() > 1) {
				    List<CompetenceCourseResultsResume> competenceCourses = competenceCoursesByScientificArea
					    .get(scientificAreaUnit);
				    if (competenceCourses == null) {
					competenceCourses = new ArrayList<CompetenceCourseResultsResume>();
					competenceCoursesByScientificArea.put(scientificAreaUnit, competenceCourses);
				    }
				    if (competenceCourseResultsResume == null) {
					competenceCourseResultsResume = new CompetenceCourseResultsResume(competenceCourse);
					competenceCourses.add(competenceCourseResultsResume);
				    }
				    competenceCourseResultsResume.addCurricularCourseResumeResult(courseResumeResult);
				}
			    }
			}
		    }
		}
	    }
	}

	for (ScientificAreaUnit scientificAreaUnit : competenceCoursesByScientificArea.keySet()) {
	    Collections.sort(competenceCoursesByScientificArea.get(scientificAreaUnit), new BeanComparator(
		    "competenceCourse.name"));
	}
	request.setAttribute("competenceCoursesByScientificArea", competenceCoursesByScientificArea);
	return mapping.findForward("viewCompetenceResults");
    }

    public ActionForward teachersResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DepartmentUnit departmentUnit = getDepartmentUnit(request);
	ExecutionSemester executionSemester = getExecutionSemester(request);

	List<DepartmentTeacherResultsResume> teachersResume = getDepartmentTeachersResume(departmentUnit, executionSemester,
		true, false);
	Collections.sort(teachersResume, new BeanComparator("teacher.person.name"));

	request.setAttribute("teachersResume", teachersResume);
	return mapping.findForward("viewTeachersResults");
    }

    public ActionForward showTeacherResultsAndComments(ActionMapping actionMapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionSemesterOID")
		.toString());
	Person person = AbstractDomainObject.fromExternalId(getFromRequest(request, "personOID").toString());

	DepartmentTeacherDetailsBean departmentTeacherDetailsBean = new DepartmentTeacherDetailsBean(person, executionSemester,
		AccessControl.getPerson(), Boolean.valueOf(request.getParameter("backToResume")));

	request.setAttribute("executionPeriod", executionSemester);
	request.setAttribute("departmentTeacherDetailsBean", departmentTeacherDetailsBean);
	request.setAttribute("showComment", getFromRequest(request, "showComment"));
	request.setAttribute("showAllComments", request.getParameter("showAllComments"));
	return actionMapping.findForward("departmentTeacherView");
    }

    public ActionForward showUCResultsAndComments(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionCourseOID")
		.toString());
	ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionDegreeOID")
		.toString());
	DepartmentUCResultsBean departmentUCResultsBean = new DepartmentUCResultsBean(executionCourse, executionDegree,
		AccessControl.getPerson(), Boolean.valueOf(request.getParameter("backToResume")));

	request.setAttribute("executionPeriod", executionCourse.getExecutionPeriod());
	request.setAttribute("executionCourse", executionCourse);
	request.setAttribute("departmentUCResultsBean", departmentUCResultsBean);
	request.setAttribute("showAllComments", request.getParameter("showAllComments"));
	return actionMapping.findForward("departmentUCView");
    }

    private List<DepartmentTeacherResultsResume> getDepartmentTeachersResume(DepartmentUnit departmentUnit,
	    ExecutionSemester executionSemester, boolean allTeachers, boolean backToResume) {
	List<DepartmentTeacherResultsResume> teachersResume = new ArrayList<DepartmentTeacherResultsResume>();
	for (Teacher teacher : departmentUnit.getDepartment().getAllTeachers(executionSemester.getBeginDateYearMonthDay(),
		executionSemester.getEndDateYearMonthDay())) {
	    DepartmentTeacherResultsResume departmentTeacherResultsResume = null;
	    for (Professorship professorship : teacher.getProfessorships(executionSemester)) {
		if (professorship.hasAnyInquiryResults()) {
		    if (allTeachers || professorship.hasResultsToImprove()) {
			List<InquiryResult> professorshipResults = professorship.getInquiryResults();
			if (!professorshipResults.isEmpty()) {
			    for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
				List<InquiryResult> teacherShiftResults = professorship.getInquiryResults(shiftType);
				if (!teacherShiftResults.isEmpty()) {
				    TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult = new TeacherShiftTypeGroupsResumeResult(
					    professorship, shiftType, ResultPersonCategory.TEACHER, "label.inquiry.shiftType",
					    RenderUtils.getEnumString(shiftType), false);

				    if (departmentTeacherResultsResume == null) {
					departmentTeacherResultsResume = new DepartmentTeacherResultsResume(teacher,
						AccessControl.getPerson(), ResultPersonCategory.DEPARTMENT_PRESIDENT,
						backToResume, getShowAllComments());
					teachersResume.add(departmentTeacherResultsResume);
				    }
				    departmentTeacherResultsResume
					    .addTeacherShiftTypeGroupsResumeResult(teacherShiftTypeGroupsResumeResult);
				}
			    }
			}
		    }
		}
	    }
	}
	return teachersResume;
    }

    public abstract boolean getShowAllComments();

    private ExecutionSemester getExecutionSemester(HttpServletRequest request) {
	DepartmentExecutionSemester departmentExecutionSemester = getRenderedObject("executionSemesterBean");
	ExecutionSemester executionSemester = null;
	if (departmentExecutionSemester != null) {
	    executionSemester = departmentExecutionSemester.getExecutionSemester();
	} else {
	    String executionSemesterOID = request.getParameter("executionSemesterOID");
	    String departmentUnitOID = request.getParameter("departmentUnitOID");
	    if (StringUtils.isEmpty(executionSemesterOID)) {
		executionSemester = ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod();
	    } else {
		executionSemester = AbstractDomainObject.fromExternalId(executionSemesterOID);
	    }
	    departmentExecutionSemester = new DepartmentExecutionSemester();
	    departmentExecutionSemester.setExecutionSemester(executionSemester);
	    departmentExecutionSemester.setDepartmentUnitOID(departmentUnitOID);
	}
	request.setAttribute("executionSemesterBean", departmentExecutionSemester);
	request.setAttribute("executionSemester", executionSemester);
	return executionSemester;
    }

    private Set<ShiftType> getShiftTypes(List<InquiryResult> professorshipResults) {
	Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
	for (InquiryResult inquiryResult : professorshipResults) {
	    shiftTypes.add(inquiryResult.getShiftType());
	}
	return shiftTypes;
    }

    public static class ExecutionSemesterQucProvider implements DataProvider {
	@Override
	public Object provide(Object source, Object currentValue) {
	    List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();
	    ExecutionSemester oldQucExecutionSemester = ExecutionSemester.readBySemesterAndExecutionYear(2, "2009/2010");
	    for (ExecutionSemester executionSemester : RootDomainObject.getInstance().getExecutionPeriods()) {
		if (executionSemester.isAfter(oldQucExecutionSemester)) {
		    executionSemesters.add(executionSemester);
		}
	    }
	    Collections.sort(executionSemesters, new ReverseComparator());
	    return executionSemesters;
	}

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}
    }
}

package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.TutorateBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor.ViewStudentsPerformanceGridDispatchAction;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.excel.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.excel.WorkbookExportFormat;

@Mapping(path = "/tutorStudentsPerformanceGrid", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "searchTutors", path = "/pedagogicalCouncil/tutorship/showStudentsPerformanceGrid.jsp"),
	@Forward(name = "viewStudentsPerformanceGrid", path = "/pedagogicalCouncil/tutorship/showStudentsPerformanceGrid.jsp") })
public class TutorStudentsPerformanceGridDA extends ViewStudentsPerformanceGridDispatchAction {

    public static class TutorSearchBean implements Serializable {
	private static final long serialVersionUID = 161580136110904806L;

	private DomainReference<Department> department;

	private DomainReference<Teacher> teacher;

	private Boolean onlyTutors = Boolean.TRUE;

	public Department getDepartment() {
	    return department == null ? null : department.getObject();
	}

	public void setDepartment(Department department) {
	    this.department = department == null ? null : new DomainReference<Department>(department);
	}

	public Teacher getTeacher() {
	    return teacher == null ? null : teacher.getObject();
	}

	public void setTeacher(Teacher teacher) {
	    this.teacher = teacher == null ? null : new DomainReference<Teacher>(teacher);
	}

	public Boolean getOnlyTutors() {
	    return onlyTutors;
	}

	public void setOnlyTutors(Boolean onlyTutors) {
	    this.onlyTutors = onlyTutors;
	}

	public void setOnlyTutors(String onlyTutors) {
	    this.onlyTutors = Boolean.valueOf(onlyTutors);
	}
    }

    public static class DepartmentTeachersProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object current) {
	    TutorSearchBean bean = (TutorSearchBean) source;
	    Set<Teacher> teachers = new HashSet<Teacher>();
	    if (bean.getDepartment() != null) {
		for (Employee employee : bean.getDepartment().getAllCurrentActiveWorkingEmployees()) {
		    if (employee.getPerson().getTeacher() != null) {
			if (!bean.getOnlyTutors() || employee.getPerson().getTeacher().hasAnyTutorships()) {
			    teachers.add(employee.getPerson().getTeacher());
			}
		    }
		}
	    } else {
		for (Teacher teacher : rootDomainObject.getTeachersSet()) {
		    if (!bean.getOnlyTutors() || teacher.hasAnyTutorships()) {
			teachers.add(teacher);
		    }
		}
	    }
	    return teachers;
	}
    }

    public ActionForward prepareTutorSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("tutorateBean", new TutorSearchBean());
	return mapping.findForward("searchTutors");
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TutorSearchBean bean = (TutorSearchBean) getRenderedObject("tutorateBean");
	RenderUtils.invalidateViewState();
	request.setAttribute("tutorateBean", bean);
	if (bean.getTeacher() != null) {
	    Person person = bean.getTeacher().getPerson();
	    generateStudentsPerformanceBean(request, person);
	    request.setAttribute("tutor", person);
	    return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
	} else {
	    return mapping.findForward("searchTutors");
	}
    }

    public ActionForward exportXls(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String tutorId = request.getParameter("tutorOID");
	Person person = (Person) rootDomainObject.readPartyByOID(Integer.parseInt(tutorId));
	request.setAttribute("tutorateBean", new TutorateBean(person));
	generateStudentsPerformanceBeanFromRequest(request, person);
	prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
	PerformanceGridTableDTO performanceGridTable = (PerformanceGridTableDTO) request.getAttribute("performanceGridTable");

	SpreadsheetBuilder<PerformanceGridLine> builder = new PerformanceGridSheetBuilder(performanceGridTable);
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=" + person.getTeacher().getTeacherNumber()
		+ "-students-performance.xls");
	builder.build(WorkbookExportFormat.EXCEL, response.getOutputStream());
	response.flushBuffer();
	return null;
    }

    @Override
    public ActionForward prepareAllStudentsStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String tutorId = request.getParameter("tutorOID");
	Person person = (Person) rootDomainObject.readPartyByOID(Integer.parseInt(tutorId));
	request.setAttribute("tutorateBean", new TutorateBean(person));
	StudentsPerformanceInfoBean bean = generateStudentsPerformanceBeanFromRequest(request, person);
	if (!bean.getTutorships().isEmpty()) {

	    List<DegreeCurricularPlan> plans = new ArrayList<DegreeCurricularPlan>(bean.getDegree().getDegreeCurricularPlans());
	    Collections.sort(plans,
		    DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

	    List<StudentCurricularPlan> students = plans.get(0).getStudentsCurricularPlanGivenEntryYear(
		    bean.getStudentsEntryYear());

	    putAllStudentsStatisticsInTheRequest(request, students, bean.getCurrentMonitoringYear());

	    request.setAttribute("entryYear", bean.getStudentsEntryYear());
	    request.setAttribute("totalEntryStudents", students.size());
	}
	return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
    }
}

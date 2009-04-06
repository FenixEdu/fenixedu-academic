package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.TutorateBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor.ViewStudentsPerformanceGridDispatchAction;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.excel.SpreadsheetBuilder;

@Mapping(path = "/tutorStudentsPerformanceGrid", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "searchTutors", path = "/pedagogicalCouncil/tutorship/showStudentsPerformanceGrid.jsp"),
	@Forward(name = "viewStudentsPerformanceGrid", path = "/pedagogicalCouncil/tutorship/showStudentsPerformanceGrid.jsp") })
public class TutorStudentsPerformanceGridDA extends ViewStudentsPerformanceGridDispatchAction {

    public ActionForward prepareTutorSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("tutorateBean", new TutorateBean());
	return mapping.findForward("searchTutors");
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TutorateBean bean = (TutorateBean) getRenderedObject("tutorateBean");
	Person person = Teacher.readByNumber(bean.getPersonNumber()).getPerson();
	generateStudentsPerformanceBean(request, person);
	request.setAttribute("tutor", person);
	request.setAttribute("tutorateBean", bean);
	RenderUtils.invalidateViewState();
	return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
    }

    public ActionForward exportXls(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String tutorId = request.getParameter("tutorOID");
	Person person = (Person) rootDomainObject.readPartyByOID(Integer.parseInt(tutorId));
	request.setAttribute("tutorateBean", new TutorateBean(person));
	generateStudentsPerformanceBeanFromRequest(request, person);
	prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
	PerformanceGridTableDTO performanceGridTable = (PerformanceGridTableDTO) request.getAttribute("performanceGridTable");

	HSSFWorkbook book = new HSSFWorkbook();
	SpreadsheetBuilder<PerformanceGridLine> builder = new PerformanceGridSheetBuilder(book, performanceGridTable);
	builder.build(person.getTeacher().getTeacherNumber() + "-students-performance", performanceGridTable
		.getPerformanceGridTableLines());
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=" + person.getTeacher().getTeacherNumber()
		+ "-students-performance.xls");
	final ServletOutputStream writer = response.getOutputStream();
	book.write(writer);
	writer.flush();
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

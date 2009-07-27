package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.TutorateBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor.ViewStudentsPerformanceGridDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.excel.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.excel.WorkbookExportFormat;

@Mapping(path = "/tutorStudentsPerformanceGrid", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "viewStudentsPerformanceGrid", path = "/pedagogicalCouncil/tutorship/showStudentsPerformanceGrid.jsp") })
public class TutorStudentsPerformanceGridDA extends ViewStudentsPerformanceGridDispatchAction {

    public ActionForward prepareTutorSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("tutorateBean", new TutorSearchBean());
	return mapping.findForward("viewStudentsPerformanceGrid");
    }

    public ActionForward viewStudentsPerformanceGrid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
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
	    return mapping.findForward("viewStudentsPerformanceGrid");
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

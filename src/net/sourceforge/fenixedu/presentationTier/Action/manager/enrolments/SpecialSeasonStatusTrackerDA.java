package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult.ExternalSupervisorViewsBean;

@Mapping(path = "/specialSeason/specialSeasonStatusTracker", module = "manager")
@Forwards( { @Forward(name = "selectCourse", path = "/manager/specialSeason/selectCourse.jsp"),
	@Forward(name = "listStudents", path = "/manager/specialSeason/listStudents.jsp") })
public class SpecialSeasonStatusTrackerDA extends FenixDispatchAction {
    
    public ActionForward selectCourses (ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SpecialSeasonStatusTrackerBean bean = getRenderedObject();
	if(bean == null) {
	   bean = new SpecialSeasonStatusTrackerBean();
	}
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState();
	return mapping.findForward("selectCourse");
    }
    
    public ActionForward updateDepartmentSelection (ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SpecialSeasonStatusTrackerBean bean = getRenderedObject();
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState();
	return mapping.findForward("selectCourse");
    }
    
    public ActionForward listStudents (ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final SpecialSeasonStatusTrackerBean bean = getRenderedObject();
	List<Registration> registrations = new ArrayList<Registration>();
	List<CompetenceCourse> courses = new ArrayList<CompetenceCourse>();
	if(bean.getCompetenceCourse() == null) {
	    courses.addAll(bean.getDepartment().getBolonhaCompetenceCourses());
	} else {
	    courses.add(bean.getCompetenceCourse());
	}
	for(CompetenceCourse competence : courses) {
	    for(CurricularCourse course : competence.getAssociatedCurricularCourses()) {
		for(Enrolment enrolment : course.getActiveEnrollments(bean.getExecutionSemester())) {
		    if(enrolment.isSpecialSeason()) {
			registrations.add(enrolment.getRegistration());
		    }
		}
	    }   
	}
	bean.setRegistrations(registrations);
	request.setAttribute("bean", bean);
	request.setAttribute("totalStudents", bean.getRegistrations().size());
	RenderUtils.invalidateViewState();
	return mapping.findForward("listStudents");
    }
    
    public ActionForward exportXLS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
	SpecialSeasonStatusTrackerBean bean = getRenderedObject();
	final Spreadsheet spreadsheet = generateSpreadsheet(bean);

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=" + getFilename(bean) + ".xls");
	spreadsheet.exportToXLSSheet(response.getOutputStream());
	response.getOutputStream().flush();
	response.flushBuffer();
	return null;
    }
    
    private String getFilename(SpecialSeasonStatusTrackerBean bean) {
	StringBuilder strBuilder = new StringBuilder();
	//strBuilder.append(ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale()).getString(
	//	"special.season"));
	strBuilder.append("EpocaEspecial");
	strBuilder.append("_");
	strBuilder.append((bean.getCompetenceCourse() != null ? bean.getCompetenceCourse().getAcronym() : ""));
	strBuilder.append("_");
	strBuilder.append(bean.getExecutionSemester().getSemester());
	strBuilder.append("_");
	strBuilder.append(bean.getExecutionSemester().getExecutionYear().getName());
	return strBuilder.toString();
    }
    
    private Spreadsheet generateSpreadsheet(SpecialSeasonStatusTrackerBean bean) {
	final Spreadsheet spreadsheet = createSpreadSheet();
	for (final Registration registration : bean.getRegistrations()) {
	    final Row row = spreadsheet.addRow();

	    row.setCell(registration.getPerson().getUsername());
	    row.setCell(registration.getNumber());
	    row.setCell(registration.getPerson().getName());
	    row.setCell(registration.getPerson().getInstitutionalOrDefaultEmailAddressValue());
	    row.setCell(registration.getDegree().getSigla());
	    row.setCell(registration.getStudentCurricularPlan(bean.getExecutionSemester()).getName());
	}

	return spreadsheet;
    }
    
    private Spreadsheet createSpreadSheet() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
	final Spreadsheet spreadsheet = new Spreadsheet(bundle.getString("list.students"));

	spreadsheet.setHeaders(new String[] {
		
	bundle.getString("label.istid"),

	bundle.getString("label.number"),

	bundle.getString("label.name"),

	bundle.getString("label.email"),
	
	bundle.getString("label.Degree"),
	
	bundle.getString("label.curricularPlan"),

	" ", " " });

	return spreadsheet;
    }

}

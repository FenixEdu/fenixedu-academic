package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

@Mapping(module = "scientificCouncil", path = "/exportCredits", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "exportDepartmentCourses", path = "/credits/export/exportDepartmentCourses.jsp") })
public class CreditsReportsDA extends FenixDispatchAction {

    public ActionForward prepareExportDepartmentCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
	DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
	departmentCreditsBean.setAvailableDepartments(new ArrayList<Department>(rootDomainObject.getDepartments()));
	request.setAttribute("departmentCreditsBean", departmentCreditsBean);
	return mapping.findForward("exportDepartmentCourses");
    }

    public ActionForward exportDepartmentCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException, IOException {
	DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
	List<Department> departments = new ArrayList<Department>();
	if (departmentCreditsBean.getDepartment() != null) {
	    departments.add(departmentCreditsBean.getDepartment());
	} else {
	    departments.addAll(departmentCreditsBean.getAvailableDepartments());
	}
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet();
	for (Department department : departments) {
	    String sheetName = "Disciplinas_" + department.getAcronym();
	    spreadsheet.getSheet(sheetName);
	    spreadsheet.newHeaderRow();
	    spreadsheet.addHeader("Disciplina", 5000);
	    spreadsheet.addHeader("Cursos");
	    spreadsheet.addHeader("Tipo");
	    spreadsheet.addHeader("Tem Horário");
	    for (CompetenceCourse competenceCourse : department.getDepartmentUnit().getCompetenceCourses()) {
		for (ExecutionCourse executionCourse : competenceCourse
			.getExecutionCoursesByExecutionPeriod(departmentCreditsBean.getExecutionSemester())) {
		    spreadsheet.newRow();
		    spreadsheet.addCell(executionCourse.getName());
		    spreadsheet.addCell(executionCourse.getDegreePresentationString());
		    spreadsheet.addCell(executionCourse.isDissertation() ? "DISS"
			    : executionCourse.getProjectTutorialCourse() ? "A" : "B");
		    spreadsheet.addCell(executionCourse.hasAnyLesson() ? "S" : "N");
		}
	    }
	}

	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=Disciplinas.xls");
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }
}
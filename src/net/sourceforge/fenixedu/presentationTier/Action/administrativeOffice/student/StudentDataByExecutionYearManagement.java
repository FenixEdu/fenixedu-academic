package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentDataByExecutionYearManagement extends FenixDispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Student student = getStudent(request);
	request.setAttribute("student", student);
	request.setAttribute("sortedStudentDataByExecutionYear", getSortedStudentDataByExecutionYear(student));
	return mapping.findForward("showStudentData");
    }

    private List<StudentDataByExecutionYear> getSortedStudentDataByExecutionYear(final Student student) {
	final List<StudentDataByExecutionYear> result = new ArrayList<StudentDataByExecutionYear>(student
		.getStudentDataByExecutionYear());
	Collections.sort(result, new ReverseComparator(StudentDataByExecutionYear.COMPARATOR_BY_EXECUTION_YEAR));
	return result;
    }

    private Student getStudent(final HttpServletRequest request) {
	return rootDomainObject.readStudentByOID(getIntegerFromRequest(request, "studentId"));
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("createBean", new StudentDataByExecutionYearBean(getStudent(request)));
	return show(mapping, actionForm, request, response);
    }

    public ActionForward prepareCreateInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("createBean", getRenderedObject());
	return show(mapping, actionForm, request, response);
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final StudentDataByExecutionYearBean studentDataByExecutionYearBean = (StudentDataByExecutionYearBean) getRenderedObject();
	try {
	    executeService("CreateStudentDataByExecutionYear", new Object[] { studentDataByExecutionYearBean });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getKey());
	    request.setAttribute("createBean", studentDataByExecutionYearBean);
	}
	return show(mapping, actionForm, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeService("DeleteStudentDataByExecutionYear", new Object[] { getStudentDataByExecutionYear(request) });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}
	return show(mapping, actionForm, request, response);
    }

    private StudentDataByExecutionYear getStudentDataByExecutionYear(final HttpServletRequest request) {
	return getStudentDataByExecutionYear(getStudent(request), request);
    }

    private StudentDataByExecutionYear getStudentDataByExecutionYear(final Student student, final HttpServletRequest request) {
	final Integer studentDataId = getIntegerFromRequest(request, "studentDataId");
	for (final StudentDataByExecutionYear data : student.getStudentDataByExecutionYearSet()) {
	    if (data.getIdInternal().equals(studentDataId)) {
		return data;
	    }
	}
	return null;
    }
}

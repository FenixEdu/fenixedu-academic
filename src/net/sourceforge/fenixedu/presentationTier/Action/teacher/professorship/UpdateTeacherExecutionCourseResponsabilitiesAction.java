/*
 * Created on Dec 16, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.UpdateProfessorshipWithPerson;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author jpvl
 */
@Mapping(module = "departmentAdmOffice", path = "/updateTeacherExecutionYearExecutionCourseResponsabilities",
        input = "show-teacher-professorships-for-management", attribute = "teacherExecutionCourseResponsabilities",
        formBean = "teacherExecutionCourseResponsabilities", scope = "request")
@Forwards(value = { @Forward(name = "successfull-update", path = "/showTeacherProfessorshipsForManagement.do") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory.class,
                        key = "message.professorship.invalidCategory", handler = org.apache.struts.action.ExceptionHandler.class,
                        path = "/showTeacherProfessorshipsForManagement.do", scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed.class,
                        key = "message.professorship.numberOfResponsibleForExceeded",
                        handler = net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship.exception.handler.MaxResponsibleForExceedHandler.class,
                        path = "/showTeacherProfessorshipsForManagement.do", scope = "request") })
public class UpdateTeacherExecutionCourseResponsabilitiesAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm teacherExecutionYearResponsabilitiesForm = (DynaActionForm) form;
        Integer[] executionCourseResponsabilities =
                (Integer[]) teacherExecutionYearResponsabilitiesForm.get("executionCourseResponsability");

        String teacherId = (String) teacherExecutionYearResponsabilitiesForm.get("teacherName");
        Integer executionYearId = (Integer) teacherExecutionYearResponsabilitiesForm.get("executionYearId");
        Person person = Person.readPersonByIstUsername(teacherId);
        ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(executionYearId);

        UpdateProfessorshipWithPerson.run(person, executionYear, Arrays.asList(executionCourseResponsabilities));

        return mapping.findForward("successfull-update");
    }
}
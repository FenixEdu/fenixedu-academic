/*
 * Created on Dec 11, 2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteProfessorship.ExistingAssociatedCredits;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.RemoveProfessorshipWithPerson;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.TeacherSearchForExecutionCourseAssociation;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ExceptionHandler;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author jpvl
 */
@Mapping(module = "departmentAdmOffice", path = "/removeProfessorship", input = "/showTeacherProfessorshipsForManagement.do",
        formBean = "teacherExecutionCourseForm", validate = false,
        functionality = TeacherSearchForExecutionCourseAssociation.class)
@Forwards({ @Forward(name = "successfull-delete", path = "/departmentAdmOffice/showTeacherProfessorshipsForManagement.do") })
@Exceptions(value = {
        @ExceptionHandling(type = notAuthorizedServiceDeleteException.class, key = "message.professorship.isResponsibleFor",
                handler = ExceptionHandler.class, path = "/showTeacherProfessorshipsForManagement.do", scope = "request"),
        @ExceptionHandling(type = ExistingAssociatedCredits.class, key = "message.existing.associatedCredits",
                handler = ExceptionHandler.class, path = "/showTeacherProfessorshipsForManagement.do", scope = "request") })
public class RemoveProfessorshipAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;

        String id = (String) teacherExecutionCourseForm.get("teacherId");
        String executionCourseId = (String) teacherExecutionCourseForm.get("executionCourseId");

        ActionMessages actionMessages = getMessages(request);
        try {
            RemoveProfessorshipWithPerson.run(Person.readPersonByUsername(id),
                    FenixFramework.<ExecutionCourse> getDomainObject(executionCourseId));
        } catch (DomainException de) {
            actionMessages.add(de.getMessage(), new ActionMessage(de.getMessage()));
            saveMessages(request, actionMessages);
        }
        return mapping.findForward("successfull-delete");
    }
}
/*
 * Created on Dec 11, 2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.RemoveProfessorshipWithPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author jpvl
 */
@Mapping(module = "departmentAdmOffice", path = "/removeProfessorship", input = "/showTeacherProfessorshipsForManagement.do",
        attribute = "teacherExecutionCourseForm", formBean = "teacherExecutionCourseForm", scope = "request", validate = false,
        parameter = "method")
@Forwards(value = { @Forward(name = "successfull-delete", path = "/showTeacherProfessorshipsForManagement.do") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException.class,
                        key = "message.professorship.isResponsibleFor",
                        handler = org.apache.struts.action.ExceptionHandler.class,
                        path = "/showTeacherProfessorshipsForManagement.do", scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteProfessorship.ExistingAssociatedCredits.class,
                        key = "message.existing.associatedCredits", handler = org.apache.struts.action.ExceptionHandler.class,
                        path = "/showTeacherProfessorshipsForManagement.do", scope = "request") })
public class RemoveProfessorshipAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;

        String id = (String) teacherExecutionCourseForm.get("teacherId");
        Integer executionCourseId = Integer.valueOf((String) teacherExecutionCourseForm.get("executionCourseId"));

        ActionMessages actionMessages = getMessages(request);
        try {
            RemoveProfessorshipWithPerson.run(Person.readPersonByIstUsername(id), RootDomainObject.getInstance()
                    .readExecutionCourseByOID(executionCourseId));
        } catch (DomainException de) {
            actionMessages.add(de.getMessage(), new ActionMessage(de.getMessage()));
            saveMessages(request, actionMessages);
        }
        return mapping.findForward("successfull-delete");
    }
}
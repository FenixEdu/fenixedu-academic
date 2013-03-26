/**
 *  Apr 21, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "scientificCouncil", path = "/otherServiceManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "editOtherService", path = "/credits/otherService/editOtherService.jsp"),
        @Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class,
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
public class ManageOtherServiceDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEditOtherService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
        OtherService otherService = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "otherServiceOid"));
        if (otherService != null) {
            request.setAttribute("otherService", otherService);
        } else {
            ExecutionSemester executionSemester =
                    AbstractDomainObject.fromExternalId((String) getFromRequest(request, "executionPeriodOid"));
            Teacher teacher = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherId"));
            TeacherService teacherService = TeacherService.getTeacherService(teacher, executionSemester);
            request.setAttribute("teacherService", teacherService);
        }
        return mapping.findForward("editOtherService");
    }

    public ActionForward deleteOtherService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
        OtherService otherService = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "otherServiceOid"));
        request.setAttribute("teacherOid", otherService.getTeacherService().getTeacher().getExternalId());
        request.setAttribute("executionYearOid", otherService.getTeacherService().getExecutionPeriod().getExecutionYear()
                .getExternalId());
        otherService.delete();
        return mapping.findForward("viewAnnualTeachingCredits");
    }
}
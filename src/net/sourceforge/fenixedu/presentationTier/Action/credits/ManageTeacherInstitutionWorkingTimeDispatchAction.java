/**
 * Nov 23, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;

/**
 * @author Ricardo Rodrigues
 * 
 */

@Forwards(value = { @Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits"),
	@Forward(name = "edit-institution-work-time", path = "/credits/workingTime/editTeacherInstitutionWorkTime.jsp") })
public class ManageTeacherInstitutionWorkingTimeDispatchAction extends FenixDispatchAction {

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws NumberFormatException, FenixFilterException, FenixServiceException {
	Teacher teacher = DomainObject.fromExternalId((String) getFromRequest(request, "teacherId"));
	ExecutionSemester executionPeriod = DomainObject.fromExternalId((String) getFromRequest(request, "executionPeriodId"));
	TeacherService teacherService = TeacherService.getTeacherService(teacher, executionPeriod);
	request.setAttribute("teacherService", teacherService);
	return mapping.findForward("edit-institution-work-time");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
	InstitutionWorkTime institutionWorkTime = DomainObject.fromExternalId((String) getFromRequest(request,
		"institutionWorkTimeOid"));
	request.setAttribute("institutionWorkTime", institutionWorkTime);
	return mapping.findForward("edit-institution-work-time");
    }

    protected ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, RoleType roleType) throws NumberFormatException, FenixFilterException,
	    FenixServiceException {
	InstitutionWorkTime institutionWorkTime = DomainObject.fromExternalId((String) getFromRequest(request,
		"institutionWorkTimeOid"));
	request.setAttribute("teacherOid", institutionWorkTime.getTeacherService().getTeacher().getExternalId());
	request.setAttribute("executionYearOid", institutionWorkTime.getTeacherService().getExecutionPeriod().getExecutionYear()
		.getExternalId());
	institutionWorkTime.delete(roleType);
	return mapping.findForward("viewAnnualTeachingCredits");
    }

}

package net.sourceforge.fenixedu.presentationTier.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ReductionService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ManageCreditsReductionsDispatchAction extends FenixDispatchAction {

    public ActionForward editCreditsReduction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId((String) getFromRequest(request,
		"executionPeriodOID"));
	Teacher teacher = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherOID"));
	TeacherService teacherService = getTeacherService(teacher, executionSemester);
	ReductionService reductionService = teacherService.getReductionService();
	if (reductionService != null) {
	    request.setAttribute("reductionService", reductionService);
	} else {
	    request.setAttribute("teacherService", teacherService);
	}
	return mapping.findForward("editReductionService");
    }

    @Service
    private TeacherService getTeacherService(Teacher teacher, ExecutionSemester executionSemester) {
	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	if (teacherService == null) {
	    teacherService = new TeacherService(teacher, executionSemester);
	}
	return teacherService;
    }
}

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

import pt.ist.fenixframework.FenixFramework;

public class ManageCreditsReductionsDispatchAction extends FenixDispatchAction {

    public ActionForward editCreditsReduction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionSemester executionSemester =
                FenixFramework.getDomainObject((String) getFromRequest(request, "executionPeriodOID"));
        Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherOID"));
        TeacherService teacherService = TeacherService.getTeacherService(teacher, executionSemester);
        ReductionService reductionService = teacherService.getReductionService();
        if (reductionService != null) {
            request.setAttribute("reductionService", reductionService);
        } else {
            request.setAttribute("teacherService", teacherService);
        }
        return mapping.findForward("editReductionService");
    }
}

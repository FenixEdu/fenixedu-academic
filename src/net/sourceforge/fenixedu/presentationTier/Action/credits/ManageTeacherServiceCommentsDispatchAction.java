package net.sourceforge.fenixedu.presentationTier.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceComment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ManageTeacherServiceCommentsDispatchAction extends FenixDispatchAction {

	public ActionForward editTeacherServiceComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

		TeacherServiceComment teacherServiceComment =
				AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherServiceCommentOid"));
		if (teacherServiceComment != null) {
			request.setAttribute("teacherServiceComment", teacherServiceComment);
		} else {
			Teacher teacher = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherOid"));
			ExecutionYear executionYear =
					AbstractDomainObject.fromExternalId((String) getFromRequest(request, "executionYearOid"));

			ExecutionSemester firstExecutionPeriod = executionYear.getFirstExecutionPeriod();
			TeacherService teacherService = TeacherService.getTeacherService(teacher, firstExecutionPeriod);

			request.setAttribute("teacherService", teacherService);
		}
		return mapping.findForward("editTeacherServiceComment");
	}

	public ActionForward deleteTeacherServiceComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

		TeacherServiceComment teacherServiceComment =
				AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherServiceCommentOid"));
		request.setAttribute("teacherOid", teacherServiceComment.getTeacherService().getTeacher().getExternalId());
		request.setAttribute("executionYearOid", teacherServiceComment.getTeacherService().getExecutionPeriod()
				.getExecutionYear().getExternalId());
		if (teacherServiceComment != null) {
			teacherServiceComment.delete();
		}
		return mapping.findForward("viewAnnualTeachingCredits");
	}
}

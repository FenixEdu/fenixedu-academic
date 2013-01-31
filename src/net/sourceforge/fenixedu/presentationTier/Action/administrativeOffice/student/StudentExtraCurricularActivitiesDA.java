package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivity;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Pedro Santos (pedro.miguel.santos@ist.utl.pt)
 */
@Mapping(path = "/studentExtraCurricularActivities", module = "academicAdministration")
@Forwards({ @Forward(
		name = "manageActivities",
		path = "/academicAdminOffice/student/extraCurricularActivities/manageActivities.jsp") })
public class StudentExtraCurricularActivitiesDA extends FenixDispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Student student = DomainObject.fromExternalId(request.getParameter("studentId"));
		request.setAttribute("student", student);
		return mapping.findForward("manageActivities");
	}

	public ActionForward createActivity(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		ExtraCurricularActivity activity = getRenderedObject();
		RenderUtils.invalidateViewState();
		request.setAttribute("student", activity.getStudent());
		return mapping.findForward("manageActivities");
	}

	public ActionForward deleteActivity(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		ExtraCurricularActivity activity = DomainObject.fromExternalId(request.getParameter("activityId"));
		RenderUtils.invalidateViewState();
		final Student student = activity.getStudent();
		activity.delete();
		request.setAttribute("student", student);
		return mapping.findForward("manageActivities");
	}
}

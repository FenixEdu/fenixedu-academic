package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorSummaryBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/tutorshipSummary", module = "teacher")
@Forwards(tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp"), value = {
		@Forward(name = "searchTeacher", path = "/teacher/tutor/tutorshipSummaries.jsp", tileProperties = @Tile(
				title = "private.teacher.managementmentoring.tutorform")),
		@Forward(name = "createSummary", path = "/pedagogicalCouncil/tutorship/createSummary.jsp"),
		@Forward(name = "editSummary", path = "/pedagogicalCouncil/tutorship/editSummary.jsp"),
		@Forward(name = "processCreateSummary", path = "/pedagogicalCouncil/tutorship/processCreateSummary.jsp"),
		@Forward(name = "confirmCreateSummary", path = "/pedagogicalCouncil/tutorship/confirmCreateSummary.jsp"),
		@Forward(name = "viewSummary", path = "/pedagogicalCouncil/tutorship/viewSummary.jsp") })
public class TutorshipSummaryDA extends net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipSummaryDA {

	@Override
	public ActionForward searchTeacher(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		TutorSummaryBean bean = new TutorSummaryBean();
		Teacher teacher = getUserView(request).getPerson().getTeacher();

		if (teacher != null) {
			bean.setTeacher(teacher);

			getTutorships(request, bean.getTeacher());

			request.setAttribute("tutor", bean.getTeacher());
		}

		request.setAttribute("tutorateBean", bean);

		return mapping.findForward("searchTeacher");

	}

}

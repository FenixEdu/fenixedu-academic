package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.commons.tutorship.ViewStudentsByTutorDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/viewStudentsByTutor", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewStudents", path = "/pedagogicalCouncil/tutorship/studentsByTutor.jsp", tileProperties = @Tile(title = "private.pedagogiccouncil.tutoring.viewtutoredstudents")) })
public class ViewStudentsByTutorWithSearchDispatchAction extends ViewStudentsByTutorDispatchAction {

    public ActionForward prepareTutorSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("tutorateBean", new TutorSearchBean());
	return mapping.findForward("viewStudents");
    }

    public ActionForward viewStudentsByTutor(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	TutorSearchBean bean = getRenderedObject("tutorateBean");
	RenderUtils.invalidateViewState();
	request.setAttribute("tutorateBean", bean);
	if (bean.getTeacher() != null) {
	    getTutorships(request, bean.getTeacher());
	    request.setAttribute("tutor", bean.getTeacher().getPerson());
	}
	return mapping.findForward("viewStudents");
    }
}

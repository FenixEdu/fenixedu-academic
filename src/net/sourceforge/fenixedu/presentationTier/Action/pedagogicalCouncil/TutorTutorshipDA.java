package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.TutorateBean;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor.ViewStudentsByTutorDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/tutorTutorship", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "searchTutors", path = "/pedagogicalCouncil/tutorship/showTutorPerformanceGrid.jsp"),
	@Forward(name = "showTutorPerformanceGrid", path = "/pedagogicalCouncil/tutorship/showTutorPerformanceGrid.jsp") })
public class TutorTutorshipDA extends ViewStudentsByTutorDispatchAction {

    public ActionForward prepareTutorSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("tutorateBean", new TutorateBean());
	return mapping.findForward("searchTutors");
    }

    @Override
    public ActionForward viewStudentsByTutor(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TutorateBean bean = (TutorateBean) getObjectFromViewState("tutorateBean");
	Teacher teacher = Teacher.readByNumber(bean.getPersonNumber());
	if (teacher != null) {
	    getTutorships(request, teacher);
	    request.setAttribute("tutor", teacher.getPerson());
	} else {
	    addActionMessage("error", request, "tutor.does.not.exist", bean.getPersonNumber().toString());
	}

	return mapping.findForward("showTutorPerformanceGrid");
    }

}

package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorSummaryBean;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherApplication.TeacherTutorApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = TeacherTutorApp.class, path = "summary", titleKey = "link.teacher.tutorship.summary")
@Mapping(path = "/tutorshipSummary", module = "teacher")
@Forwards({ @Forward(name = "searchTeacher", path = "/teacher/tutor/tutorshipSummaries.jsp"),
        @Forward(name = "createSummary", path = "/pedagogicalCouncil/tutorship/createSummary.jsp"),
        @Forward(name = "editSummary", path = "/pedagogicalCouncil/tutorship/editSummary.jsp"),
        @Forward(name = "processCreateSummary", path = "/pedagogicalCouncil/tutorship/processCreateSummary.jsp"),
        @Forward(name = "confirmCreateSummary", path = "/pedagogicalCouncil/tutorship/confirmCreateSummary.jsp"),
        @Forward(name = "viewSummary", path = "/pedagogicalCouncil/tutorship/viewSummary.jsp") })
public class TutorshipSummaryDA extends net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipSummaryDA {

    @Override
    @EntryPoint
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

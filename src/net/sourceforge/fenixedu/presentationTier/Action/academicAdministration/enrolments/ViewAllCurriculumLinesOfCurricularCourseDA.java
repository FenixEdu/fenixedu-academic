package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.enrolments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewAllCurriculumLinesOfCurricularCourse", module = "academicAdministration")
@Forwards({ @Forward(name = "viewCurriculumLinesOfCurricularCourse",
        path = "/academicAdministration/bolonha/enrolments/information/viewCurriculumLinesOfCurricularCourse.jsp") })
public class ViewAllCurriculumLinesOfCurricularCourseDA extends FenixDispatchAction {

    public ActionForward view(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        CurricularCourse curricularCourse = getDomainObject(request, "curricularCourseId");
        request.setAttribute("curricularCourse", curricularCourse);

        return mapping.findForward("viewCurriculumLinesOfCurricularCourse");
    }
}

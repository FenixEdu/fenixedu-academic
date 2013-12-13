package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadCurricularCoursesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadDegreeCurricularPlansByDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement.AssociateCurricularCoursesToCompetenceCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement.RemoveCurricularCoursesFromCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.bennu.core.security.Authenticate;

public class CurricularCourseCompetenceCourseDispatchAction extends FenixDispatchAction {

    public ActionForward removeFromCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = Authenticate.getUser();
        DynaActionForm actionForm = (DynaActionForm) form;

        String competenceCourseID = (String) actionForm.get("competenceCourseID");
        String[] curricularCoursesIDs = (String[]) actionForm.get("curricularCoursesIds");

        try {
            RemoveCurricularCoursesFromCompetenceCourse.run(competenceCourseID, curricularCoursesIDs);
        } catch (NotExistingServiceException notExistingServiceException) {
            throw new FenixActionException(notExistingServiceException.getMessage());
        }
        request.setAttribute("competenceCourseID", competenceCourseID);
        return mapping.findForward("showCompetenceCourse");
    }

    public ActionForward addToCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = Authenticate.getUser();
        DynaActionForm actionForm = (DynaActionForm) form;

        String competenceCourseID = (String) actionForm.get("competenceCourseID");
        String[] curricularCoursesIDs = (String[]) actionForm.get("curricularCoursesIds");

        try {
            AssociateCurricularCoursesToCompetenceCourse.run(competenceCourseID, curricularCoursesIDs);
        } catch (NotExistingServiceException notExistingServiceException) {
            throw new FenixActionException(notExistingServiceException.getMessage());
        }
        request.setAttribute("competenceCourseID", competenceCourseID);
        return mapping.findForward("showCompetenceCourse");
    }

    public ActionForward readDegrees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        User userView = Authenticate.getUser();
        String requestString = RequestUtils.getAndSetStringToRequest(request, "competenceCourseID");
        String competenceCourseID = (!StringUtils.isEmpty(requestString) ? requestString : null);
        DegreeType degreeType = DegreeType.DEGREE;

        List<InfoDegreeCurricularPlan> result = null;
        result = ReadDegreeCurricularPlansByDegreeType.run(degreeType);
        DynaActionForm actionForm = (DynaActionForm) form;
        actionForm.set("competenceCourseID", competenceCourseID);
        request.setAttribute("degreeCurricularPlans", result);
        return mapping.findForward("chooseDegreeCurricularPlan");
    }

    public ActionForward readCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = Authenticate.getUser();
        DynaActionForm actionForm = (DynaActionForm) form;
        String degreeCurricularPlanID = (String) actionForm.get("degreeCurricularPlanID");

        List<InfoCurricularCourse> result = null;
        try {
            result = ReadCurricularCoursesByDegreeCurricularPlan.run(degreeCurricularPlanID);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("curricularCourses", result);
        return mapping.findForward("chooseCurricularCourses");
    }
}
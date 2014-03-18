package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadCurricularCoursesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement.ReadStudentMarksListByCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings.ReadAllMasterDegrees;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings.ReadCPlanFromChosenMasterDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author Fernanda Quitério 17/Fev/2004
 * 
 */
@Mapping(path = "/marksManagement", module = "masterDegreeAdministrativeOffice",
        input = "/marksManagement/chooseCurricularCourseToManageMarks.jsp")
@Forwards(value = { @Forward(name = "showMasterDegrees", path = "/marksManagement/chooseMasterDegreeToManageMarks.jsp"),
        @Forward(name = "showDegreeCurricularPlans", path = "/marksManagement/chooseDegreeCurricularPlanToManageMarks.jsp"),
        @Forward(name = "showCurricularCourses", path = "/marksManagement/showCurricularCoursesToManageMarks.jsp"),
        @Forward(name = "NotAuthorized", path = "/student/notAuthorized.jsp"),
        @Forward(name = "showMarksManagementMenu", path = "/marksManagement/showMarksManagementMenu.jsp") })
public class MarksManagementDispatchAction extends FenixDispatchAction {
    public ActionForward prepareChooseMasterDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        List masterDegrees = null;
        User userView = Authenticate.getUser();
        DegreeType degreeType = DegreeType.MASTER_DEGREE;

        try {
            masterDegrees = ReadAllMasterDegrees.run(degreeType);
        } catch (NonExistingServiceException e) {
            errors.add("noMasterDegree", new ActionError("error.masterDegree.noDegrees"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        request.setAttribute(PresentationConstants.MASTER_DEGREE_LIST, masterDegrees);

        return mapping.findForward("showMasterDegrees");
    }

    public ActionForward prepareChooseDegreeCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        String masterDegreeId = getFromRequest("degreeId", request);

        List degreeCurricularPlans = null;
        User userView = Authenticate.getUser();

        try {

            degreeCurricularPlans = ReadCPlanFromChosenMasterDegree.run(masterDegreeId);

        } catch (NonExistingServiceException e) {
            errors.add("noDegreeCurricularPlan", new ActionError("error.masterDegree.noDegreeCurricularPlan"));
            saveErrors(request, errors);
            return prepareChooseMasterDegree(mapping, form, request, response);
        }

        Collections.sort(degreeCurricularPlans, new BeanComparator("name"));

        request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);

        return mapping.findForward("showDegreeCurricularPlans");
    }

    public ActionForward chooseCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String degreeCurricularPlanId = getFromRequest("objectCode", request);
        getFromRequest("degreeId", request);

        User userView = Authenticate.getUser();
        List curricularCourseList = null;
        try {
            curricularCourseList = ReadCurricularCoursesByDegreeCurricularPlan.run(degreeCurricularPlanId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(curricularCourseList, new BeanComparator("name"));
        request.setAttribute("curricularCourses", curricularCourseList);

        return mapping.findForward("showCurricularCourses");
    }

    public ActionForward getStudentMarksList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String curricularCourseId = getFromRequest("courseId", request);
        getFromRequest("objectCode", request);
        getFromRequest("degreeId", request);

        List listEnrolmentEvaluation = null;
        User userView = Authenticate.getUser();
        try {
            listEnrolmentEvaluation =
                    ReadStudentMarksListByCurricularCourse.runReadStudentMarksListByCurricularCourse(userView,
                            curricularCourseId, null);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        } catch (NonExistingServiceException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);
            return chooseCurricularCourses(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (listEnrolmentEvaluation.size() == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("StudentNotEnroled", new ActionError("error.students.Mark.NotAvailable"));
            saveErrors(request, actionErrors);
            return chooseCurricularCourses(mapping, form, request, response);
        }

        InfoEnrolment oneInfoEnrollment = (InfoEnrolment) listEnrolmentEvaluation.iterator().next();
        request.setAttribute("oneInfoEnrollment", oneInfoEnrollment);

        return mapping.findForward("showMarksManagementMenu");
    }

    public static String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        if (parameterString != null) {
            request.setAttribute(parameter, parameterString);
        }
        return parameterString;
    }

}
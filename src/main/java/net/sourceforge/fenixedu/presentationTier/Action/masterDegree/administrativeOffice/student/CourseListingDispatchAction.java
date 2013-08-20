package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentListByCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadCurricularCoursesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings.ReadAllMasterDegrees;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings.ReadCPlanFromChosenMasterDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *         This is the Action to display all the master degrees.
 * 
 */
public class CourseListingDispatchAction extends FenixDispatchAction {

    public ActionForward chooseDegreeFromList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DegreeType degreeType = DegreeType.MASTER_DEGREE;

        List result = null;
        try {
            result = ReadAllMasterDegrees.run(degreeType);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("O Degree de Mestrado", e);
        }

        request.setAttribute(PresentationConstants.MASTER_DEGREE_LIST, result);

        return mapping.findForward("DisplayMasterDegreeList");

    }

    public ActionForward chooseMasterDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // Get the Chosen Master Degree
        String masterDegreeID = request.getParameter("degreeID");
        if (masterDegreeID == null) {
            masterDegreeID = (String) request.getAttribute("degreeID");
        }

        List result = null;

        try {

            result = ReadCPlanFromChosenMasterDegree.run(masterDegreeID);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("O plano curricular ", e);
        }

        request.setAttribute(PresentationConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST, result);

        return mapping.findForward("MasterDegreeReady");

    }

    public ActionForward prepareList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String degreeCurricularPlanId = getFromRequest("curricularPlanID", request);

        List curricularCourseList = null;
        try {
            curricularCourseList = ReadCurricularCoursesByDegreeCurricularPlan.run(degreeCurricularPlanId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(curricularCourseList, new BeanComparator("name"));
        request.setAttribute("curricularCourses", curricularCourseList);

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward getStudentsFromCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = getUserView(request);

        // Get the Selected Course

        String scopeCodeString = request.getParameter("scopeCode");
        if (scopeCodeString == null) {
            scopeCodeString = (String) request.getAttribute("scopeCode");
        }

        String yearString = getFromRequest("executionYear", request);

        List enrolments = null;
        try {

            enrolments =
                    ReadStudentListByCurricularCourse.runReadStudentListByCurricularCourse(userView, scopeCodeString, yearString);

        } catch (NonExistingServiceException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("error.exception.noStudents", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);

            return prepareList(mapping, form, request, response);
        }

        BeanComparator numberComparator = new BeanComparator("infoStudentCurricularPlan.infoStudent.number");
        Collections.sort(enrolments, numberComparator);

        request.setAttribute(PresentationConstants.ENROLMENT_LIST, enrolments);

        return mapping.findForward("Success");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}
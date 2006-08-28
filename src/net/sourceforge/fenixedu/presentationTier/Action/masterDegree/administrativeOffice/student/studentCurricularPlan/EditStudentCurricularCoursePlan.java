package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Angela Created on 8/Out/2003
 */
public class EditStudentCurricularCoursePlan extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        DynaActionForm editStudentCurricularPlanForm = (DynaActionForm) form;
        Integer studentCurricularPlanId = new Integer(getFromRequest("studentCurricularPlanId", request));
        IUserView userView = getUserView(request);

        Object args[] = { studentCurricularPlanId };

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        try {
            infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView, "ReadPosGradStudentCurricularPlanById", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Object argsBranches[] = { infoStudentCurricularPlan.getInfoDegreeCurricularPlan()
                .getIdInternal() };
        List branchList = null;

        try {
            branchList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadBranchesByDegreeCurricularPlanId", argsBranches);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        // put request
        request.setAttribute(SessionConstants.BRANCH, (branchList == null) ? new ArrayList() : branchList);
        request.setAttribute("currentState", infoStudentCurricularPlan.getCurrentState());

        request.setAttribute("student", infoStudentCurricularPlan.getInfoStudent());
        request.setAttribute("studentCurricularPlan", infoStudentCurricularPlan);

        editStudentCurricularPlanForm.set("specialization", infoStudentCurricularPlan
                .getSpecialization().toString());

        if (infoStudentCurricularPlan.getInfoBranch() != null) {
            editStudentCurricularPlanForm.set("branch", infoStudentCurricularPlan.getInfoBranch()
                    .getIdInternal());
        }

        editStudentCurricularPlanForm.set("currentState", infoStudentCurricularPlan.getCurrentState()
                .toString());
        editStudentCurricularPlanForm.set("credits",
                (infoStudentCurricularPlan.getGivenCredits() == null) ? "0.0" : String
                        .valueOf(infoStudentCurricularPlan.getGivenCredits()));
        editStudentCurricularPlanForm
                .set("startDate", infoStudentCurricularPlan.getStartDateFormatted());
        String[] formValues = new String[infoStudentCurricularPlan.getInfoEnrolments().size()];
        int i = 0;
        for (Iterator iter = infoStudentCurricularPlan.getInfoEnrolments().iterator(); iter.hasNext();) {
            Object enrollment = iter.next();
            if (enrollment instanceof InfoEnrolmentInExtraCurricularCourse) {
                Integer enrollmentId = ((InfoEnrolmentInExtraCurricularCourse) enrollment)
                        .getIdInternal();
                formValues[i] = enrollmentId.toString();
            }
            i++;
        }
        DynaActionForm coursesForm = (DynaActionForm) form;
        coursesForm.set("extraCurricularCourses", formValues);
        return mapping.findForward("editStudentCurricularCoursePlan");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        DynaActionForm editStudentCurricularPlanForm = (DynaActionForm) form;

        String studentCurricularPlanIdString = request.getParameter("studentCurricularPlanId");
        String[] extraCurricularCoursesArray = (String[]) editStudentCurricularPlanForm
                .get("extraCurricularCourses");

        String currentState = (String) editStudentCurricularPlanForm.get("currentState");
        String specialization = (String) editStudentCurricularPlanForm.get("specialization");
        Integer branch = (Integer) editStudentCurricularPlanForm.get("branch");
        Object creditsObj = editStudentCurricularPlanForm.get("credits");
        Double credits = (creditsObj != null && ((String)creditsObj).length() > 0) ? (Double
                .valueOf((String) creditsObj)) : null;
        String startDate = (String) editStudentCurricularPlanForm.get("startDate");

        Integer studentCurricularPlanId = new Integer(studentCurricularPlanIdString);
        String observations = (String) editStudentCurricularPlanForm.get("observations");
        IUserView userView = getUserView(request);

        List extraCurricularCourses = new ArrayList();

        for (int i = 0; i < extraCurricularCoursesArray.length; i++) {
            extraCurricularCourses.add(new Integer(extraCurricularCoursesArray[i]));

        }
        Object args[] = { userView, studentCurricularPlanId, currentState, credits, startDate,
                extraCurricularCourses, observations, branch, specialization };

        try {
            ServiceManagerServiceFactory.executeService(userView,
                    "EditPosGradStudentCurricularPlanStateAndCredits", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("studentCurricularPlanId", studentCurricularPlanId);

        return mapping.findForward("ShowStudentCurricularCoursePlan");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }
}
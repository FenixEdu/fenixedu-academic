package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.withoutRules;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoAreas2Choose;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author David Santos in Apr 13, 2004
 */

public class ChangeStudentAreasDispatchAction extends DispatchAction {
    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("degreeType", DegreeType.DEGREE.toString());
        return mapping.findForward("chooseStudent");
    }

    public ActionForward showAndChooseStudentAreas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm changeStudentAreasForm = (DynaActionForm) form;

        Integer studentNumber = Integer.valueOf((String) changeStudentAreasForm.get("studentNumber"));

        String degreeTypeCode = (String) changeStudentAreasForm.get("degreeType");
        DegreeType degreeType = DegreeType.valueOf(degreeTypeCode);

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        InfoAreas2Choose infoBranches = null;

        Object[] args1 = { studentNumber, degreeType };
        try {
            List infoStudentCurricularPlans = (List) ServiceManagerServiceFactory.executeService(
                    userView, "ReadStudentCurricularPlansByNumberAndDegreeType", args1);

            infoStudentCurricularPlan = getActiveInfoStudentCurricularPlan(infoStudentCurricularPlans);
        } catch (Exception e) {
            setAcurateErrorMessage(request, e, errors, studentNumber, "chooseStudent");
        }

        Object[] args2 = { null, null, studentNumber };
        try {
            infoBranches = (InfoAreas2Choose) ServiceManagerServiceFactory.executeService(userView,
                    "ReadSpecializationAndSecundaryAreasByStudent", args2);
        } catch (Exception e) {
            setAcurateErrorMessage(request, e, errors, studentNumber, "chooseStudent");
        }

        if (!errors.isEmpty()) {
            return mapping.getInputForward();
        }

        setFromElementsValues(changeStudentAreasForm, studentNumber.toString(), degreeType,
                infoStudentCurricularPlan);

        prepareContext(request, infoStudentCurricularPlan.getInfoBranch(), infoStudentCurricularPlan
                .getInfoSecundaryBranch(), studentNumber);
        request.setAttribute("degreeCurricularPlan", infoStudentCurricularPlan
                .getInfoDegreeCurricularPlan());
        request.setAttribute("infoBranches", infoBranches);
        return mapping.findForward("showAndChooseStudentAreas");
    }

    public ActionForward showChangeOfStudentAreasConfirmation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm changeStudentAreasForm = (DynaActionForm) form;

        Integer studentNumber = Integer.valueOf((String) changeStudentAreasForm.get("studentNumber"));

        String degreeTypeCode = (String) changeStudentAreasForm.get("degreeType");
        DegreeType degreeType = DegreeType.valueOf(degreeTypeCode);

        Integer specializationAreaID = (Integer) changeStudentAreasForm.get("specializationAreaID");

        Integer secondaryAreaID = (Integer) changeStudentAreasForm.get("secondaryAreaID");

        Integer studentCurricularPlanID = (Integer) changeStudentAreasForm
                .get("studentCurricularPlanID");

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        Object[] args1 = { studentNumber, degreeType };
        try {
            List infoStudentCurricularPlans = (List) ServiceManagerServiceFactory.executeService(
                    userView, "ReadStudentCurricularPlansByNumberAndDegreeType", args1);

            infoStudentCurricularPlan = getActiveInfoStudentCurricularPlan(infoStudentCurricularPlans);
        } catch (Exception e) {
            setAcurateErrorMessage(request, e, errors, studentNumber, "showAndChooseStudentAreas");
            setFromElementsValues(changeStudentAreasForm, studentNumber.toString(), degreeType, null);
        }

        Object[] args2 = { infoStudentCurricularPlan.getInfoStudent(), degreeType,
                studentCurricularPlanID, specializationAreaID, secondaryAreaID };
        try {
            ServiceManagerServiceFactory.executeService(userView,
                    "WriteStudentAreasWithoutRestrictions", args2);
        } catch (Exception e) {
            setAcurateErrorMessage(request, e, errors, studentNumber, "showAndChooseStudentAreas");
            setFromElementsValues(changeStudentAreasForm, studentNumber.toString(), degreeType, null);
        }

        ActionForward forward = showAndChooseStudentAreas(mapping, form, request, response);

        if (!errors.isEmpty()) {
            return mapping.getInputForward();
        }

        if (!forward.equals(mapping.getInputForward())) {
            errors.add("success", new ActionError("message.change.areas.success"));
            saveErrors(request, errors);
            request.setAttribute("methodName", "showAndChooseStudentAreas");
        }

        return forward;
    }

    public ActionForward exit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("exit");
    }

    public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String methodName = (String) request.getAttribute("methodName");
        if (methodName == null) {
            Integer degreeType = Integer.valueOf(request.getParameter("degreeType"));
            request.setAttribute("degreeType", degreeType);
            return mapping.findForward("chooseStudent");
        }

        return mapping.findForward(methodName);

    }

    /**
     * @param request
     * @param e
     * @param errors
     * @param studentNumber
     * @param methodName
     */
    private void setAcurateErrorMessage(HttpServletRequest request, Exception e, ActionErrors errors,
            Integer studentNumber, String methodName) {
        e.printStackTrace();

        if (e instanceof NotAuthorizedException) {
            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
        } else if (e instanceof ExistingServiceException) {
            if (e.getMessage().equals("student")) {
                errors.add("student", new ActionError("error.no.student.in.database", studentNumber));
            } else if (e.getMessage().equals("studentCurricularPlan")) {
                errors.add("studentCurricularPlan", new ActionError(
                        "error.student.curricularPlan.nonExistent"));
            }
        } else if (e instanceof NonExistingServiceException) {
            errors.add("studentCurricularPlan", new ActionError(
                    "error.student.curricularPlan.nonExistent"));
        } else if (e instanceof BothAreasAreTheSameServiceException) {
            errors.add("bothAreas", new ActionError("error.student.enrollment.AreasNotEqual"));
        } else if (e instanceof FenixServiceException) {
            errors.add("noResult", new ActionError("error.impossible.operations"));
        } else {
            errors.add("noResult", new ActionError("error.impossible.operations"));
        }

        saveErrors(request, errors);
        request.setAttribute("methodName", methodName);
    }

    /**
     * @param form
     * @param studentNumber
     * @param degreeType
     * @param infoStudentCurricularPlan
     */
    private void setFromElementsValues(DynaActionForm form, String studentNumber, DegreeType degreeType,
            InfoStudentCurricularPlan infoStudentCurricularPlan) {
        form.set("studentNumber", studentNumber);
        form.set("degreeType", degreeType.toString());
        if (infoStudentCurricularPlan != null) {
            if (infoStudentCurricularPlan.getInfoBranch() != null) {
                form.set("specializationAreaID", infoStudentCurricularPlan.getInfoBranch()
                        .getIdInternal());
            } else {
                form.set("specializationAreaID", new Integer(0));
            }

            if (infoStudentCurricularPlan.getInfoSecundaryBranch() != null) {
                form.set("secondaryAreaID", infoStudentCurricularPlan.getInfoSecundaryBranch()
                        .getIdInternal());
            } else {
                form.set("secondaryAreaID", new Integer(0));
            }

            form.set("studentCurricularPlanID", infoStudentCurricularPlan.getIdInternal());
        }
    }

    /**
     * @param request
     * @param specializationArea
     * @param secondaryArea
     * @param studentNumber
     */
    private void prepareContext(HttpServletRequest request, InfoBranch specializationArea,
            InfoBranch secondaryArea, Integer studentNumber) {
        request.setAttribute("studentNumber", studentNumber);

        if (specializationArea != null) {
            request.setAttribute("specializationAreaName", specializationArea.getName());
        } else {
            request.setAttribute("specializationAreaName", "");
        }

        if (secondaryArea != null) {
            request.setAttribute("secondaryAreaName", secondaryArea.getName());
        } else {
            request.setAttribute("secondaryAreaName", "");
        }
    }

    /**
     * @param infoStudentCurricularPlans
     * @return InfoStudentCurricularPlan
     */
    private InfoStudentCurricularPlan getActiveInfoStudentCurricularPlan(List infoStudentCurricularPlans) {
        List result = (List) CollectionUtils.select(infoStudentCurricularPlans, new Predicate() {
            public boolean evaluate(Object obj) {
                InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) obj;
                return infoStudentCurricularPlan.getCurrentState().equals(
                        StudentCurricularPlanState.ACTIVE);
            }
        });

        return (InfoStudentCurricularPlan) result.get(0);
    }

}
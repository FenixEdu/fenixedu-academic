package ServidorApresentacao.Action.equivalence;

import java.util.ArrayList;
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

import DataBeans.InfoStudentCurricularPlan;
import DataBeans.equivalence.InfoEquivalenceContext;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author David Santos in Apr 30, 2004
 */

public class SelectStudentCurricularPlansForEnrollmentEquivalenceDispatchAction extends DispatchAction {
    public ActionForward showStudentCurricularPlans(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        ActionErrors errors = new ActionErrors();

        String degreeTypeSTR = request.getParameter("degreeType");
        String studentNumberSTR = request.getParameter("studentNumber");
        String backLink = request.getParameter("backLink");

        TipoCurso degreeType = new TipoCurso();
        degreeType.setTipoCurso(Integer.valueOf(degreeTypeSTR));
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        List infoStudentCurricularPlans = null;
        try {
            infoStudentCurricularPlans = runService(userView, studentNumber, degreeType);
        } catch (Exception e) {
            saveAcurateError(request, errors, e, "showStudentCurricularPlans", studentNumberSTR);
            setRequestAttributes(request, degreeType.getTipoCurso(), studentNumberSTR, backLink, null,
                    null, new ArrayList(), null);
            request.setAttribute("commingFrom", "showStudentCurricularPlans");
            return mapping.getInputForward();
        }

        if ((infoStudentCurricularPlans == null) || (infoStudentCurricularPlans.isEmpty())) {
            saveAcurateError(request, errors, null, "showStudentCurricularPlans", studentNumberSTR);
            setRequestAttributes(request, degreeType.getTipoCurso(), studentNumberSTR, backLink, null,
                    null, new ArrayList(), null);
            request.setAttribute("commingFrom", "showStudentCurricularPlans");
            return mapping.getInputForward();
        }

        setRequestAttributes(request, degreeType.getTipoCurso(), studentNumberSTR, backLink, null, null,
                infoStudentCurricularPlans, buildInfoEquivalenceContext(infoStudentCurricularPlans));

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "showStudentCurricularPlans", backLink));
    }

    public ActionForward chooseStudentCurricularPlans(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm selectStudentCurricularPlansForm = (DynaActionForm) form;
        ActionErrors errors = new ActionErrors();

        Integer degreeType = (Integer) selectStudentCurricularPlansForm.get("degreeType");
        String studentNumber = (String) selectStudentCurricularPlansForm.get("studentNumber");
        String backLink = (String) selectStudentCurricularPlansForm.get("backLink");
        Integer fromStudentCurricularPlanID = (Integer) selectStudentCurricularPlansForm
                .get("fromStudentCurricularPlanID");
        Integer toStudentCurricularPlanID = (Integer) selectStudentCurricularPlansForm
                .get("toStudentCurricularPlanID");

        if (fromStudentCurricularPlanID.equals(toStudentCurricularPlanID)) {
            saveAcurateError(request, errors, null, "chooseStudentCurricularPlans", studentNumber);
            setRequestAttributes(request, degreeType, studentNumber, backLink,
                    fromStudentCurricularPlanID, toStudentCurricularPlanID, null, null);
            request.setAttribute("commingFrom", "chooseStudentCurricularPlans");
            return mapping.getInputForward();
        }

        setRequestAttributes(request, degreeType, studentNumber, backLink, fromStudentCurricularPlanID,
                toStudentCurricularPlanID, null, null);

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "chooseStudentCurricularPlans", backLink));
    }

    public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm selectStudentCurricularPlansForm = (DynaActionForm) form;

        Integer degreeType = (Integer) request.getAttribute("degreeType");
        String studentNumber = (String) request.getAttribute("studentNumber");
        String backLink = (String) request.getAttribute("backLink");
        String commingFrom = (String) request.getAttribute("commingFrom");

        if (degreeType == null) {
            degreeType = (Integer) selectStudentCurricularPlansForm.get("degreeType");
        }

        if (studentNumber == null) {
            studentNumber = (String) selectStudentCurricularPlansForm.get("studentNumber");
        }

        if (backLink == null) {
            backLink = (String) selectStudentCurricularPlansForm.get("backLink");
        }

        if (commingFrom == null) {
            commingFrom = request.getParameter("commingFrom");
        }

        List infoStudentCurricularPlans = null;

        if (commingFrom.equals("chooseStudentCurricularPlans")) {
            TipoCurso realDegreeType = new TipoCurso();
            realDegreeType.setTipoCurso(degreeType);
            Integer realStudentNumber = Integer.valueOf(studentNumber);

            try {
                infoStudentCurricularPlans = runService(userView, realStudentNumber, realDegreeType);
            } catch (Exception e) {
                e.printStackTrace();
                throw new FenixActionException(e);
            }
        }

        setRequestAttributes(request, degreeType, studentNumber, backLink, null, null,
                infoStudentCurricularPlans, buildInfoEquivalenceContext(infoStudentCurricularPlans));

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "showStudentCurricularPlans", backLink));
    }

    /**
     * @param request
     * @param errors
     * @param e
     * @param methodName
     * @param studentNumber
     */
    private void saveAcurateError(HttpServletRequest request, ActionErrors errors, Exception e,
            String methodName, String studentNumber) {
        if (methodName.equals("showStudentCurricularPlans") && (e != null)) {
            if (e instanceof NonExistingServiceException) {
                errors.add("no.student.curricular.plans", new ActionError(
                        "errors.enrollment.equivalence.no.student.curricular.plans", studentNumber));
            } else if (e instanceof FenixServiceException) {
                e.printStackTrace();
                errors.add("problems.in.persistent.suport", new ActionError(
                        "errors.enrollment.equivalence.problems.in.persistent.suport"));
            }
        } else if (methodName.equals("showStudentCurricularPlans") && (e == null)) {
            errors.add("no.student.curricular.plans", new ActionError(
                    "errors.enrollment.equivalence.no.student.curricular.plans", studentNumber));
        } else if (methodName.equals("chooseStudentCurricularPlans")) {
            errors
                    .add(
                            "enrollment.equivalence.destination.student.curricular.plan.equal.to.origin.student.curricular.plan",
                            new ActionError(
                                    "errors.enrollment.equivalence.destination.student.curricular.plan.equal.to.origin.student.curricular.plan"));
        }

        saveErrors(request, errors);
    }

    /**
     * @param infoStudentCurricularPlans
     * @return InfoEquivalenceContext
     */
    private InfoEquivalenceContext buildInfoEquivalenceContext(List infoStudentCurricularPlans) {
        InfoEquivalenceContext infoEquivalenceContext = new InfoEquivalenceContext();

        List infoStudentCurricularPlansList = (List) CollectionUtils.select(infoStudentCurricularPlans,
                new Predicate() {
                    public boolean evaluate(Object obj) {
                        InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) obj;
                        return infoStudentCurricularPlan.getCurrentState().equals(
                                StudentCurricularPlanState.ACTIVE_OBJ);
                    }
                });

        infoEquivalenceContext
                .setInfoStudentCurricularPlan((InfoStudentCurricularPlan) infoStudentCurricularPlansList
                        .get(0));

        return infoEquivalenceContext;
    }

    /**
     * @param userView
     * @param studentNumber
     * @param degreeType
     * @return infoStudentCurricularPlans
     * @throws FenixServiceException
     */
    private List runService(IUserView userView, Integer studentNumber, TipoCurso degreeType)
            throws FenixServiceException, FenixFilterException {
        List infoStudentCurricularPlans = null;

        Object args[] = { studentNumber, degreeType };
        infoStudentCurricularPlans = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentCurricularPlansByNumberAndDegreeType", args);

        if (infoStudentCurricularPlans == null) {
            infoStudentCurricularPlans = new ArrayList();
        }

        return infoStudentCurricularPlans;
    }

    /**
     * @param request
     * @param degreeType
     * @param studentNumber
     * @param backLink
     * @param fromStudentCurricularPlanID
     * @param toStudentCurricularPlanID
     * @param infoStudentCurricularPlans
     * @param infoEquivalenceContext
     */
    private void setRequestAttributes(HttpServletRequest request, Integer degreeType,
            String studentNumber, String backLink, Integer fromStudentCurricularPlanID,
            Integer toStudentCurricularPlanID, List infoStudentCurricularPlans,
            InfoEquivalenceContext infoEquivalenceContext) {
        request.setAttribute("degreeType", degreeType);
        request.setAttribute("studentNumber", studentNumber);
        request.setAttribute("backLink", backLink);

        if (fromStudentCurricularPlanID != null) {
            request.setAttribute("fromStudentCurricularPlanID", fromStudentCurricularPlanID);
        }

        if (toStudentCurricularPlanID != null) {
            request.setAttribute("toStudentCurricularPlanID", toStudentCurricularPlanID);
        }

        if (infoStudentCurricularPlans != null) {
            request.setAttribute("infoStudentCurricularPlans", infoStudentCurricularPlans);
        }

        if (infoEquivalenceContext != null) {
            // Just for displaying a context of work (not necessary).
            request.setAttribute("infoEquivalenceContext", infoEquivalenceContext);
        }
    }

}
package ServidorApresentacao.Action.equivalence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoEnrolment;
import DataBeans.equivalence.InfoEquivalenceContext;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author David Santos in Apr 28, 2004
 */

public class DeleteEnrollmentEquivalenceDispatchAction extends DispatchAction {
    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer fromStudentCurricularPlanID = (Integer) request
                .getAttribute("fromStudentCurricularPlanID");
        String backLink = (String) request.getAttribute("backLink");
        Integer degreeTypeCode = (Integer) request.getAttribute("degreeType");
        String studentNumberSTR = (String) request.getAttribute("studentNumber");

        TipoCurso degreeType = new TipoCurso();
        degreeType.setTipoCurso(degreeTypeCode);
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        InfoEquivalenceContext infoEquivalenceContext = null;
        try {
            infoEquivalenceContext = runFirstService(userView, studentNumber, degreeType,
                    fromStudentCurricularPlanID);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        sortInfoEquivalenceContext(infoEquivalenceContext);

        setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                fromStudentCurricularPlanID, infoEquivalenceContext);

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "show", backLink));
    }

    public ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm deleteEnrollmentEquivalenceFrom = (DynaActionForm) form;
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        ActionErrors errors = new ActionErrors();

        Integer degreeTypeCode = (Integer) deleteEnrollmentEquivalenceFrom.get("degreeType");
        String studentNumberSTR = (String) deleteEnrollmentEquivalenceFrom.get("studentNumber");
        String backLink = (String) deleteEnrollmentEquivalenceFrom.get("backLink");
        Integer fromStudentCurricularPlanID = (Integer) deleteEnrollmentEquivalenceFrom
                .get("fromStudentCurricularPlanID");

        List idsOfChosenEnrollments = getIDsOfChosenEnrollments(deleteEnrollmentEquivalenceFrom);

        if (idsOfChosenEnrollments.isEmpty()) {
            saveAcurateError(request, errors, "confirm");
            setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                    fromStudentCurricularPlanID, null);
            request.setAttribute("commingFrom", "confirm");
            return mapping.getInputForward();
        }

        TipoCurso degreeType = new TipoCurso();
        degreeType.setTipoCurso(degreeTypeCode);
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        InfoEquivalenceContext infoEquivalenceContext = null;
        try {
            infoEquivalenceContext = runFirstService(userView, studentNumber, degreeType,
                    fromStudentCurricularPlanID);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        keepOnlyChosenEnrollments(infoEquivalenceContext, idsOfChosenEnrollments);

        sortInfoEquivalenceContext(infoEquivalenceContext);

        setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                fromStudentCurricularPlanID, infoEquivalenceContext);

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "confirm", backLink));
    }

    public ActionForward accept(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm deleteEnrollmentEquivalenceFrom = (DynaActionForm) form;
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer degreeTypeCode = (Integer) deleteEnrollmentEquivalenceFrom.get("degreeType");
        String studentNumberSTR = (String) deleteEnrollmentEquivalenceFrom.get("studentNumber");
        String backLink = (String) deleteEnrollmentEquivalenceFrom.get("backLink");
        Integer fromStudentCurricularPlanID = (Integer) deleteEnrollmentEquivalenceFrom
                .get("fromStudentCurricularPlanID");

        TipoCurso degreeType = new TipoCurso();
        degreeType.setTipoCurso(degreeTypeCode);
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        List idsOfChosenEnrollments = getIDsOfChosenEnrollments(deleteEnrollmentEquivalenceFrom);
        try {
            runSecondService(userView, studentNumber, degreeType, idsOfChosenEnrollments);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                fromStudentCurricularPlanID, null);

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "accept", backLink));
    }

    public ActionForward details(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String degreeTypeCode = request.getParameter("degreeType");
        String studentNumberSTR = request.getParameter("studentNumber");
        String backLink = request.getParameter("backLink");
        String fromStudentCurricularPlanID = request.getParameter("fromStudentCurricularPlanID");
        String enrollmentID = request.getParameter("enrollmentID");

        TipoCurso degreeType = new TipoCurso();
        degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        InfoEquivalenceContext infoEquivalenceContext = null;
        try {
            infoEquivalenceContext = runFirstService(userView, studentNumber, degreeType, Integer
                    .valueOf(fromStudentCurricularPlanID));
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        Object args[] = { studentNumber, degreeType, Integer.valueOf(enrollmentID) };

        List infoEnrolmentEvaluations = null;
        try {
            infoEnrolmentEvaluations = (List) ServiceManagerServiceFactory.executeService(userView,
                    "GetEnrollmentEvaluationWithEquivalence", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        request.setAttribute("infoEnrolmentEvaluations", infoEnrolmentEvaluations);
        setRequestAttributes(request, Integer.valueOf(degreeTypeCode), studentNumberSTR, backLink,
                Integer.valueOf(fromStudentCurricularPlanID), infoEquivalenceContext);

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "details", backLink));
    }

    public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm deleteEnrollmentEquivalenceFrom = (DynaActionForm) form;

        String forward = null;

        Integer degreeType = (Integer) request.getAttribute("degreeType");
        String studentNumber = (String) request.getAttribute("studentNumber");
        String backLink = (String) request.getAttribute("backLink");
        Integer fromStudentCurricularPlanID = (Integer) request
                .getAttribute("fromStudentCurricularPlanID");
        String commingFrom = (String) request.getAttribute("commingFrom");

        if (degreeType == null) {
            degreeType = (Integer) deleteEnrollmentEquivalenceFrom.get("degreeType");
        }

        if (studentNumber == null) {
            studentNumber = (String) deleteEnrollmentEquivalenceFrom.get("studentNumber");
        }

        if (backLink == null) {
            backLink = (String) deleteEnrollmentEquivalenceFrom.get("backLink");
        }

        if (fromStudentCurricularPlanID == null) {
            fromStudentCurricularPlanID = (Integer) deleteEnrollmentEquivalenceFrom
                    .get("fromStudentCurricularPlanID");
        }

        if (commingFrom == null) {
            commingFrom = request.getParameter("commingFrom");
        }

        InfoEquivalenceContext infoEquivalenceContext = null;

        if (commingFrom.equals("confirm")) {
            TipoCurso realDegreeType = new TipoCurso();
            realDegreeType.setTipoCurso(degreeType);
            Integer realStudentNumber = Integer.valueOf(studentNumber);

            try {
                infoEquivalenceContext = runFirstService(userView, realStudentNumber, realDegreeType,
                        fromStudentCurricularPlanID);
            } catch (Exception e) {
                e.printStackTrace();
                throw new FenixActionException(e);
            }

            forward = PrepareAplicationContextForEnrollmentEquivalenceAction.findForward("show",
                    backLink);

            sortInfoEquivalenceContext(infoEquivalenceContext);
        }

        setRequestAttributes(request, degreeType, studentNumber, backLink, fromStudentCurricularPlanID,
                infoEquivalenceContext);

        return mapping.findForward(forward);
    }

    /**
     * @param request
     * @param errors
     * @param methodName
     */
    private void saveAcurateError(HttpServletRequest request, ActionErrors errors, String methodName) {
        if (methodName.equals("confirm")) {
            errors.add("must.select.enrollment.to.delete.equivalence", new ActionError(
                    "errors.enrollment.equivalence.must.select.enrollment.to.delete.equivalence"));
        }

        saveErrors(request, errors);
    }

    /**
     * @param infoEquivalenceContext
     */
    private void sortInfoEquivalenceContext(InfoEquivalenceContext infoEquivalenceContext) {
        List infoEnrollmentsFromEquivalences = infoEquivalenceContext
                .getInfoEnrollmentsFromEquivalences();

        if ((infoEnrollmentsFromEquivalences != null) && (!infoEnrollmentsFromEquivalences.isEmpty())) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            Collections.sort(infoEnrollmentsFromEquivalences, comparatorChain);
        }
    }

    /**
     * @param deleteEnrollmentEquivalenceFrom
     * @return List
     */
    private List getIDsOfChosenEnrollments(DynaActionForm deleteEnrollmentEquivalenceFrom) {
        Integer[] idsOfEnrollmentsToGiveEquivalence = (Integer[]) deleteEnrollmentEquivalenceFrom
                .get("curricularCoursesToGiveEquivalence");

        List idsOfChosenEnrollmentsToGiveEquivalence = new ArrayList();

        if (idsOfEnrollmentsToGiveEquivalence != null) {
            for (int i = 0; i < idsOfEnrollmentsToGiveEquivalence.length; i++) {
                Integer enrollmentID = idsOfEnrollmentsToGiveEquivalence[i];
                idsOfChosenEnrollmentsToGiveEquivalence.add(enrollmentID);
            }
        }

        return idsOfChosenEnrollmentsToGiveEquivalence;
    }

    /**
     * @param infoEquivalenceContext
     * @param idsOfChosenEnrollments
     */
    private void keepOnlyChosenEnrollments(InfoEquivalenceContext infoEquivalenceContext,
            List idsOfChosenEnrollments) {
        List infoEnrollmentsFromEquivalences = infoEquivalenceContext
                .getInfoEnrollmentsFromEquivalences();
        List elementsToRemove = new ArrayList();

        for (int i = 0; i < infoEnrollmentsFromEquivalences.size(); i++) {
            InfoEnrolment infoEnrollment = (InfoEnrolment) infoEnrollmentsFromEquivalences.get(i);

            if (!idsOfChosenEnrollments.contains(infoEnrollment.getIdInternal())) {
                elementsToRemove.add(infoEnrollment);
            }
        }

        infoEnrollmentsFromEquivalences.removeAll(elementsToRemove);
        infoEquivalenceContext.setInfoEnrollmentsFromEquivalences(infoEnrollmentsFromEquivalences);
    }

    /**
     * @param request
     * @param degreeType
     * @param studentNumber
     * @param backLink
     * @param fromStudentCurricularPlanID
     * @param infoEquivalenceContext
     */
    private void setRequestAttributes(HttpServletRequest request, Integer degreeType,
            String studentNumber, String backLink, Integer fromStudentCurricularPlanID,
            InfoEquivalenceContext infoEquivalenceContext) {
        request.setAttribute("degreeType", degreeType);
        request.setAttribute("studentNumber", studentNumber);
        request.setAttribute("backLink", backLink);
        request.setAttribute("fromStudentCurricularPlanID", fromStudentCurricularPlanID);

        if (infoEquivalenceContext != null) {
            request.setAttribute("infoEquivalenceContext", infoEquivalenceContext);
        }
    }

    /**
     * @param userView
     * @param studentNumber
     * @param degreeType
     * @param fromStudentCurricularPlanID
     * @return InfoEquivalenceContext
     * @throws FenixServiceException
     */
    private InfoEquivalenceContext runFirstService(IUserView userView, Integer studentNumber,
            TipoCurso degreeType, Integer fromStudentCurricularPlanID) throws FenixServiceException {
        InfoEquivalenceContext infoEquivalenceContext = null;

        Object args[] = { studentNumber, degreeType, fromStudentCurricularPlanID };
        infoEquivalenceContext = (InfoEquivalenceContext) ServiceManagerServiceFactory.executeService(
                userView, "ReadListsOfEnrollmentsWithEquivalences", args);

        return infoEquivalenceContext;
    }

    /**
     * @param userView
     * @param studentNumber
     * @param degreeType
     * @param idsOfChosenEnrollments
     * @return InfoEquivalenceContext
     * @throws FenixServiceException
     */
    private InfoEquivalenceContext runSecondService(IUserView userView, Integer studentNumber,
            TipoCurso degreeType, List idsOfChosenEnrollments) throws FenixServiceException {
        InfoEquivalenceContext infoEquivalenceContext = null;

        Object args[] = { studentNumber, degreeType, idsOfChosenEnrollments };
        infoEquivalenceContext = (InfoEquivalenceContext) ServiceManagerServiceFactory.executeService(
                userView, "DeleteChosenEnrollmentEquivalences", args);

        return infoEquivalenceContext;
    }

}
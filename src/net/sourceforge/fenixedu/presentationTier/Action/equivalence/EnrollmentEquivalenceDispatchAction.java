package net.sourceforge.fenixedu.presentationTier.Action.equivalence;

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

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.equivalence.InfoCurricularCourseGrade;
import net.sourceforge.fenixedu.dataTransferObject.equivalence.InfoEquivalenceContext;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.TipoCurso;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author David Santos in Apr 28, 2004
 */

public class EnrollmentEquivalenceDispatchAction extends DispatchAction {
    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer fromStudentCurricularPlanID = (Integer) request
                .getAttribute("fromStudentCurricularPlanID");
        Integer toStudentCurricularPlanID = (Integer) request.getAttribute("toStudentCurricularPlanID");
        String backLink = (String) request.getAttribute("backLink");
        Integer degreeTypeCode = (Integer) request.getAttribute("degreeType");
        String studentNumberSTR = (String) request.getAttribute("studentNumber");

        TipoCurso degreeType = new TipoCurso();
        degreeType.setTipoCurso(degreeTypeCode);
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        InfoEquivalenceContext infoEquivalenceContext = null;
        try {
            infoEquivalenceContext = runFirstService(userView, studentNumber, degreeType,
                    fromStudentCurricularPlanID, toStudentCurricularPlanID);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        sortInfoEquivalenceContext(infoEquivalenceContext);

        setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                fromStudentCurricularPlanID, toStudentCurricularPlanID, infoEquivalenceContext);

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "show", backLink));
    }

    public ActionForward grades(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm makeEnrollmentEquivalenceFrom = (DynaActionForm) form;
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        ActionErrors errors = new ActionErrors();

        Integer degreeTypeCode = (Integer) makeEnrollmentEquivalenceFrom.get("degreeType");
        String studentNumberSTR = (String) makeEnrollmentEquivalenceFrom.get("studentNumber");
        String backLink = (String) makeEnrollmentEquivalenceFrom.get("backLink");
        Integer fromStudentCurricularPlanID = (Integer) makeEnrollmentEquivalenceFrom
                .get("fromStudentCurricularPlanID");
        Integer toStudentCurricularPlanID = (Integer) makeEnrollmentEquivalenceFrom
                .get("toStudentCurricularPlanID");

        List idsOfChosenEnrollmentsToGiveEquivalence = getIDsOfChosenEnrollmentsToGiveEquivalence(makeEnrollmentEquivalenceFrom);
        List idsOfChosenCurricularCoursesToGetEquivalence = getIDsOfChosenCurricularCoursesToGetEquivalence(makeEnrollmentEquivalenceFrom);

        TipoCurso degreeType = new TipoCurso();
        degreeType.setTipoCurso(degreeTypeCode);
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        if (idsOfChosenEnrollmentsToGiveEquivalence.isEmpty()) {
            saveAcurateError(request, errors, "grades", "from");
            setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                    fromStudentCurricularPlanID, toStudentCurricularPlanID, null);
            request.setAttribute("commingFrom", "grades");
            return mapping.getInputForward();
        }

        if (idsOfChosenCurricularCoursesToGetEquivalence.isEmpty()) {
            saveAcurateError(request, errors, "grades", "to");
            setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                    fromStudentCurricularPlanID, toStudentCurricularPlanID, null);
            request.setAttribute("commingFrom", "grades");
            return mapping.getInputForward();
        }

        InfoEquivalenceContext infoEquivalenceContext = null;
        try {
            infoEquivalenceContext = runSecondService(userView, studentNumber, degreeType,
                    idsOfChosenEnrollmentsToGiveEquivalence,
                    idsOfChosenCurricularCoursesToGetEquivalence);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        sortInfoEquivalenceContext(infoEquivalenceContext);

        setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                fromStudentCurricularPlanID, toStudentCurricularPlanID, infoEquivalenceContext);

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "grades", backLink));
    }

    public ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm makeEnrollmentEquivalenceFrom = (DynaActionForm) form;
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        ActionErrors errors = new ActionErrors();

        Integer degreeTypeCode = (Integer) makeEnrollmentEquivalenceFrom.get("degreeType");
        String studentNumberSTR = (String) makeEnrollmentEquivalenceFrom.get("studentNumber");
        String backLink = (String) makeEnrollmentEquivalenceFrom.get("backLink");
        Integer fromStudentCurricularPlanID = (Integer) makeEnrollmentEquivalenceFrom
                .get("fromStudentCurricularPlanID");
        Integer toStudentCurricularPlanID = (Integer) makeEnrollmentEquivalenceFrom
                .get("toStudentCurricularPlanID");

        List idsOfChosenEnrollmentsToGiveEquivalence = getIDsOfChosenEnrollmentsToGiveEquivalence(makeEnrollmentEquivalenceFrom);
        List idsOfChosenCurricularCoursesToGetEquivalence = getIDsOfChosenCurricularCoursesToGetEquivalence(request);

        TipoCurso degreeType = new TipoCurso();
        degreeType.setTipoCurso(degreeTypeCode);
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        InfoEquivalenceContext infoEquivalenceContext = null;
        try {
            infoEquivalenceContext = runSecondService(userView, studentNumber, degreeType,
                    idsOfChosenEnrollmentsToGiveEquivalence,
                    idsOfChosenCurricularCoursesToGetEquivalence);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        setChosenCurricularCourseGradesInInfoEquivalenceContext(request, infoEquivalenceContext);

        Object args[] = { studentNumber, degreeType,
                infoEquivalenceContext.getInfoStudentCurricularPlan(),
                infoEquivalenceContext.getChosenInfoCurricularCourseGradesToGetEquivalence() };

        String result = null;
        try {
            result = (String) ServiceManagerServiceFactory.executeService(userView,
                    "ValidateCurricularCourseGradesForEnrollmentEquivalence", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        if (result != null) {
            saveAcurateError(request, errors, "confirm", result);
            setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                    fromStudentCurricularPlanID, toStudentCurricularPlanID, null);
            request.setAttribute("commingFrom", "confirm");
            request.setAttribute("idsOfChosenEnrollmentsToGiveEquivalence",
                    idsOfChosenEnrollmentsToGiveEquivalence);
            request.setAttribute("idsOfChosenCurricularCoursesToGetEquivalence",
                    idsOfChosenCurricularCoursesToGetEquivalence);
            return mapping.getInputForward();
        }

        sortInfoEquivalenceContext(infoEquivalenceContext);

        setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                fromStudentCurricularPlanID, toStudentCurricularPlanID, infoEquivalenceContext);

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "confirm", backLink));
    }

    public ActionForward accept(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm makeEnrollmentEquivalenceFrom = (DynaActionForm) form;
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer degreeTypeCode = (Integer) makeEnrollmentEquivalenceFrom.get("degreeType");
        String studentNumberSTR = (String) makeEnrollmentEquivalenceFrom.get("studentNumber");
        String backLink = (String) makeEnrollmentEquivalenceFrom.get("backLink");
        Integer fromStudentCurricularPlanID = (Integer) makeEnrollmentEquivalenceFrom
                .get("fromStudentCurricularPlanID");
        Integer toStudentCurricularPlanID = (Integer) makeEnrollmentEquivalenceFrom
                .get("toStudentCurricularPlanID");

        List idsOfChosenEnrollmentsToGiveEquivalence = getIDsOfChosenEnrollmentsToGiveEquivalence(makeEnrollmentEquivalenceFrom);
        List idsOfChosenCurricularCoursesToGetEquivalence = getIDsOfChosenCurricularCoursesToGetEquivalence(request);

        TipoCurso degreeType = new TipoCurso();
        degreeType.setTipoCurso(degreeTypeCode);
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        InfoEquivalenceContext infoEquivalenceContext = null;
        try {
            infoEquivalenceContext = runSecondService(userView, studentNumber, degreeType,
                    idsOfChosenEnrollmentsToGiveEquivalence,
                    idsOfChosenCurricularCoursesToGetEquivalence);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        setChosenCurricularCourseGradesInInfoEquivalenceContext(request, infoEquivalenceContext);

        Object args[] = { studentNumber, degreeType, infoEquivalenceContext, toStudentCurricularPlanID,
                userView };

        try {
            ServiceManagerServiceFactory.executeService(userView, "WriteEnrollmentEquivalences", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        setRequestAttributes(request, degreeTypeCode, studentNumberSTR, backLink,
                fromStudentCurricularPlanID, toStudentCurricularPlanID, null);

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
        String toStudentCurricularPlanID = request.getParameter("toStudentCurricularPlanID");
        String enrollmentID = request.getParameter("enrollmentID");

        TipoCurso degreeType = new TipoCurso();
        degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));
        Integer studentNumber = Integer.valueOf(studentNumberSTR);

        InfoEquivalenceContext infoEquivalenceContext = null;
        try {
            infoEquivalenceContext = runFirstService(userView, studentNumber, degreeType, Integer
                    .valueOf(fromStudentCurricularPlanID), Integer.valueOf(toStudentCurricularPlanID));
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        Object args[] = { studentNumber, degreeType, Integer.valueOf(enrollmentID) };

        InfoEnrolmentEvaluation infoEnrolmentEvaluation = null;
        try {
            infoEnrolmentEvaluation = (InfoEnrolmentEvaluation) ServiceManagerServiceFactory
                    .executeService(userView, "GetEnrollmentEvaluation", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        request.setAttribute("infoEnrolmentEvaluation", infoEnrolmentEvaluation);
        setRequestAttributes(request, Integer.valueOf(degreeTypeCode), studentNumberSTR, backLink,
                Integer.valueOf(fromStudentCurricularPlanID),
                Integer.valueOf(toStudentCurricularPlanID), infoEquivalenceContext);

        return mapping.findForward(PrepareAplicationContextForEnrollmentEquivalenceAction.findForward(
                "details", backLink));
    }

    public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm makeEnrollmentEquivalenceFrom = (DynaActionForm) form;

        String forward = null;

        Integer degreeType = (Integer) request.getAttribute("degreeType");
        String studentNumber = (String) request.getAttribute("studentNumber");
        String backLink = (String) request.getAttribute("backLink");
        Integer fromStudentCurricularPlanID = (Integer) request
                .getAttribute("fromStudentCurricularPlanID");
        Integer toStudentCurricularPlanID = (Integer) request.getAttribute("toStudentCurricularPlanID");
        String commingFrom = (String) request.getAttribute("commingFrom");

        if (degreeType == null) {
            degreeType = (Integer) makeEnrollmentEquivalenceFrom.get("degreeType");
        }

        if (studentNumber == null) {
            studentNumber = (String) makeEnrollmentEquivalenceFrom.get("studentNumber");
        }

        if (backLink == null) {
            backLink = (String) makeEnrollmentEquivalenceFrom.get("backLink");
        }

        if (fromStudentCurricularPlanID == null) {
            fromStudentCurricularPlanID = (Integer) makeEnrollmentEquivalenceFrom
                    .get("fromStudentCurricularPlanID");
        }

        if (toStudentCurricularPlanID == null) {
            toStudentCurricularPlanID = (Integer) makeEnrollmentEquivalenceFrom
                    .get("toStudentCurricularPlanID");
        }

        if (commingFrom == null) {
            commingFrom = request.getParameter("commingFrom");
        }

        InfoEquivalenceContext infoEquivalenceContext = null;

        if (commingFrom.equals("show")) {
            forward = PrepareAplicationContextForEnrollmentEquivalenceAction.findForward("show",
                    backLink);
        } else if (commingFrom.equals("grades")) {
            TipoCurso realDegreeType = new TipoCurso();
            realDegreeType.setTipoCurso(degreeType);
            Integer realStudentNumber = Integer.valueOf(studentNumber);

            try {
                infoEquivalenceContext = runFirstService(userView, realStudentNumber, realDegreeType,
                        fromStudentCurricularPlanID, toStudentCurricularPlanID);
            } catch (Exception e) {
                e.printStackTrace();
                throw new FenixActionException(e);
            }

            sortInfoEquivalenceContext(infoEquivalenceContext);

            forward = PrepareAplicationContextForEnrollmentEquivalenceAction.findForward("show",
                    backLink);
        } else if (commingFrom.equals("confirm")) {
            TipoCurso realDegreeType = new TipoCurso();
            realDegreeType.setTipoCurso(degreeType);
            Integer realStudentNumber = Integer.valueOf(studentNumber);

            List idsOfChosenEnrollmentsToGiveEquivalence = (List) request
                    .getAttribute("idsOfChosenEnrollmentsToGiveEquivalence");
            List idsOfChosenCurricularCoursesToGetEquivalence = (List) request
                    .getAttribute("idsOfChosenCurricularCoursesToGetEquivalence");

            try {
                infoEquivalenceContext = runSecondService(userView, realStudentNumber, realDegreeType,
                        idsOfChosenEnrollmentsToGiveEquivalence,
                        idsOfChosenCurricularCoursesToGetEquivalence);
            } catch (Exception e) {
                e.printStackTrace();
                throw new FenixActionException(e);
            }

            sortInfoEquivalenceContext(infoEquivalenceContext);

            forward = PrepareAplicationContextForEnrollmentEquivalenceAction.findForward("grades",
                    backLink);
        }

        setRequestAttributes(request, degreeType, studentNumber, backLink, fromStudentCurricularPlanID,
                toStudentCurricularPlanID, infoEquivalenceContext);

        return mapping.findForward(forward);
    }

    /**
     * @param request
     * @param errors
     * @param methodName
     * @param arg1
     */
    private void saveAcurateError(HttpServletRequest request, ActionErrors errors, String methodName,
            String arg1) {
        if (methodName.equals("grades")) {
            if (arg1.equals("from")) {
                errors.add("must.select.from", new ActionError(
                        "errors.enrollment.equivalence.must.select.from"));
            } else if (arg1.equals("to")) {
                errors.add("must.select.to", new ActionError(
                        "errors.enrollment.equivalence.must.select.to"));
            }
        } else if (methodName.equals("confirm")) {
            errors.add("must.select.from", new ActionError(arg1));
        }

        saveErrors(request, errors);
    }

    /**
     * @param infoEquivalenceContext
     */
    private void sortInfoEquivalenceContext(InfoEquivalenceContext infoEquivalenceContext) {
        List infoEnrolmentsToGiveEquivalence = infoEquivalenceContext
                .getInfoEnrolmentsToGiveEquivalence();
        List infoCurricularCoursesToGetEquivalence = infoEquivalenceContext
                .getInfoCurricularCoursesToGetEquivalence();
        List infoEnrollmentGradesToGiveEquivalences = infoEquivalenceContext
                .getChosenInfoEnrollmentGradesToGiveEquivalence();
        List infoCurricularCourseGradesToGetEquivalences = infoEquivalenceContext
                .getChosenInfoCurricularCourseGradesToGetEquivalence();

        if ((infoEnrolmentsToGiveEquivalence != null) && (!infoEnrolmentsToGiveEquivalence.isEmpty())) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            Collections.sort(infoEnrolmentsToGiveEquivalence, comparatorChain);
        }

        if ((infoCurricularCoursesToGetEquivalence != null)
                && (!infoCurricularCoursesToGetEquivalence.isEmpty())) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("name"));
            Collections.sort(infoCurricularCoursesToGetEquivalence, comparatorChain);
        }

        if ((infoEnrollmentGradesToGiveEquivalences != null)
                && (!infoEnrollmentGradesToGiveEquivalences.isEmpty())) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain
                    .addComparator(new BeanComparator("infoEnrollment.infoCurricularCourse.name"));
            Collections.sort(infoEnrollmentGradesToGiveEquivalences, comparatorChain);
        }

        if ((infoCurricularCourseGradesToGetEquivalences != null)
                && (!infoCurricularCourseGradesToGetEquivalences.isEmpty())) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            Collections.sort(infoCurricularCourseGradesToGetEquivalences, comparatorChain);
        }

    }

    /**
     * @param makeEnrollmentEquivalenceFrom
     * @return List
     */
    private List getIDsOfChosenEnrollmentsToGiveEquivalence(DynaActionForm makeEnrollmentEquivalenceFrom) {
        Integer[] idsOfEnrollmentsToGiveEquivalence = (Integer[]) makeEnrollmentEquivalenceFrom
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
     * @param makeEnrollmentEquivalenceFrom
     * @return List
     */
    private List getIDsOfChosenCurricularCoursesToGetEquivalence(
            DynaActionForm makeEnrollmentEquivalenceFrom) {
        Integer[] idsOfCurricularCoursesToGetEquivalence = (Integer[]) makeEnrollmentEquivalenceFrom
                .get("curricularCoursesToGetEquivalence");

        List idsOfChosenCurricularCoursesToGetEquivalence = new ArrayList();

        if (idsOfCurricularCoursesToGetEquivalence != null) {
            for (int i = 0; i < idsOfCurricularCoursesToGetEquivalence.length; i++) {
                Integer curricularCourseID = idsOfCurricularCoursesToGetEquivalence[i];
                idsOfChosenCurricularCoursesToGetEquivalence.add(curricularCourseID);
            }
        }

        return idsOfChosenCurricularCoursesToGetEquivalence;
    }

    /**
     * @param request
     * @return List
     */
    private List getIDsOfChosenCurricularCoursesToGetEquivalence(HttpServletRequest request) {
        List idsOfChosenCurricularCoursesToGetEquivalence = new ArrayList();

        Integer size = new Integer(request.getParameter("size"));

        for (int i = 0; i < size.intValue(); i++) {
            Integer curricularCourseID = Integer.valueOf(request
                    .getParameter("infoCurricularCourseGrade[" + i + "].curricularCourseID"));

            idsOfChosenCurricularCoursesToGetEquivalence.add(curricularCourseID);

        }

        return idsOfChosenCurricularCoursesToGetEquivalence;
    }

    /**
     * @param request
     * @param infoEquivalenceContext
     */
    private void setChosenCurricularCourseGradesInInfoEquivalenceContext(HttpServletRequest request,
            InfoEquivalenceContext infoEquivalenceContext) {
        Integer size = new Integer(request.getParameter("size"));

        for (int i = 0; i < size.intValue(); i++) {
            String grade = request.getParameter("infoCurricularCourseGrade[" + i + "].grade");
            Integer curricularCourseID = Integer.valueOf(request
                    .getParameter("infoCurricularCourseGrade[" + i + "].curricularCourseID"));

            for (int j = 0; j < infoEquivalenceContext
                    .getChosenInfoCurricularCourseGradesToGetEquivalence().size(); j++) {
                InfoCurricularCourseGrade infoCurricularCourseGrade = (InfoCurricularCourseGrade) infoEquivalenceContext
                        .getChosenInfoCurricularCourseGradesToGetEquivalence().get(j);

                if (infoCurricularCourseGrade.getInfoCurricularCourse().getIdInternal().equals(
                        curricularCourseID)) {
                    infoCurricularCourseGrade.setGrade(grade);
                }
            }
        }
    }

    /**
     * @param request
     * @param degreeType
     * @param studentNumber
     * @param backLink
     * @param fromStudentCurricularPlanID
     * @param toStudentCurricularPlanID
     * @param infoEquivalenceContext
     */
    private void setRequestAttributes(HttpServletRequest request, Integer degreeType,
            String studentNumber, String backLink, Integer fromStudentCurricularPlanID,
            Integer toStudentCurricularPlanID, InfoEquivalenceContext infoEquivalenceContext) {
        request.setAttribute("degreeType", degreeType);
        request.setAttribute("studentNumber", studentNumber);
        request.setAttribute("backLink", backLink);
        request.setAttribute("fromStudentCurricularPlanID", fromStudentCurricularPlanID);
        request.setAttribute("toStudentCurricularPlanID", toStudentCurricularPlanID);

        if (infoEquivalenceContext != null) {
            request.setAttribute("infoEquivalenceContext", infoEquivalenceContext);
        }
    }

    /**
     * @param userView
     * @param studentNumber
     * @param degreeType
     * @param fromStudentCurricularPlanID
     * @param toStudentCurricularPlanID
     * @return InfoEquivalenceContext
     * @throws FenixServiceException
     */
    private InfoEquivalenceContext runFirstService(IUserView userView, Integer studentNumber,
            TipoCurso degreeType, Integer fromStudentCurricularPlanID, Integer toStudentCurricularPlanID)
            throws FenixServiceException, FenixFilterException {
        InfoEquivalenceContext infoEquivalenceContext = null;

        Object args[] = { studentNumber, degreeType, fromStudentCurricularPlanID,
                toStudentCurricularPlanID };
        infoEquivalenceContext = (InfoEquivalenceContext) ServiceManagerServiceFactory.executeService(
                userView, "ReadListsOfCurricularCoursesForEnrollmentEquivalence", args);

        return infoEquivalenceContext;
    }

    /**
     * @param userView
     * @param studentNumber
     * @param degreeType
     * @param idsOfChosenEnrollmentsToGiveEquivalence
     * @param idsOfChosenCurricularCoursesToGetEquivalence
     * @return InfoEquivalenceContext
     * @throws FenixServiceException
     */
    private InfoEquivalenceContext runSecondService(IUserView userView, Integer studentNumber,
            TipoCurso degreeType, List idsOfChosenEnrollmentsToGiveEquivalence,
            List idsOfChosenCurricularCoursesToGetEquivalence) throws FenixServiceException, FenixFilterException {
        InfoEquivalenceContext infoEquivalenceContext = null;

        Object args[] = { studentNumber, degreeType, idsOfChosenEnrollmentsToGiveEquivalence,
                idsOfChosenCurricularCoursesToGetEquivalence };
        infoEquivalenceContext = (InfoEquivalenceContext) ServiceManagerServiceFactory.executeService(
                userView, "PrepareToGetGradesOfCurricularCoursesForEnrollmentEquivalence", args);

        return infoEquivalenceContext;
    }

}
package ServidorApresentacao.Action.degreeAdministrativeOffice.depercated;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.depercated.InfoEnrolmentContext;
import ServidorApresentacao.Action.commons.depercated.GeneralCurricularCourseEnrolmentManagerDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 *  
 */

public class OptionalCurricularCourseEnrolmentWithoutRulesManagerDispatchAction
    extends GeneralCurricularCourseEnrolmentManagerDispatchAction
{

    private final String[] forwards =
        {
            "showAvailableOptionalCurricularCourses",
            "verifyEnrolment",
            "acceptEnrolment",
            "searchOptionalCurricularCourses",
            "concreteOptionalList",
            "cancel" };

    public ActionForward start(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.createToken(request);

        HttpSession session = request.getSession();

        session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
        session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
        session.removeAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);

        InfoExecutionPeriod infoExecutionPeriod =
            (InfoExecutionPeriod) session.getServletContext().getAttribute(
                SessionConstants.INFO_EXECUTION_PERIOD_KEY);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoStudent infoStudent = super.getInfoStudent(request, form, userView);
        Object args[] = { infoStudent, infoExecutionPeriod };

        InfoEnrolmentContext infoEnrolmentContext =
            (InfoEnrolmentContext) ServiceUtils.executeService(
                userView,
                "ShowAvailableOptionalCurricularCoursesWithoutRules",
                args);
        //        ManualEquivalenceManagerDispatchAction.sort(
        //            infoEnrolmentContext.getInfoEnrolmentsAprovedByStudent(),
        //            infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled());

        session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);

        super.initializeForm(infoEnrolmentContext, (DynaActionForm) form);

        super.initializeRemovedCurricularCoursesList(request, infoEnrolmentContext);

        return mapping.findForward(forwards[0]);
    }

    public ActionForward verifyEnrolment(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.validateToken(request, form, mapping, "error.transaction.enrolment");

        HttpSession session = request.getSession();

        if (isCancelled(request))
        {
            session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
            session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
            session.removeAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);
            return mapping.findForward(forwards[5]);
        }

        DynaActionForm enrolmentForm = (DynaActionForm) form;

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoEnrolmentContext infoEnrolmentContext =
            this.processEnrolment(request, enrolmentForm, session);

        List list = (List) session.getAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);

        Object args[] = { infoEnrolmentContext, list };

        infoEnrolmentContext =
            (InfoEnrolmentContext) ServiceUtils.executeService(
                userView,
                "ValidateActualOptionalEnrolmentWithouRules",
                args);

        session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
        if (!infoEnrolmentContext.getEnrolmentValidationResult().isSucess())
        {
            super.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
            return mapping.findForward(forwards[0]);
        }
        else
        {
            return mapping.findForward(forwards[1]);
        }
    }

    public ActionForward accept(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.validateToken(request, form, mapping, "error.transaction.enrolment");

        if (isCancelled(request))
        {
            return mapping.findForward(forwards[0]);
        }

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoEnrolmentContext infoEnrolmentContext =
            (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

        Object args[] = { infoEnrolmentContext };

        infoEnrolmentContext =
            (InfoEnrolmentContext) ServiceUtils.executeService(
                userView,
                "ConfirmActualOptionalEnrolmentWithoutRules",
                args);
        if (infoEnrolmentContext.getEnrolmentValidationResult().isSucess())
        {
            session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
            session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
            session.removeAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);
            return mapping.findForward(forwards[2]);
        }
        else
        {
            session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
            super.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
            return mapping.findForward(forwards[0]);
        }

    }

    public ActionForward startEnrolmentInOptional(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.validateToken(request, form, mapping, "error.transaction.enrolment");

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        InfoEnrolmentContext infoEnrolmentContext =
            (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

        DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
        Integer optionalCurricularCourseIndex = (Integer) enrolmentForm.get("optionalCourseIndex");
        InfoCurricularCourse infoCurricularCourse =
            (InfoCurricularCourse) infoEnrolmentContext
                .getInfoFinalCurricularCoursesSpanToBeEnrolled()
                .get(
                optionalCurricularCourseIndex.intValue());
        infoEnrolmentContext.setInfoChosenOptionalCurricularCourse(infoCurricularCourse);

        Object args[] = { infoEnrolmentContext };

        infoEnrolmentContext =
            (InfoEnrolmentContext) ServiceUtils.executeService(
                userView,
                "ShowAvailableDegreesForOptionWithoutRules",
                args);

        session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);

        return mapping.findForward(forwards[3]);
    }

    public ActionForward showOptionalCurricularCourses(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.validateToken(request, form, mapping, "error.transaction.enrolment");

        DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
        HttpSession session = request.getSession();

        InfoEnrolmentContext infoEnrolmentContext =
            (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

        if (isCancelled(request))
        {
            super.uncheckCurricularCourse(enrolmentForm, infoEnrolmentContext);
            return mapping.findForward(forwards[0]);
        }

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        enrolmentForm.set("optionalCurricularCourse", null);
        Integer infoDegreeInternalId = (Integer) enrolmentForm.get("infoDegree");
        List infoDegreeList = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses();

        Iterator iterator = infoDegreeList.iterator();
        InfoDegree infoDegree = null;
        while (iterator.hasNext())
        {
            InfoDegree infoDegree2 = (InfoDegree) iterator.next();
            if (infoDegree2.getIdInternal().equals(infoDegreeInternalId))
            {
                infoDegree = infoDegree2;
                break;
            }
        }
        infoEnrolmentContext.setChosenOptionalInfoDegree(infoDegree);

        Object args[] = { infoEnrolmentContext };

        infoEnrolmentContext =
            (InfoEnrolmentContext) ServiceUtils.executeService(
                userView,
                "ShowAvailableCurricularCoursesForOptionWithoutRules",
                args);
        session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
        return mapping.findForward(forwards[4]);
    }

    public ActionForward chooseOptionalCourse(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        super.validateToken(request, form, mapping, "error.transaction.enrolment");

        if (isCancelled(request))
        {
            return mapping.findForward(forwards[3]);
        }

        DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoEnrolmentContext infoEnrolmentContext =
            (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

        Integer optionalCurricularCourseIndex = (Integer) enrolmentForm.get("optionalCourseIndex");

        InfoCurricularCourse infoCurricularCourse =
            (InfoCurricularCourse) infoEnrolmentContext
                .getInfoFinalCurricularCoursesSpanToBeEnrolled()
                .get(
                optionalCurricularCourseIndex.intValue());

        Integer optionalCourseChoosenIndex = (Integer) enrolmentForm.get("optionalCurricularCourse");

        if (optionalCourseChoosenIndex == null)
        {
            infoEnrolmentContext.getEnrolmentValidationResult().reset();
            infoEnrolmentContext.getEnrolmentValidationResult().setErrorMessage(
                EnrolmentValidationResult.NO_OPTIONAL_CURRICULAR_COURSES_TO_ENROLL);
            super.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
            return mapping.findForward(forwards[4]);
        }
        else
        {
            InfoCurricularCourse infoCurricularCourseOptional =
                (InfoCurricularCourse) infoEnrolmentContext
                    .getOptionalInfoCurricularCoursesToChooseFromDegree()
                    .get(
                    optionalCourseChoosenIndex.intValue());

            infoEnrolmentContext.setInfoChosenOptionalCurricularCourse(infoCurricularCourse);

            Object args[] = { infoEnrolmentContext, infoCurricularCourseOptional };

            infoEnrolmentContext =
                (InfoEnrolmentContext) ServiceUtils.executeService(
                    userView,
                    "SelectOptionalCurricularCourseWithoutRules",
                    args);

            session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);

            return mapping.findForward(forwards[0]);
        }
    }
}
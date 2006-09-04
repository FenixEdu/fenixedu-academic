package net.sourceforge.fenixedu.presentationTier.Action.sop.exams;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class DefineCommentAction extends
        FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoExamsMap infoExamsMap = getExamsMap(request);
        request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);

        Integer indexExecutionCourse = Integer.valueOf(request.getParameter("indexExecutionCourse"));

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) infoExamsMap
                .getExecutionCourses().get(indexExecutionCourse.intValue());

        Integer curricularYear = infoExecutionCourse.getCurricularYear();

        request.setAttribute(SessionConstants.CURRICULAR_YEAR_KEY, curricularYear);

        request.setAttribute(SessionConstants.EXECUTION_COURSE_KEY, infoExecutionCourse);

        request.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoExamsMap);

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);
        request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getIdInternal()
                .toString());

        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionCourse
                .getInfoExecutionPeriod().getIdInternal().toString());

        request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourse);
        request.setAttribute(SessionConstants.EXECUTION_COURSE_OID, infoExecutionCourse.getIdInternal()
                .toString());

        request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, curricularYear.toString());
        ContextUtils.setCurricularYearContext(request);

        return mapping.findForward("defineExamComment");
    }

    public ActionForward define(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaValidatorForm defineExamCommentForm = (DynaValidatorForm) form;

        String comment = (String) defineExamCommentForm.get("comment");
        String executionCourseCode = request.getParameter("executionCourseCode");
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object argsDefineComment[] = { executionCourseCode, infoExecutionPeriod.getIdInternal(), comment };
        try {
            ServiceUtils.executeService(userView, "DefineExamComment", argsDefineComment);
        } catch (FenixServiceException e) {
           ActionErrors actionErrors = new ActionErrors();
           actionErrors.add(e.getMessage(), new ActionError(e.getMessage()));
           saveErrors(request, actionErrors);           
        }
        return mapping.findForward("showExamsMap");
    }

    private InfoExamsMap getExamsMap(HttpServletRequest request) throws FenixActionException, FenixFilterException {
        final IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        final InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);

        final List curricularYears = (List) request.getAttribute(SessionConstants.CURRICULAR_YEARS_LIST);

        final InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        final Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
        InfoExamsMap infoExamsMap;
        try {
            infoExamsMap = (InfoExamsMap) ServiceUtils.executeService(userView, "ReadExamsMap", args);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return infoExamsMap;
    }

}
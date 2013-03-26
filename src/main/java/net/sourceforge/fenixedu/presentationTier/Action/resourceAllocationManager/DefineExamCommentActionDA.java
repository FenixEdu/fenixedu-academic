package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.DefineExamComment;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class DefineExamCommentActionDA extends
        FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        InfoExamsMap infoExamsMap = getExamsMap(request);
        request.setAttribute(PresentationConstants.INFO_EXAMS_MAP, infoExamsMap);

        Integer indexExecutionCourse = new Integer(request.getParameter("indexExecutionCourse"));

        InfoExecutionCourse infoExecutionCourse = infoExamsMap.getExecutionCourses().get(indexExecutionCourse.intValue());

        Integer curricularYear = infoExecutionCourse.getCurricularYear();

        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_KEY, curricularYear);

        request.setAttribute(PresentationConstants.EXECUTION_COURSE_KEY, infoExecutionCourse);

        request.setAttribute(PresentationConstants.INFO_EXAMS_KEY, infoExamsMap);

        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getIdInternal().toString());

        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionCourse.getInfoExecutionPeriod()
                .getIdInternal().toString());

        request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);
        request.setAttribute(PresentationConstants.EXECUTION_COURSE_OID, infoExecutionCourse.getIdInternal().toString());

        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_OID, curricularYear.toString());
        ContextUtils.setCurricularYearContext(request);

        return mapping.findForward("defineExamComment");
    }

    public ActionForward define(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaValidatorForm defineExamCommentForm = (DynaValidatorForm) form;

        String comment = (String) defineExamCommentForm.get("comment");
        String executionCourseCode = request.getParameter("executionCourseCode");
        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        // Define comment

        try {
            DefineExamComment.run(executionCourseCode, infoExecutionPeriod.getIdInternal(), comment);
        } catch (ExistingServiceException ex) {
            throw new ExistingActionException("O comentario do exame", ex);
        }

        return mapping.findForward("showExamsMap");
    }

    private InfoExamsMap getExamsMap(HttpServletRequest request) throws FenixActionException, FenixFilterException {
        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

        List curricularYears = (List) request.getAttribute(PresentationConstants.CURRICULAR_YEARS_LIST);

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        InfoExamsMap infoExamsMap = ReadExamsMap.run(infoExecutionDegree, curricularYears, infoExecutionPeriod);
        return infoExamsMap;
    }

}
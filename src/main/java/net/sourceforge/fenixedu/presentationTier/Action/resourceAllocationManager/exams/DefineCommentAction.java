package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.DefineExamComment;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
@Mapping(module = "resourceAllocationManager", path = "/defineComment", input = "/defineComment.do?page=0",
        attribute = "examCommentForm", formBean = "examCommentForm", scope = "request", validate = false, parameter = "method")
@Forwards(value = { @Forward(name = "defineExamComment", path = "df.page.defineComment"),
        @Forward(name = "showExamsMap", path = "/showExamsManagement.do?method=view") })
public class DefineCommentAction extends
        FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        InfoExamsMap infoExamsMap = getExamsMap(request);
        request.setAttribute(PresentationConstants.INFO_EXAMS_MAP, infoExamsMap);

        Integer indexExecutionCourse = Integer.valueOf(request.getParameter("indexExecutionCourse"));

        InfoExecutionCourse infoExecutionCourse = infoExamsMap.getExecutionCourses().get(indexExecutionCourse.intValue());

        Integer curricularYear = infoExecutionCourse.getCurricularYear();

        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_KEY, curricularYear);

        request.setAttribute(PresentationConstants.EXECUTION_COURSE_KEY, infoExecutionCourse);

        request.setAttribute(PresentationConstants.INFO_EXAMS_KEY, infoExamsMap);

        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getExternalId().toString());

        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionCourse.getInfoExecutionPeriod()
                .getExternalId().toString());

        request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);
        request.setAttribute(PresentationConstants.EXECUTION_COURSE_OID, infoExecutionCourse.getExternalId().toString());

        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_OID, curricularYear.toString());
        ContextUtils.setCurricularYearContext(request);

        return mapping.findForward("defineExamComment");
    }

    public ActionForward define(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();

        DynaValidatorForm defineExamCommentForm = (DynaValidatorForm) form;

        String comment = (String) defineExamCommentForm.get("comment");
        String executionCourseCode = request.getParameter("executionCourseCode");
        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        try {
            DefineExamComment.run(executionCourseCode, infoExecutionPeriod.getExternalId(), comment);
        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(e.getMessage(), new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);
        }
        return mapping.findForward("showExamsMap");
    }

    private InfoExamsMap getExamsMap(HttpServletRequest request) throws FenixActionException {
        final User userView = getUserView(request);

        final InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

        final List curricularYears = (List) request.getAttribute(PresentationConstants.CURRICULAR_YEARS_LIST);

        final InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        InfoExamsMap infoExamsMap = ReadExamsMap.run(infoExecutionDegree, curricularYears, infoExecutionPeriod);
        return infoExamsMap;
    }

}
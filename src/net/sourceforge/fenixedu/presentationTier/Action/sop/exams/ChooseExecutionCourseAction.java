/**
 * Project Sop 
 * 
 * Created on 30/Out/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.sop.exams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Ana e Ricardo
 * 
 *  
 */
public class ChooseExecutionCourseAction
//extends
        // FenixDateAndTimeAndClassAndExecutionDegreeAndCurricularYearContextAction
        // {
        extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        SessionUtils.getExecutionCourses(request);

        String nextPage = request.getParameter("nextPage");
        request.setAttribute(SessionConstants.NEXT_PAGE, nextPage);
        return mapping.findForward("showForm");

    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;

        Integer executionCourseID = new Integer((String) chooseCourseForm.get("executionCourseID"));

        request.setAttribute(SessionConstants.EXECUTION_COURSE_OID, executionCourseID.toString());

        ContextUtils.setCurricularYearContext(request);
        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setExecutionPeriodContext(request);
        ContextUtils.setCurricularYearsContext(request);

        return mapping.findForward("forwardChoose");
    }

}
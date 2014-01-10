/**
 * Project Sop 
 * 
 * Package presentationTier.Action.sop.utils
 * 
 * Created on 9/Dez/2002
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixDateAndTimeAndClassAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author jpvl
 * 
 * 
 */
public class ChooseExecutionCourseAction extends FenixDateAndTimeAndClassAndExecutionDegreeAndCurricularYearContextAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        super.execute(mapping, form, request, response);

        DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;

        final InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        final String courseInitials = (String) chooseCourseForm.get("courseInitials");
        Integer page = (Integer) chooseCourseForm.get("page");

        if (courseInitials != null && !courseInitials.equals("")) {
            final ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());
            final ExecutionCourse executionCourse = executionSemester.getExecutionCourseByInitials(courseInitials);
            final InfoExecutionCourse infoCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);

            request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoCourse);
            return mapping.findForward("forwardChoose");
        }
        if (page != null && page.intValue() > 1) {
            request.removeAttribute(PresentationConstants.EXECUTION_COURSE);
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("label.choose.executionCourse", new ActionError("label.choose.executionCourse"));
            saveErrors(request, actionErrors);
        }
        return mapping.findForward("showForm");

    }

}
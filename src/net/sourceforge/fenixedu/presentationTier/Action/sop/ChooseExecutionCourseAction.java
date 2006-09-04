/**
 * Project Sop 
 * 
 * Package presentationTier.Action.sop.utils
 * 
 * Created on 9/Dez/2002
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixDateAndTimeAndClassAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author jpvl
 * 
 * 
 */
public class ChooseExecutionCourseAction extends
	FenixDateAndTimeAndClassAndExecutionDegreeAndCurricularYearContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	super.execute(mapping, form, request, response);

	DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;

	final InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
		.getAttribute(SessionConstants.EXECUTION_PERIOD);

	final String courseInitials = (String) chooseCourseForm.get("courseInitials");
	Integer page = (Integer) chooseCourseForm.get("page");

	if (courseInitials != null && !courseInitials.equals("")) {
	    final ExecutionPeriod executionPeriod = RootDomainObject.getInstance()
		    .readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());
	    final ExecutionCourse executionCourse = executionPeriod
		    .getExecutionCourseByInitials(courseInitials);
	    final InfoExecutionCourse infoCourse = InfoExecutionCourse
		    .newInfoFromDomain(executionCourse);

	    request.setAttribute(SessionConstants.EXECUTION_COURSE, infoCourse);
	    return mapping.findForward("forwardChoose");
	}
	if (page != null && page.intValue() > 1) {
	    request.removeAttribute(SessionConstants.EXECUTION_COURSE);
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("label.choose.executionCourse", new ActionError(
		    "label.choose.executionCourse"));
	    saveErrors(request, actionErrors);
	}
	return mapping.findForward("showForm");

    }

}
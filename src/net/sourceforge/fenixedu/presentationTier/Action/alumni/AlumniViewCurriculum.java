package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.CurriculumDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AlumniViewCurriculum extends CurriculumDispatchAction {

    public ActionForward checkValidation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (getLoggedPerson(request).getStudent().hasAlumni()) {

	    if (getLoggedPerson(request).getStudent().getAlumni().hasAnyPendingIdentityRequests()) {
		return mapping.findForward("alumni.view.curriculum.validate.identity");
	    }
	    return super.prepare(mapping, form, request, response);
	} else {
	    if (getLoggedPerson(request).hasRole(RoleType.ALUMNI)) {
		return super.prepare(mapping, form, request, response);
	    } else {
		return mapping.findForward("alumni.view.curriculum.not.authorized");
	    }
	}
    }
}

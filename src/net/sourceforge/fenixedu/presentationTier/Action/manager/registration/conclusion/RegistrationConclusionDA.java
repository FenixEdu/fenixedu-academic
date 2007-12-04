package net.sourceforge.fenixedu.presentationTier.Action.manager.registration.conclusion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class RegistrationConclusionDA extends FenixDispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final Registration registration = getRegistration(request);
	if (registration.isBolonha()) {
	    request.setAttribute("student", registration.getStudent());
	    request.setAttribute("registrationConclusionBeans", buildRegistrationConclusionBeansForCycles(registration));
	    return mapping.findForward("showByCycles");
	} else {
	    request.setAttribute("registrationConclusionBean", new RegistrationConclusionBean(registration));
	    return mapping.findForward("showForRegistration");
	}

    }

    private List<RegistrationConclusionBean> buildRegistrationConclusionBeansForCycles(final Registration registration) {
	final List<RegistrationConclusionBean> result = new ArrayList<RegistrationConclusionBean>();
	for (final CycleCurriculumGroup cycleCurriculumGroup : registration.getLastStudentCurricularPlan()
		.getInternalCycleCurriculumGrops()) {
	    result.add(new RegistrationConclusionBean(registration, cycleCurriculumGroup));
	}

	return result;
    }

    private Registration getRegistration(final HttpServletRequest request) {
	return rootDomainObject.readRegistrationByOID(getIntegerFromRequest(request, "registrationId"));
    }

    public ActionForward prepareEditForRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("registration", getRegistration(request));

	return mapping.findForward("editForRegistration");
    }

    public ActionForward prepareRemoveConclusionForRegistration(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("registration", getRegistration(request));

	return mapping.findForward("confirmRemoveConclusion");
    }

    public ActionForward prepareEditForCycle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("cycleCurriculumGroup", getCycleCurriculumGroup(request));

	return mapping.findForward("editForCycle");
    }

    private CycleCurriculumGroup getCycleCurriculumGroup(HttpServletRequest request) {
	return (CycleCurriculumGroup) rootDomainObject.readCurriculumModuleByOID(getIntegerFromRequest(request,
		"cycleCurriculumGroupId"));
    }
}

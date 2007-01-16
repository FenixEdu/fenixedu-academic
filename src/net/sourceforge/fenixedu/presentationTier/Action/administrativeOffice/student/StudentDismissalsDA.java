package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.SetEmail.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.DismissalClass;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.DismissalType;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedExternalEnrolment;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class StudentDismissalsDA extends FenixDispatchAction {
    
    private StudentCurricularPlan getSCP(final HttpServletRequest request) {
	final Integer scpID = getIntegerFromRequest(request, "scpID");
	return rootDomainObject.readStudentCurricularPlanByOID(scpID);
    }
    
    public ActionForward manage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	final DismissalBean dismissalBean = new DismissalBean();
	dismissalBean.setStudentCurricularPlan(getSCP(request));
	request.setAttribute("dismissalBean", dismissalBean);
	return mapping.findForward("manage");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	final DismissalBean dismissalBean = new DismissalBean();
	dismissalBean.setStudentCurricularPlan(getSCP(request));
	dismissalBean.setDismissalClass(DismissalClass.CREDITS);
	request.setAttribute("dismissalBean", dismissalBean);
	
	Collection<SelectedEnrolment> enrolments = new HashSet<SelectedEnrolment>();
	for (StudentCurricularPlan studentCurricularPlan : dismissalBean.getStudentCurricularPlan().getRegistration().getStudent().getAllStudentCurricularPlans()) {
	    for (Enrolment enrolment : studentCurricularPlan.getAprovedEnrolments()) {
		enrolments.add(new DismissalBean.SelectedEnrolment(enrolment));
	    }
	}
	dismissalBean.setEnrolments(enrolments);
	
	Collection<SelectedExternalEnrolment> externalEnrolments = new HashSet<SelectedExternalEnrolment>();
	for (ExternalEnrolment externalEnrolment : dismissalBean.getStudentCurricularPlan().getRegistration().getStudent().getExternalEnrolmentsSet()) {
	    externalEnrolments.add(new DismissalBean.SelectedExternalEnrolment(externalEnrolment));
	}
	dismissalBean.setExternalEnrolments(externalEnrolments);
	
	return mapping.findForward("chooseDismissalEnrolments");
    }
    
    public ActionForward dismissalTypePostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
	final DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	dismissalBean.setCredits(null);
	dismissalBean.setCurriculumGroup(null);
	dismissalBean.setDismissals(null);
	
        RenderUtils.invalidateViewState();
        request.setAttribute("dismissalBean", dismissalBean);
        
        return mapping.findForward("chooseEquivalents");
    }
    
    public ActionForward dismissalClassPostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
	final DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	dismissalBean.setGrade(null);
	
        RenderUtils.invalidateViewState();
        request.setAttribute("dismissalBean", dismissalBean);
        
        return mapping.findForward("chooseDismissalEnrolments");
    }
    
    public ActionForward chooseEquivalents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DismissalBean dismissalBean = (DismissalBean) getRenderedObject("dismissalBean");
	dismissalBean.setDismissalType(DismissalType.CURRICULUM_GROUP_CREDITS);
	request.setAttribute("dismissalBean", dismissalBean);
	return mapping.findForward("chooseEquivalents");
    }
    
    public ActionForward prepareChooseCreditsDismissalEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareChooseDismissalEnrolments(mapping, form, request, response, DismissalClass.CREDITS);
    }
    
    public ActionForward prepareChooseEquivalenceDismissalEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareChooseDismissalEnrolments(mapping, form, request, response, DismissalClass.EQUIVALENCE);
    }

    private ActionForward prepareChooseDismissalEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, DismissalClass dismissalClass) {
	DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	dismissalBean.setDismissalClass(dismissalClass);
	request.setAttribute("dismissalBean", dismissalBean);
	
	try {
	    checkArguments(request, dismissalBean);
	} catch (FenixActionException e) {
	    return mapping.findForward("chooseDismissalEnrolments");
	}
	
	Collection<SelectedEnrolment> enrolments = new HashSet<SelectedEnrolment>();
	for (StudentCurricularPlan studentCurricularPlan : dismissalBean.getStudentCurricularPlan().getRegistration().getStudent().getAllStudentCurricularPlans()) {
	    for (Enrolment enrolment : studentCurricularPlan.getAprovedEnrolments()) {
		enrolments.add(new DismissalBean.SelectedEnrolment(enrolment));
	    }
	}
	dismissalBean.setEnrolments(enrolments);
	
	return mapping.findForward("chooseDismissalEnrolments");
    }

    private void checkArguments(HttpServletRequest request, DismissalBean dismissalBean) throws FenixActionException {
	if(dismissalBean.getDismissalType() == DismissalType.CURRICULAR_COURSE_CREDITS) {
	    if(dismissalBean.getDismissals() == null || dismissalBean.getDismissals().isEmpty()) {
		addActionMessage("error", request, "error.studentDismissal.curricularCourse.required");
		throw new FenixActionException();
	    }
	} else {
	    if(dismissalBean.getCurriculumGroup() == null) {
		addActionMessage("error", request, "error.studentDismissal.curriculumGroup.required");
		throw new FenixActionException();
	    }
	}
    }
    
    public ActionForward stepOne(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	request.setAttribute("dismissalBean", dismissalBean);
	return mapping.findForward("chooseDismissalEnrolments");
    }
    
    public ActionForward stepTwo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	request.setAttribute("dismissalBean", dismissalBean);
	return mapping.findForward("chooseEquivalents");
    }

    
    public ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	request.setAttribute("dismissalBean", dismissalBean);
	return mapping.findForward("manage");
    }
    
    public ActionForward createDismissals(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	
	try {
	    checkArguments(request, dismissalBean);
	} catch (FenixActionException e) {
	    return stepTwo(mapping, form, request, response);
	}

	try {
	    if (dismissalBean.getDismissalClass().equals(DismissalClass.CREDITS)) {

		executeService("CreateNewCreditsDismissal", new Object[] { dismissalBean.getStudentCurricularPlan(),
			dismissalBean.getCurriculumGroup(), dismissalBean.getDismissals(),
			dismissalBean.getSelectedEnrolments(), dismissalBean.getCredits() });

	    } else {
		executeService("CreateNewEquivalenceDismissal", new Object[] { dismissalBean.getStudentCurricularPlan(),
			dismissalBean.getCurriculumGroup(), dismissalBean.getDismissals(),
			dismissalBean.getSelectedEnrolments(), dismissalBean.getCredits(),
			dismissalBean.getGrade() });
	    }
	} catch (NotAuthorizedException e) {
	    
	} catch (DomainException e) {
	    
	}

	return back(mapping, form, request, response);
    }
    
    public ActionForward deleteCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	String[] creditsIDs = ((DynaActionForm) form).getStrings("creditsToDelete");
	final DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	
	try {
	    executeService(request, "DeleteCredits", new Object[] { dismissalBean.getStudentCurricularPlan(), creditsIDs });
	} catch (DomainException e) {
	    
	}
	
	request.setAttribute("dismissalBean", dismissalBean);
	return mapping.findForward("manage");
    }
    
    public ActionForward backViewRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	request.setAttribute("registrationId", dismissalBean.getStudentCurricularPlan().getRegistration().getIdInternal().toString());
	return mapping.findForward("visualizeRegistration");
    }
}

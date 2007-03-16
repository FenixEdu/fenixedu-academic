package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
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

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class StudentDismissalsDA extends FenixDispatchAction {
    
    private StudentCurricularPlan getSCP(final HttpServletRequest request) {
	final Integer scpID = getIntegerFromRequest(request, "scpID");
	return rootDomainObject.readStudentCurricularPlanByOID(scpID);
    }
    
    public ActionForward manage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("studentCurricularPlan", getSCP(request));
	return mapping.findForward("manage");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	final DismissalBean dismissalBean = new DismissalBean();
	dismissalBean.setStudentCurricularPlan(getSCP(request));
	request.setAttribute("dismissalBean", dismissalBean);
	dismissalBean.setEnrolments(buildStudentEnrolmentsInformation(dismissalBean));
	dismissalBean.setExternalEnrolments(buildStudentExternalEnrolmentsInformation(dismissalBean));
	
	return mapping.findForward("chooseDismissalEnrolments");
    }

    private Collection<SelectedExternalEnrolment> buildStudentExternalEnrolmentsInformation(final DismissalBean dismissalBean) {
	final Collection<SelectedExternalEnrolment> externalEnrolments = new HashSet<SelectedExternalEnrolment>();
	for (final ExternalEnrolment externalEnrolment : dismissalBean.getStudentCurricularPlan().getRegistration().getStudent().getSortedExternalEnrolments()) {
	    externalEnrolments.add(new DismissalBean.SelectedExternalEnrolment(externalEnrolment));
	}
	return externalEnrolments;
    }

    private Collection<SelectedEnrolment> buildStudentEnrolmentsInformation(final DismissalBean dismissalBean) {
	final Collection<SelectedEnrolment> enrolments = new HashSet<SelectedEnrolment>();
	for (final StudentCurricularPlan studentCurricularPlan : dismissalBean.getStudentCurricularPlan().getRegistration().getStudent().getAllStudentCurricularPlans()) {
	    final List<Enrolment> approvedEnrolments = new ArrayList<Enrolment>(studentCurricularPlan.getAprovedEnrolments());
	    Collections.sort(approvedEnrolments, new BeanComparator("name"));
	    for (Enrolment enrolment : approvedEnrolments) {
		enrolments.add(new DismissalBean.SelectedEnrolment(enrolment));
	    }
	}
	return enrolments;
    }

    public ActionForward chooseEquivalents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DismissalBean dismissalBean = (DismissalBean) getRenderedObject("dismissalBean");
	dismissalBean.setDismissalType(DismissalType.CURRICULAR_COURSE_CREDITS);
	request.setAttribute("dismissalBean", dismissalBean);
	return mapping.findForward("chooseEquivalents");
    }
    
    public ActionForward dismissalTypePostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
	final DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	dismissalBean.setCredits(null);
	dismissalBean.setCourseGroup(null);
	dismissalBean.setDismissals(null);
	
        RenderUtils.invalidateViewState();
        request.setAttribute("dismissalBean", dismissalBean);
        
        return mapping.findForward("chooseEquivalents");
    }
    
    private void checkArguments(HttpServletRequest request, DismissalBean dismissalBean) throws FenixActionException {
	if(dismissalBean.getDismissalType() == DismissalType.CURRICULAR_COURSE_CREDITS) {
	    if(dismissalBean.getDismissals() == null || dismissalBean.getDismissals().isEmpty()) {
		addActionMessage("error", request, "error.studentDismissal.curricularCourse.required");
		throw new FenixActionException();
	    }
	} else {
	    if(dismissalBean.getCourseGroup() == null) {
		addActionMessage("error", request, "error.studentDismissal.curriculumGroup.required");
		throw new FenixActionException();
	    }
	}
    }
    
    public ActionForward confirmCreateDismissals(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final DismissalBean dismissalBean = (DismissalBean) getRenderedObject();
	request.setAttribute("dismissalBean", dismissalBean);
	
	try {
	    checkArguments(request, dismissalBean);
	} catch (FenixActionException e) {
	    return stepTwo(mapping, actionForm, request, response);
	}
	
	return mapping.findForward("confirmCreateDismissals");
    }
    
    public ActionForward stepOne(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("dismissalBean", getRenderedObject());
	return mapping.findForward("chooseDismissalEnrolments");
    }
    
    public ActionForward stepTwo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("dismissalBean", getRenderedObject());
	return mapping.findForward("chooseEquivalents");
    }

    public ActionForward stepThree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("dismissalBean", getRenderedObject());
	return mapping.findForward("confirmCreateDismissals");
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
	    if (StringUtils.isEmpty(dismissalBean.getGrade())) {

		executeService("CreateNewCreditsDismissal", new Object[] { dismissalBean.getStudentCurricularPlan(),
			dismissalBean.getCourseGroup(), dismissalBean.getDismissals(),
			dismissalBean.getSelectedEnrolments(), dismissalBean.getCredits() });

	    } else {
		
		executeService("CreateNewEquivalenceDismissal", new Object[] { dismissalBean.getStudentCurricularPlan(),
			dismissalBean.getCourseGroup(), dismissalBean.getDismissals(),
			dismissalBean.getSelectedEnrolments(), dismissalBean.getCredits(),
			dismissalBean.getGrade() });
	    }
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return confirmCreateDismissals(mapping, form, request, response);
	}

	return manage(mapping, form, request, response);
    }
    
    public ActionForward deleteCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
	String[] creditsIDs = ((DynaActionForm) form).getStrings("creditsToDelete");
	final StudentCurricularPlan studentCurricularPlan = getSCP(request);
	
	try {
	    executeService(request, "DeleteCredits", new Object[] { studentCurricularPlan, creditsIDs });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}
	
	request.setAttribute("studentCurricularPlan", studentCurricularPlan);
	return mapping.findForward("manage");
    }
    
    public ActionForward backViewRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("registrationId", getSCP(request).getRegistration().getIdInternal().toString());
	return mapping.findForward("visualizeRegistration");
    }
}

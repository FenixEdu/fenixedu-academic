package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.DismissalClass;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.DismissalType;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedExternalEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalEnrolmentBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.student.Student;
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
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    return stepTwo(mapping, form, request, response);
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
	    addActionMessage(request, e.getMessage());
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
    
    public ActionForward chooseExternalCurricularCourse(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("studentCurricularPlan", getSCP(request));
	request.setAttribute("unit", UnitUtils.readEarthUnit());
	return mapping.findForward("chooseExternalCurricularCourse");
    }
    
    public ActionForward prepareCreateExternalEnrolment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("studentCurricularPlan", getSCP(request));
	request.setAttribute("externalEnrolmentBean", new CreateExternalEnrolmentBean(getExternalCurricularCourse(request)));
	return mapping.findForward("prepareCreateExternalEnrolment");
    }
    
    private ExternalCurricularCourse getExternalCurricularCourse(final HttpServletRequest request) {
	return rootDomainObject.readExternalCurricularCourseByOID(getIntegerFromRequest(request, "oid"));
    }
    
    public ActionForward createExternalEnrolment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateExternalEnrolmentBean externalEnrolmentBean = (CreateExternalEnrolmentBean) getRenderedObject();
	final StudentCurricularPlan studentCurricularPlan = getSCP(request); 
	final Student student = studentCurricularPlan.getRegistration().getStudent();
	
	try {
	    executeService("CreateExternalEnrolment", new Object[] {externalEnrolmentBean, student});
	    request.setAttribute("scpID", studentCurricularPlan.getIdInternal());
	    return manage(mapping, actionForm, request, response);
	    
	} catch (final NotAuthorizedException e) {
	    addActionMessage("error", request, "error.notAuthorized");
	} catch (final DomainException e) {
	    addActionMessage("error", request, e.getMessage());
	}

	request.setAttribute("externalEnrolmentBean", externalEnrolmentBean);
	return mapping.findForward("prepareCreateExternalEnrolment");
    }
}

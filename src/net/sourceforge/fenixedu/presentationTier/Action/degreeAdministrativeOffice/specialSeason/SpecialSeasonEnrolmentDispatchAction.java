package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.specialSeason;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.EnrolmentException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment.SpecialSeasonEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment.SpecialSeasonToEnrolBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SpecialSeasonEnrolmentDispatchAction extends FenixDispatchAction {
	
    public ActionForward prepareChooseStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
    	SpecialSeasonEnrolmentBean specialSeasonEnrolmentBean = new SpecialSeasonEnrolmentBean();
    	ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
    	specialSeasonEnrolmentBean.setExecutionYear(executionYear);
    	request.setAttribute("bean", specialSeasonEnrolmentBean);
    	return mapping.findForward("prepareChooseStudent");
    }

    
    private ActionForward specialSeasonEnrolments(SpecialSeasonEnrolmentBean specialSeasonEnrolmentBean, ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
    	
    	specialSeasonEnrolmentBean.setSpecialSeasonCode(specialSeasonEnrolmentBean.getStudent().getSpecialSeasonCodeByExecutionYear(specialSeasonEnrolmentBean.getExecutionYear()));
    	
    	Set<SpecialSeasonToEnrolBean> toEnrol = new HashSet<SpecialSeasonToEnrolBean>();
    	for (Enrolment enrolment : specialSeasonEnrolmentBean.getStudent().getActiveStudentCurricularPlan().getSpecialSeasonToEnrol(specialSeasonEnrolmentBean.getExecutionYear())) {
			SpecialSeasonToEnrolBean enrolmentBean = new SpecialSeasonToEnrolBean();
			enrolmentBean.setEnrolment(enrolment);
			if(enrolment.getEnrollmentState() == EnrollmentState.ENROLLED) {
				enrolmentBean.setEnrolmentCondition(EnrollmentCondition.TEMPORARY);
			} else {
				enrolmentBean.setEnrolmentCondition(EnrollmentCondition.FINAL);
			}
			toEnrol.add(enrolmentBean);
		}
    	
    	Set<SpecialSeasonToEnrolBean> alreadyEnroled = new HashSet<SpecialSeasonToEnrolBean>();
    	for (Enrolment enrolment : specialSeasonEnrolmentBean.getStudent().getActiveStudentCurricularPlan().getSpecialSeasonEnrolments(specialSeasonEnrolmentBean.getExecutionYear())) {
			SpecialSeasonToEnrolBean enrolmentBean = new SpecialSeasonToEnrolBean();
			enrolmentBean.setEnrolment(enrolment);
			alreadyEnroled.add(enrolmentBean);
		}
    	
    	specialSeasonEnrolmentBean.setSpecialSeasonToEnrol(toEnrol);
    	specialSeasonEnrolmentBean.setSpecialSeasonAlreadyEnroled(alreadyEnroled);
    	
    	request.setAttribute("bean", specialSeasonEnrolmentBean);
    	
    	return mapping.findForward("viewSpecialSeasonEnrolments");
    }
    
    public ActionForward viewSpecialSeasonEnrolments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
    	SpecialSeasonEnrolmentBean specialSeasonEnrolmentBean = (SpecialSeasonEnrolmentBean) RenderUtils.getViewState().getMetaObject().getObject();

    	Registration student = Registration.readStudentByNumberAndDegreeType(specialSeasonEnrolmentBean.getStudentNumber(), DegreeType.DEGREE);
    	
    	if(student == null) {
    		addActionMessage(request, "error.student.notExist", specialSeasonEnrolmentBean.getStudentNumber().toString());
    		request.setAttribute("bean", specialSeasonEnrolmentBean);
    		return mapping.findForward("prepareChooseStudent");
    	}
    	
    	specialSeasonEnrolmentBean.setStudent(student);
    		
    	return specialSeasonEnrolments(specialSeasonEnrolmentBean, mapping, form, request, response);
    }
    
    public ActionForward createSpecialSeasonEnrolments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
    	SpecialSeasonEnrolmentBean specialSeasonEnrolmentBean = (SpecialSeasonEnrolmentBean) RenderUtils.getViewState("enrol").getMetaObject().getObject();
    	RenderUtils.invalidateViewState();
    	
    	Collection<Enrolment> specialSeasonToEnrolSubmited = specialSeasonEnrolmentBean.getSpecialSeasonToEnrolSubmited();
    	if(!specialSeasonToEnrolSubmited.isEmpty()) {
    		try {
				ServiceUtils.executeService(getUserView(request), "CreateSpecialSeasonEvaluations", new Object[] {specialSeasonEnrolmentBean.getStudent(), specialSeasonEnrolmentBean.getExecutionYear(), specialSeasonToEnrolSubmited});
			} catch (EnrolmentException e) {
				addActionMessage(request, e.getMessage(), e.getArgs());
			} 
    	}
    	
    	return specialSeasonEnrolments(specialSeasonEnrolmentBean, mapping, form, request, response);
    }
    
    public ActionForward deleteSpecialSeasonEnrolments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
    	SpecialSeasonEnrolmentBean specialSeasonEnrolmentBean = (SpecialSeasonEnrolmentBean) RenderUtils.getViewState("unenrol").getMetaObject().getObject();
    	RenderUtils.invalidateViewState();
    	
    	Collection<Enrolment> specialSeasonAlreadyEnroledSubmited = specialSeasonEnrolmentBean.getSpecialSeasonAlreadyEnroledSubmited();
    	if(!specialSeasonAlreadyEnroledSubmited.isEmpty()) {
    		try {
				ServiceUtils.executeService(getUserView(request), "DeleteSpecialSeasonEvaluations", new Object[] {specialSeasonAlreadyEnroledSubmited});
			} catch (NotAuthorizedFilterException e) {
				// TODO Auto-generated catch block
				
			} catch (DomainException e) {
				// TODO Auto-generated catch block
				
			}
    	}
    	
    	return specialSeasonEnrolments(specialSeasonEnrolmentBean, mapping, form, request, response);
    }
    
    public ActionForward changeSpecialSeasonCodePostBack(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        
    	SpecialSeasonEnrolmentBean specialSeasonEnrolmentBean = (SpecialSeasonEnrolmentBean) RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        
        try {
			ServiceUtils.executeService(getUserView(request), "ChangeSpecialSeasonCode", new Object[] {specialSeasonEnrolmentBean.getStudent(), specialSeasonEnrolmentBean.getExecutionYear(), specialSeasonEnrolmentBean.getSpecialSeasonCode()});
		} catch (DomainException e) {
			addActionMessage(request, e.getMessage(), null);
		}
        
		return specialSeasonEnrolments(specialSeasonEnrolmentBean, mapping, form, request, response);
    }

}

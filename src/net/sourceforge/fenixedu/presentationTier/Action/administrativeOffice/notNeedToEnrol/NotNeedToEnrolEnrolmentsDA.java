package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.notNeedToEnrol;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.notNeedToEnrol.NotNeedToEnrolEnrolmentsBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.notNeedToEnrol.NotNeedToEnrolEnrolmentsBean.SelectedAprovedEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.notNeedToEnrol.NotNeedToEnrolEnrolmentsBean.SelectedExternalEnrolment;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class NotNeedToEnrolEnrolmentsDA extends FenixDispatchAction{

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	NotNeedToEnrolEnrolmentsBean bean = new NotNeedToEnrolEnrolmentsBean();
	request.setAttribute("bean", bean);
	return mapping.findForward("chooseStudent");
    }
    
    public ActionForward readNotNeedToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	NotNeedToEnrolEnrolmentsBean bean = (NotNeedToEnrolEnrolmentsBean) getRenderedObject();
	Student student = Student.readStudentByNumber(bean.getNumber());
	if(student == null) {
	    addActionMessage("error", request, "error.invalid.student.number", bean.getNumber().toString());
	    RenderUtils.invalidateViewState();
	    return prepare(mapping, form, request, response);
	}
	
	bean.setStudent(student);
	
	return readNotNeedToEnrol(mapping, form, request, response, bean);
    }
    
    public ActionForward chooseNotNeedToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	NotNeedToEnrolEnrolmentsBean bean = (NotNeedToEnrolEnrolmentsBean) getRenderedObject();
	NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (NotNeedToEnrollInCurricularCourse) bean.getSelected();
	
	Collection<SelectedAprovedEnrolment> aprovedEnrolments = new ArrayList<SelectedAprovedEnrolment>();
	for (Enrolment enrolment : bean.getStudent().getDismissalApprovedEnrolments()) {
	    aprovedEnrolments.add(new SelectedAprovedEnrolment(enrolment, notNeedToEnrollInCurricularCourse.getEnrolments().contains(enrolment)));
	}
	bean.setAprovedEnrolments(aprovedEnrolments);
	
	Collection<SelectedExternalEnrolment> externalEnrolments = new ArrayList<SelectedExternalEnrolment>();
	for (final Registration registration : bean.getStudent().getRegistrations()) {
	    if(registration.hasAnyExternalEnrolments()){
		for (ExternalEnrolment enrolment : registration.getExternalEnrolmentsSet()) {
		    externalEnrolments.add(new SelectedExternalEnrolment(enrolment,
			    notNeedToEnrollInCurricularCourse.getExternalEnrolmentsSet().contains(
				    enrolment)));
		}
	    }
	}
	
	if(!externalEnrolments.isEmpty()){
	    bean.setExternalEnrolments(externalEnrolments);
	}
	
	request.setAttribute("bean", bean);
	
	return mapping.findForward("showAprovedEnrolments");
    }
    
    public ActionForward editNotNeedToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	NotNeedToEnrolEnrolmentsBean bean = (NotNeedToEnrolEnrolmentsBean) getRenderedObject("notNeedToEnrolBean");
	
	executeService(request, "AssociateEnrolmentsToNotNeedToEnrol", 
		new Object[] {bean.getStudent(), bean.getSelected(), bean.getSelectedAprovedEnrolments(), bean.getSelectedExternalEnrolments()});
	
	return readNotNeedToEnrol(mapping, form, request, response, bean);
    }
    
    private ActionForward readNotNeedToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, NotNeedToEnrolEnrolmentsBean notNeedToEnrolEnrolmentsBean) {
	List<DomainObject> notNeedToEnrolList = new ArrayList<DomainObject>();
	for (Registration registration : notNeedToEnrolEnrolmentsBean.getStudent().getActiveRegistrations()) {
	    for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
		notNeedToEnrolList.addAll(studentCurricularPlan.getNotNeedToEnrollCurricularCourses());
	    }
	}
	
	if(notNeedToEnrolList.isEmpty()) {
	    addActionMessage("error", request, "error.notNeedToEnrol.empty", notNeedToEnrolEnrolmentsBean.getNumber().toString());
	    RenderUtils.invalidateViewState();
	    return prepare(mapping, form, request, response);
	}
	
	Collections.sort(notNeedToEnrolList, new BeanComparator("curricularCourse.name", Collator.getInstance()));
	notNeedToEnrolEnrolmentsBean.setObjects(notNeedToEnrolList);
	request.setAttribute("bean", notNeedToEnrolEnrolmentsBean);
	
	return mapping.findForward("chooseNotNeedToEnrol");
    }
    
    public ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	NotNeedToEnrolEnrolmentsBean bean = (NotNeedToEnrolEnrolmentsBean) getRenderedObject("notNeedToEnrolBean");
	return readNotNeedToEnrol(mapping, form, request, response, bean);
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	NotNeedToEnrolEnrolmentsBean bean = (NotNeedToEnrolEnrolmentsBean) getRenderedObject("notNeedToEnrolBeanDelete");
	executeService(request, "DeleteNotNeedToEnrollInCurricularCourse", 
		new Object[] {bean.getSelected().getIdInternal()});
	return readNotNeedToEnrol(mapping, form, request, response, bean);
    }    
}

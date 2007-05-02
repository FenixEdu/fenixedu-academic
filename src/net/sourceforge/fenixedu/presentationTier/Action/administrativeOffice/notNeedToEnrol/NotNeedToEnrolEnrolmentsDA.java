package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.notNeedToEnrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.notNeedToEnrol.NotNeedToEnrolEnrolmentsBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.notNeedToEnrol.NotNeedToEnrolEnrolmentsBean.SelectedAprovedEnrolment;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.tests.Render;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

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
	for (Enrolment enrolment : bean.getStudent().getApprovedEnrolments()) {
	    aprovedEnrolments.add(new SelectedAprovedEnrolment(enrolment, notNeedToEnrollInCurricularCourse.getEnrolments().contains(enrolment)));
	}
	bean.setAprovedEnrolments(aprovedEnrolments);
	
	request.setAttribute("bean", bean);
	
	return mapping.findForward("showAprovedEnrolments");
    }
    
    public ActionForward editNotNeedToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	NotNeedToEnrolEnrolmentsBean bean = (NotNeedToEnrolEnrolmentsBean) getRenderedObject("notNeedToEnrolBean");
	
	executeService(request, "AssociateEnrolmentsToNotNeedToEnrol", 
		new Object[] {bean.getStudent(), bean.getSelected(), bean.getSelectedAprovedEnrolments()});
	
	return readNotNeedToEnrol(mapping, form, request, response, bean);
    }
    
    private ActionForward readNotNeedToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, NotNeedToEnrolEnrolmentsBean notNeedToEnrolEnrolmentsBean) {
	List<DomainObject> notNeedToEnrolList = new ArrayList<DomainObject>();
	for (Registration registration : notNeedToEnrolEnrolmentsBean.getStudent().getActiveRegistrations()) {
	    notNeedToEnrolList.addAll(registration.getActiveStudentCurricularPlan().getNotNeedToEnrollCurricularCourses());
	}
	
	notNeedToEnrolEnrolmentsBean.setObjects(notNeedToEnrolList);
	request.setAttribute("bean", notNeedToEnrolEnrolmentsBean);
	
	return mapping.findForward("chooseNotNeedToEnrol");
    }
    
    public ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	NotNeedToEnrolEnrolmentsBean bean = (NotNeedToEnrolEnrolmentsBean) getRenderedObject("notNeedToEnrolBean");
	return readNotNeedToEnrol(mapping, form, request, response, bean);
    }
}

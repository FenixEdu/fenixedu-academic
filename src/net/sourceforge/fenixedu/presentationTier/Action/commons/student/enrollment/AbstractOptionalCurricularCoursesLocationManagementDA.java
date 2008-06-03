package net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class AbstractOptionalCurricularCoursesLocationManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
	request.setAttribute("studentCurricularPlan", studentCurricularPlan);

	final List<Enrolment> enrolments = studentCurricularPlan.getEnrolments();
	Collections.sort(enrolments, Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
	request.setAttribute("enrolments", enrolments);

	return mapping.findForward("showEnrolments");
    }

    public ActionForward chooseNewDestination(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
	request.setAttribute("studentCurricularPlan", studentCurricularPlan);

	final OptionalCurricularCoursesLocationBean bean = new OptionalCurricularCoursesLocationBean(studentCurricularPlan);
	final String[] enrolmentIds = ((OptionalCurricularCoursesLocationForm) actionForm).getEnrolmentsToChange();
	bean.addEnrolments(getEnrolments(bean.getStudentCurricularPlan(), enrolmentIds));

	request.setAttribute("optionalCurricularCoursesLocationBean", bean);
	return mapping.findForward("chooseNewDestination");
    }

    public ActionForward chooseNewDestinationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final OptionalCurricularCoursesLocationBean bean = getBean();
	RenderUtils.invalidateViewState();

	request.setAttribute("optionalCurricularCoursesLocationBean", bean);
	request.setAttribute("studentCurricularPlan", bean.getStudentCurricularPlan());

	return mapping.findForward("chooseNewDestination");
    }

    public ActionForward moveEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final OptionalCurricularCoursesLocationBean bean = getBean();
	try {
	    executeService("MoveCurriculumLines", bean);
	    return backToStudentEnrolments(mapping, actionForm, request, response);

	} catch (EnrollmentDomainException ex) {
	    addRuleResultMessagesToActionMessages("error", request, ex.getFalseResult());
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	} catch (FenixServiceException e) {
	    addActionMessage(request, e.getMessage());
	}

	request.setAttribute("optionalCurricularCoursesLocationBean", bean);
	request.setAttribute("studentCurricularPlan", bean.getStudentCurricularPlan());
	return mapping.findForward("chooseNewDestination");

    }

    protected StudentCurricularPlan getStudentCurricularPlan(HttpServletRequest request) {
	return rootDomainObject.readStudentCurricularPlanByOID(getRequestParameterAsInteger(request, "scpID"));
    }

    private OptionalCurricularCoursesLocationBean getBean() {
	return (OptionalCurricularCoursesLocationBean) getRenderedObject("optionalCurricularCoursesLocationBean");
    }

    private List<Enrolment> getEnrolments(final StudentCurricularPlan studentCurricularPlan, final String[] enrolmentIds) {
	final List<Enrolment> result = new ArrayList<Enrolment>();
	final List<Enrolment> enrolments = studentCurricularPlan.getEnrolments();
	for (final String stringId : enrolmentIds) {
	    final Enrolment enrolment = getEnrolment(enrolments, Integer.valueOf(stringId));
	    if (enrolment != null) {
		result.add(enrolment);
	    }
	}
	return result;
    }

    private Enrolment getEnrolment(final List<Enrolment> enrolments, final Integer enrolmentId) {
	for (final Enrolment enrolment : enrolments) {
	    if (enrolment.getIdInternal().equals(enrolmentId)) {
		return enrolment;
	    }
	}
	return null;
    }
    
    static public class OptionalCurricularCoursesLocationForm extends FenixActionForm {
	    
        private String[] enrolmentsToChange;
    
        public String[] getEnrolmentsToChange() {
            return enrolmentsToChange;
        }
    
        public void setEnrolmentsToChange(String[] enrolmentsToChange) {
            this.enrolmentsToChange = enrolmentsToChange;
        }
    }

    abstract public ActionForward backToStudentEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response);
}

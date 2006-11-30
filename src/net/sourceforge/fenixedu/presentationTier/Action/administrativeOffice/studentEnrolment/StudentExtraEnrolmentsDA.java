package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentExtraEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentOptionalEnrolmentBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment.DeleteEnrolmentExecutor;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentExtraEnrolmentsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final Integer scpID = Integer.valueOf(request.getParameter("scpID"));
	final Integer executionPeriodID = Integer.valueOf(request.getParameter("executionPeriodID"));
	final NoCourseGroupCurriculumGroupType groupType = NoCourseGroupCurriculumGroupType
		.valueOf(request.getParameter("type"));

	StudentExtraEnrolmentBean extraEnrolmentBean = new StudentExtraEnrolmentBean(rootDomainObject
		.readStudentCurricularPlanByOID(scpID), rootDomainObject
		.readExecutionPeriodByOID(executionPeriodID), groupType);

	return showExtraEnrolments(extraEnrolmentBean, mapping, actionForm, request, response);
    }
    
    private ActionForward showExtraEnrolments(StudentExtraEnrolmentBean extraEnrolmentBean, ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("extraEnrolmentBean", extraEnrolmentBean);
	
	NoCourseGroupCurriculumGroup noCourseGroupCurriculumGroup = extraEnrolmentBean.getStudentCurricularPlan().getNoCourseGroupCurriculumGroup(extraEnrolmentBean.getGroupType());
	if(noCourseGroupCurriculumGroup != null) {
	    extraEnrolmentBean.setCurriculumGroup(noCourseGroupCurriculumGroup);
	    
	    if(!noCourseGroupCurriculumGroup.getEnrolments().isEmpty()) {
		request.setAttribute("extraEnrolments", noCourseGroupCurriculumGroup);
	    }
	}

	return mapping.findForward("showExtraEnrolments");
    }
    
    
    public ActionForward postBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        StudentExtraEnrolmentBean extraEnrolmentBean = (StudentExtraEnrolmentBean) RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("extraEnrolmentBean", extraEnrolmentBean);
        
        return mapping.findForward("chooseExtraEnrolment");
    }

    public ActionForward chooseCurricular(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)  {
	StudentExtraEnrolmentBean extraEnrolmentBean = (StudentExtraEnrolmentBean) getRenderedObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("extraEnrolmentBean", extraEnrolmentBean);
	return mapping.findForward("chooseExtraEnrolment");
    }
    
    public ActionForward enrol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer scpID = Integer.valueOf(request.getParameter("scpID"));
	Integer executionPeriodID = Integer.valueOf(request.getParameter("executionPeriodID"));
	Integer optionalCCID = Integer.valueOf(request.getParameter("optionalCCID"));
	NoCourseGroupCurriculumGroupType groupType = NoCourseGroupCurriculumGroupType.valueOf(request.getParameter("type"));
	
	final StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(scpID);
	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
	
	try {
	    ServiceUtils.executeService(getUserView(request), "CreateExtraEnrolment", new Object[] {studentCurricularPlan,
		executionPeriod, (CurricularCourse) rootDomainObject.readDegreeModuleByOID(optionalCCID), groupType});
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    StudentExtraEnrolmentBean extraEnrolmentBean = new StudentExtraEnrolmentBean(studentCurricularPlan, executionPeriod, groupType);
	    extraEnrolmentBean.setDegreeType(DegreeType.valueOf(request.getParameter("degreeType")));
	    extraEnrolmentBean.setDegree(rootDomainObject.readDegreeByOID(Integer.valueOf(request.getParameter("degreeID"))));
	    extraEnrolmentBean.setDegreeCurricularPlan(rootDomainObject.readDegreeCurricularPlanByOID(Integer.valueOf(request.getParameter("dcpID"))));
	    
	    request.setAttribute("extraEnrolmentBean", extraEnrolmentBean);
	    
	    return mapping.findForward("chooseExtraEnrolment");
	}

	StudentExtraEnrolmentBean extraEnrolmentBean = new StudentExtraEnrolmentBean(studentCurricularPlan, executionPeriod, groupType);
	
	return showExtraEnrolments(extraEnrolmentBean, mapping, actionForm, request, response);    
		
    }
    
    public ActionForward back(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)  {
	StudentExtraEnrolmentBean extraEnrolmentBean = (StudentExtraEnrolmentBean) RenderUtils.getViewState().getMetaObject().getObject();
	
	final StudentEnrolmentBean enrolmentBean = new StudentEnrolmentBean();
	enrolmentBean.setStudentCurricularPlan(extraEnrolmentBean.getStudentCurricularPlan());
	enrolmentBean.setExecutionPeriod(extraEnrolmentBean.getExecutionPeriod());
	
	request.setAttribute("studentEnrolmentBean", enrolmentBean);
	    
	return mapping.findForward("showDegreeModulesToEnrol");
	
    }
    
    public ActionForward back2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)  {
	StudentExtraEnrolmentBean extraEnrolmentBean = (StudentExtraEnrolmentBean) RenderUtils.getViewState().getMetaObject().getObject();
	
	return showExtraEnrolments(extraEnrolmentBean, mapping, actionForm, request, response);
    }

    
    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException  {
	Integer enrolmentID = Integer.valueOf(request.getParameter("enrolment"));
	final Integer scpID = Integer.valueOf(request.getParameter("scpID"));
	final Integer executionPeriodID = Integer.valueOf(request.getParameter("executionPeriodID"));
	final NoCourseGroupCurriculumGroupType groupType = NoCourseGroupCurriculumGroupType
		.valueOf(request.getParameter("type"));

	StudentExtraEnrolmentBean extraEnrolmentBean = new StudentExtraEnrolmentBean(rootDomainObject
		.readStudentCurricularPlanByOID(scpID), rootDomainObject
		.readExecutionPeriodByOID(executionPeriodID), groupType);
	
	Enrolment enrolment = (Enrolment) rootDomainObject.readCurriculumModuleByOID(enrolmentID);
	DeleteEnrolmentExecutor deleteEnrolmentExecutor = new Enrolment.DeleteEnrolmentExecutor(enrolment);
	
	try {
	    executeService(request, "ExecuteFactoryMethod", new Object[] {deleteEnrolmentExecutor});
	} catch(DomainException e) {
	    addActionMessage(request, e.getMessage());
	}
	
	return showExtraEnrolments(extraEnrolmentBean, mapping, actionForm, request, response);
    }
	 

    
}

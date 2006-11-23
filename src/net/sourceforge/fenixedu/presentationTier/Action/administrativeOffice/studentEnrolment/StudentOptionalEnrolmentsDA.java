package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentOptionalEnrolmentBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentOptionalEnrolmentsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	Integer scpID = Integer.valueOf(request.getParameter("scpID"));
	Integer executionPeriodID = Integer.valueOf(request.getParameter("executionPeriodID"));
	Integer curriculumGroupID = Integer.valueOf(request.getParameter("curriculumGroupID"));
	Integer contextID = Integer.valueOf(request.getParameter("contextID"));
	
	StudentOptionalEnrolmentBean optionalEnrolmentBean = new StudentOptionalEnrolmentBean(rootDomainObject.readStudentCurricularPlanByOID(scpID),
		rootDomainObject.readExecutionPeriodByOID(executionPeriodID), (CurriculumGroup) rootDomainObject.readCurriculumModuleByOID(curriculumGroupID),
		rootDomainObject.readContextByOID(contextID));
	
	
	request.setAttribute("optionalBean", optionalEnrolmentBean);
	
	return mapping.findForward("chooseOptionalEnrolments");
    }
    
    public ActionForward postBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        StudentOptionalEnrolmentBean optionalEnrolmentBean = (StudentOptionalEnrolmentBean) RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("optionalBean", optionalEnrolmentBean);
        
        return mapping.findForward("chooseOptionalEnrolments");
    }
    
    public ActionForward enrol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer scpID = Integer.valueOf(request.getParameter("scpID"));
	Integer executionPeriodID = Integer.valueOf(request.getParameter("executionPeriodID"));
	Integer curriculumGroupID = Integer.valueOf(request.getParameter("curriculumGroupID"));
	Integer contextID = Integer.valueOf(request.getParameter("contextID"));
	Integer optionalCCID = Integer.valueOf(request.getParameter("optionalCCID"));
	
	final StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(scpID);
	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
	final CurriculumGroup curriculumGroup = (CurriculumGroup) rootDomainObject.readCurriculumModuleByOID(curriculumGroupID);
	final Context context = rootDomainObject.readContextByOID(contextID);
	
	try {
	    ServiceUtils.executeService(getUserView(request), "CreateOptionalEnrolment", new Object[] {studentCurricularPlan,
		executionPeriod, (CurriculumGroup) rootDomainObject.readCurriculumModuleByOID(curriculumGroupID),
		rootDomainObject.readContextByOID(contextID), (CurricularCourse) rootDomainObject.readDegreeModuleByOID(optionalCCID), EnrollmentCondition.VALIDATED});
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    StudentOptionalEnrolmentBean optionalEnrolmentBean = new StudentOptionalEnrolmentBean(studentCurricularPlan, executionPeriod, 
		    curriculumGroup, context);
	    optionalEnrolmentBean.setDegreeType(DegreeType.valueOf(request.getParameter("degreeType")));
	    optionalEnrolmentBean.setDegree(rootDomainObject.readDegreeByOID(Integer.valueOf(request.getParameter("degreeID"))));
	    optionalEnrolmentBean.setDegreeCurricularPlan(rootDomainObject.readDegreeCurricularPlanByOID(Integer.valueOf(request.getParameter("dcpID"))));
	    
	    request.setAttribute("optionalBean", optionalEnrolmentBean);
	    
	    return mapping.findForward("chooseOptionalEnrolments");
	}

	final StudentEnrolmentBean enrolmentBean = new StudentEnrolmentBean();
	enrolmentBean.setStudentCurricularPlan(studentCurricularPlan);
	enrolmentBean.setExecutionPeriod(executionPeriod);
	
	request.setAttribute("studentEnrolmentBean", enrolmentBean);
	    
	return mapping.findForward("showDegreeModulesToEnrol");	
    }
    
    public ActionForward back(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	StudentOptionalEnrolmentBean optionalEnrolmentBean = (StudentOptionalEnrolmentBean) RenderUtils.getViewState().getMetaObject().getObject();
	
	final StudentEnrolmentBean enrolmentBean = new StudentEnrolmentBean();
	enrolmentBean.setStudentCurricularPlan(optionalEnrolmentBean.getStudentCurricularPlan());
	enrolmentBean.setExecutionPeriod(optionalEnrolmentBean.getExecutionPeriod());
	
	request.setAttribute("studentEnrolmentBean", enrolmentBean);
	    
	return mapping.findForward("showDegreeModulesToEnrol");
	
    }

    
}

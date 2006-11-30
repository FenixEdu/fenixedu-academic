package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.EnrolmentRuleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.CurriculumModuleBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentExtraEnrolmentBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentEnrolmentsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixActionException{
	Integer scpID = Integer.valueOf(request.getParameter("scpID"));
	StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(scpID);
	StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
	if(studentCurricularPlan != null) {
	    studentEnrolmentBean.setStudentCurricularPlan(studentCurricularPlan);
	    studentEnrolmentBean.setExecutionPeriod(ExecutionPeriod.readActualExecutionPeriod());
	    return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, actionForm, request, response);
	} else {
	    throw new FenixActionException();
	}
    }
    
    public ActionForward prepareFromExtraEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	StudentEnrolmentBean studentEnrolmentBean = (StudentEnrolmentBean) request.getAttribute("studentEnrolmentBean");
	return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, actionForm, request, response);
    }

    
    private ActionForward showExecutionPeriodEnrolments(StudentEnrolmentBean studentEnrolmentBean, ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("studentEnrolmentBean", studentEnrolmentBean);
	
	if(studentEnrolmentBean.getExecutionPeriod() != null) {
	    request.setAttribute("studentEnrolments", studentEnrolmentBean.getStudentCurricularPlan().getEnrolmentsByExecutionPeriod(studentEnrolmentBean.getExecutionPeriod()));
	}
	
	return mapping.findForward("prepareChooseExecutionPeriod");
    }
    
    public ActionForward showDegreeModulesToEnrol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
	Integer scpID = Integer.valueOf(request.getParameter("scpID"));
	Integer epID = Integer.valueOf(request.getParameter("executionPeriodID"));
	
	StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(scpID);
	ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(epID);
	
	if(studentCurricularPlan != null && executionPeriod != null) {
	    StudentEnrolmentBean enrolmentBean = new StudentEnrolmentBean();
	    enrolmentBean.setStudentCurricularPlan(studentCurricularPlan);
	    enrolmentBean.setExecutionPeriod(executionPeriod);
	    request.setAttribute("studentEnrolmentBean", enrolmentBean);
	    setCurriculumModuleBean(enrolmentBean);
	    return mapping.findForward("showDegreeModulesToEnrol");
	} else {
	    throw new FenixActionException();
	}
    }    
    
    public ActionForward showDegreeModulesToEnrolOptional(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	StudentEnrolmentBean studentEnrolmentBean = (StudentEnrolmentBean) request.getAttribute("studentEnrolmentBean");
	setCurriculumModuleBean(studentEnrolmentBean);
	return mapping.findForward("showDegreeModulesToEnrol");
    }
    
    public ActionForward postBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
	StudentEnrolmentBean enrolmentBean = (StudentEnrolmentBean) getRenderedObject();
        RenderUtils.invalidateViewState();
        
        return showExecutionPeriodEnrolments(enrolmentBean, mapping, actionForm, request, response);
    }

    
    public ActionForward enrol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException{
	StudentEnrolmentBean studentEnrolmentBean = (StudentEnrolmentBean) getRenderedObject();
	request.setAttribute("studentEnrolmentBean", studentEnrolmentBean);
	RenderUtils.invalidateViewState();
	try {
	    ServiceUtils.executeService(getUserView(request), "CreateStudentEnrolmentsWithoutRules", new Object[] {studentEnrolmentBean});
	} catch (EnrolmentRuleServiceException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}
	
	setCurriculumModuleBean(studentEnrolmentBean);
	return mapping.findForward("showDegreeModulesToEnrol");
    }
    
    public ActionForward end(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException{
	StudentEnrolmentBean studentEnrolmentBean = (StudentEnrolmentBean) getRenderedObject();
	request.setAttribute("studentNumber", studentEnrolmentBean.getStudentCurricularPlan().getRegistration().getNumber().toString());
	return mapping.findForward("viewCurriculum");
    }
    
    private void setCurriculumModuleBean(StudentEnrolmentBean enrolmentBean) {
	CurriculumGroup group = enrolmentBean.getStudentCurricularPlan().getRoot();
	enrolmentBean.setCurriculumModuleBean(getCurriculumModuleBean(group, enrolmentBean.getExecutionPeriod()));
    }
    
    private CurriculumModuleBean getCurriculumModuleBean(CurriculumGroup group, ExecutionPeriod executionPeriod) {
	CurriculumModuleBean curriculumModuleBean = new CurriculumModuleBean();
	curriculumModuleBean.setGroupsToEnrol(getGroupsToEnrol(group, executionPeriod));
	curriculumModuleBean.setCurricularCoursesToEnrol(getCurricularCoursesToEnrol(group, executionPeriod));
	curriculumModuleBean.setCurricularCoursesEnroled(getCurricularCoursesEnroled(group, executionPeriod));
	curriculumModuleBean.setGroupsEnroled(getGroupsEnroled(group, executionPeriod));
	curriculumModuleBean.setCurriculumModule(group);
	return curriculumModuleBean;
    }


    private List<CurriculumModuleBean> getGroupsEnroled(CurriculumGroup group, ExecutionPeriod executionPeriod) {
	List<CurriculumModuleBean> result = new ArrayList<CurriculumModuleBean>();
	for (CurriculumGroup curriculumGroup : group.getCurriculumGroups()) {
	    result.add(getCurriculumModuleBean(curriculumGroup, executionPeriod));
	}
	return result;
    }

    private List<DegreeModuleToEnrol> getGroupsToEnrol(CurriculumGroup group, ExecutionPeriod executionPeriod) {
	List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>();
	List<Context> courseGroupContextsToEnrol = group.getCourseGroupContextsToEnrol(executionPeriod);

	for (Context context : courseGroupContextsToEnrol) {
	    DegreeModuleToEnrol degreeModuleToEnrol = new DegreeModuleToEnrol(group, context);
	    result.add(degreeModuleToEnrol);
	}
	
	return result;
    }

    private List<DegreeModuleToEnrol> getCurricularCoursesToEnrol(CurriculumGroup group, ExecutionPeriod executionPeriod) {
	List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>();
	List<Context> curricularCoursesToEnrol = group.getCurricularCourseContextsToEnrol(executionPeriod);
	
	for (Context context : curricularCoursesToEnrol) {
	    DegreeModuleToEnrol degreeModuleToEnrol = new DegreeModuleToEnrol(group, context);
	    result.add(degreeModuleToEnrol);
	}
	
	return result;
    }

    private List<CurriculumModuleBean> getCurricularCoursesEnroled(CurriculumGroup group, ExecutionPeriod executionPeriod) {
	List<CurriculumModuleBean> result = new ArrayList<CurriculumModuleBean>();
	
	for (CurriculumLine curriculumLine : group.getCurriculumLines()) {
	    if(((CurriculumLine) curriculumLine).isEnrolment()) {
		Enrolment enrolment = (Enrolment) curriculumLine;
		if(enrolment.getExecutionPeriod().equals(executionPeriod) && enrolment.isEnroled()) {
		    CurriculumModuleBean curriculumModuleBean = new CurriculumModuleBean();
		    curriculumModuleBean.setCurriculumModule(enrolment);
		    result.add(curriculumModuleBean);
		}
	    }
	}
	
	return result;
    }
}

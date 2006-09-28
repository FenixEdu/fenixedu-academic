package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.EnrolmentRuleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

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
	    request.setAttribute("studentEnrolmentBean", studentEnrolmentBean);
	} else {
	    throw new FenixActionException();
	}
	return mapping.findForward("prepareChooseExecutionPeriod");
    }
    
    public ActionForward showDegreeModulesToEnrol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixActionException{
	StudentEnrolmentBean studentEnrolmentBean = (StudentEnrolmentBean) getRenderedObject();
	request.setAttribute("studentEnrolmentBean", studentEnrolmentBean);
	return mapping.findForward("showDegreeModulesToEnrol");
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
	return mapping.findForward("showDegreeModulesToEnrol");
    }
}

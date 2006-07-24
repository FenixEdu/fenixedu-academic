package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.GradesToSubmitExecutionCourseSendMailBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSendMailBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetToConfirmSendMailBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SendMailMarkSheetDispatchAction extends MarkSheetDispatchAction {
	
	public ActionForward prepareSearchSendMail(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
		MarkSheetSendMailBean bean = new MarkSheetSendMailBean();
		request.setAttribute("bean", bean);
		return mapping.findForward("searchSendMail");
	}
	
    public ActionForward prepareSearchSendMailPostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        Object object = RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("bean", object);
        
        return mapping.getInputForward();
    }
    
    public ActionForward prepareSearchSendMailInvalid(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        request.setAttribute("bean", RenderUtils.getViewState().getMetaObject().getObject());        
        return mapping.getInputForward();
    }
	
	public ActionForward searchSendMail(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
		MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState().getMetaObject().getObject();
		Collection<MarkSheet> markSheets = bean.getExecutionPeriod().getMarkSheetsToConfirm(bean.getDegreeCurricularPlan());
		Collection<ExecutionCourse> executionCourses = bean.getExecutionPeriod().getExecutionCoursesWithDegreeGradesToSubmit(bean.getDegreeCurricularPlan());
		if(!markSheets.isEmpty()) {
			Map<CurricularCourse, MarkSheetToConfirmSendMailBean> map = new HashMap<CurricularCourse, MarkSheetToConfirmSendMailBean>();
			for (MarkSheet markSheet : markSheets) {
				if(map.get(markSheet.getCurricularCourse()) == null) {
					map.put(markSheet.getCurricularCourse(), new MarkSheetToConfirmSendMailBean(markSheet, true));
				}
			}
			bean.setMarkSheetToConfirmSendMailBean(new ArrayList<MarkSheetToConfirmSendMailBean>(map.values()));
		}
		if(!executionCourses.isEmpty()) {
			Collection<GradesToSubmitExecutionCourseSendMailBean> executionCoursesBean =  new ArrayList<GradesToSubmitExecutionCourseSendMailBean>();
			for (ExecutionCourse course : executionCourses) {
				executionCoursesBean.add(new GradesToSubmitExecutionCourseSendMailBean(course, true));
			}
			bean.setGradesToSubmitExecutionCourseSendMailBean(executionCoursesBean);
		}
		request.setAttribute("bean", bean);
		return mapping.findForward("searchSendMail");
	}
	
	public ActionForward prepareMarkSheetsToConfirmSendMail(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
		MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState("sendMailBean").getMetaObject().getObject();
		bean.setFrom(getResources(request, "GLOBAL_RESOURCES").getMessage("degreeAdminOffice.mail"));
		request.setAttribute("bean", bean);
		return mapping.findForward("prepareMarkSheetsToConfirmSendMail");
	}
	
	public ActionForward markSheetsToConfirmSendMail(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState().getMetaObject().getObject();
		Object[] args = new Object[] { bean.getMarkSheetToConfirmSendMailBeanToSubmit(), bean.getFrom(), bean.getCc(), bean.getSubject(), bean.getMessage() };
		
		ServiceUtils.executeService(getUserView(request), "MarkSheetsToConfirmSendMail", args);
		resetMail(bean);
		return searchSendMail(mapping, actionForm, request, response);
	}


	public ActionForward prepareGradesToSubmitSendMail(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
		MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState("sendMailBean").getMetaObject().getObject();
		bean.setFrom(getResources(request, "GLOBAL_RESOURCES").getMessage("degreeAdminOffice.mail"));
		request.setAttribute("bean", bean);
		return mapping.findForward("prepareGradesToSubmitSendMail");
	}

	public ActionForward gradesToSubmitSendMail(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState().getMetaObject().getObject();
		Object[] args = new Object[] { bean.getGradesToSubmitExecutionCourseSendMailBeanToSubmit(), bean.getFrom(), bean.getCc(), bean.getSubject(), bean.getMessage() };
		
		ServiceUtils.executeService(getUserView(request), "GradesToSubmitSendMail", args);
		resetMail(bean);
		return searchSendMail(mapping, actionForm, request, response);
	}

	private void resetMail(MarkSheetSendMailBean bean) {
		bean.setCc(null);
		bean.setSubject(null);
		bean.setMessage(null);
		bean.setFrom(null);
	}

    
}

package net.sourceforge.fenixedu.presentationTier.Action.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementByEntryYearBean;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.util.Month;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class TutorshipManagementDispatchAction extends TutorManagementDispatchAction {

	public ActionForward insertTutorshipWithOneStudent(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		TutorshipManagementBean bean = (TutorshipManagementBean) RenderUtils.getViewState("associateOneStudentBean").getMetaObject().getObject();
		RenderUtils.invalidateViewState("associateOneStudentBean");

		final Teacher teacher = bean.getTeacher();

		Object[] args = new Object[] { bean.getExecutionDegreeID(), bean };
		try {
			executeService(request, "InsertTutorship", args);
		} catch (FenixServiceException e) {
			addActionMessage(request, e.getMessage(), e.getArgs());
		}

		bean.setStudentNumber(null);
		bean.setTutorshipEndMonth(Month.SEPTEMBER);
		bean.setTutorshipEndYear(Tutorship.getLastPossibleTutorshipYear());

		if (!teacher.getActiveTutorships().isEmpty()) {
			List<TutorshipManagementByEntryYearBean> beans = getTutorshipManagementBeansByEntryYear(teacher, teacher.getActiveTutorships());
			request.setAttribute("tutorshipManagementBeansByEntryYear", beans);
		}

		request.setAttribute("tutorshipManagementBean", bean);
		return mapping.findForward("showStudentsByTutor");
	}

	public ActionForward manageTutorships(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final TutorshipManagementBean bean = (TutorshipManagementBean) RenderUtils.getViewState("tutorshipManagementBean").getMetaObject().getObject();
		
		if(request.getParameter("remove") != null) {
			return removeTutorships(mapping, actionForm, request, response, bean);
		}
		else if(request.getParameter("transfer") != null) {
			return prepareTransferTutorship(mapping, actionForm, request, response, bean);
		}
		
		request.setAttribute("tutorshipManagementBean", bean);
		return mapping.findForward("showStudentsByTutor");
	}
	
	public ActionForward removeTutorships(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response, TutorshipManagementBean bean) throws Exception {
		
		final Teacher teacher = bean.getTeacher();
		List<Tutorship> tutorshipsToRemove = getTutorshipsToRemove();
		
		if(tutorshipsToRemove.isEmpty()) {
			addActionMessage(request, "error.coordinator.tutor.manageTutorships.mustSelectStudents");
		}
		else {
			Object[] args = { bean.getExecutionDegreeID(), bean.getTeacherNumber(), tutorshipsToRemove };
	
			List<TutorshipErrorBean> tutorshipsNotRemoved = new ArrayList<TutorshipErrorBean>();
			try {
				tutorshipsNotRemoved = (List<TutorshipErrorBean>)executeService(request, "DeleteTutorship", args);
			} catch (FenixServiceException e) {
				addActionMessage(request, e.getMessage(), e.getArgs());
			}
			
			if (!tutorshipsNotRemoved.isEmpty()) {
				for (TutorshipErrorBean tutorship : tutorshipsNotRemoved) {
					addActionMessage(request, tutorship.getMessage(), tutorship.getArgs());
				}
			}
		}

		if (!teacher.getActiveTutorships().isEmpty()) {
			List<TutorshipManagementByEntryYearBean> beans = getTutorshipManagementBeansByEntryYear(teacher, teacher.getActiveTutorships());
			request.setAttribute("tutorshipManagementBeansByEntryYear", beans);
		}
		
		request.setAttribute("tutorshipManagementBean", bean);
		return mapping.findForward("showStudentsByTutor");
	}
	
	public ActionForward prepareTransferTutorship(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response, TutorshipManagementBean bean) throws Exception {
		
		List<TutorshipManagementByEntryYearBean> tutorshipsToTransferBeans = getTutorshipsToTransferBeans();
		
		List<Tutorship> tutorshipsToTransfer = new ArrayList<Tutorship>();
		for(TutorshipManagementByEntryYearBean tutorshipToTransferBean : tutorshipsToTransferBeans) {
			tutorshipsToTransfer.addAll(tutorshipToTransferBean.getStudentsList());
		}
		
		if(tutorshipsToTransfer.isEmpty()) {
			addActionMessage(request, "error.coordinator.tutor.manageTutorships.mustSelectStudents");
			
			final Teacher teacher = bean.getTeacher();
			
			if (!teacher.getActiveTutorships().isEmpty()) {
				List<TutorshipManagementByEntryYearBean> beans = getTutorshipManagementBeansByEntryYear(teacher, teacher.getActiveTutorships());
				request.setAttribute("tutorshipManagementBeansByEntryYear", beans);
			}
			
			request.setAttribute("tutorshipManagementBean", bean);
			return mapping.findForward("showStudentsByTutor");
		}
		
		TutorshipManagementBean targetTutorBean = new TutorshipManagementBean(bean.getExecutionDegreeID(), bean.getDegreeCurricularPlanID());
		
		request.setAttribute("targetTutorManagementBean", targetTutorBean);
		request.setAttribute("tutorshipsToTransfer", tutorshipsToTransferBeans);
		request.setAttribute("tutorshipManagementBean", bean);
		return mapping.findForward("transferTutorships");
	}
	
	public ActionForward transferTutorship(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//Bean with previous tutor
		final TutorshipManagementBean tutorshipManagementBean = (TutorshipManagementBean) RenderUtils.getViewState("tutorshipManagementBean").getMetaObject().getObject();
		RenderUtils.invalidateViewState("tutorshipBean");
		
		//Bean with target tutor
		TutorshipManagementBean targetTutorBean = (TutorshipManagementBean) RenderUtils.getViewState("targetTutorBean").getMetaObject().getObject();
		RenderUtils.invalidateViewState("targetTutorBean");

		List<TutorshipManagementByEntryYearBean> tutorshipsToTransferBeans = (List<TutorshipManagementByEntryYearBean>)RenderUtils.getViewState("tutorshipsToTransferBean").getMetaObject().getObject();
		RenderUtils.invalidateViewState("tutorshipsToTransferBean");
		
		
		Object[] args = new Object[] {targetTutorBean.getExecutionDegreeID(), targetTutorBean, tutorshipsToTransferBeans };
		
		List<TutorshipErrorBean> tutorshipsNotRemoved = new ArrayList<TutorshipErrorBean>();
		try {
			tutorshipsNotRemoved = (List<TutorshipErrorBean>)executeService(request, "TransferTutorship", args);
		} catch (FenixServiceException e) {
			addActionMessage(request, e.getMessage(), e.getArgs());
			
			request.setAttribute("tutorshipsToTransfer", tutorshipsToTransferBeans);
			request.setAttribute("targetTutorManagementBean", targetTutorBean);
			request.setAttribute("tutorshipManagementBean", tutorshipManagementBean);
			return mapping.findForward("transferTutorships");
		}
		
		if (!tutorshipsNotRemoved.isEmpty()) {
			for (TutorshipErrorBean tutorship : tutorshipsNotRemoved) {
				addActionMessage(request, tutorship.getMessage(), tutorship.getArgs());
			}
		}

		final Teacher teacher = tutorshipManagementBean.getTeacher();
		
		if (!teacher.getActiveTutorships().isEmpty()) {
			List<TutorshipManagementByEntryYearBean> beans = getTutorshipManagementBeansByEntryYear(teacher, teacher.getActiveTutorships());
			request.setAttribute("tutorshipManagementBeansByEntryYear", beans);
		}
		
		request.setAttribute("tutorshipManagementBean", tutorshipManagementBean);
		return mapping.findForward("showStudentsByTutor");
	
	}

	/*
	 * AUXILIARY METHODS
	 */
	
	/*
	 * Returns the list of tutorships to remove from the current teacher
	 */
	private List<Tutorship> getTutorshipsToRemove(){
		List<Tutorship> tutorshipsToRemove = new ArrayList<Tutorship>();
		
		for (int i = 0; RenderUtils.getViewState("manageTutorshipBean" + i) != null; i++) {
			TutorshipManagementByEntryYearBean manageTutorshipBean = (TutorshipManagementByEntryYearBean) RenderUtils.getViewState("manageTutorshipBean" + i).getMetaObject().getObject();
			RenderUtils.invalidateViewState("manageTutorshipBean" + i);
			tutorshipsToRemove.addAll(manageTutorshipBean.getStudentsList());
		}
		return tutorshipsToRemove;
	}
	
	/*
	 * Returns a list of beans that contains the tutorships to transfer to
	 * another teacher, grouped by students entry year
	 */
	private List<TutorshipManagementByEntryYearBean> getTutorshipsToTransferBeans(){
		List<TutorshipManagementByEntryYearBean> tutorshipsToTransfer = new ArrayList<TutorshipManagementByEntryYearBean>();
		
		for (int i = 0; RenderUtils.getViewState("manageTutorshipBean" + i) != null; i++) {
			tutorshipsToTransfer.add((TutorshipManagementByEntryYearBean) RenderUtils.getViewState("manageTutorshipBean" + i).getMetaObject().getObject());
			RenderUtils.invalidateViewState("manageTutorshipBean" + i);
		}
		return tutorshipsToTransfer;
	}
}
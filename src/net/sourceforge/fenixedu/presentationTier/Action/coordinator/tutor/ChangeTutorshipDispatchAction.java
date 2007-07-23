package net.sourceforge.fenixedu.presentationTier.Action.coordinator.tutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean.ChangeTutorshipBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ChangeTutorshipDispatchAction extends TutorManagementDispatchAction {
	
	public ActionForward prepareChangeTutorshipsEndDates(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final Integer executionDegreeId = new Integer(getFromRequest(request, "executionDegreeId"));
		final Integer degreeCurricularPlanID = new Integer(getFromRequest(request, "degreeCurricularPlanID"));
		final Integer teacherNumber = new Integer(getFromRequest(request, "teacherNumber"));
		final Teacher teacher = Teacher.readByNumber(teacherNumber);
		
		TutorshipManagementBean bean = new TutorshipManagementBean(executionDegreeId, degreeCurricularPlanID, teacherNumber);
		bean.setTeacher(teacher);
		
		List<ChangeTutorshipByEntryYearBean> allTutorshipsBeans = getChangeTutorshipByEntryYearBean(teacher, teacher.getTutorships());
		List<ChangeTutorshipByEntryYearBean> activeTutorshipsBeans = getChangeTutorshipByEntryYearBean(teacher, teacher.getActiveTutorships());
		List<ChangeTutorshipByEntryYearBean> pastTutorshipsBeans = getChangeTutorshipByEntryYearBean(teacher, teacher.getPastTutorships());
		
		request.setAttribute("allTutorshipsByEntryYearBeans", allTutorshipsBeans);
		request.setAttribute("activeTutorshipsByEntryYearBeans", activeTutorshipsBeans);
		request.setAttribute("pastTutorshipsByEntryYearBeans", pastTutorshipsBeans);
		request.setAttribute("tutorshipManagementBean", bean);
		return mapping.findForward("changeTutorshipsEndDate");
	}
	
	public ActionForward changeTutorshipsEndDates(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final TutorshipManagementBean tutorshipManagementBean = (TutorshipManagementBean) RenderUtils.getViewState("tutorshipManagementBean").getMetaObject().getObject();
		
		final Teacher teacher = tutorshipManagementBean.getTeacher();
		
		final List<ChangeTutorshipBean> changeTutorshipBeans = new ArrayList<ChangeTutorshipBean>();
		changeTutorshipBeans.addAll(getChangeTutorshipBeans("changeActiveTutorshipBean"));
		changeTutorshipBeans.addAll(getChangeTutorshipBeans("changePastTutorshipBean"));
		
		if(request.getParameter("cancel") == null){
			Object[] args = new Object[] {tutorshipManagementBean.getExecutionDegreeID(), changeTutorshipBeans };
			
			List<TutorshipErrorBean> tutorshipsNotChanged = new ArrayList<TutorshipErrorBean>();
			try {
				tutorshipsNotChanged = (List<TutorshipErrorBean>)executeService(request,"ChangeTutorship", args);
			} catch (FenixServiceException e) {
				addActionMessage(request, e.getMessage(), e.getArgs());
			}
			
			if (!tutorshipsNotChanged.isEmpty()) {
				for (TutorshipErrorBean tutorship : tutorshipsNotChanged) {
					addActionMessage(request, tutorship.getMessage(), tutorship.getArgs());
				}
			}
		}
		
		if (!teacher.getActiveTutorships().isEmpty()) {
			List<TutorshipManagementByEntryYearBean> beans = getTutorshipManagementBeansByEntryYear(teacher, teacher.getActiveTutorships());
			request.setAttribute("tutorshipManagementBeansByEntryYear", beans);
		}
		
		request.setAttribute("tutorshipManagementBean", tutorshipManagementBean);
		return mapping.findForward("showStudentsByTutor");
	}
	
	public ActionForward changeAllTutorshipsEndDates(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final TutorshipManagementBean tutorshipManagementBean = (TutorshipManagementBean) RenderUtils.getViewState("tutorshipManagementBean").getMetaObject().getObject();
		final List<ChangeTutorshipByEntryYearBean> allTutorshipBeansByEntryYear = (List<ChangeTutorshipByEntryYearBean>) RenderUtils.getViewState("allTutorshipsByEntryYearBeans").getMetaObject().getObject();
		final Teacher teacher = tutorshipManagementBean.getTeacher();
		
		if(request.getParameter("cancel") == null){
			List<ChangeTutorshipBean> changeTutorshipBeans = new ArrayList<ChangeTutorshipBean>();
			
			for(ChangeTutorshipByEntryYearBean changeTutorshipByEntryYearBean : allTutorshipBeansByEntryYear) {
				for(ChangeTutorshipBean changeTutorshipBean : changeTutorshipByEntryYearBean.getChangeTutorshipsBeans()) {
					changeTutorshipBean.setTutorshipEndMonth(tutorshipManagementBean.getTutorshipEndMonth());
					changeTutorshipBean.setTutorshipEndYear(tutorshipManagementBean.getTutorshipEndYear());
				}
				changeTutorshipBeans.addAll(changeTutorshipByEntryYearBean.getChangeTutorshipsBeans());
			}
			
			Object[] args = new Object[] { tutorshipManagementBean.getExecutionDegreeID(), changeTutorshipBeans };
			
			List<TutorshipErrorBean> tutorshipsNotChanged = new ArrayList<TutorshipErrorBean>();
			try {
				tutorshipsNotChanged = (List<TutorshipErrorBean>)executeService(request,"ChangeTutorship", args);
			} catch (FenixServiceException e) {
				addActionMessage(request, e.getMessage(), e.getArgs());
			}
			
			if (!tutorshipsNotChanged.isEmpty()) {
				for (TutorshipErrorBean tutorship : tutorshipsNotChanged) {
					addActionMessage(request, tutorship.getMessage(), tutorship.getArgs());
				}
			}
		}
		
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
	
	private List<ChangeTutorshipBean> getChangeTutorshipBeans(String beanId){
		List<ChangeTutorshipBean> beans = new ArrayList<ChangeTutorshipBean>();
		
		for (int i = 0; RenderUtils.getViewState(beanId + i) != null; i++) {
			List<ChangeTutorshipBean> tutorships = (List<ChangeTutorshipBean>) RenderUtils.getViewState(beanId + i).getMetaObject().getObject();
			RenderUtils.invalidateViewState(beanId + i);
			beans.addAll(tutorships);
		}
		
		return beans;
	}
	
	/*
	 * Returns a list of ChangeTutorshipByEntryYearBean for each entry year of students associated with the tutor
	 */
	protected List<ChangeTutorshipByEntryYearBean> getChangeTutorshipByEntryYearBean(Teacher teacher, List<Tutorship> tutorships) { 
		Map<ExecutionYear, ChangeTutorshipByEntryYearBean> changeTutorshipByEntryYearBeans = new HashMap<ExecutionYear, ChangeTutorshipByEntryYearBean>();

		for (final Tutorship tutorship : tutorships) {
			if(tutorship.belongsToAnotherTeacher())
				continue;
			
			ExecutionYear entryYear = tutorship.getStudentCurricularPlan().getRegistration().getStartExecutionYear();

			if (!changeTutorshipByEntryYearBeans.containsKey(entryYear)) {
				ChangeTutorshipByEntryYearBean changeTutorshipBean = new ChangeTutorshipByEntryYearBean(entryYear);
				changeTutorshipBean.addTutorship(tutorship);
				changeTutorshipByEntryYearBeans.put(entryYear, changeTutorshipBean);
			}
			else {
				ChangeTutorshipByEntryYearBean changeTutorshipBean = changeTutorshipByEntryYearBeans.get(entryYear);
				changeTutorshipBean.addTutorship(tutorship);
				changeTutorshipByEntryYearBeans.put(entryYear, changeTutorshipBean);
			}
		}

		ArrayList<ChangeTutorshipByEntryYearBean> beans = new ArrayList<ChangeTutorshipByEntryYearBean>(changeTutorshipByEntryYearBeans.values());
		Collections.sort(beans, new BeanComparator("executionYear"));
		Collections.reverse(beans);
		
		return beans;
	}
	
}
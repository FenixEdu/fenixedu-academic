package net.sourceforge.fenixedu.presentationTier.Action.coordinator.tutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateTutorshipDispatchAction extends TutorManagementDispatchAction {
	
	public ActionForward prepareCreateTutorships(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final Person person = getLoggedPerson(request);
		final Integer executionDegreeId = new Integer(getFromRequest(request, "executionDegreeId"));
		final Integer degreeCurricularPlanID = new Integer(getFromRequest(request, "degreeCurricularPlanID"));
		
		final ExecutionDegree executionDegree = (ExecutionDegree) RootDomainObject.readDomainObjectByOID(ExecutionDegree.class, executionDegreeId);

		if (!validateDegreeTypeAccessRestrictions(executionDegree)) {
			addActionMessage(request, "error.tutor.notAuthorized.notBolonhaOrLEEC");
			return mapping.findForward("notAuthorized");
		}
		
		if (!validateCoordinationAccessRestrictions(person, executionDegree)) {
			addActionMessage(request, "error.tutor.notAuthorized.notCoordinatorOfDegree");
			return mapping.findForward("notAuthorized");
		}

		List<StudentsByEntryYearBean> studentsWithoutTutorBeans = getStudentsWithoutTutorByEntryYearBeans(degreeCurricularPlanID, executionDegreeId);
		
		if(request.getParameter("selectedEntryYear") != null) {
			ExecutionYear entryYear = ExecutionYear.readExecutionYearByName(request.getParameter("selectedEntryYear"));
			StudentsByEntryYearBean selectedBean = getSelectedBeanFromList(studentsWithoutTutorBeans, entryYear);
			request.setAttribute("filteredStudentsBean", selectedBean);
		}
		
		request.setAttribute("studentsWithoutTutor", studentsWithoutTutorBeans);
		request.setAttribute("executionDegreeId", executionDegreeId);
		request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
		return mapping.findForward("createTutorships");
	}
	
	public ActionForward prepareSelectGivenNumberOfTutorships(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final List<StudentsByEntryYearBean> studentsWithoutTutorBeans = (List<StudentsByEntryYearBean>)getViewState("studentsWithoutTutor");
		StudentsByEntryYearBean selectedBean = (StudentsByEntryYearBean)getViewState("numberOfStudentsBean");
		RenderUtils.invalidateViewState();
		
		selectedBean.selectStudentsToCreateTutorshipList();
		
		request.setAttribute("filteredStudentsBean", selectedBean);
		request.setAttribute("studentsWithoutTutor", studentsWithoutTutorBeans);
		request.setAttribute("executionDegreeId", selectedBean.getExecutionDegreeID().toString());
		request.setAttribute("degreeCurricularPlanID", selectedBean.getDegreeCurricularPlanID().toString());
		return mapping.findForward("createTutorships");
	}
	
	public ActionForward selectStudentsToCreateTutorships(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final List<StudentsByEntryYearBean> studentsWithoutTutorBeans = (List<StudentsByEntryYearBean>)getViewState("studentsWithoutTutor");
		StudentsByEntryYearBean selectedStudentsBean = (StudentsByEntryYearBean)getViewState("selectedStudentsBean");
		RenderUtils.invalidateViewState();
		
		if(selectedStudentsBean != null) {
			int numberOfStudents = selectedStudentsBean.getStudentsToCreateTutorshipList().size();
			selectedStudentsBean.setNumberOfStudentsToCreateTutorship(numberOfStudents);
		}
		
		if(request.getParameter("clearSelection") != null) {
			selectedStudentsBean.clearSelectedStudentsToCreateTutorshipList();
			request.setAttribute("filteredStudentsBean", selectedStudentsBean);
			request.setAttribute("studentsWithoutTutor", studentsWithoutTutorBeans);
		}
		else if(selectedStudentsBean.getStudentsToCreateTutorshipList().isEmpty()) {
			addActionMessage(request, "error.coordinator.tutor.createTutorship.mustSelectAtLeastOneStudent");
			request.setAttribute("filteredStudentsBean", selectedStudentsBean);
			request.setAttribute("studentsWithoutTutor", studentsWithoutTutorBeans);
		} 
		else {
			selectedStudentsBean.setTutorshipEndMonth(Month.SEPTEMBER);
			selectedStudentsBean.setTutorshipEndYear(Tutorship.getLastPossibleTutorshipYear());
			request.setAttribute("selectedStudentsBean", selectedStudentsBean);
		}
		
		request.setAttribute("executionDegreeId", selectedStudentsBean.getExecutionDegreeID().toString());
		request.setAttribute("degreeCurricularPlanID", selectedStudentsBean.getDegreeCurricularPlanID().toString());
		return mapping.findForward("createTutorships");
	}
	
	public ActionForward prepareSelectTutor(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		StudentsByEntryYearBean selectedStudentsBean = (StudentsByEntryYearBean)getViewState("selectedStudentsBean");
		
		request.setAttribute("selectedStudentsBean", selectedStudentsBean);
		request.setAttribute("executionDegreeId", selectedStudentsBean.getExecutionDegreeID().toString());
		request.setAttribute("degreeCurricularPlanID", selectedStudentsBean.getDegreeCurricularPlanID().toString());
		return mapping.findForward("createTutorships");
	}
	
	public ActionForward prepareCreateTutorshipForSelectedStudents(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		StudentsByEntryYearBean selectedStudentsBean = (StudentsByEntryYearBean)getViewState("selectedStudentsBean");
		RenderUtils.invalidateViewState();

		final ExecutionDegree executiondegree = rootDomainObject.readExecutionDegreeByOID(selectedStudentsBean.getExecutionDegreeID());
		List<Teacher> possibleTutorsForExecutionDegree = executiondegree.getPossibleTutorsFromExecutionDegreeDepartments();
		
		final Teacher teacher = Teacher.readByNumber(selectedStudentsBean.getTeacherNumber());
		
		if (!possibleTutorsForExecutionDegree.contains(teacher)) {
			selectedStudentsBean.setTeacherNumber(null);
			addActionMessage(request, "error.tutor.cannotBeTutorOfExecutionDegree");
		}
		else {
			selectedStudentsBean.setTeacher(teacher);
		}
		
		request.setAttribute("selectedStudentsBean", selectedStudentsBean);
		request.setAttribute("executionDegreeId", selectedStudentsBean.getExecutionDegreeID().toString());
		request.setAttribute("degreeCurricularPlanID", selectedStudentsBean.getDegreeCurricularPlanID().toString());
		return mapping.findForward("createTutorships");
	}
	
	public ActionForward createTutorshipForSelectedStudentsAndTutor(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		StudentsByEntryYearBean selectedStudentsAndTutorBean = (StudentsByEntryYearBean)getViewState("selectedStudentsAndTutorBean");
		RenderUtils.invalidateViewState();
		
		Object[] args = new Object[] {selectedStudentsAndTutorBean.getExecutionDegreeID(), selectedStudentsAndTutorBean };
		
		List<TutorshipErrorBean> tutorshipsNotInserted = new ArrayList<TutorshipErrorBean>();
		try {
			tutorshipsNotInserted = (List<TutorshipErrorBean>) executeService(request,"InsertTutorship", args);
		} catch (FenixServiceException e) {
			addActionMessage(request, e.getMessage(), e.getArgs());
		}

		request.setAttribute("executionDegreeId", selectedStudentsAndTutorBean.getExecutionDegreeID().toString());
		request.setAttribute("degreeCurricularPlanID", selectedStudentsAndTutorBean.getDegreeCurricularPlanID().toString());
		
		if (!tutorshipsNotInserted.isEmpty()) {
			for (TutorshipErrorBean tutorship : tutorshipsNotInserted) {
				addActionMessage(request, tutorship.getMessage(), tutorship.getArgs());
			}
			request.setAttribute("selectedStudentsBean", selectedStudentsAndTutorBean);
			return mapping.findForward("createTutorships");
		}
		else {
			return prepareCreateTutorships(mapping, actionForm, request, response);
		}
	}
	
	/*
	 * AUXILIARY METHODS
	 */
	
	private StudentsByEntryYearBean getSelectedBeanFromList(List<StudentsByEntryYearBean> studentsWithoutTutorBeans, ExecutionYear entryYear) {
		for(StudentsByEntryYearBean bean : studentsWithoutTutorBeans) {
			if(bean.getExecutionYear().equals(entryYear))
				return bean;
		}
		return null;
	}
	
	/*
	 * Returns a list of beans containing a list of students without tutor for
	 * the last 5 entry years
	 */
	private List<StudentsByEntryYearBean> getStudentsWithoutTutorByEntryYearBeans(Integer degreeCurricularPlanID, Integer executionDegreeID) {
		final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) RootDomainObject.readDomainObjectByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);
		
		Map<ExecutionYear, StudentsByEntryYearBean> studentsWithoutTutorByEntryYear = new HashMap<ExecutionYear, StudentsByEntryYearBean>();
		
		ExecutionYear entryYear = ExecutionYear.readCurrentExecutionYear();
		for(int i = 0; i < 5; i++) {
			List<StudentCurricularPlan> studentsWithoutTutor = degreeCurricularPlan.getStudentsWithoutTutorGivenEntryYear(entryYear);
			if(!studentsWithoutTutor.isEmpty()) {
				StudentsByEntryYearBean bean = new StudentsByEntryYearBean(entryYear);
				bean.setStudentsList(studentsWithoutTutor);
				bean.setDegreeCurricularPlanID(degreeCurricularPlanID);
				bean.setExecutionDegreeID(executionDegreeID);
				studentsWithoutTutorByEntryYear.put(entryYear, bean);
			}
			entryYear = entryYear.getPreviousExecutionYear();
		}
		ArrayList<StudentsByEntryYearBean> beans = new ArrayList<StudentsByEntryYearBean>(studentsWithoutTutorByEntryYear.values());
		Collections.sort(beans, new BeanComparator("executionYear"));
		Collections.reverse(beans);
		
		return beans;
	}
	
}
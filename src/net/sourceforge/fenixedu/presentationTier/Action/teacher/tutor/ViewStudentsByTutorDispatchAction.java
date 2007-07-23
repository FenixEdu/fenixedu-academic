package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsByTutorBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.TutorTutorshipsHistoryBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewStudentsByTutorDispatchAction extends FenixDispatchAction {

	public ActionForward viewStudentsByTutor(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		final Person person = getLoggedPerson(request);
		final Teacher teacher = person.getTeacher();
		
		if(!teacher.getTutorships().isEmpty()) {
			TutorTutorshipsHistoryBean tutorshipHistory = new TutorTutorshipsHistoryBean(teacher);
			
			List<StudentsByTutorBean> activeTutorshipsByEntryYear = getTutorshipsByEntryYear(teacher.getActiveTutorships());
			tutorshipHistory.setActiveTutorshipsByEntryYear(activeTutorshipsByEntryYear);
			
			List<StudentsByTutorBean> pastTutorshipsByEntryYear = getTutorshipsByEntryYear(teacher.getPastTutorships());
			tutorshipHistory.setPastTutorshipsByEntryYear(pastTutorshipsByEntryYear);
			
			request.setAttribute("tutorshipHistory", tutorshipHistory);
		}
		
		request.setAttribute("tutor", person);
		return mapping.findForward("viewStudentsByTutor");
	}
	
	/*
	 * AUXILIARY METHODS
	 */
	
	private List<StudentsByTutorBean> getTutorshipsByEntryYear(List<Tutorship> tutorships){
		Map<ExecutionYear,StudentsByTutorBean> tutorshipsMapByEntryYear = new HashMap<ExecutionYear,StudentsByTutorBean>();
		
		for (final Tutorship tutorship : tutorships) {
			ExecutionYear entryYear = ExecutionYear.getExecutionYearByDate(tutorship.getStudentCurricularPlan().getRegistration().getStartDate());
			if(tutorshipsMapByEntryYear.containsKey(entryYear)) {
				List<Tutorship> studentsByEntryYearList = tutorshipsMapByEntryYear.get(entryYear).getStudentsList();
				studentsByEntryYearList.add(tutorship);
				Collections.sort(studentsByEntryYearList, Tutorship.TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER);
				tutorshipsMapByEntryYear.get(entryYear).setStudentsList(studentsByEntryYearList);
			} 
			else {
				List<Tutorship> studentsByEntryYearList = new ArrayList<Tutorship>();
				studentsByEntryYearList.add(tutorship);
				Collections.sort(studentsByEntryYearList, Tutorship.TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER);		
				StudentsByTutorBean studentsByTutorBean = new StudentsByTutorBean(tutorship.getTeacher(), entryYear, studentsByEntryYearList);
				tutorshipsMapByEntryYear.put(entryYear, studentsByTutorBean);
			}
		}
		
		List<StudentsByTutorBean> tutorshipsByEntryYear = new ArrayList<StudentsByTutorBean>(tutorshipsMapByEntryYear.values());
		Collections.sort(tutorshipsByEntryYear, new BeanComparator("studentsEntryYear"));
		Collections.reverse(tutorshipsByEntryYear);
		
		return tutorshipsByEntryYear;
	}
	
}

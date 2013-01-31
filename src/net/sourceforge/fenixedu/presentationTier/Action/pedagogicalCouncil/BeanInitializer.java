package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

/**
 * Class for initializing several types of beans
 * 
 * Dependency Injection
 * 
 * @author jaime
 */
public class BeanInitializer {

	/**
	 * Method for StudentsByEntryYearBean
	 * 
	 * @param studentsByEntryYearBean
	 * @param tutorBean
	 * @param contextBean
	 * @param selectedPersons
	 * @param tutorshipDuration
	 */
	public static void initializeBean(StudentsByEntryYearBean studentsByEntryYearBean, TeacherTutorshipCreationBean tutorBean,
			ContextTutorshipCreationBean contextBean, String[] selectedPersons, int tutorshipDuration) {
		ExecutionDegree executionDegree = contextBean.getExecutionDegree();
		studentsByEntryYearBean.receiveStudentsToCreateTutorshipList(selectedPersons, executionDegree);
		studentsByEntryYearBean.setTeacher(tutorBean.getTeacher().getTeacher());
		DateTime today = new DateTime();
		studentsByEntryYearBean.setTutorshipEndMonth(Month.fromDateTime(today));
		studentsByEntryYearBean.setTutorshipEndYear(today.getYear() + tutorshipDuration);
	}

	/**
	 * Method for different parameters
	 * 
	 * @param studentsByEntryYearBean
	 * @param teacher
	 * @param executionDegree
	 * @param personStudent
	 * @param endDate
	 */
	public static void initializeBean(StudentsByEntryYearBean studentsByEntryYearBean, Teacher teacher,
			ExecutionDegree executionDegree, Person personStudent, Partial endDate) {
		// TODO Auto-generated method stub
		String[] persons = { personStudent.getExternalId().toString() };
		studentsByEntryYearBean.receiveStudentsToCreateTutorshipList(persons, executionDegree);
		studentsByEntryYearBean.setTeacher(teacher);
		int month = endDate.get(DateTimeFieldType.monthOfYear());
		int year = endDate.get(DateTimeFieldType.year());
		studentsByEntryYearBean.setTutorshipEndMonth(Month.fromInt(month));
		studentsByEntryYearBean.setTutorshipEndYear(year);
	}

}

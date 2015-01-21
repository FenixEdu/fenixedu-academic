package org.fenixedu.academic.task;

import java.util.Locale;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;
import org.fenixedu.bennu.scheduler.custom.CustomTask;

public class CreateUsernameForStudent extends CustomTask {

	@Override
	public void runTask() throws Exception {
		Student student  = Student.readStudentByNumber(1);
		Person person = student.getPerson();
		
		UserProfile profile = new UserProfile("Student", "Studentson", "Student", "student@sor.bu.edu", Locale.getDefault());
		User user = new User("student01", profile);
		person.setUser(user);
	}

}

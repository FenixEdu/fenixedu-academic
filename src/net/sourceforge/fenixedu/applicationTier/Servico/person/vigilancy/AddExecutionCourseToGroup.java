package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixWebFramework.services.Service;

public class AddExecutionCourseToGroup extends FenixService {

	@Service
	public static List<ExecutionCourse> run(VigilantGroup group, List<ExecutionCourse> executionCourses) {

		List<ExecutionCourse> executionCoursesUnableToAdd = new ArrayList<ExecutionCourse>();
		for (ExecutionCourse course : executionCourses) {
			try {
				group.addExecutionCourses(course);

			} catch (DomainException e) {
				executionCoursesUnableToAdd.add(course);
			}
		}
		return executionCoursesUnableToAdd;
	}

}
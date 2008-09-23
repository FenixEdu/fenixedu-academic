package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveExecutionCoursesFromGroup extends FenixService {

    public void run(VigilantGroup group, List<ExecutionCourse> executionCourses) {

	for (ExecutionCourse course : executionCourses) {
	    group.removeExecutionCourses(course);
	}
    }

}

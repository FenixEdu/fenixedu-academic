package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveExecutionCoursesFromGroup extends Service {

    public void run(VigilantGroup group, List<ExecutionCourse> executionCourses)
            throws ExcepcaoPersistencia {

        for (ExecutionCourse course : executionCourses) {
            group.removeExecutionCourses(course);
        }
    }

}

package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixWebFramework.services.Service;

public class RemoveExecutionCoursesFromGroup {

    @Service
    public static void run(VigilantGroup group, List<ExecutionCourse> executionCourses) {

        for (ExecutionCourse course : executionCourses) {
            group.removeExecutionCourses(course);
        }
    }

}
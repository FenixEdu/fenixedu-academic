package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import org.fenixedu.spaces.domain.Space;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenTest;
import pt.ist.fenixframework.Atomic;

public class TeacherEditWrittenTestRooms {

    protected void run(ExecutionCourse executionCourse, Teacher teacher, WrittenTest writtenTest, List<Space> rooms) {
        writtenTest.teacherEditRooms(teacher, executionCourse.getExecutionPeriod(), rooms);
    }

    // Service Invokers migrated from Berserk

    private static final TeacherEditWrittenTestRooms serviceInstance = new TeacherEditWrittenTestRooms();

    @Atomic
    public static void runTeacherEditWrittenTestRooms(ExecutionCourse executionCourse, Teacher teacher, WrittenTest writtenTest,
            List<Space> rooms) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourse);
        serviceInstance.run(executionCourse, teacher, writtenTest, rooms);
    }

}
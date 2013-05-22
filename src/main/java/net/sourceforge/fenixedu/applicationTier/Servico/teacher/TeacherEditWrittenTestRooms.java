package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.services.Service;

public class TeacherEditWrittenTestRooms extends FenixService {

    protected void run(ExecutionCourse executionCourse, Teacher teacher, WrittenTest writtenTest, List<AllocatableSpace> rooms) {
        writtenTest.teacherEditRooms(teacher, executionCourse.getExecutionPeriod(), rooms);
    }

    // Service Invokers migrated from Berserk

    private static final TeacherEditWrittenTestRooms serviceInstance = new TeacherEditWrittenTestRooms();

    @Service
    public static void runTeacherEditWrittenTestRooms(ExecutionCourse executionCourse, Teacher teacher, WrittenTest writtenTest,
            List<AllocatableSpace> rooms) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourse);
        serviceInstance.run(executionCourse, teacher, writtenTest, rooms);
    }

}
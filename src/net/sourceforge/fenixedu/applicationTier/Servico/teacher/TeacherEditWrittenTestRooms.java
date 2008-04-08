package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

public class TeacherEditWrittenTestRooms extends Service {

    public void run(ExecutionCourse executionCourse, Teacher teacher, WrittenTest writtenTest, List<AllocatableSpace> rooms) {
	writtenTest.teacherEditRooms(teacher, executionCourse.getExecutionPeriod(), rooms);
    }
}

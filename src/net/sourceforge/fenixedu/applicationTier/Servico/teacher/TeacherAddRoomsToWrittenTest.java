package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

public class TeacherAddRoomsToWrittenTest extends Service {

    public void run(ExecutionCourse executionCourse, Teacher teacher, WrittenTest writtenTest, List<AllocatableSpace> rooms)
	    throws InvalidArgumentsServiceException {
	writtenTest.teacherAddRooms(executionCourse, teacher, rooms);
    }
}

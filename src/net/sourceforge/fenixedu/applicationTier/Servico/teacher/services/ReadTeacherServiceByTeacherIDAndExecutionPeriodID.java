/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadTeacherServiceByTeacherIDAndExecutionPeriodID extends FenixService {

    @Service
    public static TeacherService run(Integer teacherID, Integer executionPeriodID) {
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);

        return teacher.getTeacherServiceByExecutionPeriod(executionSemester);
    }

}
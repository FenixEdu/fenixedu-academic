/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;


import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadTeacherServiceByTeacherIDAndExecutionPeriodID {

    @Service
    public static TeacherService run(Integer teacherID, Integer executionPeriodID) {
        Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(teacherID);
        ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodID);

        return teacher.getTeacherServiceByExecutionPeriod(executionSemester);
    }

}
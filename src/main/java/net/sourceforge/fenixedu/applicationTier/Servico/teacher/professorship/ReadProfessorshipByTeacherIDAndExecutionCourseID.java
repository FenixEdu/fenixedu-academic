/**
 * Nov 21, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;


import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadProfessorshipByTeacherIDAndExecutionCourseID {

    @Service
    public static Professorship run(final Integer teacherID, final Integer executionCourseID) {
        Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(teacherID);
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseID);

        return teacher.getProfessorshipByExecutionCourse(executionCourse);
    }
}
/*
 * Created on Aug 26, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;

/**
 * @author João Mota
 * 
 */
public class ReadExecutionCoursesByTeacherResponsibility {

    @Atomic
    public static List run(String id) throws FenixServiceException {

        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        Person person = Person.readPersonByUsername(id);
        if (person.getTeacher() != null) {
            Teacher teacher = person.getTeacher();

            final List<Professorship> responsibilities = teacher.responsibleFors();

            if (responsibilities != null) {
                for (final Professorship professorship : responsibilities) {
                    infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(professorship.getExecutionCourse()));
                }
            }
            return infoExecutionCourses;
        } else {
            return new ArrayList<Professorship>();
        }
    }
}
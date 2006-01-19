/**
 * Jan 16, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDomainTeacherByNumber extends Service {

    public Teacher run(final Integer teacherNumber) throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<Teacher> teachers = (List<Teacher>) persistentSupport.getIPersistentTeacher().readAll(
                Teacher.class);

        Teacher teacher = (Teacher) CollectionUtils.find(teachers, new Predicate() {

            public boolean evaluate(Object object) {
                Teacher tempTeacher = (Teacher) object;
                return tempTeacher.getTeacherNumber().equals(teacherNumber);
            }
        });

        if (teacher == null) {
            throw new FenixServiceException("errors.invalid.teacher-number");
        }
        return teacher;
    }
}

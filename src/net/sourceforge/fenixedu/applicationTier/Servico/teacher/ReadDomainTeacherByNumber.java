package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.Predicate;

public class ReadDomainTeacherByNumber extends Service {

    public Teacher run(final Integer teacherNumber) throws FenixServiceException {
        List<Teacher> teachers = rootDomainObject.getTeachers();
        
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

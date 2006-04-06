/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class InsertStudentDistributedTestAdvisory extends Service {

    public void run(final Integer executionCourseId, final Integer advisoryId, final Integer studentId) throws ExcepcaoPersistencia {
        Advisory advisory = rootDomainObject.readAdvisoryByOID(advisoryId);
        Student student = rootDomainObject.readStudentByOID(studentId);
        advisory.addPeople(student.getPerson());
    }

}
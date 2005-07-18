/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAdvisory;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertStudentDistributedTestAdvisory implements IService {

    public void run(final Integer executionCourseId, final Integer advisoryId, final Integer studentId) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentAdvisory persistentAdvisory = persistentSuport.getIPersistentAdvisory();

        final IAdvisory advisory = (IAdvisory) persistentAdvisory.readByOID(Advisory.class, advisoryId);
        final IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(Student.class, studentId);
        final IPerson person = student.getPerson();

        persistentAdvisory.simpleLockWrite(advisory);
        persistentAdvisory.simpleLockWrite(person);

        advisory.getPeople().add(person);
        person.getAdvisories().add(advisory);
    }

}
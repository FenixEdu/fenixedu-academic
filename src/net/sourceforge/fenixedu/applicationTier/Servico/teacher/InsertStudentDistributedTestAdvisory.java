/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertStudentDistributedTestAdvisory implements IService {

    public InsertStudentDistributedTestAdvisory() {
    }

    public void run(Integer executionCourseId, Integer advisoryId, Integer studentId)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IAdvisory advisory = (IAdvisory) persistentSuport.getIPersistentAdvisory().readByOID(
                    Advisory.class, advisoryId);
            IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(
                    Student.class, studentId);

            persistentSuport.getIPersistentAdvisory().write(advisory, student.getPerson());
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
    }
}
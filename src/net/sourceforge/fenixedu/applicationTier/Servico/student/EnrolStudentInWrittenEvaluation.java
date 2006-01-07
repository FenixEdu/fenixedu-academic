package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EnrolStudentInWrittenEvaluation implements IService {

	public void run(String username, Integer writtenEvaluationOID) throws FenixServiceException,
			ExcepcaoPersistencia {
		final ISuportePersistente persistenceSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();

		final IPersistentStudent persistentStudent = persistenceSupport.getIPersistentStudent();
		final Student student = persistentStudent.readByUsername(username);

		final IPersistentObject persistentObject = persistenceSupport.getIPersistentObject();
		final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) persistentObject.readByOID(
				WrittenEvaluation.class, writtenEvaluationOID, true);
		if (writtenEvaluation == null || student == null) {
			throw new InvalidArgumentsServiceException();
		}

		writtenEvaluation.enrolStudent(student);
	}

}

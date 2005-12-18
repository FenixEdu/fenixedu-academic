package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadGratuitySituationByExecutionDegreeIDAndStudentID implements IService {

	public InfoGratuitySituation run(Integer executionDegreeID, Integer studentID)
			throws FenixServiceException, ExcepcaoPersistencia {

		InfoGratuitySituation infoGratuitySituation = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IExecutionDegree executionDegree = (IExecutionDegree) sp.getIPersistentExecutionDegree()
				.readByOID(ExecutionDegree.class, executionDegreeID);
		IStudent student = (IStudent) sp.getIPersistentStudent().readByOID(Student.class, studentID);

		if ((executionDegree == null) || (student == null)) {
			return null;
		}

		IGratuitySituation gratuitySituation = sp.getIPersistentGratuitySituation()
				.readGratuitySituationByExecutionDegreeAndStudent(executionDegree.getIdInternal(),
						student.getIdInternal());

		infoGratuitySituation = InfoGratuitySituation.newInfoFromDomain(gratuitySituation);

		return infoGratuitySituation;
	}

}
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsTeacher;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Tânia Pousão
 *
 */
public class CreditsTeacherOJB extends ObjectFenixOJB implements IPersistentCreditsTeacher {

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentTeacherShiftPercentage#readByUnique(Dominio.IShiftProfessorship)
	 */
	public ICredits readByUnique(ICredits creditsTeacher) throws ExcepcaoPersistencia {
		ICredits creditsTeacherFromBD = null;
		Criteria criteria = new Criteria();

		IExecutionPeriod executionPeriod = creditsTeacher.getExecutionPeriod();
		criteria.addEqualTo("executionPeriod.semester", executionPeriod.getSemester());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo("executionPeriod.executionYear.year", executionPeriod.getExecutionYear().getYear());

		ITeacher teacher = creditsTeacher.getTeacher();
		criteria.addEqualTo("teacher.teacherNumber", teacher.getTeacherNumber());
		criteria.addEqualTo("teacher.person.username", teacher.getPerson().getUsername());

		creditsTeacherFromBD = (ICredits) queryObject(Credits.class, criteria);
		return creditsTeacherFromBD;
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentObject#lockWrite(java.lang.Object)
	 */
	public void lockWrite(Object obj) throws ExcepcaoPersistencia {
		if (obj instanceof ICredits) {

			ICredits creditsTeacherToWrite = (ICredits) obj;

			ICredits creditsTeacherFromBD = this.readByUnique(creditsTeacherToWrite);

			// If credits is not in database, then write it.

			if (creditsTeacherFromBD == null) {
				super.lockWrite(creditsTeacherToWrite);
				//				else If the credits is mapped to the database, then write any existing changes.				
			} else if (creditsTeacherFromBD.getIdInternal().equals(creditsTeacherToWrite.getIdInternal())) {
				super.lockWrite(creditsTeacherToWrite);

			} else { // else Throw an already existing exception
				throw new ExistingPersistentException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentCreditsTeacher#delete(Dominio.ICredits)
	 */
	public void delete(ICredits creditsTeacher) throws ExcepcaoPersistencia {
		super.delete(creditsTeacher);
	}
}

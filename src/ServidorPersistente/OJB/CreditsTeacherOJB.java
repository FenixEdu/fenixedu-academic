package ServidorPersistente.OJB;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

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
	 * @see ServidorPersistente.IPersistentTeacherShiftPercentage#readByUnique(Dominio.ITeacherShiftPercentage)
	 */
	public ICredits readByUnique(ICredits creditsTeacher) throws ExcepcaoPersistencia {
		ICredits creditsTeacherFromBD = null;

		PersistenceBroker broker = ((HasBroker) odmg.currentTransaction()).getBroker();

		Criteria criteria = new Criteria();

		IExecutionPeriod executionPeriod = creditsTeacher.getExecutionPeriod();
		criteria.addEqualTo("executionPeriod.semester", executionPeriod.getSemester());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo("executionPeriod.state.stateCode", executionPeriod.getState().getCode());
		criteria.addEqualTo("executionPeriod.executionYear.state.stateCode", executionPeriod.getExecutionYear().getState().getCode());
		criteria.addEqualTo("executionPeriod.executionYear.year", executionPeriod.getExecutionYear().getYear());

		ITeacher teacher = creditsTeacher.getTeacher();
		criteria.addEqualTo("teacher.teacherNumber", teacher.getTeacherNumber());
		criteria.addEqualTo("teacher.person.username", teacher.getPerson().getUsername());

		Query queryPB = new QueryByCriteria(Credits.class, criteria);
		creditsTeacherFromBD = (ICredits) broker.getObjectByQuery(queryPB);
		return creditsTeacherFromBD;
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentObject#lockWrite(java.lang.Object)
	 */
	public void lockWrite(Object obj) throws ExcepcaoPersistencia {
		if (obj instanceof ICredits) {

			ICredits creditsTeacherToWrite = (ICredits) obj;

			ICredits creditsTeacherFromBD = this.readByUnique(creditsTeacherToWrite);
			
			// If department is not in database, then write it.
			if (creditsTeacherFromBD == null) {
				super.lockWrite(creditsTeacherToWrite);
			} else if (
				// else If the department is mapped to the database, then write any existing changes.
			creditsTeacherFromBD
				.getIdInternal()
				.equals(
					creditsTeacherToWrite.getIdInternal())) {
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

/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

import Dominio.IDisciplinaExecucao;
import Dominio.ITeacher;
import Dominio.ITeacherShiftPercentage;
import Dominio.TeacherShiftPercentage;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacherShiftPercentage;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author jpvl
 */
public class TeacherShiftPercentageOJB
	extends ObjectFenixOJB
	implements IPersistentTeacherShiftPercentage {

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentTeacherShiftPercentage#readByUnique(Dominio.ITeacherShiftPercentage)
	 */
	public ITeacherShiftPercentage readByUnique(ITeacherShiftPercentage teacherShiftPercentage)
		throws ExcepcaoPersistencia {
		ITeacherShiftPercentage teacherShiftPercentageFromBD = null;

		PersistenceBroker broker =
			((HasBroker) odmg.currentTransaction()).getBroker();

		Criteria criteria = new Criteria();

		IDisciplinaExecucao executionCourse =
			teacherShiftPercentage.getShift().getDisciplinaExecucao();

		criteria.addEqualTo(
			"shift.nome",
			teacherShiftPercentage.getShift().getNome());

		criteria.addEqualTo(
			"shift.disciplinaExecucao.sigla",
			executionCourse.getSigla());
		criteria.addEqualTo(
			"shift.disciplinaExecucao.executionPeriod.name",
			executionCourse.getExecutionPeriod().getName());
		criteria.addEqualTo(
			"shift.disciplinaExecucao.executionPeriod.executionYear.year",
			executionCourse.getExecutionPeriod().getExecutionYear().getYear());

		ITeacher teacher =
			teacherShiftPercentage.getProfessorShip().getTeacher();

		criteria.addEqualTo(
			"professorShip.teacher.teacherNumber",
			teacher.getTeacherNumber());
		criteria.addEqualTo(
			"professorShip.teacher.person.username",
			teacher.getPerson().getUsername());
		criteria.addEqualTo(
			"professorShip.executionCourse.sigla",
			executionCourse.getSigla());
		criteria.addEqualTo(
			"professorShip.executionCourse.executionPeriod.name",
			executionCourse.getExecutionPeriod().getName());
		criteria.addEqualTo(
			"professorShip.executionCourse.executionPeriod.executionYear.year",
			executionCourse.getExecutionPeriod().getExecutionYear().getYear());

		Query queryPB =
			new QueryByCriteria(TeacherShiftPercentage.class, criteria);
		teacherShiftPercentageFromBD =
			(ITeacherShiftPercentage) broker.getObjectByQuery(queryPB);
		return teacherShiftPercentageFromBD;
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentObject#lockWrite(java.lang.Object)
	 */
	public void lockWrite(Object obj) throws ExcepcaoPersistencia {
		if (obj instanceof ITeacherShiftPercentage) {

			ITeacherShiftPercentage teacherShiftPercentageToWrite =
				(ITeacherShiftPercentage) obj;

			ITeacherShiftPercentage teacherShiftPercentageFromBD =
				this.readByUnique(teacherShiftPercentageToWrite);
			// If department is not in database, then write it.
			if (teacherShiftPercentageFromBD == null){
				super.lockWrite(teacherShiftPercentageToWrite);
			}else if (// else If the department is mapped to the database, then write any existing changes.
				teacherShiftPercentageFromBD.getIdInternal().equals(
					teacherShiftPercentageToWrite.getIdInternal())) {
				super.lockWrite(teacherShiftPercentageToWrite);

			} else { // else Throw an already existing exception
				throw new ExistingPersistentException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentTeacherShiftPercentage#delete(Dominio.ITeacherShiftPercentage)
	 */
	public void delete(ITeacherShiftPercentage teacherShiftPercentage) throws ExcepcaoPersistencia {
		super.delete(teacherShiftPercentage);
	}
}
/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package ServidorPersistente.OJB.teacher.professorship;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.IShiftProfessorship;
import Dominio.ITurno;
import Dominio.ShiftProfessorship;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author jpvl
 */
public class ShiftProfessorshipOJB
	extends ObjectFenixOJB
	implements IPersistentShiftProfessorship {

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentTeacherShiftPercentage#readByUnique(Dominio.IShiftProfessorship)
	 */
	public IShiftProfessorship readByUnique(IShiftProfessorship teacherShiftPercentage)
		throws ExcepcaoPersistencia {
		IShiftProfessorship teacherShiftPercentageFromBD = null;

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
			teacherShiftPercentage.getProfessorship().getTeacher();

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
			new QueryByCriteria(ShiftProfessorship.class, criteria);
		teacherShiftPercentageFromBD =
			(IShiftProfessorship) broker.getObjectByQuery(queryPB);
		return teacherShiftPercentageFromBD;
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentObject#lockWrite(java.lang.Object)
	 */
	public void lockWrite(Object obj) throws ExcepcaoPersistencia {
		if (obj instanceof IShiftProfessorship) {

			IShiftProfessorship teacherShiftPercentageToWrite =
				(IShiftProfessorship) obj;

			IShiftProfessorship teacherShiftPercentageFromBD =
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
	 * @see ServidorPersistente.IPersistentTeacherShiftPercentage#delete(Dominio.IShiftProfessorship)
	 */
	public void delete(IShiftProfessorship teacherShiftPercentage) throws ExcepcaoPersistencia {
		super.delete(teacherShiftPercentage);
	}

    /* (non-Javadoc)
     * @see ServidorPersistente.IPersistentShiftProfessorship#readByProfessorshipAndShift(Dominio.IProfessorship, Dominio.ITurno)
     */
    public IShiftProfessorship readByProfessorshipAndShift(IProfessorship professorship, ITurno shift) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProfessorship", professorship.getIdInternal());
        criteria.addEqualTo("keyShift", shift.getIdInternal());
        return (IShiftProfessorship) queryObject(ShiftProfessorship.class, criteria);
    }
}
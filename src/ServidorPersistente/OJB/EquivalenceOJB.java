package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.Equivalence;
import Dominio.IEnrolment;
import Dominio.IEquivalence;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEquivalence;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class EquivalenceOJB extends ObjectFenixOJB implements IPersistentEquivalence {
    
	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Equivalence.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void lockWrite(IEquivalence equivalenceToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		IEquivalence equivalenceFromDB = null;

		// If there is nothing to write, simply return.
		if (equivalenceToWrite == null) {
			return;
		}

		// Read Equivalence from database.
		equivalenceFromDB = this.readEquivalenceByEnrolmentAndEquivalentEnrolment(equivalenceToWrite.getEnrolment(), equivalenceToWrite.getEquivalentEnrolment());

		// If Equivalence is not in database, then write it.
		if (equivalenceFromDB == null) {
			super.lockWrite(equivalenceToWrite);
		// else If the Equivalence is mapped to the database, then write any existing changes.
		} else if ((equivalenceToWrite instanceof Equivalence) && ((Equivalence) equivalenceFromDB).getInternalID().equals(((Equivalence) equivalenceToWrite).getInternalID())) {
			super.lockWrite(equivalenceToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IEquivalence enrolment) throws ExcepcaoPersistencia {
		try {
			super.delete(enrolment);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public IEquivalence readEquivalenceByEnrolmentAndEquivalentEnrolment(IEnrolment enrolment, IEnrolment equivalentEnrolment) throws ExcepcaoPersistencia {

		try {
			IEquivalence equivalence = null;
			String oqlQuery = "select all from " + Equivalence.class.getName();
			oqlQuery += " where enrolment.studentCurricularPlan.student.number = $1";
			oqlQuery += " and enrolment.studentCurricularPlan.student.degreeType = $2";
			oqlQuery += " and enrolment.studentCurricularPlan.currentState = $3";
			oqlQuery += " and enrolment.curricularCourse.name = $4";
			oqlQuery += " and enrolment.curricularCourse.code = $5";
			oqlQuery += " and enrolment.curricularCourse.degreeCurricularPlan.name = $6";
			oqlQuery += " and enrolment.curricularCourse.degreeCurricularPlan.degree.nome = $7";
			oqlQuery += " and enrolment.curricularCourse.degreeCurricularPlan.degree.sigla = $8";

			oqlQuery += " and equivalentEnrolment.studentCurricularPlan.student.number = $9";
			oqlQuery += " and equivalentEnrolment.studentCurricularPlan.student.degreeType = $10";
			oqlQuery += " and equivalentEnrolment.studentCurricularPlan.currentState = $11";
			oqlQuery += " and equivalentEnrolment.curricularCourse.name = $12";
			oqlQuery += " and equivalentEnrolment.curricularCourse.code = $13";
			oqlQuery += " and equivalentEnrolment.curricularCourse.degreeCurricularPlan.name = $14";
			oqlQuery += " and equivalentEnrolment.curricularCourse.degreeCurricularPlan.degree.nome = $15";
			oqlQuery += " and equivalentEnrolment.curricularCourse.degreeCurricularPlan.degree.sigla = $16";

			query.create(oqlQuery);

			query.bind(enrolment.getStudentCurricularPlan().getStudent().getNumber());
			query.bind(enrolment.getStudentCurricularPlan().getStudent().getDegreeType());
			query.bind(enrolment.getStudentCurricularPlan().getCurrentState());
			query.bind(enrolment.getCurricularCourse().getName());
			query.bind(enrolment.getCurricularCourse().getCode());
			query.bind(enrolment.getCurricularCourse().getDegreeCurricularPlan().getName());
			query.bind(enrolment.getCurricularCourse().getDegreeCurricularPlan().getDegree().getNome());
			query.bind(enrolment.getCurricularCourse().getDegreeCurricularPlan().getDegree().getSigla());

			query.bind(equivalentEnrolment.getStudentCurricularPlan().getStudent().getNumber());
			query.bind(equivalentEnrolment.getStudentCurricularPlan().getStudent().getDegreeType());
			query.bind(equivalentEnrolment.getStudentCurricularPlan().getCurrentState());
			query.bind(equivalentEnrolment.getCurricularCourse().getName());
			query.bind(equivalentEnrolment.getCurricularCourse().getCode());
			query.bind(equivalentEnrolment.getCurricularCourse().getDegreeCurricularPlan().getName());
			query.bind(equivalentEnrolment.getCurricularCourse().getDegreeCurricularPlan().getDegree().getNome());
			query.bind(equivalentEnrolment.getCurricularCourse().getDegreeCurricularPlan().getDegree().getSigla());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if( (result != null) && (result.size() != 0) ) {
				equivalence = (IEquivalence) result.get(0);
			}
			return equivalence;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ArrayList readAll() throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + Equivalence.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if( (result != null) && (result.size() != 0) ) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((IEquivalence) iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}
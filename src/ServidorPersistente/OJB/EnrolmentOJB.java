package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class EnrolmentOJB
	extends ObjectFenixOJB
	implements IPersistentEnrolment {

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Enrolment.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void lockWrite(IEnrolment enrolmentToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		IEnrolment enrolmentFromDB = null;

		// If there is nothing to write, simply return.
		if (enrolmentToWrite == null) {
			return;
		}

		// Read Enrolment from database.
		/*		enrolmentFromDB =
					this.readEnrolmentByStudentCurricularPlanAndCurricularCourse(
						enrolmentToWrite.getStudentCurricularPlan(),
						enrolmentToWrite.getCurricularCourse());
		*/
		enrolmentFromDB =(IEnrolment) this.readEnrolmentByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				enrolmentToWrite.getStudentCurricularPlan(),
				enrolmentToWrite.getCurricularCourse(),
				enrolmentToWrite.getExecutionPeriod());
		// If Enrolment is not in database, then write it.
		if (enrolmentFromDB == null) {
			super.lockWrite(enrolmentToWrite);
			// else If the Enrolment is mapped to the database, then write any existing changes.
		} else if (
			(enrolmentToWrite instanceof Enrolment)
				&& ((Enrolment) enrolmentFromDB).getInternalID().equals(
					((Enrolment) enrolmentToWrite).getInternalID())) {
			super.lockWrite(enrolmentToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IEnrolment enrolment) throws ExcepcaoPersistencia {
		try {
			super.delete(enrolment);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourse(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse)
		throws ExcepcaoPersistencia {

		try {
			IEnrolment enrolment = null;
			String oqlQuery = "select all from " + Enrolment.class.getName();
			oqlQuery += " where studentCurricularPlan.student.number = $1";
			oqlQuery += " and studentCurricularPlan.student.degreeType = $2";
			oqlQuery += " and studentCurricularPlan.currentState = $3";
			oqlQuery += " and curricularCourse.name = $4";
			oqlQuery += " and curricularCourse.code = $5";
			oqlQuery += " and curricularCourse.degreeCurricularPlan.name = $6";
			oqlQuery
				+= " and curricularCourse.degreeCurricularPlan.degree.nome = $7";
			oqlQuery
				+= " and curricularCourse.degreeCurricularPlan.degree.sigla = $8";

			query.create(oqlQuery);

			query.bind(studentCurricularPlan.getStudent().getNumber());
			query.bind(studentCurricularPlan.getStudent().getDegreeType());
			query.bind(studentCurricularPlan.getCurrentState());
			query.bind(curricularCourse.getName());
			query.bind(curricularCourse.getCode());
			query.bind(curricularCourse.getDegreeCurricularPlan().getName());
			query.bind(
				curricularCourse
					.getDegreeCurricularPlan()
					.getDegree()
					.getNome());
			query.bind(
				curricularCourse
					.getDegreeCurricularPlan()
					.getDegree()
					.getSigla());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				enrolment = (IEnrolment) result.get(0);
			}
			return enrolment;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readAll() throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + Enrolment.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((IEnrolment) iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
		IStudentCurricularPlan studentCurricularPlan,
		EnrolmentState enrolmentState)
		throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + Enrolment.class.getName();
			oqlQuery += " where studentCurricularPlan.student.number = $1";
			oqlQuery += " and studentCurricularPlan.student.degreeType = $2";
			oqlQuery += " and studentCurricularPlan.currentState = $3";
			oqlQuery += " and state = $4";

			query.create(oqlQuery);

			query.bind(studentCurricularPlan.getStudent().getNumber());
			query.bind(studentCurricularPlan.getStudent().getDegreeType());
			query.bind(studentCurricularPlan.getCurrentState());
			query.bind(enrolmentState.getState());

			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((IEnrolment) iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEnrolment#readAllByStudentCurricularPlan(Dominio.IStudentCurricularPlan)
	 */
	public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia {
		List enrolments = new ArrayList();
		try {
			String oqlQuery =
				"select all from "
					+ Enrolment.class.getName()
					+ " where studentCurricularPlan.student.number = $1"
					+ " and studentCurricularPlan.student.degreeType = $2"
					+ " and studentCurricularPlan.currentState = $3";

			query.create(oqlQuery);

			query.bind(studentCurricularPlan.getStudent().getNumber());
			query.bind(studentCurricularPlan.getStudent().getDegreeType());
			query.bind(studentCurricularPlan.getCurrentState());

			enrolments = (List) query.execute();

			lockRead(enrolments);
		} catch (QueryException e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
		return enrolments;
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEnrolment#readEnrolmentByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(Dominio.IStudentCurricularPlan, Dominio.ICurricularCourse, Dominio.IExecutionPeriod)
	 */
	public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		try {
			IEnrolment enrolment = null;
			String oqlQuery = "select all from " + Enrolment.class.getName();
			oqlQuery += " where studentCurricularPlan.student.number = $1";
			oqlQuery += " and studentCurricularPlan.student.degreeType = $2";
			oqlQuery += " and studentCurricularPlan.currentState = $3";
			oqlQuery += " and curricularCourse.name = $4";
			oqlQuery += " and curricularCourse.code = $5";
			oqlQuery += " and curricularCourse.degreeCurricularPlan.name = $6";
			oqlQuery
				+= " and curricularCourse.degreeCurricularPlan.degree.nome = $7";
			oqlQuery
				+= " and curricularCourse.degreeCurricularPlan.degree.sigla = $8";
			oqlQuery += " and executionPeriod.name = $9";
			oqlQuery += " and executionPeriod.executionYear.year = $10";

			query.create(oqlQuery);

			query.bind(studentCurricularPlan.getStudent().getNumber());
			query.bind(studentCurricularPlan.getStudent().getDegreeType());
			query.bind(studentCurricularPlan.getCurrentState());
			query.bind(curricularCourse.getName());
			query.bind(curricularCourse.getCode());
			query.bind(curricularCourse.getDegreeCurricularPlan().getName());
			query.bind(
				curricularCourse
					.getDegreeCurricularPlan()
					.getDegree()
					.getNome());
			query.bind(
				curricularCourse
					.getDegreeCurricularPlan()
					.getDegree()
					.getSigla());

			query.bind(executionPeriod.getName());
			query.bind(executionPeriod.getExecutionYear().getYear());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				enrolment = (IEnrolment) result.get(0);
			}
			return enrolment;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
}
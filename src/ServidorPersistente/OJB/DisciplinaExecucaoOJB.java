/*
 * SitioOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.CurricularCourse;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class DisciplinaExecucaoOJB
	extends ObjectFenixOJB
	implements IDisciplinaExecucaoPersistente {

	public DisciplinaExecucaoOJB() {
	}

	public boolean apagarTodasAsDisciplinasExecucao() {
		try {
			String oqlQuery =
				"select all from " + DisciplinaExecucao.class.getName();
			super.deleteAll(oqlQuery);
			return true;
		} catch (ExcepcaoPersistencia ex) {
			return false;
		}
	}

	public void escreverDisciplinaExecucao(IDisciplinaExecucao executionCourseToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		IDisciplinaExecucao executionCourseFromDB = null;

		// If there is nothing to write, simply return.
		if (executionCourseToWrite == null)
			return;

		// Read execution course from database.
		executionCourseFromDB =
			this.readByExecutionCourseInitialsAndExecutionPeriod(
				executionCourseToWrite.getSigla(),
				executionCourseToWrite.getExecutionPeriod());

		// If execution course is not in database, then write it.
		if (executionCourseFromDB == null)
			super.lockWrite(executionCourseToWrite);
		// else If the execution course is mapped to the database, then write any existing changes.
		else if (
			(executionCourseToWrite instanceof DisciplinaExecucao)
				&& ((DisciplinaExecucao) executionCourseFromDB)
					.getCodigoInterno()
					.equals(
					((DisciplinaExecucao) executionCourseToWrite)
						.getCodigoInterno())) {
			super.lockWrite(executionCourseToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	// TODO : Write test for this method
	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IDisciplinaExecucao readBySiglaAndAnoLectivoAndSiglaLicenciatura(
		String sigla,
		String anoLectivo,
		String siglaLicenciatura)
		throws ExcepcaoPersistencia {
		try {
			IDisciplinaExecucao disciplinaExecucao = null;
			String oqlQuery =
				"select disciplinaExecucao from "
					+ DisciplinaExecucao.class.getName();
			oqlQuery += " where sigla = $1";
			oqlQuery += " and executionPeriod.executionYear.year = $2";
			oqlQuery
				+= " and associatedCurricularCourses.degreeCurricularPlan.degree.sigla = $3";
			query.create(oqlQuery);
			query.bind(sigla);
			query.bind(anoLectivo);
			query.bind(siglaLicenciatura);
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				disciplinaExecucao = (IDisciplinaExecucao) result.get(0);
			return disciplinaExecucao;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/**
	 * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByAnoCurricularAndAnoLectivoAndSiglaLicenciatura(java.lang.Integer, Dominio.IExecutionPeriod, java.lang.String)
	 */
//	TODO: David: Este metodo faz uma query atraves de CurricularCourse.curricularYear. Serviços onde isto é usado: sop.LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular e publico.SelectExecutionCourse.
	public List readByCurricularYearAndExecutionPeriodAndExecutionDegree(
		Integer curricularYear,
		IExecutionPeriod executionPeriod,
		ICursoExecucao executionDegree)
		throws ExcepcaoPersistencia {
		List resultList = new ArrayList();

		try {
			String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}


		try {

			String oqlQuery =
				"select distinct all from "
					+ CurricularCourse.class.getName()
					+ " where curricularYear = $1 "
					+ " and degreeCurricularPlan.name = $2 "
					+ " and degreeCurricularPlan.degree.sigla = $3";

			query.create(oqlQuery);

			query.bind(curricularYear);
			query.bind(executionDegree.getCurricularPlan().getName());
			query.bind(
				executionDegree.getCurricularPlan().getDegree().getSigla());

			List result = (List) query.execute();
			lockRead(result);
			
			Iterator iterator = result.listIterator();

			
			while (iterator.hasNext()) {
				ICurricularCourse curricularCourse =
					(ICurricularCourse) iterator.next();
				Iterator executionCourseIterator =
					curricularCourse.getAssociatedExecutionCourses().iterator();

				while (executionCourseIterator.hasNext()) {
					IDisciplinaExecucao executionCourse =
						(IDisciplinaExecucao) executionCourseIterator.next();
					if (executionCourse
						.getExecutionPeriod()
						.equals(executionPeriod)
						&& !resultList.contains(executionCourse)) {
						resultList.add(executionCourse);
					}
				}
			}

		} catch (QueryException ex) {
			ex.printStackTrace(System.out);
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		return resultList;
	}
	/**
	 * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionCourseInitials(java.lang.String)
	 */
	public IDisciplinaExecucao readByExecutionCourseInitialsAndExecutionPeriod(
		String courseInitials,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		try {

			String oqlQuery =
				"select distinct all from "
					+ DisciplinaExecucao.class.getName();
			oqlQuery += " where executionPeriod.name = $1 ";
			oqlQuery += " and executionPeriod.executionYear.year = $2 "
				+ " and sigla = $3 ";

			query.create(oqlQuery);

			query.bind(executionPeriod.getName());
			query.bind(executionPeriod.getExecutionYear().getYear());
			query.bind(courseInitials);

			List result = (List) query.execute();
			lockRead(result);

			IDisciplinaExecucao executionCourse = null;
			if (!result.isEmpty())
				executionCourse = (IDisciplinaExecucao) result.get(0);
			return executionCourse;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void deleteExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from " + DisciplinaExecucao.class.getName();
			oqlQuery += " where executionPeriod.name = $1 "
				+ " and executionPeriod.executionYear.year = $2 "
				+ " and sigla = $3 ";
			query.create(oqlQuery);

			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(
				executionCourse
					.getExecutionPeriod()
					.getExecutionYear()
					.getYear());
			query.bind(executionCourse.getSigla());

			List result = (List) query.execute();
			lockRead(result);

			if (!result.isEmpty()) {
				IDisciplinaExecucao executionCourseTemp =
					(IDisciplinaExecucao) result.get(0);
				// Delete All Attends

				List attendsTemp =
					SuportePersistenteOJB
						.getInstance()
						.getIFrequentaPersistente()
						.readByExecutionCourse(executionCourseTemp);
				Iterator iterator = attendsTemp.iterator();
				while (iterator.hasNext()) {
					SuportePersistenteOJB
						.getInstance()
						.getIFrequentaPersistente()
						.delete(
						(IFrequenta) iterator.next());
				}

				// Delete All Shifts
				List shiftsTemp =
					SuportePersistenteOJB
						.getInstance()
						.getITurnoPersistente()
						.readByExecutionCourse(executionCourseTemp);
				iterator = shiftsTemp.iterator();
				while (iterator.hasNext()) {
					SuportePersistenteOJB
						.getInstance()
						.getITurnoPersistente()
						.delete(
						(ITurno) iterator.next());
				}
				super.delete(executionCourseTemp);
			}

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByExecutionPeriod(IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from " + DisciplinaExecucao.class.getName();
			oqlQuery += " where executionPeriod.name = $1 "
				+ " and executionPeriod.executionYear.year = $2 ";
			query.create(oqlQuery);

			query.bind(executionPeriod.getName());
			query.bind(executionPeriod.getExecutionYear().getYear());

			List result = (List) query.execute();
			lockRead(result);

			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}

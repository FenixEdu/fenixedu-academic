/*
 * TurnoAlunoOJB.java
 *
 * Created on 21 de Outubro de 2002, 19:03
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.IDisciplinaExecucao;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.TurnoAluno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurnoAlunoPersistente;
import Util.TipoAula;

public class TurnoAlunoOJB
	extends ObjectFenixOJB
	implements ITurnoAlunoPersistente {

	public ITurnoAluno readByTurnoAndAluno(ITurno turno, IStudent aluno)
		throws ExcepcaoPersistencia {
		try {
			ITurnoAluno turnoAluno = null;
			String oqlQuery = "select turnoaluno from " + TurnoAluno.class.getName()
							+ " where aluno.number = $1"
							+ " and aluno.degreeType = $2"
							+ " and turno.nome = $3"
							+ " and turno.disciplinaExecucao.sigla = $4"
							+ " and turno.disciplinaExecucao.executionPeriod.name = $5"
							+ " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $6";
			
			query.create(oqlQuery);
			query.bind(aluno.getNumber());
			query.bind(aluno.getDegreeType());
			query.bind(turno.getNome());
			query.bind(turno.getDisciplinaExecucao().getSigla());
			query.bind(turno.getDisciplinaExecucao().getExecutionPeriod().getName());
			query.bind(turno.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
			
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				turnoAluno = (ITurnoAluno) result.get(0);
			return turnoAluno;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ITurnoAluno turnoAluno) throws ExcepcaoPersistencia {
		super.lockWrite(turnoAluno);
	}

	public void delete(ITurnoAluno turnoAluno) throws ExcepcaoPersistencia {
		super.delete(turnoAluno);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + TurnoAluno.class.getName();
		super.deleteAll(oqlQuery);
	}

	/**
	 * FIXME: wrong link from executionCourse to ExecutionDegree
	 */
	public ITurno readByStudentIdAndShiftType(
		Integer id,
		TipoAula shiftType,
		String nameExecutionCourse)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select turno from " + TurnoAluno.class.getName();
			oqlQuery += " where turno.tipo = $1";
			oqlQuery
				+= " and turno.disciplinaExecucao.licenciaturaExecucao.nome = $2";
			oqlQuery += " and aluno.number = $3";
			query.create(oqlQuery);
			query.bind(shiftType);
			query.bind(nameExecutionCourse);
			query.bind(id);
			List result = (List) query.execute();
			lockRead(result);
			if (result != null && !(result.isEmpty()))
				return ((ITurnoAluno) result.get(0)).getTurno();
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		return null;
	}
	/**
	 * @see ServidorPersistente.ITurnoAlunoPersistente#readByTurno(Dominio.ITurno)
	 */
	public List readByShift(ITurno shift) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select turno from " + TurnoAluno.class.getName()
							+ " where turno.nome = $1"
							+ " and turno.disciplinaExecucao.sigla = $2"
							+ " and turno.disciplinaExecucao.executionPeriod.name = $3"
							+ " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $4";
			query.create(oqlQuery);
			query.bind(shift.getNome());
			
			IDisciplinaExecucao executionCourse = shift.getDisciplinaExecucao();
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

			List result = (List) query.execute();
			

			lockRead(result);
			List studentList = new ArrayList();
			
			Iterator iterator = result.iterator();
			while (iterator.hasNext()) {
				ITurnoAluno shiftStudent = (ITurnoAluno) iterator.next();
				studentList.add(shiftStudent.getAluno());	
			}			
			return studentList;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
}


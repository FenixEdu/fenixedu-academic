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
			String oqlQuery =
				"select turnoaluno from " + TurnoAluno.class.getName();
			oqlQuery += " where turno.nome = $1";
			oqlQuery += " and turno.disciplinaExecucao.sigla = $2";
			oqlQuery
				+= " and turno.disciplinaExecucao.licenciaturaExecucao.anoLectivo = $3"
				+ " and turno.disciplinaExecucao.licenciaturaExecucao.curso.sigla = $4"
				+ " and aluno.number = $5";
			
			query.create(oqlQuery);
			query.bind(turno.getNome());
			query.bind(turno.getDisciplinaExecucao().getSigla());
			query.bind(turno.getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo());
			query.bind(turno.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla());
			query.bind(aluno.getNumber());
			
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

	/*
	 * 
	 * @deprecated
 	 */
	public List readByTurno(String nomeTurno) throws ExcepcaoPersistencia {
		try {
			List alunos = new ArrayList();
			String oqlQuery =
				"select alunos from " + TurnoAluno.class.getName();
			oqlQuery += " where turno.nome = $1";
			query.create(oqlQuery);
			query.bind(nomeTurno);
			List result = (List) query.execute();
			lockRead(result);
			for (int i = 0; i != result.size(); i++) {
				//                alunos.add("Student" + i);
				alunos.add(((ITurnoAluno) (result.get(i))).getAluno());
			}
			return alunos;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

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
			String oqlQuery = "select turno from " + TurnoAluno.class.getName();
			oqlQuery += " where turno.nome = $1";
			oqlQuery += " and turno.disciplinaExecucao.sigla = $2";
			oqlQuery
				+= " and turno.disciplinaExecucao.executionPeriod.name = $3"
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


/*
 * ITurnoOJB.java
 *
 * Created on 17 de Outubro de 2002, 19:35
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
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ITurnoAula;
import Dominio.TurmaTurno;
import Dominio.Turno;
import Dominio.TurnoAluno;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurnoPersistente;
import Util.TipoAula;

public class TurnoOJB extends ObjectFenixOJB implements ITurnoPersistente {

	public ITurno readByNome(String nome) throws ExcepcaoPersistencia {
		try {
			ITurno turno = null;
			String oqlQuery = "select turnonome from " + Turno.class.getName();
			oqlQuery += " where nome = $1";
			query.create(oqlQuery);
			query.bind(nome);
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				turno = (ITurno) result.get(0);
			return turno;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ITurno readByNomeAndExecutionCourse(
		String nome,
		IDisciplinaExecucao IDE)
		throws ExcepcaoPersistencia {
		//		try {
		//				if (((DisciplinaExecucao)IDE).getCodigoInterno()==null){
		//				DisciplinaExecucaoOJB discExecOJB = new DisciplinaExecucaoOJB();
		//				IDE= discExecOJB.
		//				
		//				}	
		//						String oqlQuery2 = "select turnonome from " + Turno.class.getName();
		//						oqlQuery2 += " where nome = $1 and chaveDisciplinaExecucao = $2 ";
		//						query.create(oqlQuery2);
		//						query.bind(nome);
		//						query.bind( ((DisciplinaExecucao)IDE).getCodigoInterno());
		//						List result = (List) query.execute();
		//						lockRead(result);
		//						if (result.size() != 0)
		//							turno = (ITurno) result.get(0);
		//						return turno;
		//					} catch (QueryException ex) {
		//						throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		//					}

		try {
			ITurno turno = null;
			String oqlQuery = "select turnonome from " + Turno.class.getName();
			oqlQuery += " where nome = $1 and disciplinaExecucao.sigla = $2 "
				+ "and  disciplinaExecucao.licenciaturaExecucao.anoLectivo = $3 and "
				+ " disciplinaExecucao.licenciaturaExecucao.curso.sigla = $4 ";
			query.create(oqlQuery);
			query.bind(nome);
			query.bind(IDE.getSigla());
			query.bind(IDE.getLicenciaturaExecucao().getAnoLectivo());
			query.bind(IDE.getLicenciaturaExecucao().getCurso().getSigla());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				turno = (ITurno) result.get(0);
			return turno;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ITurno turno) throws ExcepcaoPersistencia {
		super.lockWrite(turno);
	}

	public void delete(ITurno turno) throws ExcepcaoPersistencia {

		try {

			ITurnoAula turnoAula = null;
			TurnoAulaOJB turnoAulaOJB = new TurnoAulaOJB();
			String oqlQuery = "select all from " + TurnoAula.class.getName();
			oqlQuery
				+= " where turno.nome = $1 and turno.disciplinaExecucao.sigla = $2 "
				+ "and  turno.disciplinaExecucao.licenciaturaExecucao.anoLectivo = $3 and "
				+ " turno.disciplinaExecucao.licenciaturaExecucao.curso.sigla = $4";
			query.create(oqlQuery);
			query.bind(turno.getNome());
			query.bind(turno.getDisciplinaExecucao().getSigla());
			query.bind(
				turno
					.getDisciplinaExecucao()
					.getLicenciaturaExecucao()
					.getAnoLectivo());
			query.bind(
				turno
					.getDisciplinaExecucao()
					.getLicenciaturaExecucao()
					.getCurso()
					.getSigla());
			List result = (List) query.execute();
			lockRead(result);
			Iterator iterador = result.iterator();
			while (iterador.hasNext()) {
				turnoAula = (ITurnoAula) iterador.next();
				turnoAulaOJB.delete(turnoAula);
			}
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		try {
			ITurmaTurno turmaTurno = null;
			TurmaTurnoOJB turmaTurnoOJB = new TurmaTurnoOJB();
			String oqlQuery1 = "select all from " + TurmaTurno.class.getName();
			oqlQuery1
				+= " where turno.nome = $1 and turno.disciplinaExecucao.sigla = $2 "
				+ "and  turno.disciplinaExecucao.licenciaturaExecucao.anoLectivo = $3 and "
				+ " turno.disciplinaExecucao.licenciaturaExecucao.curso.sigla = $4";
			query.create(oqlQuery1);
			query.bind(turno.getNome());
			query.bind(turno.getDisciplinaExecucao().getSigla());
			query.bind(
				turno
					.getDisciplinaExecucao()
					.getLicenciaturaExecucao()
					.getAnoLectivo());
			query.bind(
				turno
					.getDisciplinaExecucao()
					.getLicenciaturaExecucao()
					.getCurso()
					.getSigla());
			List result = (List) query.execute();
			lockRead(result);
			Iterator iterador = result.iterator();
			while (iterador.hasNext()) {
				turmaTurno = (ITurmaTurno) iterador.next();
				turmaTurnoOJB.delete(turmaTurno);
			}
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		try {
			ITurnoAluno turnoAluno = null;
			TurnoAlunoOJB turnoAlunoOJB = new TurnoAlunoOJB();
			String oqlQuery2 = "select all from " + TurnoAluno.class.getName();
			oqlQuery2
				+= " where turno.nome = $1 and turno.disciplinaExecucao.sigla = $2 "
				+ "and  turno.disciplinaExecucao.licenciaturaExecucao.anoLectivo = $3 and "
				+ " turno.disciplinaExecucao.licenciaturaExecucao.curso.sigla = $4";
			query.create(oqlQuery2);
			query.bind(turno.getNome());
			query.bind(turno.getDisciplinaExecucao().getSigla());
			query.bind(
				turno
					.getDisciplinaExecucao()
					.getLicenciaturaExecucao()
					.getAnoLectivo());
			query.bind(
				turno
					.getDisciplinaExecucao()
					.getLicenciaturaExecucao()
					.getCurso()
					.getSigla());
			List result = (List) query.execute();
			lockRead(result);
			Iterator iterador = result.iterator();
			while (iterador.hasNext()) {
				turnoAluno = (ITurnoAluno) iterador.next();
				turnoAlunoOJB.delete(turnoAluno);
			}
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

		//		
		//		ITurno turnoCriteria = new Turno();
		//		
		//		turnoCriteria.setNome(turno.getNome());
		//		turnoCriteria.setDisciplinaExecucao(turno.getDisciplinaExecucao());
		//		
		//		ITurmaTurno turmaTurno = new TurmaTurno(); 
		//		turmaTurno.setTurno(turnoCriteria);
		//
		//		ITurnoAula turnoAula = new TurnoAula();
		//		turnoAula.setTurno(turnoCriteria);

		super.delete(turno);

		//		deleteByCriteria(turmaTurno);
		//		deleteByCriteria(turnoAula);

		/*
		TurmaTurnoOJB turmaTurnoOJB = new TurmaTurnoOJB();
		TurnoAulaOJB turnoAulaOJB = new TurnoAulaOJB();
		
		turmaTurnoOJB.delete(turmaTurno);
		turnoAulaOJB.delete(turnoAula);
		*/

	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Turno.class.getName();
		super.deleteAll(oqlQuery);
	}

	public Integer querie2(String nomeTurno) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + TurmaTurno.class.getName();
			//oqlQuery += ", " + Turno.class.getName();// + " " ;//, " + TurmaTurno.class.getName() + ") ";
			oqlQuery += " where turno.nome = $1 and turno.tipo = $2";
			//                        "";
			//                        "turma_turno.chave_turma = turma.codigo_interno";
			query.create(oqlQuery);
			query.bind(nomeTurno);
			query.bind(new Integer(TipoAula.TEORICA));
			List result = (List) query.execute();
			lockRead(result);

			//return new Integer(result.size());
			List result2 = null;
			for (int i = 0; i != result.size(); i++) {
				try {
					oqlQuery = "select all from " + TurmaTurno.class.getName();
					//oqlQuery += ", " + Turma.class.getName() + ", " + TurmaTurno.class.getName() + ")";
					oqlQuery += " where turno.tipo = $1 and turma.nome = $2";
					query.create(oqlQuery);
					query.bind(new Integer(TipoAula.PRATICA));
					query.bind(
						((TurmaTurno) (result.get(i))).getTurma().getNome());
					if (i == 0) {
						result2 = (List) query.execute();
						lockRead(result2);
					} else {
						List result_tmp = (List) query.execute();
						lockRead(result_tmp);
						for (int j = 0; j != result_tmp.size(); j++)
							if (!result2.contains(result_tmp.get(j)))
								result2.add(result_tmp.get(j));
					}
				} catch (QueryException ex) {
					throw new ExcepcaoPersistencia(
						ExcepcaoPersistencia.QUERY,
						ex);
				}
			}
			return new Integer(result2.size());

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ArrayList readByDisciplinaExecucao(
		String sigla,
		String anoLectivo,
		String siglaLicenciatura)
		throws ExcepcaoPersistencia {
		try {
			ArrayList turnos = new ArrayList();
			String oqlQuery = "select turnos from " + Turno.class.getName();
			oqlQuery += " where disciplinaExecucao.sigla = $1";
			oqlQuery
				+= " and disciplinaExecucao.licenciaturaExecucao.anoLectivo = $2";
			oqlQuery
				+= " and disciplinaExecucao.licenciaturaExecucao.curso.sigla = $3";
			query.create(oqlQuery);
			query.bind(sigla);
			query.bind(anoLectivo);
			query.bind(siglaLicenciatura);
			List result = (List) query.execute();
			lockRead(result);
			for (int i = 0; i != result.size(); i++)
				turnos.add((ITurno) (result.get(i)));
			return turnos;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	public ArrayList readByDisciplinaExecucaoAndType(
		String sigla,
		String anoLectivo,
		String siglaLicenciatura,
		Integer type)
		throws ExcepcaoPersistencia {
		try {
			ArrayList turnos = new ArrayList();
			String oqlQuery = "select turnos from " + Turno.class.getName();
			oqlQuery += " where disciplinaExecucao.sigla = $1";
			oqlQuery
				+= " and disciplinaExecucao.licenciaturaExecucao.anoLectivo = $2";
			oqlQuery
				+= " and disciplinaExecucao.licenciaturaExecucao.curso.sigla = $3";
			oqlQuery += " and tipo = $4";
			query.create(oqlQuery);
			query.bind(sigla);
			query.bind(anoLectivo);
			query.bind(siglaLicenciatura);
			query.bind(type);
			List result = (List) query.execute();
			lockRead(result);
			for (int i = 0; i != result.size(); i++)
				turnos.add((ITurno) (result.get(i)));
			return turnos;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}
/*
 * TurmaOJB.java
 *
 * Created on 17 de Outubro de 2002, 18:44
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  tfc130
 */
import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.Turma;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurmaPersistente;

public class TurmaOJB extends ObjectFenixOJB implements ITurmaPersistente {

	public ITurma readByNome(String nome) throws ExcepcaoPersistencia {
		try {
			ITurma turma = null;
			String oqlQuery = "select turmanome from " + Turma.class.getName();
			oqlQuery += " where nome = $1";
			query.create(oqlQuery);
			query.bind(nome);
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				turma = (ITurma) result.get(0);
			return turma;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ITurma turma) throws ExcepcaoPersistencia {
		super.lockWrite(turma);
	}

	public void delete(ITurma turma) throws ExcepcaoPersistencia {
		try {
			ITurmaTurno turmaTurno = null;
			TurmaTurnoOJB turmaTurnoOJB = new TurmaTurnoOJB();
			String oqlQuery = "select all from " + TurmaTurno.class.getName();
			oqlQuery += " where turma.nome = $1 ";
				
			query.create(oqlQuery);
			query.bind(turma.getNome());
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

		super.delete(turma);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Turma.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Turma.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readBySemestreAndAnoCurricularAndSiglaLicenciatura(
		Integer semestre,
		Integer anoCurricular,
		String siglaLicenciatura)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select turmas from " + Turma.class.getName();
			oqlQuery += " where semestre = $1";
			oqlQuery += " and anoCurricular = $2";
			oqlQuery += " and licenciatura.sigla = $3";
			query.create(oqlQuery);
			query.bind(semestre);
			query.bind(anoCurricular);
			query.bind(siglaLicenciatura);
			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}

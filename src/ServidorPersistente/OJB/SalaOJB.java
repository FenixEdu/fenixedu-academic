/*
 * SalaOJB.java
 *
 * Created on 21 de Agosto de 2002, 16:36
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.Aula;
import Dominio.Exam;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.ISala;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.Sala;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import ServidorPersistente.exceptions.notAuthorizedPersistentDeleteException;
import Util.TipoSala;

public class SalaOJB extends ObjectFenixOJB implements ISalaPersistente {

	public ISala readByName(String nome) throws ExcepcaoPersistencia {
		try {
			ISala sala = null;
			String oqlQuery = "select salanome from " + Sala.class.getName();
			oqlQuery += " where nome = $1";
			query.create(oqlQuery);
			query.bind(nome);
			List result = (List) query.execute();
		
			lockRead(result);
			if (result.size() != 0)
				sala = (ISala) result.get(0);
			return sala;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ISala roomToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		ISala roomFromDB = null;

		// If there is nothing to write, simply return.
		if (roomToWrite == null)
			return;

		// Read room from database.
		roomFromDB = this.readByName(roomToWrite.getNome());

		// If room is not in database, then write it.
		if (roomFromDB == null)
			super.lockWrite(roomToWrite);
		// else If the room is mapped to the database, then write any existing changes.
		else if (
			(roomToWrite instanceof Sala)
				&& ((Sala) roomFromDB).getIdInternal().equals(
					((Sala) roomToWrite).getIdInternal())) {
			super.lockWrite(roomToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ISala sala) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Aula.class.getName();
			oqlQuery += " where sala.nome = $1";
			query.create(oqlQuery);
			query.bind(sala.getNome());
			List result = (List) query.execute();
			if (result.size() != 0) {
				throw new notAuthorizedPersistentDeleteException("Cannot delete rooms with classes");
			} else {
				super.delete(sala);
			}

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Sala.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Sala.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/**
	 * Reads all salas that with certains properties. The properties are
	 * specified by the arguments of this method. If an argument is
	 * null, then the sala can have any value concerning that
	 * argument. In what concerns the capacidadeNormal and
	 * capacidadeExame, this two arguments specify the minimal value that a sala
	 * can have in order to be selected.
	 *
	 * @return a list with all salas that satisfy the conditions specified by the 
	 * non-null arguments.
	 **/
	public List readSalas(
		String nome,
		String edificio,
		Integer piso,
		Integer tipo,
		Integer capacidadeNormal,
		Integer capacidadeExame)
		throws ExcepcaoPersistencia {

		if (nome == null
			&& edificio == null
			&& piso == null
			&& tipo == null
			&& capacidadeExame == null
			&& capacidadeNormal == null) {
			return readAll();
		}

		try {
			StringBuffer oqlQuery = new StringBuffer("select sala from ");
			boolean hasPrevious = false;

			oqlQuery.append(Sala.class.getName()).append(" where ");
			if (nome != null) {
				hasPrevious = true;
				oqlQuery.append("nome = \"").append(nome).append("\"");
			}

			if (edificio != null) {
				if (hasPrevious)
					oqlQuery.append(" and ");
				else
					hasPrevious = true;

				oqlQuery.append(" edificio = \"").append(edificio).append("\"");
			}

			if (piso != null) {
				if (hasPrevious)
					oqlQuery.append(" and ");
				else
					hasPrevious = true;

				oqlQuery.append(" piso = \"").append(piso).append("\"");
			}

			if (tipo != null) {
				if (hasPrevious)
					oqlQuery.append(" and ");
				else
					hasPrevious = true;

				oqlQuery.append(" tipo = \"").append(tipo).append("\"");
			}

			if (capacidadeNormal != null) {
				if (hasPrevious)
					oqlQuery.append(" and ");
				else
					hasPrevious = true;

				oqlQuery
					.append(" capacidadeNormal > \"")
					.append(capacidadeNormal.intValue() - 1)
					.append("\"");
			}

			if (capacidadeExame != null) {
				if (hasPrevious)
					oqlQuery.append(" and ");
				else
					hasPrevious = true;

				oqlQuery
					.append(" capacidadeExame > \"")
					.append(capacidadeExame.intValue() - 1)
					.append("\"");
			}

			query.create(oqlQuery.toString());
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readAvailableRooms(IExam exam) throws ExcepcaoPersistencia {
		List availableRooms = null;

		try {

			String oqlQuery = "select exam from " + Exam.class.getName();
			oqlQuery += " where idInternal = $1";

			query.create(oqlQuery);
			query.bind(((Exam) exam).getIdInternal());
			List examList = (List) query.execute();
			lockRead(examList);
			if (examList != null) {
				IExam examFromDB = (IExam) examList.get(0);

				oqlQuery = "select everyOtherExam from " + Exam.class.getName();
				oqlQuery += " where idInternal != $1";
				oqlQuery += " and day = $2";
				oqlQuery += " and beginning = $3";
				oqlQuery
					+= " and associatedExecutionCourses.executionPeriod.name = $4";
				oqlQuery
					+= " and associatedExecutionCourses.executionPeriod.executionYear.year = $5";

				query.create(oqlQuery);
				query.bind(((Exam) exam).getIdInternal());
				query.bind(exam.getDay());
				query.bind(exam.getBeginning());
				query.bind(
					((IExecutionCourse) examFromDB
						.getAssociatedExecutionCourses()
						.get(0))
						.getExecutionPeriod()
						.getName());
				query.bind(
					((IExecutionCourse) examFromDB
						.getAssociatedExecutionCourses()
						.get(0))
						.getExecutionPeriod()
						.getExecutionYear()
						.getYear());
				List otherExams = (List) query.execute();
				lockRead(otherExams);

				List occupiedRooms = new ArrayList();
				for (int i = 0; i < otherExams.size(); i++) {
					IExam someOtherExam = (IExam) otherExams.get(i);
					occupiedRooms.addAll(someOtherExam.getAssociatedRooms());
				}

				oqlQuery = "select allExamRooms from " + Sala.class.getName();
				oqlQuery += " where tipo != $1";
				query.create(oqlQuery);
				query.bind(new TipoSala(TipoSala.LABORATORIO));
				List allExamRooms = (List) query.execute();
				lockRead(allExamRooms);
				availableRooms =
					(List) CollectionUtils.subtract(
						allExamRooms,
						occupiedRooms);
			}
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

		return availableRooms;
	}

	public List readForRoomReservation() throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addNotEqualTo("tipo", new TipoSala(TipoSala.LABORATORIO));
		criteria.addNotLike("edificio", "Tagus%");
		criteria.addNotLike("edificio", "Local%");
		return queryList(Sala.class, criteria);
	}

	public List readByPavillion(String pavillion) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("edificio", pavillion);
		return queryList(Sala.class, criteria);
	}
	/**
	 * Returns a class list
	 * @see ServidorPersistente.ITurmaTurnoPersistente#readByClass(ITurma)
	 */
	public List readByShift(ITurno group) throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select turma from " + TurmaTurno.class.getName();
			oqlQuery += " where turno.idInternal = $1 ";
			query.create(oqlQuery);

			query.bind(group.getIdInternal());
			
			List result = (List) query.execute();
			lockRead(result);
			
			List classList = new ArrayList();
			Iterator resultIterator = result.iterator();
			while (resultIterator.hasNext()) {
				ITurmaTurno classShift = (ITurmaTurno) resultIterator.next();
				classList.add(classShift.getTurma());
			}
			return classList;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}

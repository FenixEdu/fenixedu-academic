/*
 * ExamOJB.java
 *
 * Created on 2003/03/19
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.odmg.QueryException;

import Dominio.Exam;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class ExamOJB extends ObjectFenixOJB implements IPersistentExam {

	public IExam readBy(Date day, Calendar beginning, IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia {
		try {
			IExam exam = null;
			String oqlQuery = "select exam from " + Exam.class.getName();
			oqlQuery += " where day = $1";
			oqlQuery += " and beginning = $2";
			oqlQuery += " and executionCourse.sigla = $3";
			oqlQuery += " and executionCourse.executionPeriod.name = $4";
			oqlQuery += " and executionCourse.executionPeriod.executionYear.year = $5";
			query.create(oqlQuery);
			query.bind(day);
			query.bind(beginning);
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				exam = (IExam) result.get(0);
			return exam;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readBy(Date day, Calendar beginning) throws ExcepcaoPersistencia{
		try {
			String oqlQuery = "select exams from " + Exam.class.getName();
			oqlQuery += " where day = $1";
			oqlQuery += " and beginning = $2";
			query.create(oqlQuery);
			query.bind(day);
			query.bind(beginning);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}


	public List readBy(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia{
		try {
	  		String oqlQuery = "select exams from " + Exam.class.getName();
			oqlQuery += " where executionCourse.sigla = $1";
			oqlQuery += " and executionCourse.executionPeriod.name = $2";
			oqlQuery += " and executionCourse.executionPeriod.executionYear.year = $3";
			oqlQuery += " order by executionCourse.sigla asc, season asc";			
			query.create(oqlQuery);
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}


	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Exam.class.getName();
			oqlQuery += " order by executionCourse.sigla asc, season asc";			
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(IExam examToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {
		IExam examFromDB = null;
		if (examToWrite == null)
			// Should we throw an exception saying nothing to write or
			// something of the sort?
			// By default, if OJB received a null object it would complain.
			return;

		// read exam		
		examFromDB =
			this.readBy(
				examToWrite.getDay(),
				examToWrite.getBeginning(),
				examToWrite.getExecutionCourse());

		// if (exam not in database) then write it
		if (examFromDB == null){
			super.lockWrite(examToWrite);
		}
			
		// else if (exam is mapped to the database then write any existing changes)
		else if (
			(examToWrite instanceof Exam)
				&& ((Exam) examFromDB).getIdInternal().equals(
					((Exam) examToWrite).getIdInternal())) {
			examFromDB.setEnd(examToWrite.getEnd());
			// No need to werite it because it is already mapped.
			//super.lockWrite(examToWrite);
			// else throw an AlreadyExists exception.
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IExam exam) throws ExcepcaoPersistencia {
		super.delete(exam);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Exam.class.getName();
		super.deleteAll(oqlQuery);
	}

}

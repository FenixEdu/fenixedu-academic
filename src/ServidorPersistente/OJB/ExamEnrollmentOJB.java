/*
 * Created on 21/Mai/2003
 *
 */
package ServidorPersistente.OJB;

/**
 * @author Tânia Nunes
 *
 */

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.Exam;
import Dominio.ExamEnrollment;
import Dominio.IExam;
import Dominio.IExamEnrollment;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamEnrollment;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class ExamEnrollmentOJB extends ObjectFenixOJB implements IPersistentExamEnrollment{

	public IExamEnrollment readIExamEnrollmentByExam(IExam exam) throws ExcepcaoPersistencia {
		try {
			IExamEnrollment examEnrollment = null;
			String oqlQuery = "select examEnrollment from " + ExamEnrollment.class.getName();
			oqlQuery += " where exam.idInternal = $1";
			query.create(oqlQuery);
			query.bind(exam.getIdInternal());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				examEnrollment = (IExamEnrollment) result.get(0);
			return examEnrollment;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readAll() throws ExcepcaoPersistencia {
		
		return	queryList(ExamEnrollment.class,new Criteria());
		
	}

	public void lockWrite(IExamEnrollment examEnrollmentToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {
		IExamEnrollment examEnrollmentFromDB = null;
		if (examEnrollmentToWrite == null)
			// Should we throw an exception saying nothing to write or
			// something of the sort?
			// By default, if OJB received a null object it would complain.
			return;

		// read examEnrollment		
		examEnrollmentFromDB =
			this.readIExamEnrollmentByExam((Exam) examEnrollmentToWrite.getExam());

		// if (examEnrollment not in database) then write it
		if (examEnrollmentFromDB == null)
			super.lockWrite(examEnrollmentToWrite);
		// else if (examEnrollment is mapped to the database then write any existing changes)
		else if (
			(examEnrollmentToWrite instanceof ExamEnrollment)
				&& ((ExamEnrollment) examEnrollmentFromDB).getExam().equals(
					((ExamEnrollment) examEnrollmentToWrite).getExam())) {

			examEnrollmentFromDB.setBeginDate(
				examEnrollmentToWrite.getBeginDate());
			examEnrollmentFromDB.setEndDate(examEnrollmentToWrite.getEndDate());
			// No need to werite it because it is already mapped.
			//super.lockWrite(examEnrollmentToWrite);
			// else throw an AlreadyExists exception.
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IExamEnrollment examEnrollment)
		throws ExcepcaoPersistencia {
		
			super.delete(examEnrollment);
		
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + ExamEnrollment.class.getName();
		super.deleteAll(oqlQuery);
	}

}

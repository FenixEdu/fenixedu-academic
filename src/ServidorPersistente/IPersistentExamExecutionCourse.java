/*
 * IPersistentExam.java
 *
 * Created on 2003/03/29
 */

package ServidorPersistente;

import java.util.Calendar;
import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public interface IPersistentExamExecutionCourse extends IPersistentObject {
	public IExamExecutionCourse readBy(
		IExam exam,
		IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia;
	public List readBy(Calendar day, Calendar beginning)
			throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public void lockWrite(IExamExecutionCourse examExecutionCourseToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(IExamExecutionCourse examExecutionCourse)
		throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
}

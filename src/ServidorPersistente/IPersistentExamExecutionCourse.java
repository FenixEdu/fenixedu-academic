/*
 * IPersistentExam.java
 *
 * Created on 2003/03/29
 */

package ServidorPersistente;

import java.util.List;

import Dominio.IExecutionCourse;
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
		IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public void lockWrite(IExamExecutionCourse examExecutionCourseToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(IExamExecutionCourse examExecutionCourse)
		throws ExcepcaoPersistencia;
	
	public void delete(IExam exam) throws ExcepcaoPersistencia;
	public List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia;
	
		
		 }

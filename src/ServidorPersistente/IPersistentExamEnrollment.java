/*
 * Created on 22/Mai/2003
 *
 */
package ServidorPersistente;


import java.util.List;

import Dominio.IExam;
import Dominio.IExamEnrollment;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Tânia Nunes
 *
 */ 
public interface IPersistentExamEnrollment extends IPersistentObject{
	public void deleteAll() throws ExcepcaoPersistencia;
		public void lockWrite(IExamEnrollment examEnrollmentToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
		public void delete(IExamEnrollment examEnrollment) throws ExcepcaoPersistencia;
		public IExamEnrollment readIExamEnrollmentByExam(IExam exam) throws ExcepcaoPersistencia;
		public List readAll() throws ExcepcaoPersistencia;
		}

/*
 * IPersistentExam.java
 *
 * Created on 2003/03/19
 */

package ServidorPersistente;

import java.util.Calendar;
import java.util.List;

import Dominio.IExam;
import Dominio.IExecutionPeriod;
import Dominio.ISala;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public interface IPersistentExam extends IPersistentObject {
	public List readBy(Calendar day, Calendar beginning) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public void delete(IExam exam) throws ExcepcaoPersistencia;
	
	public List readBy(ISala room, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
}

/*
 * IPersistentExam.java
 *
 * Created on 2003/03/19
 */

package ServidorPersistente;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Util.Season;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public interface IPersistentExam extends IPersistentObject {
	public IExam readBy(Date day, Calendar beginning, IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
	public IExam readBy(IDisciplinaExecucao executionCourse, Season season) throws ExcepcaoPersistencia;	
	public List readBy(Date day, Calendar beginning) throws ExcepcaoPersistencia;	
	public List readBy(IDisciplinaExecucao disciplinaExecucao) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
    public void delete(IExam exam) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
}

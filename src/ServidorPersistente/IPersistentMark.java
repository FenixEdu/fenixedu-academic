/*
 * IPersistentExam.java
 *
 * Created on 2003/03/19
 */

package ServidorPersistente;

import java.util.List;

import Dominio.IExam;
import Dominio.IFrequenta;
import Dominio.IMark;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 *
 * @author  Angela
 */
public interface IPersistentMark extends IPersistentObject {
	public List readAll() throws ExcepcaoPersistencia;
	public void lockWrite(IMark mark) throws ExcepcaoPersistencia, ExistingPersistentException;
    public void deleteAll() throws ExcepcaoPersistencia;
	public IMark readBy(IExam exam, IFrequenta attend) throws ExcepcaoPersistencia;
	public List readBy(IExam exam) throws ExcepcaoPersistencia;
	public void delete(IMark mark)throws ExcepcaoPersistencia;
}

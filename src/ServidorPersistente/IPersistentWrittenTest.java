/*
 * Created on 20/Out/2003
 *
  */
package ServidorPersistente;

import java.util.Calendar;
import java.util.List;

import Dominio.IWrittenTest;

/**
 * @author Ana e Ricardo
 *
 */
public interface IPersistentWrittenTest extends IPersistentObject {
	public List readBy(Calendar day, Calendar beginning) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public void delete(IWrittenTest writtenTest) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;

}

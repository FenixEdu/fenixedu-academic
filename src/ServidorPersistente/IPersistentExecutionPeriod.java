package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;

/**
 * Created on 11/Fev/2003
 * @author João Mota
 * Package ServidorPersistente
 * 
 */
public interface IPersistentExecutionPeriod extends IPersistentObject {
	/**
	 * 
	 * @param name
	 * @param executionYear
	 * @return IExecutionPeriod
	 * @throws ExcepcaoPersistencia
	 */
	public IExecutionPeriod readExecutionPeriodByNameAndExecutionYear(
		String name,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia;
	/**
	 * 
	 * @return ArrayList
	 * @throws ExcepcaoPersistencia
	 */
	public ArrayList readAllExecutionPeriod() throws ExcepcaoPersistencia;
	/**
	 * 
	 * @param executionYear
	 * @return ArrayList
	 * @throws ExcepcaoPersistencia
	 */
	public ArrayList readAllExecutionPeriodByExecutionYear(IExecutionYear executionYear)
		throws ExcepcaoPersistencia;
	/**
	 * 
	 * @param executionPeriod
	 * @return boolean
	 */

	public boolean writeExecutionPeriod(IExecutionPeriod executionPeriod);
	/**
	 * 
	 * @param executionPeriod
	 * @return boolean
	 */
	public boolean delete(IExecutionPeriod executionPeriod);
	/**
	 * 
	 * @return boolean
	 */
	public boolean deleteAll();
	/**
	 * Returns the actual executionPeriod using the actual date.
	 * @return IExecutionPeriod
	 * @throws ExcepcaoPersistencia
	 */
	public IExecutionPeriod readActualExecutionPeriod() throws ExcepcaoPersistencia;	
}

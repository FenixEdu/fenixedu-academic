package ServidorPersistente;

import java.util.List;

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
	 * @return ArrayList
	 * @throws ExcepcaoPersistencia
	 */
	public List readAllExecutionPeriod() throws ExcepcaoPersistencia;

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
	
	/**
	 * Method readByNameAndExecutionYear.
	 * @param string
	 * @param iExecutionYear
	 * @return IExecutionPeriod
	 */
	public IExecutionPeriod readByNameAndExecutionYear(
		String executionPeriodName,
		IExecutionYear executionYear) throws ExcepcaoPersistencia;
}

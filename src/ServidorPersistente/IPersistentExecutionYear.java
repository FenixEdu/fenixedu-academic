package ServidorPersistente;

import java.util.List;

import Dominio.IExecutionYear;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * Created on 11/Fev/2003
 * @author João Mota
 * Package ServidorPersistente
 * 
 */
public interface IPersistentExecutionYear extends IPersistentObject {
	/**
	 * 
	 * @param year
	 * @return IExecutionYear
	 */
	public IExecutionYear readExecutionYearByName(String year) throws ExcepcaoPersistencia;
	/**
	 * 
	 * @return ArrayList
	 */
	public List readAllExecutionYear()throws ExcepcaoPersistencia;
	/**
	 * 
	 * @return ArrayList
	 */
	public List readNotClosedExecutionYears()throws ExcepcaoPersistencia;	
	/**
	 * 
	 * @param executionYear
	 * @return boolean
	 */
	public void writeExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia, ExistingPersistentException;
	
	
	/**
	 * 
	 * @param executionYear
	 * @return boolean
	 */
	public boolean delete(IExecutionYear executionYear);
	
	
	public IExecutionYear readCurrentExecutionYear() throws ExcepcaoPersistencia ;
}

package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IExecutionYear;

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
	public ArrayList readAllExecutionYear()throws ExcepcaoPersistencia;
	/**
	 * 
	 * @param executionYear
	 * @return boolean
	 */
	public boolean writeExecutionYear(IExecutionYear executionYear);
	/**
	 * 
	 * @return boolean
	 */
	public boolean deleteAll();
}

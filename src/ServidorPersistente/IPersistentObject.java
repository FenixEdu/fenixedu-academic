package ServidorPersistente;

import java.util.List;

import Dominio.IDomainObject;

/**
 * @author tfc130
 *
 * 
 */
public interface IPersistentObject {
	List readByCriteria(Object obj) throws ExcepcaoPersistencia;
	Object readDomainObjectByCriteria(Object obj) throws ExcepcaoPersistencia;
	void deleteByCriteria(Object obj) throws ExcepcaoPersistencia;
	void lockWrite(Object obj) throws ExcepcaoPersistencia;
	/**
	 * Reads an object from db using his <code>Identity</code>.
	 * @param obj Domain object to read from database
	 * @param lockWrite tells if after reading the object it should be marked with a WRITE <code>true</code> or READ <code>false</code> lock.
	 * @return Object that we want to read
	 */	
	public IDomainObject readByOId(IDomainObject obj, boolean lockWrite);
	
	/**
	 * Only locks the object for write. <b>Doesn't do anything else</b>
	 * @param obj object to lock
	 * @throws ExcepcaoPersistencia when can't lock object.
	 */
	void simpleLockWrite(IDomainObject obj) throws ExcepcaoPersistencia;	
	/**
	 * Reads an object using from Database using it's unique.
	 * @param domainObject
	 * @param lockWrite
	 * @return
	 * @throws IllegalArgumentException
	 */
	public IDomainObject readByUnique(
		IDomainObject domainObject,
		boolean lockWrite) throws IllegalArgumentException;
}

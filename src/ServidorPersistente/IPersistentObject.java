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
	public IDomainObject readByOId(IDomainObject obj) throws ExcepcaoPersistencia;
}

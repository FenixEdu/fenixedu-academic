package ServidorPersistente;

import java.util.List;

/**
 * @author tfc130
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface IPersistentObject {
	List readByCriteria(Object obj) throws ExcepcaoPersistencia;
	Object readDomainObjectByCriteria(Object obj) throws ExcepcaoPersistencia;
	void deleteByCriteria(Object obj) throws ExcepcaoPersistencia;
	void lockWrite(Object obj) throws ExcepcaoPersistencia;	
	public Object readByOId(Object obj) throws ExcepcaoPersistencia;
}

/*
 * Created on 3/Fev/2004
 *
 */
package ServidorPersistente;

import Dominio.IDomainObject;
import Dominio.ITestScope;

/**
 * 
 * @author Susana Fernandes
 *  
 */
public interface IPersistentTestScope extends IPersistentObject {
    public ITestScope readByDomainObject(IDomainObject object) throws ExcepcaoPersistencia;
}
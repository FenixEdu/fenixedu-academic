/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITestScope;

/**
 * 
 * @author Susana Fernandes
 *  
 */
public interface IPersistentTestScope extends IPersistentObject {
    public ITestScope readByDomainObject(IDomainObject object) throws ExcepcaoPersistencia;
}
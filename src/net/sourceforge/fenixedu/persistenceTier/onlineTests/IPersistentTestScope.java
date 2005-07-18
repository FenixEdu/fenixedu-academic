/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import net.sourceforge.fenixedu.domain.onlineTests.ITestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public interface IPersistentTestScope extends IPersistentObject {
    public ITestScope readByDomainObject(String className, Integer idInternal) throws ExcepcaoPersistencia;
}
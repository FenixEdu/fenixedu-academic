/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITest;

/**
 * @author Susana Fernandes
 */
public interface IPersistentTest extends IPersistentObject {
    public List readByTestScopeObject(IDomainObject object) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(ITest test) throws ExcepcaoPersistencia;
}
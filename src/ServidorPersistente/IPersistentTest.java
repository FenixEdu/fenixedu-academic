/*
 * Created on 28/Jul/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDomainObject;
import Dominio.ITest;

/**
 * @author Susana Fernandes
 */
public interface IPersistentTest extends IPersistentObject {
    public List readByTestScopeObject(IDomainObject object) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(ITest test) throws ExcepcaoPersistencia;
}
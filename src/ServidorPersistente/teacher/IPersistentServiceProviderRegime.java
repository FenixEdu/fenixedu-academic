/*
 * Created on 16/Nov/2003
 *
 */
package ServidorPersistente.teacher;

import Dominio.ITeacher;
import Dominio.teacher.IServiceProviderRegime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public interface IPersistentServiceProviderRegime extends IPersistentObject
{
    public IServiceProviderRegime readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}

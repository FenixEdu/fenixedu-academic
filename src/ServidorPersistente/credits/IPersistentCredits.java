/*
 * Created on Dec 26, 2003 by jpvl
 *  
 */
package ServidorPersistente.credits;

import Dominio.ICredits;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentCredits extends IPersistentObject
{
    public ICredits readByTeacherAndExecutionPeriod( ITeacher teacher, IExecutionPeriod executionPeriod )
                    throws ExcepcaoPersistencia;
}

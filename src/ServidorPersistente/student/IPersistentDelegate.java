/*
 * Created on Feb 18, 2004
 *  
 */
package ServidorPersistente.student;

import java.util.List;

import Dominio.ICurso;
import Dominio.IExecutionYear;
import Dominio.student.IDelegate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.DelegateType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *  
 */
public interface IPersistentDelegate extends IPersistentObject
{
    public List readByDegreeAndExecutionYear(ICurso degree, IExecutionYear executionYear)
        throws ExcepcaoPersistencia;
    public IDelegate readByDegreeAndExecutionYearAndType(
        ICurso degree,
        IExecutionYear executionYear,
        DelegateType type)
        throws ExcepcaoPersistencia;
}

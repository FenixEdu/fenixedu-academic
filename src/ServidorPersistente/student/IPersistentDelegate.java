/*
 * Created on Feb 18, 2004
 *  
 */
package ServidorPersistente.student;

import java.util.List;

import Dominio.ICurso;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.student.IDelegate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public interface IPersistentDelegate extends IPersistentObject {
    public List readByDegreeAndExecutionYear(ICurso degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    public List readByDegreeAndExecutionYearAndYearType(ICurso degree, IExecutionYear executionYear,
            DelegateYearType type) throws ExcepcaoPersistencia;

    public IDelegate readByStudent(IStudent student) throws ExcepcaoPersistencia;

    public List readDegreeDelegateByDegreeAndExecutionYear(ICurso degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;
}
package ServidorPersistente;

import java.util.List;

import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.ITeacher;

/**
 * Created on 2003/10/27
 * 
 * @author João Mota Package ServidorPersistente
 *  
 */
public interface IPersistentCoordinator extends IPersistentObject {

    public List readExecutionDegreesByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;

    public List readCurricularPlansByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;

    public List readCoordinatorsByExecutionDegree(ICursoExecucao executionDegree)
            throws ExcepcaoPersistencia;

    public ICoordinator readCoordinatorByTeacherAndExecutionDegreeId(ITeacher teacher,
            Integer executionDegreeId) throws ExcepcaoPersistencia;

    public ICoordinator readCoordinatorByTeacherAndExecutionDegree(ITeacher teacher,
            ICursoExecucao executionDegree) throws ExcepcaoPersistencia;

    public ICoordinator readCoordinatorByTeacherAndDegreeCurricularPlanID(ITeacher teacher,
            Integer degreeCurricularPlanID) throws ExcepcaoPersistencia;
}
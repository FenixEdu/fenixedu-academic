package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ITeacher;

/**
 * Created on 2003/10/27
 * 
 * @author João Mota Package ServidorPersistente
 *  
 */
public interface IPersistentCoordinator extends IPersistentObject {

    public List readExecutionDegreesByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;

    public List readCurricularPlansByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;

    public List readCoordinatorsByExecutionDegree(IExecutionDegree executionDegree)
            throws ExcepcaoPersistencia;

    public ICoordinator readCoordinatorByTeacherAndExecutionDegreeId(ITeacher teacher,
            Integer executionDegreeId) throws ExcepcaoPersistencia;

    public ICoordinator readCoordinatorByTeacherAndExecutionDegree(ITeacher teacher,
            IExecutionDegree executionDegree) throws ExcepcaoPersistencia;

    public ICoordinator readCoordinatorByTeacherAndDegreeCurricularPlanID(ITeacher teacher,
            Integer degreeCurricularPlanID) throws ExcepcaoPersistencia;
}
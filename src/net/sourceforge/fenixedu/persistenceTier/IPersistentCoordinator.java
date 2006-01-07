package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;

/**
 * Created on 2003/10/27
 * 
 * @author João Mota Package ServidorPersistente
 */
public interface IPersistentCoordinator extends IPersistentObject {

    public List readExecutionDegreesByTeacher(Integer teacherID) throws ExcepcaoPersistencia;

    public List readCurricularPlansByTeacher(Integer teacherID) throws ExcepcaoPersistencia;

    public List readCoordinatorsByExecutionDegree(Integer executionDegreeID) throws ExcepcaoPersistencia;

    public Coordinator readCoordinatorByTeacherIdAndExecutionDegreeId(Integer teacherID,
            Integer executionDegreeId) throws ExcepcaoPersistencia;
}
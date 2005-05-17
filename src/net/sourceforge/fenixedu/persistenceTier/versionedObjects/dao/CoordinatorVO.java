package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * fenix-head ServidorPersistente.OJB
 * 
 * @author João Mota 28/Out/2003
 * @author Francisco Paulo 27/Out/2004 frnp@mega.ist.utl.pt(edit) CoordinatorOJB
 *         class, implements the read methods available for coordinators
 */

public class CoordinatorVO extends VersionedObjectsBase implements IPersistentCoordinator {

    public List readExecutionDegreesByTeacher(final Integer teacherID) throws ExcepcaoPersistencia {

        final ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherID);
        final List<ICoordinator> coordinators = teacher.getCoordinators();
        final List<IExecutionDegree> executionDegrees = new ArrayList(coordinators.size());
        for (final ICoordinator coordinator : coordinators) {
            executionDegrees.add(coordinator.getExecutionDegree());
        }
        return executionDegrees;
    }

    public List readCurricularPlansByTeacher(final Integer teacherID) throws ExcepcaoPersistencia {

        final ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherID);
        final List<ICoordinator> coordinators = teacher.getCoordinators();
        final List<IDegreeCurricularPlan> degreeCurricularPlans = new ArrayList();
        for (final ICoordinator coordinator : coordinators) {
            if (!degreeCurricularPlans.contains(coordinator.getExecutionDegree()
                    .getDegreeCurricularPlan())) {
                degreeCurricularPlans.add(coordinator.getExecutionDegree().getDegreeCurricularPlan());
            }
        }
        return degreeCurricularPlans;
    }

    public List readCoordinatorsByExecutionDegree(final Integer executionDegreeID) throws ExcepcaoPersistencia {

        final IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeID);

        return executionDegree != null ? executionDegree.getCoordinatorsList() : new ArrayList(0);
    }

    public ICoordinator readCoordinatorByTeacherIdAndExecutionDegreeId(final Integer teacherID,
            final Integer executionDegreeId) throws ExcepcaoPersistencia {

        final IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeId);

        final List<ICoordinator> coordinators = executionDegree.getCoordinatorsList();
        for (final ICoordinator coordinator : coordinators) {
            if (coordinator.getTeacher().getIdInternal().equals(teacherID)) {
                return coordinator;
            }
        }
        return null;
    }

}
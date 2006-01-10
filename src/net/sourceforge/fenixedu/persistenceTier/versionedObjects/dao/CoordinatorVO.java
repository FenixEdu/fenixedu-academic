package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
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

        final Teacher teacher = (Teacher) readByOID(Teacher.class, teacherID);
        final List<Coordinator> coordinators = teacher.getCoordinators();
        final List<ExecutionDegree> executionDegrees = new ArrayList(coordinators.size());
        for (final Coordinator coordinator : coordinators) {
            executionDegrees.add(coordinator.getExecutionDegree());
        }
        return executionDegrees;
    }

    public List readCurricularPlansByTeacher(final Integer teacherID) throws ExcepcaoPersistencia {

        final Teacher teacher = (Teacher) readByOID(Teacher.class, teacherID);
        final List<Coordinator> coordinators = teacher.getCoordinators();
        final List<DegreeCurricularPlan> degreeCurricularPlans = new ArrayList();
        for (final Coordinator coordinator : coordinators) {
            if (!degreeCurricularPlans.contains(coordinator.getExecutionDegree()
                    .getDegreeCurricularPlan())) {
                degreeCurricularPlans.add(coordinator.getExecutionDegree().getDegreeCurricularPlan());
            }
        }
        return degreeCurricularPlans;
    }

    public List readCoordinatorsByExecutionDegree(final Integer executionDegreeID) throws ExcepcaoPersistencia {

        final ExecutionDegree executionDegree = (ExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeID);

        return executionDegree != null ? executionDegree.getCoordinatorsList() : new ArrayList(0);
    }

    public Coordinator readCoordinatorByTeacherIdAndExecutionDegreeId(final Integer teacherID,
            final Integer executionDegreeId) throws ExcepcaoPersistencia {

        final ExecutionDegree executionDegree = (ExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeId);

        final List<Coordinator> coordinators = executionDegree.getCoordinatorsList();
        for (final Coordinator coordinator : coordinators) {
            if (coordinator.getTeacher().getIdInternal().equals(teacherID)) {
                return coordinator;
            }
        }
        return null;
    }

}
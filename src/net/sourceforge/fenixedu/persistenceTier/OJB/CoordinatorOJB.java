package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;

import org.apache.ojb.broker.query.Criteria;

/**
 * fenix-head ServidorPersistente.OJB
 * 
 * @author João Mota 28/Out/2003
 * @author Francisco Paulo 27/Out/2004 frnp@mega.ist.utl.pt(edit) CoordinatorOJB
 *         class, implements the read methods available for coordinators
 */

public class CoordinatorOJB extends PersistentObjectOJB implements IPersistentCoordinator {

    public List readExecutionDegreesByTeacher(final Integer teacherID) throws ExcepcaoPersistencia {
        final Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherID);

        final List<Coordinator> coordinators = queryList(Coordinator.class, criteria);
        final List<ExecutionDegree> executionDegrees = new ArrayList(coordinators.size());

        for (final Coordinator coordinator : coordinators) {
            executionDegrees.add(coordinator.getExecutionDegree());
        }
        return executionDegrees;
    }

    public List readCurricularPlansByTeacher(final Integer teacherID) throws ExcepcaoPersistencia {

        final Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherID);

        final List<Coordinator> coordinators = queryList(Coordinator.class, criteria);
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
        final Criteria criteria = new Criteria();
        criteria.addEqualTo("executionDegree.idInternal", executionDegreeID);
        return queryList(Coordinator.class, criteria);

    }

    public Coordinator readCoordinatorByTeacherIdAndExecutionDegreeId(final Integer teacherID,
            final Integer executionDegreeId) throws ExcepcaoPersistencia {
        final Criteria criteria = new Criteria();
        criteria.addEqualTo("executionDegree.idInternal", executionDegreeId);
        criteria.addEqualTo("teacher.idInternal", teacherID);
        return (Coordinator) queryObject(Coordinator.class, criteria);
    }

}
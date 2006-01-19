/*
 * Created on 3/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 3/Dez/2003
 *  
 */
public class ReadExecutionCoursesByDegreeAndExecutionPeriodId implements IService {

    public List run(Integer degreeId, Integer executionPeriodId) throws FenixServiceException, ExcepcaoPersistencia {
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = ps.getIPersistentExecutionPeriod();
            ICursoPersistente persistentDegree = ps.getICursoPersistente();

            final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentExecutionPeriod
                    .readByOID(ExecutionPeriod.class, executionPeriodId);
            if (executionPeriod == null) {
                throw new InvalidArgumentsServiceException();
            }
            final Degree degree = (Degree) persistentDegree.readByOID(Degree.class, degreeId);
            if (degree == null) {
                throw new InvalidArgumentsServiceException();
            }

            final List infoExecutionCourses = new ArrayList();
            for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
                if (satisfiesCriteria(executionCourse, degree)) {
                    infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
                }
            }

            return infoExecutionCourses;
    }

    private boolean satisfiesCriteria(final ExecutionCourse executionCourse, final Degree degree) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE
                    && degreeCurricularPlan.getCurricularStage() == CurricularStage.OLD) {
                final Degree degreeOfCurricularCourse = degreeCurricularPlan.getDegree();
                if (degree == degreeOfCurricularCourse) {
                    return true;
                }
            }
        }
        return false;
    }

}
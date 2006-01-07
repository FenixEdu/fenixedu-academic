/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod implements IService {

    public void run(Integer[] degreeCurricularPlansIDs, Integer executionPeriodID)
            throws ExcepcaoPersistencia {

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ExecutionPeriod executionPeriod = (ExecutionPeriod) ps.getIPersistentExecutionPeriod()
                .readByOID(ExecutionPeriod.class, executionPeriodID);

        Set<String> existentsExecutionCoursesSiglas = readExistingExecutionCoursesSiglas(executionPeriod);

        for (Integer degreeCurricularPlanID : degreeCurricularPlansIDs) {

            DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) ps
                    .getIPersistentObject()
                    .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);

            List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();

            for (CurricularCourse curricularCourse : curricularCourses) {

                if (curricularCourse.getActiveScopesInExecutionPeriodAndSemester(executionPeriod).isEmpty()) {
                    continue;
                }

                if (curricularCourse.getExecutionCoursesByExecutionPeriod(executionPeriod).isEmpty()) {

                    ExecutionCourse executionCourse = DomainFactory.makeExecutionCourse();
                    Site site = DomainFactory.makeSite();

                    executionCourse.setSite(site);
                    executionCourse.setExecutionPeriod(executionPeriod);
                    executionCourse.setNome(curricularCourse.getName());

                    String sigla = getUniqueSigla(existentsExecutionCoursesSiglas, curricularCourse
                            .getCode());

                    executionCourse.setSigla(sigla);
                    executionCourse.setTheoreticalHours(0.0);
                    executionCourse.setTheoPratHours(0.0);
                    executionCourse.setPraticalHours(0.0);
                    executionCourse.setLabHours(0.0);
                    executionCourse.setComment("");

                    curricularCourse.addAssociatedExecutionCourses(executionCourse);

                }

            }

        }

    }

    private String getUniqueSigla(Set<String> existentsExecutionCoursesSiglas, String sigla) {

        if (existentsExecutionCoursesSiglas.contains(sigla)) {
            int suffix = 1;
            while (existentsExecutionCoursesSiglas.contains(sigla + "-" + ++suffix))
                ;
            sigla = sigla + "-" + suffix;
        }

        existentsExecutionCoursesSiglas.add(sigla);

        return sigla;
    }

    private Set<String> readExistingExecutionCoursesSiglas(ExecutionPeriod executionPeriod) {

        Set<String> existingExecutionCoursesSiglas = new HashSet<String>(executionPeriod
                .getAssociatedExecutionCoursesCount());

        for (ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
            existingExecutionCoursesSiglas.add(executionCourse.getSigla());
        }

        return existingExecutionCoursesSiglas;
    }

}

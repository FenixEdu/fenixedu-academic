package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateExecutionDegreesForExecutionYear implements IService {

    public void run(final Integer[] degreeCurricularPlansIDs, final Integer executionYearID,
            final String campusName, final Boolean temporaryExamMap,
            final Calendar lessonSeason1BeginDate, final Calendar lessonSeason1EndDate,
            final Calendar lessonSeason2BeginDate, final Calendar lessonSeason2EndDate,
            final Calendar examsSeason1BeginDate, final Calendar examsSeason1EndDate,
            final Calendar examsSeason2BeginDate, final Calendar examsSeason2EndDate)
            throws ExcepcaoPersistencia {

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ExecutionYear executionYear = (ExecutionYear) ps.getIPersistentExecutionYear()
                .readByOID(ExecutionYear.class, executionYearID);

        final Campus campus = ps.getIPersistentCampus().readByName(campusName);

        final OccupationPeriod lessonSeason1 = createPeriod(lessonSeason1BeginDate, lessonSeason1EndDate);
        final OccupationPeriod lessonSeason2 = createPeriod(lessonSeason2BeginDate, lessonSeason2EndDate);
        final OccupationPeriod examsSeason1 = createPeriod(examsSeason1BeginDate, examsSeason1EndDate);
        final OccupationPeriod examsSeason2 = createPeriod(examsSeason2BeginDate, examsSeason2EndDate);

        for (Integer degreeCurricularPlanID : degreeCurricularPlansIDs) {

            final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) ps
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            degreeCurricularPlanID);

            ExecutionDegree storedExecutionDegree = ps.getIPersistentExecutionDegree()
                    .readByDegreeCurricularPlanIDAndExecutionYear(degreeCurricularPlanID,
                            executionYear.getYear());

            if (storedExecutionDegree != null) {
                continue;
            }

            createExecutionDegree(executionYear, campus, degreeCurricularPlan, temporaryExamMap,
                    examsSeason1, examsSeason2, lessonSeason1, lessonSeason2);

        }

    }

    private OccupationPeriod createPeriod(final Calendar startDate, final Calendar endDate) {
        final OccupationPeriod period = DomainFactory.makeOccupationPeriod();

        period.setStartDate(startDate);
        period.setEndDate(endDate);
        period.setNextPeriod(null);

        return period;
    }

    protected void createExecutionDegree(

    final ExecutionYear executionYear, Campus campus, DegreeCurricularPlan degreeCurricularPlan,
            Boolean temporaryExamMap, OccupationPeriod periodExamsSeason1, OccupationPeriod periodExamsSeason2,
            OccupationPeriod periodLessonSeason1, OccupationPeriod periodLessonSeason2) {

        final ExecutionDegree executionDegree = DomainFactory.makeExecutionDegree();

        executionDegree.setCampus(campus);
        executionDegree.setDegreeCurricularPlan(degreeCurricularPlan);
        executionDegree.setExecutionYear(executionYear);
        executionDegree.setPeriodExamsFirstSemester(periodExamsSeason1);
        executionDegree.setPeriodExamsSecondSemester(periodExamsSeason2);
        executionDegree.setPeriodLessonsFirstSemester(periodLessonSeason1);
        executionDegree.setPeriodLessonsSecondSemester(periodLessonSeason2);
        executionDegree.setScheduling(null);
        executionDegree.setTemporaryExamMap(temporaryExamMap);

    }

}

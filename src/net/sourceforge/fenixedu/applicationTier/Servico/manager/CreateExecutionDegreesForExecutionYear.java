package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ICampus;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IOccupationPeriod;
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

        final IExecutionYear executionYear = (IExecutionYear) ps.getIPersistentExecutionYear()
                .readByOID(ExecutionYear.class, executionYearID);

        final ICampus campus = ps.getIPersistentCampus().readByName(campusName);

        final IOccupationPeriod lessonSeason1 = createPeriod(lessonSeason1BeginDate, lessonSeason1EndDate);
        final IOccupationPeriod lessonSeason2 = createPeriod(lessonSeason2BeginDate, lessonSeason2EndDate);
        final IOccupationPeriod examsSeason1 = createPeriod(examsSeason1BeginDate, examsSeason1EndDate);
        final IOccupationPeriod examsSeason2 = createPeriod(examsSeason2BeginDate, examsSeason2EndDate);

        for (Integer degreeCurricularPlanID : degreeCurricularPlansIDs) {

            final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) ps
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            degreeCurricularPlanID);

            IExecutionDegree storedExecutionDegree = ps.getIPersistentExecutionDegree()
                    .readByDegreeCurricularPlanIDAndExecutionYear(degreeCurricularPlanID,
                            executionYear.getYear());

            if (storedExecutionDegree != null) {
                continue;
            }

            createExecutionDegree(executionYear, campus, degreeCurricularPlan, temporaryExamMap,
                    examsSeason1, examsSeason2, lessonSeason1, lessonSeason2);

        }

    }

    private IOccupationPeriod createPeriod(final Calendar startDate, final Calendar endDate) {
        final IOccupationPeriod period = DomainFactory.makeOccupationPeriod();

        period.setStartDate(startDate);
        period.setEndDate(endDate);
        period.setNextPeriod(null);

        return period;
    }

    protected void createExecutionDegree(

    final IExecutionYear executionYear, ICampus campus, IDegreeCurricularPlan degreeCurricularPlan,
            Boolean temporaryExamMap, IOccupationPeriod periodExamsSeason1, IOccupationPeriod periodExamsSeason2,
            IOccupationPeriod periodLessonSeason1, IOccupationPeriod periodLessonSeason2) {

        final IExecutionDegree executionDegree = DomainFactory.makeExecutionDegree();

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

package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateExecutionDegreesForExecutionYear extends Service {

    public void run(final Integer[] degreeCurricularPlansIDs,
            final Integer[] bolonhaDegreeCurricularPlansIDs, final Integer executionYearID,
            final String campusName, final Boolean temporaryExamMap,
            final Calendar lessonSeason1BeginDate, final Calendar lessonSeason1EndDate,
            final Calendar lessonSeason2BeginDate, final Calendar lessonSeason2EndDate,
            final Calendar examsSeason1BeginDate, final Calendar examsSeason1EndDate,
            final Calendar examsSeason2BeginDate, final Calendar examsSeason2EndDate)
            throws ExcepcaoPersistencia {

        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
        final Campus campus = readCampusByName(campusName);

        final OccupationPeriod lessonSeason1 = createPeriod(lessonSeason1BeginDate, lessonSeason1EndDate);
        final OccupationPeriod lessonSeason2 = createPeriod(lessonSeason2BeginDate, lessonSeason2EndDate);
        final OccupationPeriod examsSeason1 = createPeriod(examsSeason1BeginDate, examsSeason1EndDate);
        final OccupationPeriod examsSeason2 = createPeriod(examsSeason2BeginDate, examsSeason2EndDate);

        final Set<Integer> allDegreeCurricularPlanIDs = new HashSet<Integer>();
        allDegreeCurricularPlanIDs.addAll(Arrays.asList(degreeCurricularPlansIDs));
        allDegreeCurricularPlanIDs.addAll(Arrays.asList(bolonhaDegreeCurricularPlansIDs));
        
        for (final Integer degreeCurricularPlanID : allDegreeCurricularPlanIDs) {
            final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
            if (degreeCurricularPlan == null || degreeCurricularPlan.hasAnyExecutionDegreeFor(executionYear)) {
                continue;
            }
            createExecutionDegree(executionYear, campus, degreeCurricularPlan, temporaryExamMap,
                    examsSeason1, examsSeason2, lessonSeason1, lessonSeason2);
        }
    }

    private Campus readCampusByName(String campusName) throws ExcepcaoPersistencia {
        for (Campus campus : rootDomainObject.getCampuss()) {
            if (campus.getName().equalsIgnoreCase(campusName)) {
                return campus;
            }
        }
        return null;
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
            Boolean temporaryExamMap, OccupationPeriod periodExamsSeason1,
            OccupationPeriod periodExamsSeason2, OccupationPeriod periodLessonSeason1,
            OccupationPeriod periodLessonSeason2) {

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

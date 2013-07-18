package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionPeriodsEnrolment {

    private static final ExecutionSemester since;
    static {
        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName("2004/2005");
        since = executionYear == null ? null : executionYear.getFirstExecutionPeriod();
    }

    private final Date masterDegreeFirstExecutionPeriodDate = new GregorianCalendar(2002, Calendar.SEPTEMBER, 01).getTime();

    protected List<InfoExecutionPeriod> run(DegreeType degreeType) {
        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionSemester executionSemester : RootDomainObject.getInstance().getExecutionPeriods()) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
        }

        List<InfoExecutionPeriod> newRes = new ArrayList<InfoExecutionPeriod>();
        for (InfoExecutionPeriod infoExecutionPeriod : result) {
            ExecutionSemester executionSemester = infoExecutionPeriod.getExecutionPeriod();

            if (executionSemester.isAfterOrEquals(since)) {
                newRes.add(infoExecutionPeriod);
            } else if (executionSemester.getBeginDate().after(this.masterDegreeFirstExecutionPeriodDate)
                    && degreeType != null
                    && (degreeType.equals(DegreeType.MASTER_DEGREE) || degreeType
                            .equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA))) {
                // master degree extra execution periods
                newRes.add((infoExecutionPeriod));
            }
        }

        return newRes;
    }

    // Service Invokers migrated from Berserk

    private static final ReadExecutionPeriodsEnrolment serviceInstance = new ReadExecutionPeriodsEnrolment();

    @Service
    public static List<InfoExecutionPeriod> runReadExecutionPeriodsEnrollmentFenix(DegreeType degreeType) {
        return serviceInstance.run(degreeType);
    }

}
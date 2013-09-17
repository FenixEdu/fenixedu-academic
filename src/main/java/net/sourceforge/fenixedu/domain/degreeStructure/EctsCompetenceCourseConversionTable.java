package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class EctsCompetenceCourseConversionTable extends EctsCompetenceCourseConversionTable_Base {

    protected EctsCompetenceCourseConversionTable(CompetenceCourse competenceCourse, AcademicInterval year,
            EctsComparabilityTable table) {
        super();
        init(year, table);
        setCompetenceCourse(competenceCourse);
    }

    protected RootDomainObject getRootDomainObject() {
        return getCompetenceCourse().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
        return getCompetenceCourse();
    }

    @Atomic
    public static void createConversionTable(CompetenceCourse competence, AcademicInterval year, String[] table) {
        EctsCompetenceCourseConversionTable conversion = EctsTableIndex.readOrCreateByYear(year).getEnrolmentTableBy(competence);
        EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
        if (conversion != null) {
            conversion.delete();
        }
        if (ectsTable != null) {
            new EctsCompetenceCourseConversionTable(competence, year, ectsTable);
        }
    }

    @Override
    public CurricularYear getCurricularYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CycleType getCycle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EctsComparabilityPercentages getPercentages() {
        return new NullEctsComparabilityPercentages();
    }

    @Override
    public void delete() {
        setCompetenceCourse(null);
        super.delete();
    }
    @Deprecated
    public boolean hasCompetenceCourse() {
        return getCompetenceCourse() != null;
    }

}

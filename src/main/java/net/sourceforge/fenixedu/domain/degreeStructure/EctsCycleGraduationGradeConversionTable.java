package net.sourceforge.fenixedu.domain.degreeStructure;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class EctsCycleGraduationGradeConversionTable extends EctsCycleGraduationGradeConversionTable_Base {

    protected EctsCycleGraduationGradeConversionTable(Unit institution, AcademicInterval year, CycleType type,
            EctsComparabilityTable table, EctsComparabilityPercentages percentages) {
        super();
        init(year, type, table, percentages);
        setSchool(institution);
    }

    protected Bennu getRootDomainObject() {
        return getSchool().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
        return getSchool();
    }

    @Atomic
    public static void createConversionTable(Unit institution, AcademicInterval year, CycleType type, String[] table,
            String[] percentages) {
        EctsCycleGraduationGradeConversionTable conversion = EctsTableIndex.readOrCreateByYear(year).getGraduationTableBy(type);
        EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
        EctsComparabilityPercentages ectsPercentages = EctsComparabilityPercentages.fromStringArray(percentages);
        if (conversion != null) {
            conversion.delete();
        }
        if (ectsTable != null && ectsPercentages != null) {
            new EctsCycleGraduationGradeConversionTable(institution, year, type, ectsTable, ectsPercentages);
        }
    }

    @Override
    public CurricularYear getCurricularYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() {
        setSchool(null);
        super.delete();
    }
    @Deprecated
    public boolean hasSchool() {
        return getSchool() != null;
    }

}

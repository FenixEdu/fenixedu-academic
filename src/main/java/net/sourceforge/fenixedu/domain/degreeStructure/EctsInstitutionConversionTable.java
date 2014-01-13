package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class EctsInstitutionConversionTable extends EctsInstitutionConversionTable_Base {

    protected EctsInstitutionConversionTable(Unit school, AcademicInterval year, EctsComparabilityTable table) {
        super();
        init(year, table);
        setSchool(school);
    }

    @Override
    public DomainObject getTargetEntity() {
        return getSchool();
    }

    @Override
    public EctsComparabilityPercentages getPercentages() {
        return new NullEctsComparabilityPercentages();
    }

    @Override
    public CurricularYear getCurricularYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CycleType getCycle() {
        throw new UnsupportedOperationException();
    }

    protected Bennu getRootDomainObject() {
        return getSchool().getRootDomainObject();
    }

    @Atomic
    public static void createConversionTable(Unit instituition, AcademicInterval year, String[] table) {
        EctsInstitutionConversionTable conversion = EctsTableIndex.readOrCreateByYear(year).getEnrolmentTableBy(instituition);
        EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
        if (conversion != null) {
            conversion.delete();
        }
        if (ectsTable != null) {
            new EctsInstitutionConversionTable(instituition, year, ectsTable);
        }
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

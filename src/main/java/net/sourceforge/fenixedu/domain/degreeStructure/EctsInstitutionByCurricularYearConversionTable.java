package net.sourceforge.fenixedu.domain.degreeStructure;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class EctsInstitutionByCurricularYearConversionTable extends EctsInstitutionByCurricularYearConversionTable_Base {

    protected EctsInstitutionByCurricularYearConversionTable(Unit school, AcademicInterval year, CycleType cycle,
            CurricularYear curricularYear, EctsComparabilityTable table) {
        super();
        init(year, curricularYear, table);
        setSchool(school);
        setCycle(cycle);
    }

    protected Bennu getRootDomainObject() {
        return getSchool().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
        return getSchool();
    }

    @Atomic
    public static void createConversionTable(Unit ist, AcademicInterval year, CycleType cycleType, CurricularYear curricularYear,
            String[] table) {
        EctsInstitutionByCurricularYearConversionTable conversion =
                EctsTableIndex.readOrCreateByYear(year).getEnrolmentTableBy(ist, curricularYear, cycleType);
        EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
        if (conversion != null) {
            conversion.delete();
        }
        if (ectsTable != null) {
            new EctsInstitutionByCurricularYearConversionTable(ist, year, cycleType, curricularYear, ectsTable);
        }
    }

    @Override
    public void delete() {
        setSchool(null);
        super.delete();
    }
    @Deprecated
    public boolean hasCycle() {
        return getCycle() != null;
    }

    @Deprecated
    public boolean hasSchool() {
        return getSchool() != null;
    }

}

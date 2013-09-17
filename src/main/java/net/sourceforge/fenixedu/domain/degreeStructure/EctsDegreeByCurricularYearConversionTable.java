package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class EctsDegreeByCurricularYearConversionTable extends EctsDegreeByCurricularYearConversionTable_Base {

    protected EctsDegreeByCurricularYearConversionTable(Degree degree, AcademicInterval year, CurricularYear curricularYear,
            EctsComparabilityTable table) {
        super();
        init(year, curricularYear, table);
        setDegree(degree);
    }

    protected RootDomainObject getRootDomainObject() {
        return getDegree().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
        return getDegree();
    }

    @Atomic
    public static void createConversionTable(Degree degree, AcademicInterval year, CurricularYear curricularYear, String[] table) {
        EctsDegreeByCurricularYearConversionTable conversion =
                EctsTableIndex.readOrCreateByYear(year).getEnrolmentTableBy(degree, curricularYear);
        EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
        if (conversion != null) {
            conversion.delete();
        }
        if (ectsTable != null) {
            new EctsDegreeByCurricularYearConversionTable(degree, year, curricularYear, ectsTable);
        }
    }

    @Override
    public CycleType getCycle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() {
        setDegree(null);
        super.delete();
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

}

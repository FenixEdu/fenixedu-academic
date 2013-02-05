package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

public abstract class EctsCurricularYearConversionTable extends EctsCurricularYearConversionTable_Base {
    @Override
    public EctsComparabilityPercentages getPercentages() {
        return new NullEctsComparabilityPercentages();
    }

    protected void init(AcademicInterval year, CurricularYear curricularYear, EctsComparabilityTable table) {
        super.init(year, table);
        setCurricularYear(curricularYear);
    }

    @Override
    public void delete() {
        removeCurricularYear();
        super.delete();
    }
}

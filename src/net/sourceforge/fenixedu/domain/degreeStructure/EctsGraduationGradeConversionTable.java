package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

public abstract class EctsGraduationGradeConversionTable extends EctsGraduationGradeConversionTable_Base {

    public EctsGraduationGradeConversionTable() {
        super();
    }

    protected void init(AcademicInterval year, CycleType cycle, EctsComparabilityTable table,
            EctsComparabilityPercentages percentages) {
        super.init(year, table);
        setCycle(cycle);
        setPercentages(percentages);
    }
}

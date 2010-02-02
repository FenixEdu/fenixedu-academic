package net.sourceforge.fenixedu.domain.degreeStructure;

public abstract class EctsCurricularYearConversionTable extends EctsCurricularYearConversionTable_Base {
    public EctsComparabilityPercentages getPercentages() {
	return new NullEctsComparabilityPercentages();
    }
}

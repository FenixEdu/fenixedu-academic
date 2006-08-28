package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

public enum ValuationValueType {
	MANUAL_VALUE,
	LAST_YEAR_REAL_VALUE,
	LAST_YEAR_ESTIMATED_VALUE,
	OMISSION_VALUE,
	REAL_VALUE,
	CALCULATED_VALUE;
	
	public String getName() {
		return name();
	}
}

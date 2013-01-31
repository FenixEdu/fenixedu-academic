package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DomainObject;

public interface IEctsConversionTable {
	public DomainObject getTargetEntity();

	public EctsComparabilityTable getEctsTable();

	public EctsComparabilityPercentages getPercentages();

	public CurricularYear getCurricularYear();

	public CycleType getCycle();
}

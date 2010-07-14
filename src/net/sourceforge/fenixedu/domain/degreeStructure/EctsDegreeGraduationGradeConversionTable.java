package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.services.Service;

public class EctsDegreeGraduationGradeConversionTable extends EctsDegreeGraduationGradeConversionTable_Base {

    public static class ConversionTableCycleIsRequiredInIntegratedMasterDegree extends RuntimeException {
	private static final long serialVersionUID = 4018675993933248380L;
    }

    public static class ConversionTableCycleIsOnlyRequiredInIntegratedMasterDegree extends RuntimeException {
	private static final long serialVersionUID = -257788220757706395L;
    }

    protected EctsDegreeGraduationGradeConversionTable(Degree degree, AcademicInterval year, CycleType cycle,
	    EctsComparabilityTable table, EctsComparabilityPercentages percentages) {
	super();
	setDegree(degree);
	setYear(year);
	setCycle(cycle);
	setEctsTable(table);
	setPercentages(percentages);
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getDegree().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
	return getDegree();
    }

    @Service
    public static void createConversionTable(Degree degree, AcademicInterval executionInterval, CycleType cycle, String[] table,
	    String[] percentages) {
	EctsDegreeGraduationGradeConversionTable conversion = degree.getEctsGraduationGradeConversionTable(executionInterval,
		cycle);
	if (degree.getDegreeType().isComposite()) {
	    if (cycle == null)
		throw new ConversionTableCycleIsRequiredInIntegratedMasterDegree();
	} else {
	    if (cycle != null)
		throw new ConversionTableCycleIsOnlyRequiredInIntegratedMasterDegree();
	}
	EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
	EctsComparabilityPercentages ectsPercentages = EctsComparabilityPercentages.fromStringArray(percentages);
	if (conversion != null) {
	    conversion.delete();
	}
	if (ectsTable != null && ectsPercentages != null) {
	    new EctsDegreeGraduationGradeConversionTable(degree, executionInterval, cycle, ectsTable, ectsPercentages);
	}
    }

    @Override
    public CurricularYear getCurricularYear() {
	throw new UnsupportedOperationException();
    }

    public void delete() {
	removeDegree();
	deleteDomainObject();
    }
}

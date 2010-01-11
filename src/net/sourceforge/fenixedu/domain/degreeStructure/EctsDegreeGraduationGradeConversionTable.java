package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
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
    public static EctsDegreeGraduationGradeConversionTable createConversionTable(Degree degree,
	    AcademicInterval executionInterval, CycleType cycle, String[] table, String[] percentages) {
	if (degree.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
	    if (cycle == null)
		throw new ConversionTableCycleIsRequiredInIntegratedMasterDegree();
	    for (EctsDegreeGraduationGradeConversionTable conversion : degree.getEctsGraduationGradeConversionTables()) {
		if (conversion.getYear().equals(executionInterval) && conversion.getCycle().equals(cycle)) {
		    throw new DuplicateEctsConversionTable();
		}
	    }
	} else {
	    if (cycle != null)
		throw new ConversionTableCycleIsOnlyRequiredInIntegratedMasterDegree();
	    for (EctsDegreeGraduationGradeConversionTable conversion : degree.getEctsGraduationGradeConversionTables()) {
		if (conversion.getYear().equals(executionInterval)) {
		    throw new DuplicateEctsConversionTable();
		}
	    }
	}
	return new EctsDegreeGraduationGradeConversionTable(degree, executionInterval, cycle, new EctsComparabilityTable(table),
		new EctsComparabilityPercentages(percentages));
    }

    @Override
    public CurricularYear getCurricularYear() {
	throw new UnsupportedOperationException();
    }
}

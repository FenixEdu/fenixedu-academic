package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.services.Service;

public class EctsCycleGraduationGradeConversionTable extends EctsCycleGraduationGradeConversionTable_Base {

    protected EctsCycleGraduationGradeConversionTable(Unit institution, AcademicInterval year, CycleType type,
	    EctsComparabilityTable table, EctsComparabilityPercentages percentages) {
	super();
	setSchool(institution);
	setYear(year);
	setCycle(type);
	setEctsTable(table);
	setPercentages(percentages);
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getSchool().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
	return getSchool();
    }

    @Service
    public static void createConversionTable(Unit institution, AcademicInterval executionInterval, CycleType type,
	    String[] table, String[] percentages) {
	EctsCycleGraduationGradeConversionTable conversion = institution.getEctsGraduationGradeConversionTable(executionInterval,
		type);
	EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
	EctsComparabilityPercentages ectsPercentages = EctsComparabilityPercentages.fromStringArray(percentages);
	if (conversion != null) {
	    conversion.delete();
	}
	if (ectsTable != null && ectsPercentages != null) {
	    new EctsCycleGraduationGradeConversionTable(institution, executionInterval, type, ectsTable, ectsPercentages);
	}
    }

    @Override
    public CurricularYear getCurricularYear() {
	throw new UnsupportedOperationException();
    }

    public void delete() {
	removeSchool();
	deleteDomainObject();
    }
}

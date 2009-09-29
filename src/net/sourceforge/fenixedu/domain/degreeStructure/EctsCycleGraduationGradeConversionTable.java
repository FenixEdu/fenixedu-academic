package net.sourceforge.fenixedu.domain.degreeStructure;

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
    public static EctsCycleGraduationGradeConversionTable createConversionTable(Unit institution,
	    AcademicInterval executionInterval, CycleType type, String[] table, String[] percentages) {
	for (EctsCycleGraduationGradeConversionTable conversion : institution.getEctsGraduationGradeConversionTables()) {
	    if (conversion.getYear().equals(executionInterval) && conversion.getCycle().equals(type)) {
		throw new DuplicateEctsConversionTable();
	    }
	}
	return new EctsCycleGraduationGradeConversionTable(institution, executionInterval, type,
		new EctsComparabilityTable(table), new EctsComparabilityPercentages(percentages));
    }
}

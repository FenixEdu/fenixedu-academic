package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.services.Service;

public class EctsCompetenceCourseConversionTable extends EctsCompetenceCourseConversionTable_Base {

    protected EctsCompetenceCourseConversionTable(CompetenceCourse competenceCourse, AcademicInterval year,
	    EctsComparabilityTable table) {
	super();
	setCompetenceCourse(competenceCourse);
	setYear(year);
	setEctsTable(table);
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getCompetenceCourse().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
	return getCompetenceCourse();
    }

    @Service
    public static EctsCompetenceCourseConversionTable createConversionTable(CompetenceCourse competence,
	    AcademicInterval executionInterval, String[] table) {
	for (EctsCompetenceCourseConversionTable conversion : competence.getEctsConversionTables()) {
	    if (conversion.getYear().equals(executionInterval)) {
		throw new DuplicateEctsConversionTable();
	    }
	}
	return new EctsCompetenceCourseConversionTable(competence, executionInterval, new EctsComparabilityTable(table));
    }

    @Override
    public CurricularYear getCurricularYear() {
	throw new UnsupportedOperationException();
    }

    @Override
    public CycleType getCycle() {
	throw new UnsupportedOperationException();
    }

    @Override
    public EctsComparabilityPercentages getPercentages() {
	return new NullEctsComparabilityPercentages();
    }
}

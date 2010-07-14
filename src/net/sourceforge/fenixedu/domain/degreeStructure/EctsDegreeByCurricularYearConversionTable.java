package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.services.Service;

public class EctsDegreeByCurricularYearConversionTable extends EctsDegreeByCurricularYearConversionTable_Base {

    protected EctsDegreeByCurricularYearConversionTable(Degree degree, AcademicInterval year, CurricularYear curricularYear,
	    EctsComparabilityTable table) {
	super();
	setDegree(degree);
	setYear(year);
	setCurricularYear(curricularYear);
	setEctsTable(table);
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
    public static void createConversionTable(Degree degree, AcademicInterval executionInterval, CurricularYear curricularYear,
	    String[] table) {
	EctsDegreeByCurricularYearConversionTable conversion = degree.getEctsCourseConversionTable(executionInterval,
		curricularYear);
	EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
	if (conversion != null) {
	    conversion.delete();
	}
	if (ectsTable != null) {
	    new EctsDegreeByCurricularYearConversionTable(degree, executionInterval, curricularYear, ectsTable);
	}
    }

    @Override
    public CycleType getCycle() {
	throw new UnsupportedOperationException();
    }

    public void delete() {
	removeDegree();
	removeCurricularYear();
	deleteDomainObject();
    }

}

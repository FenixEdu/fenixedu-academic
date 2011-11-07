package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

public abstract class EctsConversionTable extends EctsConversionTable_Base implements IEctsConversionTable {
    protected void init(AcademicInterval year, EctsComparabilityTable table) {
	setYear(year);
	setEctsTable(table);
	for (EctsTableIndex index : RootDomainObject.getInstance().getEctsTableIndexSet()) {
	    if (index.getYear().equals(year)) {
		setIndex(index);
		return;
	    }
	}
	setIndex(new EctsTableIndex(year));
    }

    public Grade convert(Grade grade) {
	switch (grade.getGradeScale()) {
	case TYPE20:
	    return Grade.createGrade(getEctsTable().convert(grade.getIntegerValue()), GradeScale.TYPEECTS);
	case TYPEAP:
	    return Grade.createGrade(GradeScale.NA, GradeScale.TYPEECTS);
	case TYPEAPT:
	    return grade;
	default:
	    throw new DomainException("error.ects.unable.to.convert.grade");
	}
    }

    public void delete() {
	removeIndex();
	deleteDomainObject();
    }
}

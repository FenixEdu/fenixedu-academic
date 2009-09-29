package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class EctsConversionTable extends EctsConversionTable_Base {

    public static class DuplicateEctsConversionTable extends RuntimeException {
	private static final long serialVersionUID = 4540431507249909228L;
    }

    public abstract DomainObject getTargetEntity();

    public Grade convert(Grade grade) {
	switch (grade.getGradeScale()) {
	case TYPE20:
	    return Grade.createGrade(getEctsTable().convert(grade.getIntegerValue()), GradeScale.TYPEECTS);
	case TYPEAP:
	    return Grade.createGrade(GradeScale.NA, GradeScale.TYPEECTS);
	default:
	    throw new DomainException("error.unable.to.convert.grade");
	}
    }
}

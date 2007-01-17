package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import dml.runtime.RelationAdapter;

public class ExternalCurricularCourse extends ExternalCurricularCourse_Base {

    static {
	ExternalCurricularCourseUnit.addListener(new RelationAdapter<ExternalCurricularCourse, Unit>() {
	    @Override
	    public void beforeAdd(ExternalCurricularCourse externalCurricularCourse, Unit unit) {
		if (unit != null) {
		    if (!unit.getType().equals(PartyTypeEnum.UNIVERSITY)
			    && !unit.getType().equals(PartyTypeEnum.SCHOOL)
			    && !unit.getType().equals(PartyTypeEnum.DEPARTMENT)) {
			throw new DomainException("error.extraCurricularCourse.invalid.unit.type");
		    }
		}
	    }
	});
    }

    public ExternalCurricularCourse(Unit unit, String name, String code) {
	super();
	if (unit == null) {
	    throw new DomainException("error.externalCurricularCourse.unit.cannot.be.null");
	}
	if (name == null || name.length() == 0) {
	    throw new DomainException("error.externalCurricularCourse.name.cannot.be.empty");
	}
	setRootDomainObject(RootDomainObject.getInstance());
	setName(name);
	setCode(code);
	setUnit(unit);
    }

    public void delete() {
	if (canBeDeleted()) {
	    removeRootDomainObject();
	    removeUnit();
	    super.deleteDomainObject();
	} else {
	    throw new DomainException("error.external.enrolment.cannot.be.deleted");
	}
    }

    private boolean canBeDeleted() {
	return !hasAnyExternalEnrolments();
    }

    public static ExternalCurricularCourse readExternalCurricularCourse(String code) {
	for (final ExternalCurricularCourse externalCurricularCourse : RootDomainObject.getInstance()
		.getExternalCurricularCourses()) {
	    if (externalCurricularCourse.getCode().equals(code)) {
		return externalCurricularCourse;
	    }
	}
	return null;
    }

}

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

import org.apache.commons.lang.StringUtils;

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

    public ExternalCurricularCourse(final Unit unit, final String name, final String code) {
	super();
	if (unit == null) {
	    throw new DomainException("error.externalCurricularCourse.unit.cannot.be.null");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new DomainException("error.externalCurricularCourse.name.cannot.be.empty");
	}

	checkForExternalCurricularCourseWithSameNameAndCode(unit, name, code);

	setRootDomainObject(RootDomainObject.getInstance());
	setUnit(unit);
	setName(name);
	setCode(code);
    }

    private void checkForExternalCurricularCourseWithSameNameAndCode(final Unit unit, final String name,
	    String code) {
	final String nameToSearch = name.toLowerCase();
	for (final ExternalCurricularCourse externalCurricularCourse : unit
		.getExternalCurricularCourses()) {
	    if (externalCurricularCourse.getName().toLowerCase().equals(nameToSearch)) {

		if ((externalCurricularCourse.getCode() != null && externalCurricularCourse.getCode()
			.equals(code))
			|| externalCurricularCourse.getCode() == null && code == null) {

		    throw new DomainException(
			    "error.externalCurricularCourse.parent.unit.already.has.externalCurricularCourse.with.same.type");
		}

	    }
	}
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

    public String getFullPathName() {
	final List<AccountabilityTypeEnum> validAccountabilityTypes = Arrays
		.asList(new AccountabilityTypeEnum[] { AccountabilityTypeEnum.GEOGRAPHIC,
			AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE });
	return UnitUtils.getUnitFullPathName(getUnit(), validAccountabilityTypes).toString() + " > "
		+ getName();
    }

    static public ExternalCurricularCourse readExternalCurricularCourse(Unit unit, String name,
	    String code) {
	for (final ExternalCurricularCourse externalCurricularCourse : unit
		.getExternalCurricularCourses()) {
	    if (externalCurricularCourse.getCode().equals(code)
		    && externalCurricularCourse.getName().equals(name)) {
		return externalCurricularCourse;
	    }
	}
	return null;
    }

    static public ExternalCurricularCourse readExternalCurricularCourse(Unit unit, String code) {
	List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>();
	for (final ExternalCurricularCourse externalCurricularCourse : unit
		.getExternalCurricularCourses()) {
	    if (StringUtils.isEmpty(externalCurricularCourse.getCode()) && StringUtils.isEmpty(code)
		    || externalCurricularCourse.getCode().equals(code)) {
		result.add(externalCurricularCourse);
	    }
	}
	if (result.size() == 1) {
	    return result.iterator().next();
	} else if (result.size() == 0) {
	    return null;
	}
	throw new DomainException("error.externalCurricularCourse.manyFoundWithSameCode");
    }

    static public List<ExternalCurricularCourse> readExternalCurricularCoursesByCode(String code) {
	List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>();
	for (final ExternalCurricularCourse externalCurricularCourse : RootDomainObject.getInstance()
		.getExternalCurricularCourses()) {
	    if (StringUtils.isEmpty(externalCurricularCourse.getCode()) && StringUtils.isEmpty(code)
		    || externalCurricularCourse.getCode().equals(code)) {
		result.add(externalCurricularCourse);
	    }
	}
	return result;
    }

    static public List<ExternalCurricularCourse> readByName(final String regex) {
	if (regex == null) {
	    return Collections.emptyList();
	}
	final String nameToMatch = regex.replaceAll("%", ".*").toLowerCase();
	final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>();
	for (final ExternalCurricularCourse externalCurricularCourse : RootDomainObject.getInstance()
		.getExternalCurricularCoursesSet()) {
	    if (externalCurricularCourse.getName().toLowerCase().matches(nameToMatch)) {
		result.add(externalCurricularCourse);
	    }
	}
	return result;
    }
}

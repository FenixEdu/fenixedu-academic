package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
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
	
	checkForExternalCurricularCourseWithSameName(unit, name);
	
	setRootDomainObject(RootDomainObject.getInstance());
	setUnit(unit);
	setName(name);
	setCode(code);
    }

    private void checkForExternalCurricularCourseWithSameName(final Unit unit, final String name) {
	final String nameToSearch = name.toLowerCase();
	for (final ExternalCurricularCourse externalCurricularCourse : unit.getExternalCurricularCourses()) {
	    if (externalCurricularCourse.getName().toLowerCase().equals(nameToSearch)) {
		throw new DomainException("error.externalCurricularCourse.parent.unit.already.has.externalCurricularCourse.with.same.type");
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
	final List<AccountabilityTypeEnum> validAccountabilityTypes = Arrays.asList(new AccountabilityTypeEnum[] { 
		    AccountabilityTypeEnum.GEOGRAPHIC, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE});
	return UnitUtils.getUnitFullPathName(getUnit(), validAccountabilityTypes).toString() + " > " + getName();
    }

    
    static public ExternalCurricularCourse readExternalCurricularCourse(String code) {
	for (final ExternalCurricularCourse externalCurricularCourse : RootDomainObject.getInstance()
		.getExternalCurricularCourses()) {
	    if (externalCurricularCourse.getCode().equals(code)) {
		return externalCurricularCourse;
	    }
	}
	return null;
    }

    static public List<ExternalCurricularCourse> readByName(final String regex) {
	if (regex == null) {
	    return Collections.emptyList();
	}
	final String nameToMatch = regex.replaceAll("%", ".*").toLowerCase();
	final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>();
	for (final ExternalCurricularCourse externalCurricularCourse : RootDomainObject.getInstance().getExternalCurricularCoursesSet()) {
	    if (externalCurricularCourse.getName().toLowerCase().matches(nameToMatch)) {
		result.add(externalCurricularCourse);
	    }
	}
	return result;
    }
}

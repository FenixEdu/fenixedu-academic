package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificAreaSite;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.ScientificAreaMemberGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class ScientificAreaUnit extends ScientificAreaUnit_Base {

    private ScientificAreaUnit() {
	super();
	super.setType(PartyTypeEnum.SCIENTIFIC_AREA);
    }

    public static ScientificAreaUnit createNewInternalScientificArea(MultiLanguageString name,
	    Integer costCenterCode, String acronym, YearMonthDay beginDate, YearMonthDay endDate,
	    Unit parentUnit, AccountabilityType accountabilityType, String webAddress,
	    UnitClassification classification, Boolean canBeResponsibleOfSpaces, Campus campus) {

	ScientificAreaUnit scientificAreaUnit = new ScientificAreaUnit();
	scientificAreaUnit.init(name, costCenterCode, acronym, beginDate, endDate, webAddress,
		classification, canBeResponsibleOfSpaces, campus);
	scientificAreaUnit.addParentUnit(parentUnit, accountabilityType);

	checkIfAlreadyExistsOneScientificAreaUnitWithSameAcronymAndName(scientificAreaUnit);

	return scientificAreaUnit;
    }

    @Override
    public void edit(MultiLanguageString unitName, Integer unitCostCenter, String acronym,
	    YearMonthDay beginDate, YearMonthDay endDate, String webAddress,
	    UnitClassification classification, Department department, Degree degree,
	    AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces, Campus campus) {

	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification,
		department, degree, administrativeOffice, canBeResponsibleOfSpaces, campus);

	checkIfAlreadyExistsOneScientificAreaUnitWithSameAcronymAndName(this);
    }

    @Override
    public void setAcronym(String acronym) {
	if (StringUtils.isEmpty(acronym)) {
	    throw new DomainException("error.unit.empty.acronym");
	}
	super.setAcronym(acronym);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
	throw new DomainException("unit.impossible.set.type");
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (parentUnit != null && (!parentUnit.isInternal() || !parentUnit.isDepartmentUnit())) {
	    throw new DomainException("error.unit.invalid.parentUnit");
	}
	return super.addParentUnit(parentUnit, accountabilityType);
    }

    @Override
    public boolean isScientificAreaUnit() {
	return true;
    }

    @Override
    public boolean hasCompetenceCourses(final CompetenceCourse competenceCourse) {
	for (Unit subUnit : getSubUnits()) {
	    if (subUnit.hasCompetenceCourses(competenceCourse)) {
		return true;
	    }
	}
	return false;
    }

    public List<CompetenceCourseGroupUnit> getCompetenceCourseGroupUnits() {
	final SortedSet<CompetenceCourseGroupUnit> result = new TreeSet<CompetenceCourseGroupUnit>(
		CompetenceCourseGroupUnit.COMPARATOR_BY_NAME_AND_ID);
	for (Unit unit : getSubUnits()) {
	    if (unit.isCompetenceCourseGroupUnit()) {
		result.add((CompetenceCourseGroupUnit) unit);
	    }
	}
	return new ArrayList<CompetenceCourseGroupUnit>(result);
    }

    public Double getScientificAreaUnitEctsCredits() {
	double result = 0.0;
	for (CompetenceCourseGroupUnit competenceCourseGroupUnit : getCompetenceCourseGroupUnits()) {
	    for (CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
		result += competenceCourse.getEctsCredits();
	    }
	}
	return result;
    }

    public Double getScientificAreaUnitEctsCredits(List<Context> contexts) {
	double result = 0.0;
	for (Context context : contexts) {
	    if (context.getChildDegreeModule().isLeaf()) {
		CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
		if (!curricularCourse.isOptional()
			&& curricularCourse.getCompetenceCourse().getScientificAreaUnit().equals(this)) {
		    result += curricularCourse.getCompetenceCourse().getEctsCredits();
		}
	    }
	}
	return result;
    }

    private static void checkIfAlreadyExistsOneScientificAreaUnitWithSameAcronymAndName(
	    ScientificAreaUnit scientificAreaUnit) {
	for (Unit parentUnit : scientificAreaUnit.getParentUnits()) {
	    for (Unit unit : parentUnit.getAllSubUnits()) {
		if (!unit.equals(scientificAreaUnit)
			&& (scientificAreaUnit.getName().equalsIgnoreCase(unit.getName()) || scientificAreaUnit
				.getAcronym().equalsIgnoreCase(unit.getAcronym()))) {
		    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
		}
	    }
	}
    }

    @Override
    protected UnitSite createSite() {
	return new ScientificAreaSite(this);
    }

    public boolean isUserMemberOfScientificArea(Person person) {
	for (Contract contract : getWorkingContracts()) {
	    if (contract.getPerson().equals(person)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isCurrentUserMemberOfScientificArea() {
	return isUserMemberOfScientificArea(AccessControl.getPerson());
    }

    @Override
    protected List<IGroup> getDefaultGroups() {
        List<IGroup> groups = super.getDefaultGroups();
        groups.add(new ScientificAreaMemberGroup(this));
        return groups;
    }
}

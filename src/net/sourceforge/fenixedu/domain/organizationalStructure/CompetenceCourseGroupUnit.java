package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class CompetenceCourseGroupUnit extends CompetenceCourseGroupUnit_Base {
    
    private CompetenceCourseGroupUnit() {
        super();
        super.setType(PartyTypeEnum.COMPETENCE_COURSE_GROUP);
    }
    
    public static Unit createNewInternalCompetenceCourseGroupUnit(String name, Integer costCenterCode, String acronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
	    AccountabilityType accountabilityType, String webAddress, UnitClassification classification, 
	    Boolean canBeResponsibleOfSpaces) {
			
	CompetenceCourseGroupUnit competenceCourseGroupUnit = new CompetenceCourseGroupUnit();	
	competenceCourseGroupUnit.init(name, costCenterCode, acronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces);
	competenceCourseGroupUnit.addParentUnit(parentUnit, accountabilityType);	
	
	checkIfAlreadyExistsOneCompetenceCourseGroupUnitWithSameAcronymAndName(competenceCourseGroupUnit);
	
	return competenceCourseGroupUnit;
    }
    
    @Override
    public void edit(String unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
            YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces) {
        
	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree, administrativeOffice, canBeResponsibleOfSpaces);
	
	checkIfAlreadyExistsOneCompetenceCourseGroupUnitWithSameAcronymAndName(this);
    }
    
    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if(parentUnit != null && (!parentUnit.isInternal() || !parentUnit.isScientificAreaUnit())) {
            throw new DomainException("error.unit.invalid.parentUnit");
        }
	return super.addParentUnit(parentUnit, accountabilityType);
    }
    
    @Override
    public boolean isCompetenceCourseGroupUnit() {
        return true;
    }
    
    @Override
    public List<CompetenceCourse> getCompetenceCourses() {
	final SortedSet<CompetenceCourse> result = new TreeSet<CompetenceCourse>(CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);
	result.addAll(super.getCompetenceCourses());
	return new ArrayList<CompetenceCourse>(result);
    }   
    
    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }
       
    public List<CurricularCourse> getCurricularCourses() {
	List<CompetenceCourse> competenceCourses = getCompetenceCourses();
	List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();

	for (CompetenceCourse competenceCourse : competenceCourses) {
	    curricularCourses.addAll(competenceCourse.getAssociatedCurricularCourses());
	}

	return curricularCourses;
    }
    
    public List<CompetenceCourse> getCompetenceCoursesByExecutionYear(ExecutionYear executionYear) {
	List<CompetenceCourse> competenceCourses = this.getCompetenceCourses();
	List<CompetenceCourse> competenceCoursesByExecutionYear = new ArrayList<CompetenceCourse>();
	for (CompetenceCourse competenceCourse : competenceCourses) {
	    if (competenceCourse.hasActiveScopesInExecutionYear(executionYear)) {
		competenceCoursesByExecutionYear.add(competenceCourse);
	    }

	}
	return competenceCoursesByExecutionYear;
    }
    
    @Override
    public void delete() {
	if (hasAnyCompetenceCourses()) {
	    throw new DomainException("error.unit.cannot.be.deleted");
	}
	super.delete();
    }
      
    private static void checkIfAlreadyExistsOneCompetenceCourseGroupUnitWithSameAcronymAndName(CompetenceCourseGroupUnit competenceCourseGroupUnit) {	
	for (Unit parentUnit : competenceCourseGroupUnit.getParentUnits()) {	    
	    for (Unit unit : parentUnit.getAllSubUnits()) {
		if (!unit.equals(competenceCourseGroupUnit) && competenceCourseGroupUnit.getName().equalsIgnoreCase(unit.getName())) {
		    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
		}
	    }
	}	
    }
}

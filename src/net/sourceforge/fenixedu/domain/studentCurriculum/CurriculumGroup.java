package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CurriculumGroup extends CurriculumGroup_Base {
    
    public CurriculumGroup(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
        super();
        if(studentCurricularPlan == null || courseGroup == null || executionPeriod == null) {
        	throw new DomainException("invalid arguments");
        }
        setDegreeModule(courseGroup);
        setParentStudentCurricularPlan(studentCurricularPlan);
        addChildCurriculumGroups(courseGroup, executionPeriod);
    }
    
    public CurriculumGroup(CurriculumGroup curriculumGroup, CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
    	super();
        if(curriculumGroup == null || courseGroup == null || executionPeriod == null) {
        	throw new DomainException("invalid arguments");
        }
        setDegreeModule(courseGroup);
        setCurriculumGroup(curriculumGroup);
        addChildCurriculumGroups(courseGroup, executionPeriod);
    }
    
    private void addChildCurriculumGroups(CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
    	for (CourseGroup childCourseGroup : courseGroup.getNotOptionalChildCourseGroup(executionPeriod)) {
			CurriculumGroup curriculumGroup = new CurriculumGroup(this, childCourseGroup, executionPeriod);
		}
    }
    
    public boolean isLeaf() {
		return false;
	}
    
    public boolean canBeDeleted() {
    	return !hasAnyCurriculumModules();
    }
    
    @Override
    public void delete() {
    	if(canBeDeleted()) {
    		super.delete();
    	} else {
    		throw new DomainException("curriculumGroup.notEmptyCurriculumGroupModules");
    	}
    }

	@Override
	public StringBuilder print(String tabs) {
		StringBuilder builder = new StringBuilder();
		builder.append(tabs);
		builder.append("[CG ").append(getDegreeModule().getName()).append(" ]\n");
		String tab = tabs + "\t";
		for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
			StringBuilder builder2 = curriculumModule.print(tab);
			builder.append(builder2);
		}
		return builder;
	}
	
	@Override
	public CourseGroup getDegreeModule() {
		return (CourseGroup) super.getDegreeModule();
	}
    
}

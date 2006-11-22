package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;

public class OptionalCurricularCourse extends OptionalCurricularCourse_Base {
    
    protected  OptionalCurricularCourse() {
        super();
    }
    
    /**
     * - This constructor is used to create a 'special' curricular course
     * that will represent any curricular course accordding to a rule
     */
    public OptionalCurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn,
	    CurricularStage curricularStage, CurricularPeriod curricularPeriod,
	    ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {

	this();
	setName(name);
	setNameEn(nameEn);
	setCurricularStage(curricularStage);
	setType(CurricularCourseType.OPTIONAL_COURSE);
	new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }
    
    @Override
    public boolean isOptionalCurricularCourse() {
        return true;
    }

    
}

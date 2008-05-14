package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class TSDCompetenceCourse extends TSDCompetenceCourse_Base {
    
    private  TSDCompetenceCourse() {
        super();
    }
        
    protected TSDCompetenceCourse(CompetenceCourse competenceCourse, ExecutionSemester executionSemester) {
    	this();
    	
    	this.setCompetenceCourse(competenceCourse);
    	this.setExecutionPeriod(executionSemester);
    	
    	buildTSDCourseLoads(competenceCourse, executionSemester);
    }
    
    
	public TSDCompetenceCourse(TeacherServiceDistribution tsd, CompetenceCourse competenceCourse, ExecutionSemester executionSemester) {
    	this(competenceCourse, executionSemester);
    	
    	this.addTeacherServiceDistributions(tsd);
    	
    	tsd.addTSDCourseToParentTree(this);
    }

	@Override
	public List<CurricularCourse> getAssociatedCurricularCourses() {
		List<CurricularCourse> courseList = new ArrayList<CurricularCourse>();
		courseList.addAll(getCompetenceCourse().getCurricularCoursesWithActiveScopesInExecutionPeriod(getExecutionPeriod()));
		return courseList;
	}
	
	@Override
	public void delete(){		
		removeCompetenceCourse();
		super.delete();
	}

	@Override
	public String getName() {
		if(getCompetenceCourse().isBolonha()){
			return getCompetenceCourse().getName();
		} else {
			ResourceBundle bundleDepartementMember = ResourceBundle.getBundle("resources.DepartmentMemberResources", LanguageUtils.getLocale());
			String nonBolonhaSuffix = bundleDepartementMember.getString("label.teacherServiceDistribution.nonBolonhaSuffix");
			
			return getCompetenceCourse().getName() + " " + nonBolonhaSuffix;
		}
	}
	
	private void buildTSDCourseLoads(CompetenceCourse competenceCourse, ExecutionSemester executionSemester) {
    	Double shiftHours = null;
		Set<ShiftType> lecturedShiftTypes = new HashSet<ShiftType>();
		
		for(ShiftType shiftType : ShiftType.values()){
		    switch(shiftType) {	    
			    case TEORICA:
			    shiftHours = competenceCourse.getTheoreticalHours(executionSemester);		
				break;
			    case PROBLEMS:
				shiftHours = competenceCourse.getProblemsHours(executionSemester);		
				break;
			    case LABORATORIAL:
				shiftHours = competenceCourse.getLaboratorialHours(executionSemester);		
				break;
			    case TRAINING_PERIOD:
				shiftHours = competenceCourse.getTrainingPeriodHours(executionSemester);		
				break;
			    case SEMINARY:
				shiftHours = competenceCourse.getSeminaryHours(executionSemester);		
				break;
			    case TUTORIAL_ORIENTATION:
				shiftHours = competenceCourse.getTutorialOrientationHours(executionSemester);		
				break;
			    case FIELD_WORK:
				shiftHours = competenceCourse.getFieldWorkHours(executionSemester);		
				break;	    
			    default:
			    shiftHours = null;
				break;
		    }
		
			if(shiftHours != null && shiftHours > 0d){
				lecturedShiftTypes.add(shiftType);
			}
		}
								
		for(ShiftType shiftType : lecturedShiftTypes){
			new TSDCurricularLoad(this, shiftType, 0d, TSDValueType.OMISSION_VALUE, 0, TSDValueType.OMISSION_VALUE,
					1d, TSDValueType.OMISSION_VALUE, 1d, TSDValueType.OMISSION_VALUE);
		}
	}

	
}

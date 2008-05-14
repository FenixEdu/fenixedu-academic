package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;

public class TSDCurricularCourse extends TSDCurricularCourse_Base {
    
    private  TSDCurricularCourse() {
        super();
    }

	public TSDCurricularCourse(TeacherServiceDistribution tsd, CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
		this();
		
		this.setCurricularCourse(curricularCourse);
		this.setExecutionPeriod(executionSemester);
		this.addTeacherServiceDistributions(tsd);
		
		tsd.addTSDCourseToTSDTree(this);
		
		buildTSDCourseLoads(curricularCourse, executionSemester);
	}

	private void buildTSDCourseLoads(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
		BigDecimal shiftHours = null;
		TSDCurricularLoad tsdLoad = null;
		List<ShiftType> lecturedShiftTypes = new ArrayList<ShiftType>();
				
		for(ShiftType shiftType : ShiftType.values()){
			shiftHours = curricularCourse.getTotalHoursByShiftType(shiftType, executionSemester);
						
			if(shiftHours != null && shiftHours.doubleValue() > 0d){
				lecturedShiftTypes.add(shiftType);
			}
		}
								
		for(ShiftType shiftType : lecturedShiftTypes){
			TSDValueType valueType = shiftType.equals(ShiftType.TEORICA) || shiftType.equals(ShiftType.PRATICA) || shiftType.equals(ShiftType.TEORICO_PRATICA)
				|| shiftType.equals(ShiftType.LABORATORIAL) ? TSDValueType.OMISSION_VALUE : TSDValueType.MANUAL_VALUE;
			
			tsdLoad = new TSDCurricularLoad(this, shiftType, 0d, TSDValueType.MANUAL_VALUE, 0, valueType, 1d, valueType, 1d, valueType);
			this.addTSDCurricularLoads(tsdLoad);
		}
		
	}

	@Override
	public List<CurricularCourse> getAssociatedCurricularCourses() {
		List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
		
		curricularCourses.add(getCurricularCourse());
		
		return curricularCourses;
	}

	@Override
	public void delete(){
		TSDCurricularCourseGroup valuationGroup  = getTSDCurricularCourseGroup();
		removeTSDCurricularCourseGroup();
		
		if(valuationGroup != null &&  valuationGroup.getTSDCurricularCoursesCount() == 0) {
			valuationGroup.delete();			
		}
			
		removeCurricularCourse();
		super.delete();
	}
	
	public void deleteTSDCourseOnly() {
		removeTSDCurricularCourseGroup();
		removeCurricularCourse();
		super.delete();
	}

	@Override
	public String getName() {
		String sigla = getCurricularCourse().getDegreeCurricularPlan().getDegree().getSigla();
		
		return getCurricularCourse().getName() + " (" + sigla + ")";
	}
	
	public String getDegreeName() {
		Degree degree = getCurricularCourse().getDegreeCurricularPlan().getDegree();
		
		return degree.getName() + " (" + degree.getSigla() + ")";
	}
}

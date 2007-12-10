package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class TSDCurricularCourseGroup extends TSDCurricularCourseGroup_Base {
    
    private  TSDCurricularCourseGroup() {
        super();
    }

	public TSDCurricularCourseGroup(TeacherServiceDistribution tsd, List<TSDCurricularCourse> tsdCurricularCourseList) {
		this();
		
		if(tsdCurricularCourseList == null || tsdCurricularCourseList.isEmpty()){
			throw new DomainException("TSDCurricularCourse.required");
		}
		
		TSDCurricularCourse tsdCurricularCourse = tsdCurricularCourseList.get(0);
		
		if(tsdCurricularCourse == null){
			throw new DomainException("TSDCurricularCourse.required");
		}
		
		this.getTSDCurricularCourses().addAll(tsdCurricularCourseList);
		this.setExecutionPeriod(tsdCurricularCourse.getExecutionPeriod());
		this.addTeacherServiceDistributions(tsd);
		
		tsd.addTSDCourseToTSDTree(this);
		
		buildTSDCourseLoads(tsdCurricularCourse.getCurricularCourse(), 
			tsdCurricularCourse.getExecutionPeriod());
	}
	
	@Override
	public List<CurricularCourse> getAssociatedCurricularCourses() {
		List<CurricularCourse> curricularCourseList = new ArrayList<CurricularCourse>();
		
		for (TSDCurricularCourse tsdCurricularCourse : getTSDCurricularCourses()) {
			curricularCourseList.add(tsdCurricularCourse.getCurricularCourse());
		}
		
		return curricularCourseList;
	}

	private String getAssociatedCurricularCoursesDescription() {
		StringBuilder sb = new StringBuilder();
		
		for (TSDCurricularCourse tsdCurricularCourse : getTSDCurricularCourses()) {
			sb.append(tsdCurricularCourse.getCurricularCourse().getDegreeCurricularPlan().getDegree().getSigla());
			sb.append("+");
		}
		
		sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}
        
    public TSDProcessPhase getTSDProcessPhase() {
    	return getTSDCurricularCourses().get(0).getTSDProcessPhase();
    }
    
    @Override
	public void delete(){
		for(TSDCurricularCourse tsdCourse : getTSDCurricularCourses()){
			tsdCourse.deleteTSDCourseOnly();
		}
		super.delete();
	}
    
    private String getGroupName() {
    	return getAssociatedCurricularCourses().get(0).getCompetenceCourse().getName()
    		 + " (" + getAssociatedCurricularCoursesDescription() + ")";
	}

	@Override
	public String getName() {
		try {
			return getGroupName();
		} catch (Throwable e){
			return getIdInternal().toString();
		}
	}
	
	
	private void buildTSDCourseLoads(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
		BigDecimal shiftHours = null;
		TSDCurricularLoad tsdLoad = null;
		List<ShiftType> lecturedShiftTypes = new ArrayList<ShiftType>();
				
		for(ShiftType shiftType : ShiftType.values()){
			shiftHours = curricularCourse.getTotalHoursByShiftType(shiftType, executionPeriod);
			
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
}

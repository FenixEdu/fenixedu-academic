package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ShiftType;

public class TSDVirtualCourseGroup extends TSDVirtualCourseGroup_Base {
    
	private  TSDVirtualCourseGroup() {
        super();
        this.setIsActive(true);
    }

	private TSDVirtualCourseGroup(TeacherServiceDistribution tsd, String name, ExecutionPeriod executionPeriod) {
		this();
		
		this.setName(name);
		this.setExecutionPeriod(executionPeriod);
		this.addTeacherServiceDistributions(tsd);
	}
	
	public TSDVirtualCourseGroup(TeacherServiceDistribution tsd, String name, ExecutionPeriod executionPeriod, 
			List<ShiftType> lecturedShiftTypes, List<DegreeCurricularPlan> plansList) {
		this(tsd, name, executionPeriod);
		
		if(plansList != null){
			this.getDegreeCurricularPlans().addAll(plansList);
		}
		
		buildTSDCourseLoads(lecturedShiftTypes, executionPeriod);
	}

	private void buildTSDCourseLoads(List<ShiftType> lecturedShiftTypes, ExecutionPeriod executionPeriod) {
		for(ShiftType shiftType : lecturedShiftTypes){
			TSDValueType valueType = shiftType.equals(ShiftType.TEORICA) || shiftType.equals(ShiftType.PRATICA) || shiftType.equals(ShiftType.TEORICO_PRATICA)
				|| shiftType.equals(ShiftType.LABORATORIAL) ? TSDValueType.OMISSION_VALUE : TSDValueType.MANUAL_VALUE;
			
			new TSDCurricularLoad(this, shiftType, 0d, TSDValueType.MANUAL_VALUE, 0, valueType, 1d, valueType, 1d, valueType);
		}
	}

	@Override
	public List<CurricularCourse> getAssociatedCurricularCourses() {
		List<CurricularCourse> emptyList = new ArrayList<CurricularCourse>();
		
		return emptyList;
	}
	
	public List<ShiftType> getLecturedShiftTypes(){
		List<ShiftType> lecturedShiftTypes = new ArrayList<ShiftType>();
		
		for(TSDCurricularLoad load : getTSDCurricularLoads()){
			lecturedShiftTypes.add(load.getType());
		}
		
		return lecturedShiftTypes;
	}
    
}

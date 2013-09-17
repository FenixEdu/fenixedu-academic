package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;

public class TSDVirtualCourseGroup extends TSDVirtualCourseGroup_Base {

    private TSDVirtualCourseGroup() {
        super();
        this.setIsActive(true);
    }

    private TSDVirtualCourseGroup(TeacherServiceDistribution tsd, String name, ExecutionSemester executionSemester) {
        this();

        this.setName(name);
        this.setExecutionPeriod(executionSemester);
        this.addTeacherServiceDistributions(tsd);
    }

    public TSDVirtualCourseGroup(TeacherServiceDistribution tsd, String name, ExecutionSemester executionSemester,
            List<ShiftType> lecturedShiftTypes, Collection<DegreeCurricularPlan> plansList) {
        this(tsd, name, executionSemester);

        if (plansList != null) {
            this.getDegreeCurricularPlans().addAll(plansList);
        }

        buildTSDCourseLoads(lecturedShiftTypes, executionSemester);
    }

    private void buildTSDCourseLoads(List<ShiftType> lecturedShiftTypes, ExecutionSemester executionSemester) {
        for (ShiftType shiftType : lecturedShiftTypes) {
            TSDValueType valueType =
                    shiftType.equals(ShiftType.TEORICA) || shiftType.equals(ShiftType.PRATICA)
                            || shiftType.equals(ShiftType.TEORICO_PRATICA) || shiftType.equals(ShiftType.LABORATORIAL) ? TSDValueType.OMISSION_VALUE : TSDValueType.MANUAL_VALUE;

            new TSDCurricularLoad(this, shiftType, 0d, TSDValueType.MANUAL_VALUE, 0, valueType, 1d, valueType, 1d, valueType);
        }
    }

    @Override
    public List<CurricularCourse> getAssociatedCurricularCourses() {
        List<CurricularCourse> emptyList = new ArrayList<CurricularCourse>();

        return emptyList;
    }

    public List<ShiftType> getLecturedShiftTypes() {
        List<ShiftType> lecturedShiftTypes = new ArrayList<ShiftType>();

        for (TSDCurricularLoad load : getTSDCurricularLoads()) {
            lecturedShiftTypes.add(load.getType());
        }

        return lecturedShiftTypes;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeCurricularPlan> getDegreeCurricularPlans() {
        return getDegreeCurricularPlansSet();
    }

    @Deprecated
    public boolean hasAnyDegreeCurricularPlans() {
        return !getDegreeCurricularPlansSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

}

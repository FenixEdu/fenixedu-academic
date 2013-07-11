package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDVirtualCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class CreateTSDVirtualGroup {
    protected TSDCourse run(String courseName, String tsdId, String periodId, String[] shiftTypesArray,
            String[] degreeCurricularPlansIdArray) {

        TeacherServiceDistribution tsd = FenixFramework.getDomainObject(tsdId);
        ExecutionSemester executionSemester = FenixFramework.getDomainObject(periodId);

        List<DegreeCurricularPlan> degreeCurricularPlansList = new ArrayList<DegreeCurricularPlan>();
        for (String planId : degreeCurricularPlansIdArray) {
            degreeCurricularPlansList.add(FenixFramework.<DegreeCurricularPlan> getDomainObject(planId));
        }

        List<ShiftType> lecturedShiftTypes = new ArrayList<ShiftType>();
        for (String typeStr : shiftTypesArray) {
            lecturedShiftTypes.add(ShiftType.valueOf(typeStr));
        }

        return new TSDVirtualCourseGroup(tsd, courseName, executionSemester, lecturedShiftTypes, degreeCurricularPlansList);
    }

    // Service Invokers migrated from Berserk

    private static final CreateTSDVirtualGroup serviceInstance = new CreateTSDVirtualGroup();

    @Service
    public static TSDCourse runCreateTSDVirtualGroup(String courseName, String tsdId, String periodId, String[] shiftTypesArray,
            String[] degreeCurricularPlansIdArray) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(courseName, tsdId, periodId, shiftTypesArray, degreeCurricularPlansIdArray);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(courseName, tsdId, periodId, shiftTypesArray, degreeCurricularPlansIdArray);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    return serviceInstance.run(courseName, tsdId, periodId, shiftTypesArray, degreeCurricularPlansIdArray);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}
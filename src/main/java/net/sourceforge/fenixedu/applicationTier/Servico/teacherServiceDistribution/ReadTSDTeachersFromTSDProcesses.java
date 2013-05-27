package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDRealTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.Pair;

public class ReadTSDTeachersFromTSDProcesses {
    public List<TSDTeacherDTOEntry> run(Map<Integer, Pair<Integer, Integer>> tsdProcessIdMap) {
        List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList = new ArrayList<TSDTeacherDTOEntry>();
        Map<Teacher, TSDTeacherDTOEntry> teacherDTOMap = new HashMap<Teacher, TSDTeacherDTOEntry>();

        for (Integer tsdProcessPhaseId : tsdProcessIdMap.keySet()) {
            TSDProcessPhase tsdProcessPhase = RootDomainObject.getInstance().readTSDProcessPhaseByOID(tsdProcessPhaseId);
            TeacherServiceDistribution tsd = null;

            tsd = RootDomainObject.getInstance().readTeacherServiceDistributionByOID(tsdProcessIdMap.get(tsdProcessPhaseId).getKey());

            List<ExecutionSemester> executionPeriodList =
                    getExecutionPeriodList(tsd, tsdProcessIdMap.get(tsdProcessPhaseId).getValue());

            List<TSDTeacher> tsdTeacherList = new ArrayList<TSDTeacher>(tsd.getTSDTeachers());
            for (TSDTeacher tsdTeacher : tsdTeacherList) {
                if (tsdTeacher instanceof TSDRealTeacher && teacherDTOMap.containsKey(((TSDRealTeacher) tsdTeacher).getTeacher())) {
                    TSDTeacherDTOEntry tsdTeacherDTOEntry = teacherDTOMap.get(((TSDRealTeacher) tsdTeacher).getTeacher());
                    tsdTeacherDTOEntry.addExecutionPeriodList(executionPeriodList);
                    tsdTeacherDTOEntry.addTSDTeacher(tsdTeacher);
                } else {
                    TSDTeacherDTOEntry tsdTeacherDTOEntry = new TSDTeacherDTOEntry(tsdTeacher, executionPeriodList);
                    tsdTeacherDTOEntryList.add(tsdTeacherDTOEntry);

                    if (tsdTeacher instanceof TSDRealTeacher) {
                        teacherDTOMap.put(((TSDRealTeacher) tsdTeacher).getTeacher(), tsdTeacherDTOEntry);
                    }
                }
            }
        }

        return tsdTeacherDTOEntryList;
    }

    private List<ExecutionSemester> getExecutionPeriodList(TeacherServiceDistribution tsd, Integer executionPeriodId) {
        List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();

        ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);

        if (executionSemester != null) {
            executionPeriodList.add(executionSemester);
        } else {
            executionPeriodList.addAll(tsd.getTSDProcessPhase().getTSDProcess().getExecutionPeriods());
        }

        return executionPeriodList;
    }

    // Service Invokers migrated from Berserk

    private static final ReadTSDTeachersFromTSDProcesses serviceInstance = new ReadTSDTeachersFromTSDProcesses();

    @Service
    public static List<TSDTeacherDTOEntry> runReadTSDTeachersFromTSDProcesses(Map<Integer, Pair<Integer, Integer>> tsdProcessIdMap)
            throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(tsdProcessIdMap);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(tsdProcessIdMap);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    return serviceInstance.run(tsdProcessIdMap);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}
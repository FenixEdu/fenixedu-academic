package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDCourseDTOEntry;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.utl.ist.fenix.tools.util.Pair;

public class ReadTSDCoursesFromTSDProcesses extends Service {
    public List<TSDCourseDTOEntry> run(Map<Integer, Pair<Integer, Integer>> tsdProcessIdMap) {
	List<TSDCourseDTOEntry> tsdCourseDTOEntryList = new ArrayList<TSDCourseDTOEntry>();

	for (Integer tsdProcessPhaseId : tsdProcessIdMap.keySet()) {
	    TeacherServiceDistribution tsd = null;

	    tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdProcessIdMap.get(tsdProcessPhaseId).getKey());

	    List<ExecutionSemester> executionPeriodList = getExecutionPeriodList(tsd, tsdProcessIdMap.get(tsdProcessPhaseId)
		    .getValue());

	    List<TSDCourse> tsdCourseList = new ArrayList<TSDCourse>(tsd
		    .getActiveTSDCourseByExecutionPeriods(executionPeriodList));

	    for (TSDCourse tsdCourse : tsdCourseList) {
		tsdCourseDTOEntryList.add(new TSDCourseDTOEntry(tsdCourse, executionPeriodList));
	    }
	}

	return tsdCourseDTOEntryList;
    }

    private List<ExecutionSemester> getExecutionPeriodList(TeacherServiceDistribution tsd, Integer executionPeriodId) {
	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();

	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);

	if (executionSemester != null) {
	    executionPeriodList.add(executionSemester);
	} else {
	    executionPeriodList.addAll(tsd.getTSDProcessPhase().getTSDProcess().getExecutionPeriods());
	}

	return executionPeriodList;
    }
}

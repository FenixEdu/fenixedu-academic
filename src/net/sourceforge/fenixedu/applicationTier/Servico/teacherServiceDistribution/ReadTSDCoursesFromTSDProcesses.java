package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDCourseDTOEntry;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.utl.fenix.utils.Pair;

public class ReadTSDCoursesFromTSDProcesses extends Service {
	public List<TSDCourseDTOEntry> run(Map<Integer, Pair<Integer, Integer>> tsdProcessIdMap) {
		List<TSDCourseDTOEntry> tsdCourseDTOEntryList = new ArrayList<TSDCourseDTOEntry>();
		
		for(Integer tsdProcessPhaseId: tsdProcessIdMap.keySet()) {
			TeacherServiceDistribution tsd = null;
			
			tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdProcessIdMap.get(tsdProcessPhaseId).getKey());
			
			List<ExecutionPeriod> executionPeriodList = getExecutionPeriodList(tsd, tsdProcessIdMap.get(tsdProcessPhaseId).getValue());
			
			List<TSDCourse> tsdCourseList = new ArrayList<TSDCourse>(tsd.getActiveTSDCourseByExecutionPeriods(executionPeriodList));
			
			for (TSDCourse tsdCourse : tsdCourseList) {
				tsdCourseDTOEntryList.add(new TSDCourseDTOEntry(tsdCourse, executionPeriodList));
			}
		}
		
		return tsdCourseDTOEntryList;
	}

	private List<ExecutionPeriod> getExecutionPeriodList(TeacherServiceDistribution tsd, Integer executionPeriodId) {
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
		
		ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
		
		if(executionPeriod != null) {
			executionPeriodList.add(executionPeriod);
		} else {
			executionPeriodList.addAll(tsd.getTSDProcessPhase().getTSDProcess().getExecutionPeriods());
		}
		
		return executionPeriodList;
	}
}

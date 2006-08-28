package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.CourseValuationDTOEntry;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import pt.ist.utl.fenix.utils.Pair;

public class ReadCourseValuationsFromTeacherServiceDistributions extends Service {
	public List<CourseValuationDTOEntry> run(Map<Integer, Pair<Integer, Integer>> teacherServiceDistributionIdMap) {
		List<CourseValuationDTOEntry> courseValuationDTOEntryList = new ArrayList<CourseValuationDTOEntry>();
		
		for(Integer valuationPhaseId: teacherServiceDistributionIdMap.keySet()) {
			ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
			ValuationGrouping valuationGrouping = null;
			
			valuationGrouping = rootDomainObject.readValuationGroupingByOID(teacherServiceDistributionIdMap.get(valuationPhaseId).getKey());
			
			List<ExecutionPeriod> executionPeriodList = getExecutionPeriodList(valuationGrouping, teacherServiceDistributionIdMap.get(valuationPhaseId).getValue());
			
			List<CourseValuation> courseValuationList = new ArrayList<CourseValuation>();
			for (ExecutionPeriod executionPeriod : executionPeriodList) {
				for (ValuationCompetenceCourse valuationCompetenceCourse : valuationGrouping.getValuationCompetenceCourses()) {
					courseValuationList.addAll(valuationCompetenceCourse.getActiveCourseValuations(valuationPhase, executionPeriod));
				}				
			}

			for (CourseValuation courseValuation : courseValuationList) {
				courseValuationDTOEntryList.add(new CourseValuationDTOEntry(courseValuation, executionPeriodList));
			}
		}
		
		return courseValuationDTOEntryList;
	}

	private List<ExecutionPeriod> getExecutionPeriodList(ValuationGrouping valuationGrouping, Integer executionPeriodId) {
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
		
		ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
		
		if(executionPeriod != null) {
			executionPeriodList.add(executionPeriod);
		} else {
			executionPeriodList.addAll(valuationGrouping.getValuationPhase().getTeacherServiceDistribution().getExecutionPeriods());
		}
		
		return executionPeriodList;
	}
}

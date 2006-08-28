package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationTeacherDTOEntry;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;
import pt.ist.utl.fenix.utils.Pair;

public class ReadValuationTeachersFromTeacherServiceDistributions extends Service {
	public List<ValuationTeacherDTOEntry> run(Map<Integer, Pair<Integer, Integer>> teacherServiceDistributionIdMap) {
		List<ValuationTeacherDTOEntry> valuationTeacherDTOEntryList = new ArrayList<ValuationTeacherDTOEntry>();
		Map<Teacher, ValuationTeacherDTOEntry> teacherDTOMap = new HashMap<Teacher, ValuationTeacherDTOEntry>();
		
		for(Integer valuationPhaseId: teacherServiceDistributionIdMap.keySet()) {
			ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
			ValuationGrouping valuationGrouping = null;
			
			valuationGrouping = rootDomainObject.readValuationGroupingByOID(teacherServiceDistributionIdMap.get(valuationPhaseId).getKey());
			
			List<ExecutionPeriod> executionPeriodList = getExecutionPeriodList(valuationGrouping, teacherServiceDistributionIdMap.get(valuationPhaseId).getValue());
			
			List<ValuationTeacher> valuationTeacherList = new ArrayList<ValuationTeacher>(valuationGrouping.getValuationTeachers());
			for (ValuationTeacher valuationTeacher : valuationTeacherList) {
				if(valuationTeacher.getIsRealTeacher() && teacherDTOMap.containsKey(valuationTeacher.getTeacher())) {
					ValuationTeacherDTOEntry valuationTeacherDTOEntry = teacherDTOMap.get(valuationTeacher.getTeacher());
					valuationTeacherDTOEntry.addExecutionPeriodList(executionPeriodList);
					valuationTeacherDTOEntry.addValuationTeacher(valuationTeacher);
				} else {
					ValuationTeacherDTOEntry valuationTeacherDTOEntry = new ValuationTeacherDTOEntry(valuationTeacher, executionPeriodList); 
					valuationTeacherDTOEntryList.add(valuationTeacherDTOEntry);
					
					if(valuationTeacher.getIsRealTeacher()) {
						teacherDTOMap.put(valuationTeacher.getTeacher(), valuationTeacherDTOEntry);
					}
				}
			}
		}
		
		return valuationTeacherDTOEntryList;
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

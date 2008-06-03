package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDRealTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.utl.ist.fenix.tools.util.Pair;

public class ReadTSDTeachersFromTSDProcesses extends Service {
    public List<TSDTeacherDTOEntry> run(Map<Integer, Pair<Integer, Integer>> tsdProcessIdMap) {
	List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList = new ArrayList<TSDTeacherDTOEntry>();
	Map<Teacher, TSDTeacherDTOEntry> teacherDTOMap = new HashMap<Teacher, TSDTeacherDTOEntry>();

	for (Integer tsdProcessPhaseId : tsdProcessIdMap.keySet()) {
	    TSDProcessPhase tsdProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(tsdProcessPhaseId);
	    TeacherServiceDistribution tsd = null;

	    tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdProcessIdMap.get(tsdProcessPhaseId).getKey());

	    List<ExecutionSemester> executionPeriodList = getExecutionPeriodList(tsd, tsdProcessIdMap.get(tsdProcessPhaseId)
		    .getValue());

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

	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);

	if (executionSemester != null) {
	    executionPeriodList.add(executionSemester);
	} else {
	    executionPeriodList.addAll(tsd.getTSDProcessPhase().getTSDProcess().getExecutionPeriods());
	}

	return executionPeriodList;
    }

}

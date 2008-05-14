package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;

import org.joda.time.YearMonthDay;

public class CopyTSDProcessPhaseService {

    private static CopyTSDProcessPhaseService instance = new CopyTSDProcessPhaseService();

    private CopyTSDProcessPhaseService() {

    }

    public static CopyTSDProcessPhaseService getInstance() {
	return instance;
    }

    public TSDProcess copyTSDProcess(TSDProcess tsdProcessCopied, List<ExecutionSemester> executionPeriodList, String name,
	    Person creator) {
	TSDProcess tsdProcess = new TSDProcess();

	tsdProcess.setDepartment(tsdProcessCopied.getDepartment());
	tsdProcess.setCreator(creator);
	tsdProcess.setName(name);
	tsdProcess.getExecutionPeriods().addAll(executionPeriodList);

	Map<ExecutionSemester, ExecutionSemester> oldAndNewExecutionPeriodMap = getExecutionPeriodTranslationMap(tsdProcessCopied
		.getExecutionPeriods(), executionPeriodList);

	for (TSDProcessPhase oldTSDProcessPhase : tsdProcessCopied.getOrderedTSDProcessPhases()) {
	    createAndCopyProcessPhase(tsdProcess, oldTSDProcessPhase, oldAndNewExecutionPeriodMap, tsdProcess
		    .getLastTSDProcessPhase());
	}

	return tsdProcess;
    }

    private TSDProcessPhase createAndCopyProcessPhase(TSDProcess newTSDProcess, TSDProcessPhase oldTSDProcessPhase,
	    Map<ExecutionSemester, ExecutionSemester> oldAndNewExecutionPeriodMap, TSDProcessPhase lastTSDProcessPhase) {
	TSDProcessPhase newTSDProcessPhase = new TSDProcessPhase();
	newTSDProcessPhase.setTSDProcess(newTSDProcess);
	newTSDProcessPhase.setPreviousTSDProcessPhase(lastTSDProcessPhase);
	if (lastTSDProcessPhase != null)
	    lastTSDProcessPhase.setNextTSDProcessPhase(newTSDProcessPhase);
	newTSDProcessPhase.setNextTSDProcessPhase(null);

	newTSDProcessPhase.setStatus(oldTSDProcessPhase.getStatus());
	newTSDProcessPhase.setName(oldTSDProcessPhase.getName());
	newTSDProcessPhase.setTSDProcess(newTSDProcess);
	newTSDProcessPhase.setIsPublished(false);

	copyDataFromTSDProcessPhase(newTSDProcessPhase, oldTSDProcessPhase, oldAndNewExecutionPeriodMap);

	return newTSDProcessPhase;
    }

    public void copyDataFromTSDProcessPhase(TSDProcessPhase newProcessPhase, TSDProcessPhase oldTSDProcessPhase) {
	Map<ExecutionSemester, ExecutionSemester> oldAndNewExecutionPeriodMap = getExecutionPeriodTranslationMap(oldTSDProcessPhase
		.getTSDProcess().getExecutionPeriods(), newProcessPhase.getTSDProcess().getExecutionPeriods());

	copyDataFromTSDProcessPhase(newProcessPhase, oldTSDProcessPhase, oldAndNewExecutionPeriodMap);
    }

    private Map<ExecutionSemester, ExecutionSemester> getExecutionPeriodTranslationMap(List<ExecutionSemester> oldExecutionPeriodList,
	    List<ExecutionSemester> newExecutionPeriodList) {
	Map<ExecutionSemester, ExecutionSemester> translationMap = new HashMap<ExecutionSemester, ExecutionSemester>();

	for (ExecutionSemester oldExecutionPeriod : oldExecutionPeriodList) {
	    for (ExecutionSemester newExecutionPeriod : newExecutionPeriodList) {
		if (oldExecutionPeriod.getSemester() == newExecutionPeriod.getSemester()) {
		    translationMap.put(oldExecutionPeriod, newExecutionPeriod);
		}
	    }
	}

	return translationMap;
    }

    private void copyDataFromTSDProcessPhase(TSDProcessPhase newProcessPhase, TSDProcessPhase oldTSDProcessPhase,
	    Map<ExecutionSemester, ExecutionSemester> oldAndNewExecutionPeriodMap) {

	newProcessPhase.deleteTSDProcessPhaseData();

	Map<TSDTeacher, TSDTeacher> oldAndNewTSDTeacherMap = createAndCopyFromTSDTeacherList(newProcessPhase, oldTSDProcessPhase
		.getRootTSD().getTSDTeachers());

	Map<TeacherServiceDistribution, TeacherServiceDistribution> oldAndNewTSDMap = new HashMap<TeacherServiceDistribution, TeacherServiceDistribution>();

	createAndCopyFromTeacherServiceDistribution(newProcessPhase, oldTSDProcessPhase.getRootTSD(), null,
		oldAndNewTSDTeacherMap, oldAndNewTSDMap);

	createAndCopyTSDCoursesAndTSDProfessorships(oldTSDProcessPhase, oldAndNewTSDMap, oldAndNewTSDTeacherMap,
		oldAndNewExecutionPeriodMap);

	newProcessPhase.setStudentsPerTheoreticalShift(oldTSDProcessPhase.getStudentsPerTheoreticalShift());
	newProcessPhase.setStudentsPerLaboratorialShift(oldTSDProcessPhase.getStudentsPerLaboratorialShift());
	newProcessPhase.setStudentsPerProblemShift(oldTSDProcessPhase.getStudentsPerProblemShift());
	newProcessPhase.setStudentsPerSeminaryShift(oldTSDProcessPhase.getStudentsPerSeminaryShift());
	newProcessPhase.setStudentsPerFieldWorkShift(oldTSDProcessPhase.getStudentsPerFieldWorkShift());
	newProcessPhase.setStudentsPerTrainingPeriodShift(oldTSDProcessPhase.getStudentsPerTrainingPeriodShift());
	newProcessPhase.setStudentsPerTutDirectionShift(oldTSDProcessPhase.getStudentsPerTutDirectionShift());

	newProcessPhase.setWeightFirstTimeEnrolledStudentsPerTheoShift(oldTSDProcessPhase
		.getWeightFirstTimeEnrolledStudentsPerTheoShift());
	newProcessPhase.setWeightFirstTimeEnrolledStudentsPerLabShift(oldTSDProcessPhase
		.getWeightFirstTimeEnrolledStudentsPerLabShift());
	newProcessPhase.setWeightFirstTimeEnrolledStudentsPerProblemShift(oldTSDProcessPhase
		.getWeightFirstTimeEnrolledStudentsPerProblemShift());
	newProcessPhase.setWeightFirstTimeEnrolledStudentsPerSeminaryShift(oldTSDProcessPhase
		.getWeightFirstTimeEnrolledStudentsPerSeminaryShift());
	newProcessPhase.setWeightFirstTimeEnrolledStudentsPerFieldWorkShift(oldTSDProcessPhase
		.getWeightFirstTimeEnrolledStudentsPerFieldWorkShift());
	newProcessPhase.setWeightFirstTimeEnrolledStudentsPerTrainingShift(oldTSDProcessPhase
		.getWeightFirstTimeEnrolledStudentsPerTrainingShift());
	newProcessPhase.setWeightFirstTimeEnrolledStudentsPerTutDirectionShift(oldTSDProcessPhase
		.getWeightFirstTimeEnrolledStudentsPerTutDirectionShift());
	
	newProcessPhase.setWeightSecondTimeEnrolledStudentsPerTheoShift(oldTSDProcessPhase
		.getWeightSecondTimeEnrolledStudentsPerTheoShift());
	newProcessPhase.setWeightSecondTimeEnrolledStudentsPerLabShift(oldTSDProcessPhase
		.getWeightSecondTimeEnrolledStudentsPerLabShift());
	newProcessPhase.setWeightSecondTimeEnrolledStudentsPerProblemShift(oldTSDProcessPhase
		.getWeightSecondTimeEnrolledStudentsPerProblemShift());
	newProcessPhase.setWeightSecondTimeEnrolledStudentsPerSeminaryShift(oldTSDProcessPhase
		.getWeightSecondTimeEnrolledStudentsPerSeminaryShift());
	newProcessPhase.setWeightSecondTimeEnrolledStudentsPerFieldWorkShift(oldTSDProcessPhase
		.getWeightSecondTimeEnrolledStudentsPerFieldWorkShift());
	newProcessPhase.setWeightSecondTimeEnrolledStudentsPerTrainingShift(oldTSDProcessPhase
		.getWeightSecondTimeEnrolledStudentsPerTrainingShift());
	newProcessPhase.setWeightSecondTimeEnrolledStudentsPerTutDirectionShift(oldTSDProcessPhase
		.getWeightSecondTimeEnrolledStudentsPerTutDirectionShift());
    }

    private void createAndCopyTSDCoursesAndTSDProfessorships(TSDProcessPhase oldTSDProcessPhase,
	    Map<TeacherServiceDistribution, TeacherServiceDistribution> oldAndNewTSDMap,
	    Map<TSDTeacher, TSDTeacher> oldAndNewTSDTeacherMap, Map<ExecutionSemester, ExecutionSemester> oldAndNewExecutionPeriodMap) {

	Map<TSDCourse, TSDCourse> processedTSDCourses = new HashMap<TSDCourse, TSDCourse>();

	for (TeacherServiceDistribution oldTSD : oldAndNewTSDMap.keySet()) {
	    for (ExecutionSemester oldExecutionPeriod : oldAndNewExecutionPeriodMap.keySet()) {
		ExecutionSemester newExecutionPeriod = oldAndNewExecutionPeriodMap.get(oldExecutionPeriod);
		TeacherServiceDistribution newTSD = oldAndNewTSDMap.get(oldTSD);
		TSDCourse newTSDCourse = null;

		if (newTSD != null && newExecutionPeriod != null) {
		    for (TSDCourse oldCourse : oldTSD.getTSDCoursesByExecutionPeriod(newExecutionPeriod)) {
			newTSDCourse = processedTSDCourses.get(oldCourse);

			if (newTSDCourse != null) {
			    newTSD.addTSDCourses(newTSDCourse);
			    continue;
			}

			if (oldCourse instanceof TSDCompetenceCourse
				&& oldCourse.getCompetenceCourse().getCurricularCoursesWithActiveScopesInExecutionPeriod(
					newExecutionPeriod).size() > 0) {
			    newTSDCourse = new TSDCompetenceCourse(newTSD, oldCourse.getCompetenceCourse(), newExecutionPeriod);
			}

			if (oldCourse instanceof TSDCurricularCourse
				&& ((TSDCurricularCourse) oldCourse).getCurricularCourse().hasActiveScopesInExecutionPeriod(
					newExecutionPeriod)) {
			    newTSDCourse = new TSDCurricularCourse(newTSD, ((TSDCurricularCourse) oldCourse)
				    .getCurricularCourse(), newExecutionPeriod);
			}

			if (oldCourse instanceof TSDCurricularCourseGroup) {
			    List<TSDCurricularCourse> coursesList = new ArrayList<TSDCurricularCourse>();

			    for (TSDCurricularCourse oldTSDCurricularCourse : ((TSDCurricularCourseGroup) oldCourse)
				    .getTSDCurricularCourses()) {
				if (oldTSDCurricularCourse.getCurricularCourse().hasActiveScopesInExecutionPeriod(
					newExecutionPeriod)) {
				    TSDCourse newTSDCurricularCourse = processedTSDCourses.get(oldCourse);

				    if (newTSDCurricularCourse == null) {
					newTSDCurricularCourse = new TSDCurricularCourse(newTSD, oldTSDCurricularCourse
						.getCurricularCourse(), newExecutionPeriod);
					fillTSDCourseFromAnotherTSDCourse(oldTSDCurricularCourse, newTSDCurricularCourse);
					createAndCopyTSDProfessorships(oldAndNewTSDTeacherMap, oldTSDCurricularCourse,
						newTSDCurricularCourse);
					processedTSDCourses.put(oldTSDCurricularCourse, newTSDCurricularCourse);
				    }

				    coursesList.add((TSDCurricularCourse) newTSDCurricularCourse);
				}
			    }

			    if (!coursesList.isEmpty()) {
				newTSDCourse = new TSDCurricularCourseGroup(newTSD, coursesList);
			    }
			}

			if (oldCourse instanceof TSDVirtualCourseGroup) {
			    TSDVirtualCourseGroup virtualCourse = (TSDVirtualCourseGroup) oldCourse;

			    newTSDCourse = new TSDVirtualCourseGroup(newTSD, virtualCourse.getName(), newExecutionPeriod,
				    virtualCourse.getLecturedShiftTypes(), virtualCourse.getDegreeCurricularPlans());
			}

			if (newTSDCourse != null) {
			    fillTSDCourseFromAnotherTSDCourse(oldCourse, newTSDCourse);
			    createAndCopyTSDProfessorships(oldAndNewTSDTeacherMap, oldCourse, newTSDCourse);
			    processedTSDCourses.put(oldCourse, newTSDCourse);
			}
		    }
		}
	    }
	}
    }

    private void createAndCopyTSDProfessorships(Map<TSDTeacher, TSDTeacher> oldAndNewTSDTeacherMap, TSDCourse oldTSDCourse,
	    TSDCourse newTSDCourse) {

	for (TSDProfessorship oldTSDProfessorship : oldTSDCourse.getTSDProfessorships()) {
	    TSDTeacher newTSDTeacher = oldAndNewTSDTeacherMap.get(oldTSDProfessorship.getTSDTeacher());

	    if (newTSDTeacher != null && newTSDCourse.getTSDCurricularLoadByShiftType(oldTSDProfessorship.getType()) != null) {
		TSDProfessorship newTSDProfessorship = new TSDProfessorship(newTSDCourse, newTSDTeacher, oldTSDProfessorship
			.getType());

		newTSDProfessorship.setHoursManual(oldTSDProfessorship.getHours());
		newTSDProfessorship.setHoursType(oldTSDProfessorship.getHoursType());
	    }
	}
    }

    private Map<TSDTeacher, TSDTeacher> createAndCopyFromTSDTeacherList(TSDProcessPhase processPhase,
	    List<TSDTeacher> tsdTeacherList) {
	Map<TSDTeacher, TSDTeacher> oldAndNewTSDTeacherMap = new HashMap<TSDTeacher, TSDTeacher>();

	Department thisDepartment = processPhase.getTSDProcess().getDepartment();
	ExecutionSemester firstPeriod = processPhase.getTSDProcess().getFirstExecutionPeriod();
	YearMonthDay beginYMD = firstPeriod.getBeginDateYearMonthDay();
	YearMonthDay endYMD = firstPeriod.getEndDateYearMonthDay();

	for (TSDTeacher tsdTeacher : tsdTeacherList) {
	    TSDTeacher newTSDTeacher = null;

	    if (tsdTeacher instanceof TSDRealTeacher
		    && thisDepartment == ((TSDRealTeacher) tsdTeacher).getTeacher().getLastWorkingDepartment(beginYMD, endYMD)) {
		newTSDTeacher = new TSDRealTeacher(((TSDRealTeacher) tsdTeacher).getTeacher());

	    } else if (tsdTeacher instanceof TSDVirtualTeacher) {
		newTSDTeacher = new TSDVirtualTeacher(tsdTeacher.getCategory(), tsdTeacher.getName(),
			((TSDVirtualTeacher) tsdTeacher).getRequiredHours());
	    }

	    oldAndNewTSDTeacherMap.put(tsdTeacher, newTSDTeacher);
	}

	return oldAndNewTSDTeacherMap;
    }

    /*---------------------- TSD ---------------------*/

    private TeacherServiceDistribution createAndCopyFromTeacherServiceDistribution(TSDProcessPhase newTSDProcessPhase,
	    TeacherServiceDistribution oldTSD, TeacherServiceDistribution fatherTSD,
	    Map<TSDTeacher, TSDTeacher> oldAndNewTSDTeacherMap,
	    Map<TeacherServiceDistribution, TeacherServiceDistribution> oldAndNewTSDMap) {
	List<TSDTeacher> newTSDTeacherList = getNewTSDTeacherListFromMap(oldTSD.getTSDTeachers(), oldAndNewTSDTeacherMap);

	List<TSDCourse> emptyList = new ArrayList<TSDCourse>();

	TeacherServiceDistribution newTeacherServiceDistribution = new TeacherServiceDistribution(newTSDProcessPhase, oldTSD
		.getName(), fatherTSD, newTSDTeacherList, emptyList, null, null, null, null);

	oldAndNewTSDMap.put(oldTSD, newTeacherServiceDistribution);

	for (TeacherServiceDistribution grouping : oldTSD.getChilds()) {
	    createAndCopyFromTeacherServiceDistribution(newTSDProcessPhase, grouping, newTeacherServiceDistribution,
		    oldAndNewTSDTeacherMap, oldAndNewTSDMap);
	}

	return newTeacherServiceDistribution;
    }

    private List<TSDTeacher> getNewTSDTeacherListFromMap(List<TSDTeacher> tsdTeachers,
	    Map<TSDTeacher, TSDTeacher> oldAndNewTSDTeacherMap) {
	List<TSDTeacher> newTSDTeacherList = new ArrayList<TSDTeacher>();

	for (TSDTeacher tsdTeacher : tsdTeachers) {
	    if (oldAndNewTSDTeacherMap.get(tsdTeacher) != null)
		newTSDTeacherList.add(oldAndNewTSDTeacherMap.get(tsdTeacher));
	}

	return newTSDTeacherList;
    }

    /*---------------------- TSD Courses---------------------*/

    private void fillTSDCourseFromAnotherTSDCourse(TSDCourse tsdCourse, TSDCourse newTSDCourse) {

	TSDValueType valueType = TSDValueType.MANUAL_VALUE;

	newTSDCourse.setFirstTimeEnrolledStudentsManual(tsdCourse.getFirstTimeEnrolledStudents());
	newTSDCourse.setFirstTimeEnrolledStudentsType(valueType);
	newTSDCourse.setSecondTimeEnrolledStudentsManual(tsdCourse.getSecondTimeEnrolledStudents());
	newTSDCourse.setSecondTimeEnrolledStudentsType(valueType);
	newTSDCourse.setIsActive(tsdCourse.getIsActive());

	TSDCurricularLoad newTSDLoad = null;

	for (TSDCurricularLoad tsdLoad : tsdCourse.getTSDCurricularLoads()) {

	    newTSDLoad = newTSDCourse.getTSDCurricularLoadByShiftType(tsdLoad.getType());

	    if (newTSDLoad != null) {
		newTSDLoad.setHoursManual(tsdLoad.getHours());
		newTSDLoad.setHoursType(valueType);
		newTSDLoad.setStudentsPerShiftManual(tsdLoad.getStudentsPerShift());
		newTSDLoad.setStudentsPerShiftType(valueType);
		newTSDLoad.setWeightFirstTimeEnrolledStudentsPerShiftManual(tsdLoad.getWeightFirstTimeEnrolledStudentsPerShift());
		newTSDLoad.setWeightFirstTimeEnrolledStudentsPerShiftType(valueType);
		newTSDLoad.setWeightSecondTimeEnrolledStudentsPerShiftManual(tsdLoad
			.getWeightSecondTimeEnrolledStudentsPerShift());
		newTSDLoad.setWeightSecondTimeEnrolledStudentsPerShiftType(valueType);
	    }
	}

    }
}
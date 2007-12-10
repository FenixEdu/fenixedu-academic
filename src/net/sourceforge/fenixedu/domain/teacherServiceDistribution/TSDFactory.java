package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class TSDFactory {
	
	private static TSDFactory instance = new TSDFactory();

    private TSDFactory() {
	
    }
    
    public static TSDFactory getInstance() {
        return instance;
    }

    
    public void createTSDTreeStructure(TSDProcessPhase tsdPhase) {
    	createRootTSD(tsdPhase);
    	createSubGroupings(tsdPhase);
    }

    private TeacherServiceDistribution createRootTSD(TSDProcessPhase tsdPhase) {
    	
    	TSDProcess tsdProcess= tsdPhase.getTSDProcess();
    	
    	List<TSDTeacher> tsdTeachers = createTSDTeacher(tsdProcess.getDepartment(), tsdProcess.getExecutionPeriods());
    	
    	List<TSDCourse> tsdCourses = createTSDCompetenceCourses(tsdProcess);

    	return new TeacherServiceDistribution(tsdPhase, tsdProcess.getDepartment().getAcronym(), null,
    			tsdTeachers, tsdCourses, null, null);
    }

    private void createSubGroupings(TSDProcessPhase tsdPhase) {
		HashMap<Unit, TeacherServiceDistribution> unitTeachersNewGrouping = new HashMap<Unit, TeacherServiceDistribution>();
		TeacherServiceDistribution rootTSD = tsdPhase.getRootTSD();
	
		for (TSDTeacher tsdTeacher : rootTSD.getTSDTeachers()) {
		    Teacher teacher = ((TSDRealTeacher)tsdTeacher).getTeacher();
		    if (teacher.getCurrentWorkingUnit() != null) {
			createChildUnitsGroupingsForTeachers(tsdPhase, tsdTeacher, teacher.getCurrentWorkingUnit(),
				null, rootTSD, unitTeachersNewGrouping);
		    }
		}
	
		HashMap<Unit, TeacherServiceDistribution> unitCoursesNewGrouping = new HashMap<Unit, TeacherServiceDistribution>();
	
		for (TSDCourse course : rootTSD.getTSDCourses()) {
		    if (course.getCompetenceCourse().getCompetenceCourseGroupUnit() != null) {
		    	createChildCompetenceCourseGroupUnits(tsdPhase, course, rootTSD,
		    		unitCoursesNewGrouping);
		    }
		}
	
		HashMap<Unit, TeacherServiceDistribution> unitScientificAreaGrouping = new HashMap<Unit, TeacherServiceDistribution>();
	
		for (Unit courseUnit : unitCoursesNewGrouping.keySet()) {
		    Iterator<Unit> iter = courseUnit.getParentUnits().iterator();
		    Unit scientificAreaUnit = (!iter.hasNext()) ? null : iter.next();
		    TeacherServiceDistribution scientificAreaGrouping = unitScientificAreaGrouping
			    .get(scientificAreaUnit);
		    TeacherServiceDistribution teachersNewGrouping = unitTeachersNewGrouping.get(scientificAreaUnit);
		    TeacherServiceDistribution coursesNewGrouping = unitCoursesNewGrouping.get(courseUnit);
	
		    if (scientificAreaGrouping == null) {
				if (teachersNewGrouping == null) {
				    scientificAreaGrouping = new TeacherServiceDistribution(tsdPhase, scientificAreaUnit.getName(),
					    rootTSD, new ArrayList<TSDTeacher>(),
					    new ArrayList<TSDCourse>(), rootTSD
						    .getCoursesAndTeachersValuationManagers(), rootTSD
						    .getCoursesAndTeachersManagementGroup());
				} else {
				    scientificAreaGrouping = teachersNewGrouping;
				}
	
				unitScientificAreaGrouping.put(scientificAreaUnit, scientificAreaGrouping);
		    }
		    
		    scientificAreaGrouping.getTSDCoursesSet().addAll(coursesNewGrouping.getTSDCourses());
		    		    
		    coursesNewGrouping.setParent(scientificAreaGrouping);
		}
    }

    private void createChildUnitsGroupingsForTeachers(TSDProcessPhase tsdPhase, TSDTeacher tsdTeacher, Unit unit, 
    		TeacherServiceDistribution childTSD, TeacherServiceDistribution rootTSD, HashMap<Unit, TeacherServiceDistribution> unitNewTSD) {

		if (unit.isDepartmentUnit()) {
		    if (childTSD != null) {
				childTSD.setParent(rootTSD);
		    }
		    return;
		}
	
		TeacherServiceDistribution newTSD = unitNewTSD.get(unit);
	
		if (newTSD == null) {
		    List<TSDTeacher> teachersList = new ArrayList<TSDTeacher>();
		    teachersList.add(tsdTeacher);
		    List<TSDCourse> emptyCourseList = new ArrayList<TSDCourse>();
	
		    newTSD = new TeacherServiceDistribution(tsdPhase, unit.getName(), null, teachersList, 
		    		emptyCourseList, null, null);
		    
		    unitNewTSD.put(unit, newTSD);
	
		    if (childTSD != null) {
		    	childTSD.setParent(newTSD);
		    }
	
		    for (Unit parentUnit : unit.getParentUnitsByOrganizationalStructureAccountabilityType()) {
				for (Unit grandParentUnit : parentUnit.getParentUnitsByOrganizationalStructureAccountabilityType()) {
				    if (!grandParentUnit.getParentUnitsByOrganizationalStructureAccountabilityType().isEmpty()) {
				    	createChildUnitsGroupingsForTeachers(tsdPhase, tsdTeacher, parentUnit, newTSD, rootTSD, unitNewTSD);
				    }
				}
		    }
		} else {
		    newTSD.addTSDTeachers(tsdTeacher);
	
		    for (TeacherServiceDistribution parentGrouping = newTSD.getParent(); parentGrouping != null
			    && parentGrouping != rootTSD; parentGrouping = parentGrouping.getParent()) {
			parentGrouping.addTSDTeachers(tsdTeacher);
		    }
		}
    }

    private void createChildCompetenceCourseGroupUnits(TSDProcessPhase tsdPhase, TSDCourse tsdCourse, 
    		TeacherServiceDistribution rootTSD, HashMap<Unit, TeacherServiceDistribution> unitNewGrouping) {

    	CompetenceCourse competenceCourse = tsdCourse.getCompetenceCourse();
		TeacherServiceDistribution newGrouping = unitNewGrouping.get(competenceCourse.getCompetenceCourseGroupUnit());
	
		if (newGrouping == null) {
		    List<TSDCourse> courseList = new ArrayList<TSDCourse>();
		    courseList.add(new TSDCompetenceCourse(competenceCourse, tsdCourse.getExecutionPeriod()));
		    List<TSDTeacher> emptyTSDTeacherList = new ArrayList<TSDTeacher>();
	
		    newGrouping = new TeacherServiceDistribution(tsdPhase, competenceCourse.getCompetenceCourseGroupUnit()
			    .getName(), rootTSD, emptyTSDTeacherList, courseList, null, null);
		    unitNewGrouping.put(competenceCourse.getCompetenceCourseGroupUnit(), newGrouping);
	
		} else {
		    newGrouping.addTSDCourses(tsdCourse);
		}
    }

    private List<TSDCourse> createTSDCompetenceCourses(TSDProcess tsdProcess) {
		List<TSDCourse> courseList = new ArrayList<TSDCourse>();
		List<ExecutionPeriod> periods = tsdProcess.getExecutionPeriods();
		
		for (CompetenceCourse competenceCourse : tsdProcess.getAllCompetenceCourses()) {
			courseList.addAll(createTSDCompetenceCourses(competenceCourse, periods));
		}
	
		return courseList; 
    }
    
    private List<TSDCourse> createTSDCompetenceCourses(CompetenceCourse course, List<ExecutionPeriod> periods) {
		List<TSDCourse> courseList = new ArrayList<TSDCourse>();
		
		for(ExecutionPeriod period : periods){
			if(course.getCurricularCoursesWithActiveScopesInExecutionPeriod(period).size() > 0)
				courseList.add(new TSDCompetenceCourse(course, period));
		}
	
		return courseList; 
    }

    private List<TSDTeacher> createTSDTeacher(Department department, List<ExecutionPeriod> executionPeriodList) {
	    	
		List<TSDTeacher> tsdTeacherList = new ArrayList<TSDTeacher>();
		Set<Teacher> teacherSet = new HashSet<Teacher>();
	
		for (ExecutionPeriod executionPeriod : executionPeriodList) {
		    teacherSet.addAll(department.getAllTeachers(executionPeriod.getBeginDateYearMonthDay(),
			    executionPeriod.getEndDateYearMonthDay()));
		}
	
		for (Teacher teacher : teacherSet) {
		    tsdTeacherList.add(new TSDRealTeacher(teacher));
		}
	
		return tsdTeacherList;
    }

}
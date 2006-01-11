package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CompetenceCourse extends CompetenceCourse_Base {
    
    private CompetenceCourseInformation recentCompetenceCourseInformation;
    
    protected CompetenceCourse() {
        super();        
    }
    
    public CompetenceCourse(String code, String name, Collection<Department> departments, CurricularStage curricularStage) {
    	this();
        setCurricularStage(curricularStage);
        fillFields(code, name);
        if(departments != null) {
        	addDepartments(departments);
        }
    }
    
    
    public CompetenceCourse(String name, String nameEn, String acronym, Boolean basic, 
            RegimeType regimeType, CurricularStage curricularStage, Unit unit) {           
        this();
        setCurricularStage(curricularStage);
        setUnit(unit);
        addCompetenceCourseInformations(new CompetenceCourseInformation(name, nameEn, acronym, basic, regimeType, null));
    }
    
    public void addCompetenceCourseLoad(Double theoreticalHours, Double problemsHours,
            Double laboratorialHours, Double seminaryHours, Double fieldWorkHours,
            Double trainingPeriodHours, Double tutorialOrientationHours, Double autonomousWorkHours,
            Double ectsCredits, Integer order) {

        getRecentCompetenceCourseInformation().addCompetenceCourseLoads(
                new CompetenceCourseLoad(theoreticalHours, problemsHours, laboratorialHours,
                        seminaryHours, fieldWorkHours, trainingPeriodHours, tutorialOrientationHours,
                        autonomousWorkHours, ectsCredits, order));
    }
    
    private void fillFields(String code, String name) {
    	if(code == null || code.length() == 0) {
			throw new DomainException("invalid.competenceCourse.values");
		}
		if(name == null || name.length() == 0) {
			throw new DomainException("invalid.competenceCourse.values");
		}		
        setCode(code);
        setName(name);
	}
    
    public void edit(String code, String name, Collection<Department> departments) {
    	fillFields(code, name);
    	for (final Department department : this.getDepartments()) {
			if(!departments.contains(department)) {
				removeDepartments(department);
			}
		}
    	for (final Department department : departments) {
			if(!hasDepartments(department)) {
				addDepartments(department);
			}
		}
    }

    public void edit(String name, String nameEn, String acronym, Boolean basic, CurricularStage curricularStage) {
        checkIfCanEdit();
        if (curricularStage.equals(CurricularStage.APPROVED)) {
            setCreationDate(Calendar.getInstance().getTime());
        }
        setCurricularStage(curricularStage);
        getRecentCompetenceCourseInformation().edit(name, nameEn, acronym, basic);
    }

    private void checkIfCanEdit() {
        if (this.getCurricularStage().equals(CurricularStage.APPROVED)) {
            throw new DomainException("competenceCourse.approved");
        }
    }

    public void edit(String objectives, String program, String evaluationMethod, String objectivesEn,
            String programEn, String evaluationMethodEn) {
        getRecentCompetenceCourseInformation().edit(objectives, program, evaluationMethod, objectivesEn,
                programEn, evaluationMethodEn);
    }

	public void delete() {
        if (hasAnyAssociatedCurricularCourses()) {
            throw new DomainException("mustDeleteCurricularCoursesFirst");
        }
		getDepartments().clear();
        removeUnit();
        for(;!getCompetenceCourseInformations().isEmpty(); getCompetenceCourseInformations().get(0).delete());        
    	super.deleteDomainObject();
    }
    
    public void addCurricularCourses(Collection<CurricularCourse> curricularCourses) {
    	for (CurricularCourse curricularCourse : curricularCourses) {
    		addAssociatedCurricularCourses(curricularCourse);
		}
    }
    
    public void addDepartments(Collection<Department> departments) {
    	for (Department department : departments) {
			addDepartments(department);
		}
    }
    
    private CompetenceCourseInformation getRecentCompetenceCourseInformation() {
        return (recentCompetenceCourseInformation != null) ? recentCompetenceCourseInformation :
            (recentCompetenceCourseInformation = findRecentCompetenceCourseInformation()); 
    }
    
    private CompetenceCourseInformation findRecentCompetenceCourseInformation() {
        for (final CompetenceCourseInformation competenceCourseInformation : getCompetenceCourseInformations()) {
            if (competenceCourseInformation.getEndDate() == null) { // endDate not defined: most recent information
                return competenceCourseInformation;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        if ((super.getName() == null || super.getName().length() == 0) && getRecentCompetenceCourseInformation() != null) {
            return getRecentCompetenceCourseInformation().getName();
        }
        return super.getName();
    }

    public String getNameEn() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getNameEn() : null;
    }
    
    public String getAcronym() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getAcronym() : null;
    }
    
    public boolean isBasic() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getBasic().booleanValue() : false;
    }
    
    public RegimeType getRegime() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getRegime() : null;
    }
    
    public void setRegime(RegimeType regimeType) {
        getRecentCompetenceCourseInformation().setRegime(regimeType);
    }
    
    public List<CompetenceCourseLoad> getCompetenceCourseLoads() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getCompetenceCourseLoads() : null;
    }
    
    public String getObjectives() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getObjectives() : null;
    }

    public String getProgram() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getProgram() : null;
    }

    public String getEvaluationMethod() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getEvaluationMethod() : null;
    }

    public String getObjectivesEn() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getObjectivesEn() : null;
    }

    public String getProgramEn() {
        return getRecentCompetenceCourseInformation().getProgramEn();
    }

    public String getEvaluationMethodEn() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getEvaluationMethodEn() : null;
    }
    
    public Map<Degree, List<CurricularCourse>> getAssociatedCurricularCoursesGroupedByDegree() {
        Map<Degree, List<CurricularCourse>> curricularCoursesMap = new HashMap<Degree, List<CurricularCourse>>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            Degree degree = curricularCourse.getDegreeCurricularPlan().getDegree();
            List<CurricularCourse> curricularCourses = curricularCoursesMap.get(degree);
            if (curricularCourses == null) {
                curricularCourses = new ArrayList<CurricularCourse>();
                curricularCoursesMap.put(degree, curricularCourses);
            }
            curricularCourses.add(curricularCourse);
        }
        return curricularCoursesMap;
    }

    public List<EnrolmentEvaluation> getActiveEnrollmentEvaluations(ExecutionYear executionYear) {
        List<EnrolmentEvaluation> results = new ArrayList<EnrolmentEvaluation>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            results.addAll(curricularCourse.getActiveEnrollmentEvaluations(executionYear));
        }
        return results;
    }
    
    public Boolean hasActiveScopesInExecutionYear(ExecutionYear executionYear) {
        List<ExecutionPeriod> executionPeriods = executionYear.getExecutionPeriods();
        List<CurricularCourse> curricularCourses = this.getAssociatedCurricularCourses();       
        for (ExecutionPeriod executionPeriod : executionPeriods) {
            for (CurricularCourse curricularCourse : curricularCourses) {
                if (curricularCourse.getActiveScopesInExecutionPeriod(executionPeriod).size() > 0) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    
    public Unit getDepartmentUnit() {
        final List<Unit> departmentUnits = this.getUnit().getTopUnits(); 
        return (departmentUnits.isEmpty()) ? null : this.getUnit().getTopUnits().get(0);
    }
}

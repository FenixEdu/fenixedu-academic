package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.ICompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.ICompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;

public class CompetenceCourse extends CompetenceCourse_Base {
    
    private ICompetenceCourseInformation recentCompetenceCourseInformation;
    
    protected CompetenceCourse() {
        super();        
    }
    
    public CompetenceCourse(String code, String name, Collection<IDepartment> departments, CurricularStage curricularStage) {
    	this();
        setCurricularStage(curricularStage);
        fillFields(code, name);
        if(departments != null) {
        	addDepartments(departments);
        }
    }
    
    
    public CompetenceCourse(String name, String nameEn, String acronym, Boolean basic, 
            RegimeType regimeType, CurricularStage curricularStage, IUnit unit) {           
        this();
        setCurricularStage(curricularStage);
        setUnit(unit);
        addCompetenceCourseInformations(new CompetenceCourseInformation(name, nameEn, acronym, basic, regimeType, null));
    }
    
    public void addCompetenceCourseLoad(Double theoreticalHours, Double problemsHours,
            Double laboratorialHours, Double seminaryHours, Double fieldWorkHours,
            Double trainingPeriodHours, Double tutorialOrientationHours, Double autonomousWorkHours,
            Double ectsCredits) {

        getRecentCompetenceCourseInformation().addCompetenceCourseLoads(
                new CompetenceCourseLoad(theoreticalHours, problemsHours, laboratorialHours,
                        seminaryHours, fieldWorkHours, trainingPeriodHours, tutorialOrientationHours,
                        autonomousWorkHours, ectsCredits));
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
    
    public void edit(String code, String name, Collection<IDepartment> departments) {
    	fillFields(code, name);
    	for (final IDepartment department : this.getDepartments()) {
			if(!departments.contains(department)) {
				removeDepartments(department);
			}
		}
    	for (final IDepartment department : departments) {
			if(!hasDepartments(department)) {
				addDepartments(department);
			}
		}
    }

    public void edit(String name, String nameEn, String acronym, Boolean basic, CurricularStage curricularStage) {
        setCurricularStage(curricularStage);
        getRecentCompetenceCourseInformation().edit(name, nameEn, acronym, basic);
    }

    public void edit(String objectives, String program, String evaluationMethod, String objectivesEn,
            String programEn, String evaluationMethodEn) {
        getRecentCompetenceCourseInformation().edit(objectives, program, evaluationMethod, objectivesEn,
                programEn, evaluationMethodEn);
    }

	public void delete() {
        if (hasAnyAssociatedCurricularCourses()) {
            throw new DomainException("error.mustDeleteCurricularCoursesFirst");
        }
		getDepartments().clear();
        removeUnit();
        for(;!getCompetenceCourseInformations().isEmpty(); getCompetenceCourseInformations().get(0).delete());        
    	super.deleteDomainObject();
    }
    
    public void addCurricularCourses(Collection<ICurricularCourse> curricularCourses) {
    	for (ICurricularCourse curricularCourse : curricularCourses) {
    		addAssociatedCurricularCourses(curricularCourse);
		}
    }
    
    public void addDepartments(Collection<IDepartment> departments) {
    	for (IDepartment department : departments) {
			addDepartments(department);
		}
    }
    
    private ICompetenceCourseInformation getRecentCompetenceCourseInformation() {
        return (recentCompetenceCourseInformation != null) ? recentCompetenceCourseInformation :
            (recentCompetenceCourseInformation = findRecentCompetenceCourseInformation()); 
    }
    
    // TODO: Check this method!!!
    private ICompetenceCourseInformation findRecentCompetenceCourseInformation() {
        for (final ICompetenceCourseInformation competenceCourseInformation : getCompetenceCourseInformations()) {
            if (competenceCourseInformation.getEndDate() == null) { // endDate not defined: most recent information
                return competenceCourseInformation;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        if (super.getName() == null || super.getName().length() == 0) {
            return getRecentCompetenceCourseInformation().getName();
        }
        return super.getName();
    }

    public String getNameEn() {
        return getRecentCompetenceCourseInformation().getNameEn();
    }
    
    public String getAcronym() {
        return getRecentCompetenceCourseInformation().getAcronym();
    }
    
    public boolean isBasic() {
        return getRecentCompetenceCourseInformation().getBasic().booleanValue();
    }
    
    public RegimeType getRegime() {
        return getRecentCompetenceCourseInformation().getRegime();
    }
    
    public void setRegime(RegimeType regimeType) {
        getRecentCompetenceCourseInformation().setRegime(regimeType);
    }
    
    public List<ICompetenceCourseLoad> getCompetenceCourseLoads() {
        return getRecentCompetenceCourseInformation().getCompetenceCourseLoads();
    }
    
    public String getObjectives() {
        return getRecentCompetenceCourseInformation().getObjectives();
    }

    public String getProgram() {
        return getRecentCompetenceCourseInformation().getProgram();
    }

    public String getEvaluationMethod() {
        return getRecentCompetenceCourseInformation().getEvaluationMethod();
    }

    public String getObjectivesEn() {
        return getRecentCompetenceCourseInformation().getObjectivesEn();
    }

    public String getProgramEn() {
        return getRecentCompetenceCourseInformation().getProgramEn();
    }

    public String getEvaluationMethodEn() {
        return getRecentCompetenceCourseInformation().getEvaluationMethodEn();
    }
    
    public Map<IDegree, List<ICurricularCourse>> getAssociatedCurricularCoursesGroupedByDegree() {
        Map<IDegree, List<ICurricularCourse>> curricularCoursesMap = new HashMap<IDegree, List<ICurricularCourse>>();
        for (ICurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            IDegree degree = curricularCourse.getDegreeCurricularPlan().getDegree();
            List<ICurricularCourse> curricularCourses = curricularCoursesMap.get(degree);
            if (curricularCourses == null) {
                curricularCourses = new ArrayList<ICurricularCourse>();
                curricularCoursesMap.put(degree, curricularCourses);
            }
            curricularCourses.add(curricularCourse);
        }
        return curricularCoursesMap;
    }

    public List<IEnrolmentEvaluation> getActiveEnrollmentEvaluations(IExecutionYear executionYear) {
        List<IEnrolmentEvaluation> results = new ArrayList<IEnrolmentEvaluation>();
        for (ICurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            results.addAll(curricularCourse.getActiveEnrollmentEvaluations(executionYear));
        }
        return results;
    }
    
    public Boolean hasActiveScopesInExecutionYear(IExecutionYear executionYear) {
        List<IExecutionPeriod> executionPeriods = executionYear.getExecutionPeriods();
        List<ICurricularCourse> curricularCourses = this.getAssociatedCurricularCourses();       
        for (IExecutionPeriod executionPeriod : executionPeriods) {
            for (ICurricularCourse curricularCourse : curricularCourses) {
                if (curricularCourse.getActiveScopesInExecutionPeriod(executionPeriod).size() > 0) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    
    public IUnit getDepartmentUnit() {
        final List<IUnit> departmentUnits = this.getUnit().getTopUnits(); 
        return (departmentUnits.isEmpty()) ? null : this.getUnit().getTopUnits().get(0);
    }
}

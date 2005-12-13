package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.ICompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;

public class CompetenceCourse extends CompetenceCourse_Base {
    
    private ICompetenceCourseInformation recentCompetenceCourseInformation;
    
    public CompetenceCourse(String code, String name, Collection<IDepartment> departments, CurricularStage curricularStage) {
    	super();
        setCurricularStage(curricularStage);
        fillFields(code, name);
        if(departments != null) {
        	addDepartments(departments);
        }
    }
    
    public CompetenceCourse(String name, String code, Double ectsCredits, Boolean basic,
            Double theoreticalHours, Double problemsHours, Double labHours, Double projectHours,
            Double seminaryHours, RegimeType regime, CurricularStage curricularStage, IUnit unit, IExecutionYear executionYear) {
        
        super();
        setCurricularStage(curricularStage);
        setUnit(unit);
        new CompetenceCourseInformation(name, code, ectsCredits, basic, theoreticalHours, problemsHours,
                labHours, projectHours, seminaryHours, regime, this, executionYear);
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
    	for (IDepartment department : getDepartments()) {
			if(!departments.contains(department)) {
				removeDepartments(department);
			}
		}
    	for (IDepartment department : departments) {
			if(!hasDepartments(department)) {
				addDepartments(department);
			}
		}
    }
    
    public void edit(String name, String code, Double ectsCredits, Boolean basic,
            Double theoreticalHours, Double problemsHours, Double labHours, Double projectHours,
            Double seminaryHours, RegimeType regime, CurricularStage curricularStage) {
        
        setCurricularStage(curricularStage);
        ICompetenceCourseInformation competenceCourseInformation = getRecentCompetenceCourseInformation();
        competenceCourseInformation.edit(name, code, ectsCredits, basic, theoreticalHours, problemsHours,
                labHours, projectHours, seminaryHours, regime);
    }

	public void delete() {
        if (hasAnyAssociatedCurricularCourses()) {
            throw new DomainException("error.mustdeleteCurricularCoursesFirst");
        }
		getDepartments().clear();
        getUnit().removeCompetenceCourses(this);
        for(;!getCourseInformations().isEmpty(); getCourseInformations().get(0).delete());        
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
        return recentCompetenceCourseInformation != null ? recentCompetenceCourseInformation :
            (recentCompetenceCourseInformation = findRecentCompetenceCourseInformation()); 
    }
    
    // TODO: Check this method!!!
    private ICompetenceCourseInformation findRecentCompetenceCourseInformation() {
        Date now = Calendar.getInstance().getTime();
        for (ICompetenceCourseInformation courseInformation : getCourseInformations()) {            
            if (now.before(courseInformation.getExecutionYear().getBeginDate()) || 
                (now.after(courseInformation.getExecutionYear().getBeginDate()) &&
                 now.before(courseInformation.getExecutionYear().getEndDate()))) {
                return courseInformation;
            }
        }
        throw new DomainException("invalid.competenceCourseInformation");
    }

    @Override
    public String getName() {
        if (super.getName() == null || super.getName().length() == 0) {
            return getRecentCompetenceCourseInformation().getName();
        }
        return super.getName();
    }
    
    @Override
    public String getCode() {
        if (super.getCode() == null || super.getCode().length() == 0) {
            return getRecentCompetenceCourseInformation().getCode();
        }
        return super.getCode();
    }
    
    public Double getEctsCredits() {
        return getRecentCompetenceCourseInformation().getEctsCredits();
    }
    
    public boolean isBasic() {
        return getRecentCompetenceCourseInformation().getBasic().booleanValue();
    }

    public Double getTheoreticalHours() {
        return getRecentCompetenceCourseInformation().getTheoreticalHours();
    }
    
    public Double getProblemsHours() {
        return getRecentCompetenceCourseInformation().getProblemsHours();
    }
    
    public Double getLaboratorialHours() {
        return getRecentCompetenceCourseInformation().getLabHours();
    }
    
    public Double getProjectHours() {
        return getRecentCompetenceCourseInformation().getProjectHours();
    }
    
    public Double getSeminaryHours() {
        return getRecentCompetenceCourseInformation().getSeminaryHours();
    }
    
    public RegimeType getRegime() {
        return getRecentCompetenceCourseInformation().getRegime();
    }
    
    public int getTotalLessonHours() {
        return getRecentCompetenceCourseInformation().getTotalLessonHours();
    }
}

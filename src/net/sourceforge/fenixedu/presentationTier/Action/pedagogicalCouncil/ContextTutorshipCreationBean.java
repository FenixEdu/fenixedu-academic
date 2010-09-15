package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * Class ContextTutorshipCreationBean.java
 * 
 * @author jaime created on Aug 4, 2010
 */

public class ContextTutorshipCreationBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private ExecutionDegree degree;
    private ExecutionSemester executionSemester;
    private ExecutionCourse executionCourse;
    private SchoolClass schoolClass;

    public static class ContextDegreesProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object arg1) {
	    final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
	    final ContextTutorshipCreationBean bean = (ContextTutorshipCreationBean) source;
	    final ExecutionSemester executionPeriod = bean.getExecutionSemester();
	    if (executionPeriod != null) {
		final ExecutionYear executionYear = executionPeriod.getExecutionYear();
		executionDegrees.addAll(executionYear.getExecutionDegreesSet());
	    }
	    Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
	    return executionDegrees;
	}
    }

    public static class SchoolClassesProvider implements DataProvider {
	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object current) {
	    ContextTutorshipCreationBean bean = (ContextTutorshipCreationBean) source;
	    ExecutionDegree executionDegree = bean.getExecutionDegree();
	    ExecutionSemester executionSemester = bean.getExecutionSemester();
	    if (executionDegree != null && executionSemester != null) {
		Set<SchoolClass> classes = new HashSet<SchoolClass>();
		for (SchoolClass schoolClass : executionDegree.getSchoolClassesSet()) {
		    if (executionSemester.getAcademicInterval().equals(schoolClass.getAcademicInterval())) {
			classes.add(schoolClass);
		    }
		}
		return classes;
	    } else {
		Set<SchoolClass> classes = new HashSet<SchoolClass>();
		return classes;
	    }
	}
    }

    public ExecutionSemester getExecutionSemester() {
	return executionSemester;
    }

    public ExecutionDegree getExecutionDegree() {
	return degree;
    }

    public void setExecutionDegree(ExecutionDegree degree) {
	this.degree = degree;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = executionCourse;
    }

    public SchoolClass getSchoolClass() {
	return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
	this.schoolClass = schoolClass;
    }

    /**
     * Code functionality copy to fill specific purpose of getting execution
     * courses in a given Year and Semester
     * 
     * @param degree
     * @param curricularYear
     * @param executionSemester
     * @return
     */
    public static List<ExecutionCourse> getExecutionCourses(final Degree degree, final ExecutionYear execYear,
	    final ExecutionSemester executionSemester) {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
	    for (final CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
		    if (executionSemester == executionCourse.getExecutionPeriod()) {
			for (final DegreeModuleScope scope : course.getDegreeModuleScopes()) {
			    // #### / ####
			    // Not so good looking hack
			    // TODO: Recode
			    String fields[] = execYear.getYear().split("/");
			    if (scope.isActiveForExecutionPeriod(executionSemester) && scope.isActiveForExecutionYear(execYear)
				    && scope.getCurricularSemester() == executionSemester.getSemester()) {
				result.add(executionCourse);
			    }
			}
		    }
		}
	    }
	}
	return result;
    }
}

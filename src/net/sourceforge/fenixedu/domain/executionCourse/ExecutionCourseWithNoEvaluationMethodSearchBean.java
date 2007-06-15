package net.sourceforge.fenixedu.domain.executionCourse;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class ExecutionCourseWithNoEvaluationMethodSearchBean implements Serializable {

    private DomainReference<ExecutionPeriod> executionPeriod;

    private List<DegreeType> degreeTypes;

    private int withEvaluationMethod = 0;

    private int withoutEvaluationMethod = 0;

    private int total = 0;

    public ExecutionCourseWithNoEvaluationMethodSearchBean() {
	super();
    }

    public Set getSearchResult() {
	withEvaluationMethod = 0;
	withoutEvaluationMethod = 0;
	total = 0;

	final ExecutionPeriod executionPeriod = getExecutionPeriod();
	final List<DegreeType> degreeTypes = getDegreeTypes();
	if (executionPeriod == null || degreeTypes == null) {
	    return null;
	}
	final Set<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
	for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCoursesSet()) {
	    if (isLecturedIn(executionCourse, degreeTypes)) {
		total++;
		if (hasNoEvaluationMethod(executionCourse)) {
		    withoutEvaluationMethod++;
		    executionCourses.add(executionCourse);
		} else {
		    withEvaluationMethod++;
		}
	    }
	}
	return executionCourses;
    }

    private boolean hasNoEvaluationMethod(final ExecutionCourse executionCourse) {
	final EvaluationMethod evaluationMethod = executionCourse.getEvaluationMethod();
	return evaluationMethod == null || evaluationMethod.getEvaluationElements() == null
		|| evaluationMethod.getEvaluationElements().getContent() == null
		|| evaluationMethod.getEvaluationElements().getContent().length() == 0;
    }

    private boolean isLecturedIn(final ExecutionCourse executionCourse, final List<DegreeType> degreeTypes) {
	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
	    final Degree degree = curricularCourse.getDegree();
	    final DegreeType degreeType = degree.getDegreeType();
	    if (degreeTypes.contains(degreeType)) {
		return true;
	    }
	}
	return false;
    }

    public List<DegreeType> getDegreeTypes() {
        return degreeTypes;
    }

    public void setDegreeTypes(List<DegreeType> degreeTypes) {
        this.degreeTypes = degreeTypes;
    }

    public ExecutionPeriod getExecutionPeriod() {
        return executionPeriod == null ? null : executionPeriod.getObject();
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod == null ? null : new DomainReference<ExecutionPeriod>(executionPeriod);
    }

    public int getTotal() {
        return total;
    }

    public int getWithEvaluationMethod() {
        return withEvaluationMethod;
    }

    public int getWithoutEvaluationMethod() {
        return withoutEvaluationMethod;
    }

}

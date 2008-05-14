package net.sourceforge.fenixedu.domain.messaging;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionSemester;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class BoardSearchBean implements Serializable, HasExecutionSemester {

    private Boolean searchExecutionCourseBoards;

    private DomainReference<Unit> unit;

    private DomainReference<ExecutionSemester> executionSemester;

    private DomainReference<ExecutionDegree> executionDegree;

    public BoardSearchBean() {
        super();
    }

    public Boolean getSearchExecutionCourseBoards() {
        return searchExecutionCourseBoards;
    }

    public void setSearchExecutionCourseBoards(Boolean searchExecutionCourseBoards) {
        this.searchExecutionCourseBoards = searchExecutionCourseBoards;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree == null ? null : executionDegree.getObject();
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree == null ? null : new DomainReference<ExecutionDegree>(executionDegree);
    }

    public ExecutionSemester getExecutionPeriod() {
	if (executionSemester == null) {
	    final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionPeriod();
	    setExecutionPeriod(executionSemester);
	}
        return executionSemester.getObject();
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester == null ? null : new DomainReference<ExecutionSemester>(executionSemester);
    }

    public void setUnit(Unit unit) {
        this.unit = new DomainReference<Unit>(unit);
    }

    public Unit getUnit() {
        return this.unit == null ? null : this.unit.getObject();
    }

    public Set getSearchResult() {
	if (getSearchExecutionCourseBoards() == null) {
	    return null;
	}
	final Set<AnnouncementBoard> boards = new TreeSet<AnnouncementBoard>(AnnouncementBoard.BY_NAME);
	if (getSearchExecutionCourseBoards().booleanValue()) {
	    final ExecutionDegree executionDegree = getExecutionDegree();
	    final ExecutionSemester executionSemester = getExecutionPeriod();
	    if (executionDegree != null && executionSemester != null) {
		for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
		    if (isExecutionCourseForExecutionDegree(executionCourse, executionDegree)) {
			boards.add(executionCourse.getBoard());
		    }
		}
	    }
	} else {
	    final Unit unit = getUnit();
	    if (unit != null) {
		boards.addAll(unit.getBoards());
	    }
	}
	return boards;
    }

    private boolean isExecutionCourseForExecutionDegree(final ExecutionCourse executionCourse, final ExecutionDegree executionDegree) {
	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
	    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
	    for (final ExecutionDegree otherExecutionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		if (executionDegree == otherExecutionDegree) {
		    return true;
		}
	    }
	}
	return false;
    }
}

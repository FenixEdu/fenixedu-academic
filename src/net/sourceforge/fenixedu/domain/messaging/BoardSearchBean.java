package net.sourceforge.fenixedu.domain.messaging;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class BoardSearchBean implements Serializable {

    private Boolean searchExecutionCourseBoards;

    private String unitName;

    private DomainReference<UnitName> unitNameObject;

    private DomainReference<ExecutionPeriod> executionPeriod;

    private DomainReference<ExecutionDegree> executionDegree;

    public BoardSearchBean() {
	super();
	System.out.println("Creating new bean object.");
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

    public ExecutionPeriod getExecutionPeriod() {
	if (executionPeriod == null) {
	    final ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
	    setExecutionPeriod(executionPeriod);
	}
        return executionPeriod.getObject();
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod == null ? null : new DomainReference<ExecutionPeriod>(executionPeriod);
    }

    public UnitName getUnitNameObject() {
        return unitNameObject == null ? null : unitNameObject.getObject();
    }

    public void setUnitNameObject(UnitName unitName) {
        this.unitNameObject = unitName == null ? null : new DomainReference<UnitName>(unitName);
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Set getSearchResult() {
	if (getSearchExecutionCourseBoards() == null) {
	    return null;
	}
	final Set<AnnouncementBoard> boards = new TreeSet<AnnouncementBoard>(AnnouncementBoard.BY_NAME);
	if (getSearchExecutionCourseBoards().booleanValue()) {
	    final ExecutionDegree executionDegree = getExecutionDegree();
	    final ExecutionPeriod executionPeriod = getExecutionPeriod();
	    if (executionDegree != null && executionPeriod != null) {
		for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCoursesSet()) {
		    if (isExecutionCourseForExecutionDegree(executionCourse, executionDegree)) {
			boards.add(executionCourse.getBoard());
		    }
		}
	    }
	} else {
	    final UnitName unitName = getUnitNameObject();
	    if (unitName != null) {
		boards.addAll(unitName.getUnit().getBoardsSet());
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

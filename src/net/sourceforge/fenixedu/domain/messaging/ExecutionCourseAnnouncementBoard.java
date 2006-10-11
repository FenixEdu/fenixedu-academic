package net.sourceforge.fenixedu.domain.messaging;

import java.util.Comparator;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExecutionCourseAnnouncementBoard extends ExecutionCourseAnnouncementBoard_Base {

    public static final Comparator<ExecutionCourseAnnouncementBoard> COMPARE_BY_EXECUTION_PERIOD_AND_NAME = new Comparator<ExecutionCourseAnnouncementBoard>() {
	public int compare(ExecutionCourseAnnouncementBoard o1, ExecutionCourseAnnouncementBoard o2) {
	    int result = o1.getExecutionCourse().getExecutionPeriod().compareTo(o2.getExecutionCourse().getExecutionPeriod());
	    if (result == 0) {
		result = o1.getExecutionCourse().getNome().compareTo(o2.getExecutionCourse().getNome());
	    }
	    return (result == 0) ? o1.getExecutionCourse().getIdInternal().compareTo(o2.getExecutionCourse().getIdInternal()) : result;
	}
    };

    public ExecutionCourseAnnouncementBoard() {
	super();
    }

    public ExecutionCourseAnnouncementBoard(String name, ExecutionCourse executionCourse, Group writers,
	    Group readers, Group managers, ExecutionCourseBoardPermittedGroupType writersGroupType,
	    ExecutionCourseBoardPermittedGroupType readersGroupType,
	    ExecutionCourseBoardPermittedGroupType managersGroupType) {

	this();
	init(name, executionCourse, writers, readers, managers, writersGroupType, readersGroupType,
		managersGroupType);
    }

    private void init(String name, ExecutionCourse executionCourse, Group writers, Group readers,
	    Group managers, ExecutionCourseBoardPermittedGroupType writersGroupType,
	    ExecutionCourseBoardPermittedGroupType readersGroupType,
	    ExecutionCourseBoardPermittedGroupType managersGroupType) {

	checkParameters(name, executionCourse);

	setName(name);
	setExecutionCourse(executionCourse);

	setWriters(writers);
	setReaders(readers);
	setManagers(managers);

	setExecutionCoursePermittedWriteGroupType(writersGroupType);
	setExecutionCoursePermittedReadGroupType(readersGroupType);
	setExecutionCoursePermittedManagementGroupType(managersGroupType);
    }

    private void checkParameters(String name, ExecutionCourse executionCourse) {
	if (name == null) {
	    throw new DomainException(
		    "error.messaging.ExecutionCourseAnnouncementBoard.name.cannot.be.null");
	}
	if (executionCourse == null) {
	    throw new DomainException(
		    "error.messaging.ExecutionCourseAnnouncementBoard.executionCourse.cannot.be.null");
	}
    }
    
    @Override
    public String getFullName() {
	final StringBuilder result = new StringBuilder(20);
	result.append(getExecutionCourse().getNome()).append(" ");
	result.append(getExecutionCourse().getExecutionPeriod().getSemester()).append("ºSem. ");
	result.append(getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
	return result.toString();
    }

    @Override
    public String getQualifiedName() {
	final StringBuilder result = new StringBuilder(20);
	result.append(getExecutionCourse().getNome()).append(" ");
	result.append(getExecutionCourse().getExecutionPeriod().getSemester()).append("ºSem. ");
	result.append(getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear()).append(" ");
	final Iterator<Degree> degrees = getExecutionCourse().getDegreesSortedByDegreeName().iterator();
	while (degrees.hasNext()) {
	    result.append(degrees.next().getSigla()).append(degrees.hasNext() ? "," : "");
	}
	return result.toString();
    }
    
    @Override
    public void delete() {
	removeExecutionCourse();
        super.delete();
    }

}

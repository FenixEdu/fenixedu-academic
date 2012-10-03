package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * Base group for every group that computes the elements from an
 * ExecutionCoursse.
 * 
 * @author cfgi
 */
public abstract class ExecutionCourseGroup extends DomainBackedGroup<ExecutionCourse> {

    private static final long serialVersionUID = 1L;

    public ExecutionCourseGroup(ExecutionCourse executionCourse) {
	super(executionCourse);
    }

    public ExecutionCourse getExecutionCourse() {
	return getObject();
    }

    public boolean hasExecutionCourse() {
	return getExecutionCourse() != null;
    }

    @Override
    final public String getPresentationNameBundle() {
	return "resources.SiteResources";
    }

    @Override
    public String getPresentationNameKey() {
	return "label.net.sourceforge.fenixedu.domain.accessControl." + this.getClass().getSimpleName();
    }

    @Override
    final public String[] getPresentationNameKeyArgs() {
	final ExecutionCourse executionCourse = getExecutionCourse();
	return executionCourse == null ? new String[0] : new String[] { getExecutionCourse().getNome() };
    }

}

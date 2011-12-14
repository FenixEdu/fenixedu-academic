package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * Class TeacherTutorshipCreationBean.java
 * 
 * @author jaime created on Aug 26, 2010
 */

public class TeacherTutorshipCreationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Person teacher;
    private ExecutionDegree executionDegree;

    public TeacherTutorshipCreationBean(ExecutionDegree executionDegree) {
	this.executionDegree = executionDegree;
    }

    public static class TutorsProvider implements DataProvider {
	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	/**
	 * provide all teachers in the departments that teach that course
	 */
	@Override
	public Object provide(Object source, Object current) {
	    TeacherTutorshipCreationBean bean = (TeacherTutorshipCreationBean) source;
	    List<Person> teachers = new ArrayList<Person>();
	    if (bean.getExecutionDegree() != null) {
		ExecutionDegree executionDegree = bean.getExecutionDegree();
		for (final Teacher teacher : executionDegree.getPossibleTutorsFromExecutionDegreeDepartments()) {
		    if (teacher.hasTutorshipIntentionFor(bean.getExecutionDegree())) {
			teachers.add(teacher.getPerson());
		    }
		}
	    }
	    return teachers;
	}
    }

    public Person getTeacher() {
	return teacher;
    }

    public void setTeacher(Person teacher) {
	this.teacher = teacher;
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegree = executionDegree;
    }

}

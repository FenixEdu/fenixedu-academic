package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;

public class ExecutionCourseStudentsGroup extends ExecutionCourseGroup
{
    private static final long serialVersionUID = 1L;

    private class AttendPersonTransformer implements Transformer
	{

		public Object transform(Object object)
		{
			Attends attend = (Attends) object;
			return attend.getAluno().getPerson();
		}		
	}
	
	public ExecutionCourseStudentsGroup(ExecutionCourse executionCourse) {
        super(executionCourse);
	}

	@Override
	public int getElementsCount()
	{
		return this.getExecutionCourse().getAttendsCount();
	}

	@Override
	public Iterator<Person> getElementsIterator()
	{
		return new TransformIterator(this.getExecutionCourse().getAttendsIterator(), new AttendPersonTransformer());
		
	}

}



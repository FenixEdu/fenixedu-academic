

package net.sourceforge.fenixedu.domain.accessControl;


import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;

public class ExecutionCourseStudentsGroup extends ExecutionCourseStudentsGroup_Base
{
	private class AttendPersonTransformer implements Transformer
	{

		public Object transform(Object arg0)
		{
			Attends attend = (Attends) arg0;
			return attend.getAluno().getPerson();
		}		
	}
	
	public ExecutionCourseStudentsGroup()
	{
		super();
		this.setCreationDate(new Date(System.currentTimeMillis()));
	}

	@Override
	public int getElementsCount()
	{
		return this.getExecutionCourse().getAttendsCount();
	}

	@Override
	public Iterator<Person> getElementsIterator()
	{
		return new TransformIterator(this.getExecutionCourse().getAttendsIterator(),new AttendPersonTransformer());
		
	}	
}



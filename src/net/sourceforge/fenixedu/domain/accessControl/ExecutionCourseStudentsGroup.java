

package net.sourceforge.fenixedu.domain.accessControl;


import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IPerson;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;

public class ExecutionCourseStudentsGroup extends ExecutionCourseStudentsGroup_Base
{
	private class AttendPersonTransformer implements Transformer
	{

		public Object transform(Object arg0)
		{
			IAttends attend = (IAttends) arg0;
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
	public Iterator<IPerson> getElementsIterator()
	{
		return new TransformIterator(this.getExecutionCourse().getAttendsIterator(),new AttendPersonTransformer());
		
	}	
}



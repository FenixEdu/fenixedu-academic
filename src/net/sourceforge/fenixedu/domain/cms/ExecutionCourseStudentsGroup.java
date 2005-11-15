

package net.sourceforge.fenixedu.domain.cms;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;

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
	}

	@Override
	public int getElementsCount()
	{
		return this.getExecutionCourse().getAttendsCount();
	}

	public Iterator<IPerson> getElementsIterator()
	{
		return new TransformIterator(this.getExecutionCourse().getAttendsIterator(),new AttendPersonTransformer());
		
	}
}

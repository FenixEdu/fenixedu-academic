package net.sourceforge.fenixedu.domain.accessControl;


import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;

public class ExecutionCourseTeachersGroup extends ExecutionCourseTeachersGroup_Base
{

	private class ProfessorshipPersonTransformer implements Transformer
	{

		public Object transform(Object arg0)
		{
			Professorship professorship = (Professorship) arg0;
			
			return professorship.getTeacher().getPerson();
		}		
	}
	
	public ExecutionCourseTeachersGroup()
	{
		super();
		this.setCreationDate(new Date(System.currentTimeMillis()));
	}
	
	@Override
	public int getElementsCount()
	{
		return this.getExecutionCourse().getProfessorshipsCount();
	}

	@Override
	public Iterator<Person> getElementsIterator()
	{
		return new TransformIterator(this.getExecutionCourse().getProfessorshipsIterator(),new ProfessorshipPersonTransformer());
	}

}

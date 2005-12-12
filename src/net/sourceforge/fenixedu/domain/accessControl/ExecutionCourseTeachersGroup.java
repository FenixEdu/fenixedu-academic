package net.sourceforge.fenixedu.domain.accessControl;


import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;

import sun.security.krb5.internal.crypto.n;

public class ExecutionCourseTeachersGroup extends ExecutionCourseTeachersGroup_Base
{

	private class ProfessorshipPersonTransformer implements Transformer
	{

		public Object transform(Object arg0)
		{
			IProfessorship professorship = (IProfessorship) arg0;
			
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
	public Iterator<IPerson> getElementsIterator()
	{
		return new TransformIterator(this.getExecutionCourse().getProfessorshipsIterator(),new ProfessorshipPersonTransformer());
	}

}

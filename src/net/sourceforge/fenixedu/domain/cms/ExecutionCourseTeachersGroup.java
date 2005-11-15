

package net.sourceforge.fenixedu.domain.cms;


import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;

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
	}
	
	@Override
	public int getElementsCount()
	{
		return this.getExecutionCourse().getProfessorshipsCount();
	}
	/* (non-Javadoc)
	 * @see net.sourceforge.fenixedu.domain.cms.UserGroup#getElementsIterator()
	 */
	@Override
	public Iterator<IPerson> getElementsIterator()
	{
		return new TransformIterator(this.getExecutionCourse().getProfessorshipsIterator(),new ProfessorshipPersonTransformer());
	}

}

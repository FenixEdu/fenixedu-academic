/**
 * 
 */
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 15:49:40,31/Mar/2006
 * @version $Id$
 */
public class GroupStudentGroup extends DomainBackedGroup<StudentGroup> implements IGroup{

	public GroupStudentGroup(StudentGroup object) {
		super(object);
	}

	public Set<Person> getElements() {
		Set<Person> elements = super.buildSet();
		for (Attends attends: this.getObject().getAttends()) {
			elements.add(attends.getAluno().getPerson());
		}
		
		return elements;
	}



}

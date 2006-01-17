package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.NotImplementedException;
import net.sourceforge.fenixedu.domain.Person;

public final class GroupDifference extends NodeGroup {
    
    private static final long serialVersionUID = 1L;

    public GroupDifference(Group ... groups) {
        super(groups);
    }

	@Override
	public int getElementsCount()
	{
		throw new NotImplementedException();
	}

	@Override
	public Iterator<Person> getElementsIterator()
	{
		throw new NotImplementedException();
	}
}

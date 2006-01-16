package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.collections.iterators.ArrayListIterator;
public class PublicRequestersGroup extends LeafGroup {
    
    private static final long serialVersionUID = 1L;

    public PublicRequestersGroup() {
        super();
    }

	@Override
	public Iterator<Person> getElementsIterator()
	{
		return new ArrayListIterator();
	}
	
	@Override
	public boolean allows(IUserView userView)
	{
		return userView.isPublicRequester();
	}

    @Override
    public boolean isMember(Person person) {
        return true;
    }
}

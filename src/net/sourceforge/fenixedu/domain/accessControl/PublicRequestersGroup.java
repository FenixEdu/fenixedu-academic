package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.IPerson;

import org.apache.commons.collections.iterators.ArrayListIterator;
public class PublicRequestersGroup extends PublicRequestersGroup_Base {
    
    public PublicRequestersGroup() {
        super();
    }

	@Override
	public Iterator<IPerson> getElementsIterator()
	{
		return new ArrayListIterator();
	}
	
	@Override
	public boolean allows(IUserView userView)
	{
		return userView.isPublicRequester();
	}
    
}

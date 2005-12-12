package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.NotImplementedException;
public class GroupDifference extends GroupDifference_Base {
    
    public GroupDifference() {
        super();
    }

	@Override
	public int getElementsCount()
	{
		throw new NotImplementedException();
	}

	@Override
	public Iterator<IPerson> getElementsIterator()
	{
		throw new NotImplementedException();
	}
	
	@Override
	public void delete()
	{
		throw new NotImplementedException();
	}
    
}

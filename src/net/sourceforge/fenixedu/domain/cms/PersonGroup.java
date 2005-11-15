package net.sourceforge.fenixedu.domain.cms;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.stm.VBox;
import net.sourceforge.fenixedu.stm.RelationList;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;
public class PersonGroup extends PersonGroup_Base {
    
    public PersonGroup() {
        super();
    }
	/* (non-Javadoc)
	 * @see net.sourceforge.fenixedu.domain.cms.UserGroup#count()
	 */
	@Override
	public int getElementsCount()
	{
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fenixedu.domain.cms.UserGroup#getElementsIterator()
	 */
	@Override
	public Iterator<IPerson> getElementsIterator()
	{
		throw new NotImplementedException();
	}
    
}

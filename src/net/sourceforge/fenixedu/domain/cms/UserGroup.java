/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * tags
 * Created on 23:13:44,20/Set/2005
 * @version $Id$
 */


package net.sourceforge.fenixedu.domain.cms;


import java.util.Collection;
import java.util.Iterator;

public abstract class UserGroup extends UserGroup_Base
{

	public UserGroup()
	{
		super();
	}

	public abstract java.util.Iterator<net.sourceforge.fenixedu.domain.IPerson> getElementsIterator();

	/**
	 * Provides a standard implementation to <code>count()</code><br/>
	 * It accesses the elements iterator and counts how many sucessfull <code>next()<code> can be called on it<br/>
	 * If any group subclassing this class can provide a more efficient way of calculating its size, then override this method
	 */
	public int getElementsCount()
	{
		int elementsCount = 0;
		Iterator iterator = this.getElementsIterator();
		while (iterator.hasNext())
		{
			elementsCount++;
			iterator.next();
		}

		return elementsCount;
	}

}

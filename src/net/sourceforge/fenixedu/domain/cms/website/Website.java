

package net.sourceforge.fenixedu.domain.cms.website;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.cms.Content;

import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.collections.iterators.UnmodifiableIterator;
import org.apache.commons.collections.list.UnmodifiableList;

public class Website extends Website_Base
{

	public Website()
	{
		super();
	}

	@Override
	public java.util.Iterator<Content> getChildrenIterator()
	{
		IteratorChain iteratorChain = new IteratorChain();
		iteratorChain.addIterator(this.getChildrenIterator());
		if (this.getType() != null)
		{
			iteratorChain.addIterator(this.getType().getMandatoryContentsIterator());
		}

		return UnmodifiableIterator.decorate(iteratorChain);
	}

	@Override
	public List<Content> getChildren()
	{
		List<Content> children = new ArrayList<Content>();
		children.addAll(super.getChildren());
		if (this.getType() != null)
		{
			children.addAll(this.getType().getMandatoryContents());
		}

		return UnmodifiableList.decorate(children);
	}

}

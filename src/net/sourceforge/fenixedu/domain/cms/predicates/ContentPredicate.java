/*
 * Created on 6:47:30 PM,Mar 16, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.domain.cms.predicates;

import net.sourceforge.fenixedu.domain.cms.Content;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 6:47:30 PM, Mar 16, 2005
 */
public abstract class ContentPredicate implements IContentPredicate
{

    public abstract boolean evaluate(Content content);

    public boolean evaluate(Object object)
    {
        return this.evaluate((Content)object);
    }

}

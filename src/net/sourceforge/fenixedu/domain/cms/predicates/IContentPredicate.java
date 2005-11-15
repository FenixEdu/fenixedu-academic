/*
 * Created on 8:51:55 PM,Mar 15, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.domain.cms.predicates;

import net.sourceforge.fenixedu.domain.cms.Content;

import org.apache.commons.collections.Predicate;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 8:51:55 PM, Mar 15, 2005
 */
public interface IContentPredicate extends Predicate
{
    abstract public boolean evaluate(Content content);
}

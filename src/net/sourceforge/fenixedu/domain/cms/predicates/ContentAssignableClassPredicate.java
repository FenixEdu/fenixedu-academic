/*
 * Created on 9:10:15 PM,Mar 15, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.domain.cms.predicates;

import net.sourceforge.fenixedu.domain.cms.Content;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz </a>
 * 
 * Created at 9:10:15 PM, Mar 15, 2005
 */
public class ContentAssignableClassPredicate extends ContentPredicate
{
    Class subject;
    
    public ContentAssignableClassPredicate(Class clazz)
    {
        this.subject = clazz;
    }
    
    @SuppressWarnings("unchecked")
	public boolean evaluate(Content content)
    {
        return this.subject.isAssignableFrom(content.getClass()); 
    }   
}

/**
 * 
 */
package net.sourceforge.fenixedu.injectionCode;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 14:40:00,17/Fev/2006
 * @version $Id$
 */
public interface IGroup {
    public int getElementsCount();

    public boolean isMember(Person person);

    public boolean allows(IUserView userView);

    public Set<Person> getElements();
    
    public String getExpression();
}

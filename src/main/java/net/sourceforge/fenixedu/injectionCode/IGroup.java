/**
 * 
 */
package net.sourceforge.fenixedu.injectionCode;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.domain.User;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/>
 * <br/>
 * <br/>
 *         Created on 14:40:00,17/Fev/2006
 * @version $Id$
 * 
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
public interface IGroup {

    public int getElementsCount();

    public boolean isMember(Person person);

    public boolean allows(User userView);

    public Set<Person> getElements();

    public String getExpression();

    public String getName();

    public boolean hasPresentationNameDynamic();

    public String getPresentationNameBundle();

    public String getPresentationNameKey();

    public String[] getPresentationNameKeyArgs();

    public org.fenixedu.bennu.core.domain.groups.Group convert();
}

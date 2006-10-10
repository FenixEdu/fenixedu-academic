package net.sourceforge.fenixedu.applicationTier;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jorge
 */
public interface IUserView extends Serializable {

    public Person getPerson();

    public String getUtilizador();

    public String getFullName();

    public Collection<RoleType> getRoleTypes();

    boolean hasRoleType(final RoleType roleType);
    
    boolean isPublicRequester();

}
package net.sourceforge.fenixedu.applicationTier;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.User;

/**
 * @author jorge
 */
public interface IUserView extends Serializable, User {

    public Person getPerson();

    public String getUtilizador();

    public String getFullName();

    public Collection<RoleType> getRoleTypes();

    public DateTime getExpirationDate();

    boolean hasRoleType(final RoleType roleType);
    
}
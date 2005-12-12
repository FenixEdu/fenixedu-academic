package net.sourceforge.fenixedu.applicationTier;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jorge
 */
public interface IUserView extends Serializable {

	public IPerson getPerson();

    public String getUtilizador();

    public String getFullName();

    public Collection getRoles();

    boolean hasRoleType(final RoleType roleType);
    
    boolean isPublicRequester();

}
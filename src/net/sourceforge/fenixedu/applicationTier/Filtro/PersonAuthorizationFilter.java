/*
 * Created on 13/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.util.RoleType;

/**
 * @author jpvl
 */
public class PersonAuthorizationFilter extends AuthorizationByRoleFilter {

    // the singleton of this class
    public final static PersonAuthorizationFilter instance = new PersonAuthorizationFilter();

    /**
     * The singleton access method of this class.
     * 
     * @return Returns the instance of this class responsible for the
     *         authorization access to services.
     */
    public static Filtro getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.PERSON;
    }

}
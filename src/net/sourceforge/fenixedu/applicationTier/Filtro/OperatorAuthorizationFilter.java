/*
 * Created on 19/Jul/2004 by Tânia Pousão
 *
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Tânia Pousão
 */
public class OperatorAuthorizationFilter extends AuthorizationByRoleFilter {

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
	return RoleType.OPERATOR;
    }

}
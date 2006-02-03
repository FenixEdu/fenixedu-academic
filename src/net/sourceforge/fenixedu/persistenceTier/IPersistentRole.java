/*
 * ISitioPersistente.java
 *
 * Created on 25 de Agosto de 2002, 0:53
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 *  
 */
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

public interface IPersistentRole {

    /**
     * 
     * @param roleType
     * @return Role
     * @throws ExcepcaoPersistencia
     */
    public Role readByRoleType(RoleType roleType) throws ExcepcaoPersistencia;

}
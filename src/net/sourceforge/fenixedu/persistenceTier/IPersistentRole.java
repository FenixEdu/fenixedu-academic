/*
 * ISitioPersistente.java
 *
 * Created on 25 de Agosto de 2002, 0:53
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 *  
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.util.RoleType;

public interface IPersistentRole {

    /**
     * 
     * @param roleType
     * @return Role
     * @throws ExcepcaoPersistencia
     */
    public IRole readByRoleType(RoleType roleType) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;
}
/*
 * ISitioPersistente.java
 *
 * Created on 25 de Agosto de 2002, 0:53
 */

package ServidorPersistente;

/**
 * 
 */
import java.util.List;

import Dominio.IRole;
import Util.RoleType;

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

/*
 * Created on Apr 28, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * @author Ricardo Rodrigues
 *
 */

public interface IPersistentInstitution extends IPersistentObject{

    public List readByName(String name) throws ExcepcaoPersistencia;
    
    public List readAll() throws ExcepcaoPersistencia;
}

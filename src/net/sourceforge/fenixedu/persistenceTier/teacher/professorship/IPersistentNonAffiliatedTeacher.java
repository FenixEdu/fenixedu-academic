/*
 * Created on Apr 28, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Ricardo Rodrigues
 *
 */

public interface IPersistentNonAffiliatedTeacher extends IPersistentObject {
    
    public List readByName(String name) throws ExcepcaoPersistencia;
    
    public INonAffiliatedTeacher readByNameAndInstitution(String name, IInstitution institution) throws ExcepcaoPersistencia;
    
    public List readAll() throws ExcepcaoPersistencia;

}

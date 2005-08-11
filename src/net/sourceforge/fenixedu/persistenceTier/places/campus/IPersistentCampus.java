package net.sourceforge.fenixedu.persistenceTier.places.campus;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICampus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentCampus extends IPersistentObject {

    List readAll() throws ExcepcaoPersistencia;
    
    public ICampus readByName(String campusName) throws ExcepcaoPersistencia; 

}
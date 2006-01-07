package net.sourceforge.fenixedu.persistenceTier.places.campus;

import java.util.List;

import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentCampus extends IPersistentObject {

    List readAll() throws ExcepcaoPersistencia;
    
    public Campus readByName(String campusName) throws ExcepcaoPersistencia; 

}
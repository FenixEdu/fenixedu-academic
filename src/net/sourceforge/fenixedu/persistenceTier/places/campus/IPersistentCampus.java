/*
 * Created on Dec 5, 2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier.places.campus;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentCampus extends IPersistentObject {

    /**
     * @return
     */
    List readAll() throws ExcepcaoPersistencia;

}
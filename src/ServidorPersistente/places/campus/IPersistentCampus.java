/*
 * Created on Dec 5, 2003 by jpvl
 *
 */
package ServidorPersistente.places.campus;

import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentCampus extends IPersistentObject {

    /**
     * @return
     */
    List readAll() throws ExcepcaoPersistencia;

}
package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author PTRLV
 *  
 */

import java.util.List;

public interface IPersistentBibliographicReference extends IPersistentObject {
    public List readBibliographicReference(Integer executionCourseOID) throws ExcepcaoPersistencia;
}
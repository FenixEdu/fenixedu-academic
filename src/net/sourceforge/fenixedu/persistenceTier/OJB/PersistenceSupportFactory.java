/*
 * Created on 2005/03/27
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * 
 * @author Luis Cruz
 */
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class PersistenceSupportFactory {

    public static SuportePersistenteOJB getDefaultPersistenceSupport() throws ExcepcaoPersistencia {
        return SuportePersistenteOJB.getInstance();
    }

}
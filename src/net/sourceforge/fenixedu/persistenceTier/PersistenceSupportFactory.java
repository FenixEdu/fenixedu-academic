/*
 * Created on 2005/03/27
 * 
 */
package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author Luis Cruz
 */
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class PersistenceSupportFactory {

    public static ISuportePersistente getDefaultPersistenceSupport() throws ExcepcaoPersistencia {
        return SuportePersistenteOJB.getInstance();
    }

    public static SuportePersistenteOJB getOJBPersistenceSupport() throws ExcepcaoPersistencia {
        return SuportePersistenteOJB.getInstance();
    }

}
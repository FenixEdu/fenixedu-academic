/*
 * Created on Apr 27, 2004
 *
 */
package ServidorPersistente.gratuity.masterDegree;

import Dominio.gratuity.masterDegree.ISibsPaymentFile;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public interface IPersistentSibsPaymentFile extends IPersistentObject {

    public abstract ISibsPaymentFile readByFilename(String filename)
            throws ExcepcaoPersistencia;
}
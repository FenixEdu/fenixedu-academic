/*
 * Created on Apr 27, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree;

import net.sourceforge.fenixedu.domain.gratuity.masterDegree.ISibsPaymentFile;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public interface IPersistentSibsPaymentFile extends IPersistentObject {

    public abstract ISibsPaymentFile readByFilename(String filename) throws ExcepcaoPersistencia;
}
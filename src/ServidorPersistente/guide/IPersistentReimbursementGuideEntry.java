/*
 * Created on 19/Mar/2004
 *  
 */
package ServidorPersistente.guide;

import java.util.List;

import Dominio.IGuideEntry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentReimbursementGuideEntry extends IPersistentObject {
    public List readByGuideEntry(IGuideEntry guideEntry) throws ExcepcaoPersistencia;

}
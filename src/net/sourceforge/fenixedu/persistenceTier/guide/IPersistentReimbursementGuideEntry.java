/*
 * Created on 19/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.guide;

import java.util.List;

import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentReimbursementGuideEntry extends IPersistentObject {
    public List readByGuideEntry(IGuideEntry guideEntry) throws ExcepcaoPersistencia;

}
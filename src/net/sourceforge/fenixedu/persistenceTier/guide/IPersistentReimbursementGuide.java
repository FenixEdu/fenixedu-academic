/*
 * Created on 17/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.guide;

import java.util.List;

import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 17/Nov/2003
 *  
 */
public interface IPersistentReimbursementGuide extends IPersistentObject {
    public List readByGuide(IGuide guide) throws ExcepcaoPersistencia;

    public Integer generateReimbursementGuideNumber() throws ExcepcaoPersistencia;
}
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.util.AdvisoryRecipients;

/**
 * Created on 2003/09/06
 * 
 * @author Luis Cruz Package ServidorPersistente
 *  
 */
public interface IPersistentAdvisory extends IPersistentObject {

    /**
     * @param advisory
     * @param advisoryRecipients
     */
    public void write(IAdvisory advisory, AdvisoryRecipients advisoryRecipients)
            throws ExcepcaoPersistencia;

    public void write(IAdvisory advisory, List group) throws ExcepcaoPersistencia;

    public void write(IAdvisory advisory, IPerson person) throws ExcepcaoPersistencia;
}
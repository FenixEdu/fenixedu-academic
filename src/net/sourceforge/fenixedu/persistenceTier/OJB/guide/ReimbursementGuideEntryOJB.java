/*
 * Created on 19/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.guide;

import java.util.List;

import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuideEntry;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReimbursementGuideEntryOJB extends PersistentObjectOJB implements
        IPersistentReimbursementGuideEntry {

    /**
     *  
     */
    public ReimbursementGuideEntryOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.guide.IPersistentReimbursementGuideEntry#readByGuideEntry(Dominio.IGuideEntry)
     */
    public List readByGuideEntry(IGuideEntry guideEntry) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("guideEntry.idInternal", guideEntry.getIdInternal());
        return queryList(ReimbursementGuideEntry.class, crit);

    }

}
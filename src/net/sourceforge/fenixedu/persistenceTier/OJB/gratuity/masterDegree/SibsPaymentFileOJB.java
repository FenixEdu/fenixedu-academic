/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.gratuity.masterDegree;

import net.sourceforge.fenixedu.domain.gratuity.masterDegree.ISibsPaymentFile;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFile;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFile;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class SibsPaymentFileOJB extends PersistentObjectOJB implements IPersistentSibsPaymentFile {

    /** Creates a new instance of MasterDegreeCandidateOJB */
    public SibsPaymentFileOJB() {
    }

    public ISibsPaymentFile readByFilename(String filename) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("filename", filename);

        return (ISibsPaymentFile) queryObject(SibsPaymentFile.class, criteria);
    }

}
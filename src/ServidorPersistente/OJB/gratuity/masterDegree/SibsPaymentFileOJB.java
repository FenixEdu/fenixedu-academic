/*
 * Created on Oct 10, 2003
 *
 *
 */
package ServidorPersistente.OJB.gratuity.masterDegree;

import org.apache.ojb.broker.query.Criteria;

import Dominio.gratuity.masterDegree.ISibsPaymentFile;
import Dominio.gratuity.masterDegree.SibsPaymentFile;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.gratuity.masterDegree.IPersistentSibsPaymentFile;

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
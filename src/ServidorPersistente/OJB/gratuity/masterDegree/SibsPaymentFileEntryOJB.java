/*
 * Created on Oct 10, 2003
 *
 *
 */
package ServidorPersistente.OJB.gratuity.masterDegree;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.gratuity.masterDegree.SibsPaymentFileEntry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import Util.gratuity.SibsPaymentStatus;
import Util.gratuity.SibsPaymentType;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class SibsPaymentFileEntryOJB extends PersistentObjectOJB implements
        IPersistentSibsPaymentFileEntry {

    /** Creates a new instance of MasterDegreeCandidateOJB */
    public SibsPaymentFileEntryOJB() {
    }

    public List readByYearAndStudentNumberAndPaymentType(Integer year, Integer studentNumber,
            SibsPaymentType paymentType) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("year", year);
        criteria.addEqualTo("studentNumber", studentNumber);
        criteria.addEqualTo("paymentType", paymentType);

        return queryList(SibsPaymentFileEntry.class, criteria);
    }

    public List readByPaymentStatus(SibsPaymentStatus paymentStatus) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("paymentStatus", paymentStatus);

        return queryList(SibsPaymentFileEntry.class, criteria);
    }

    public List readNonProcessed() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("paymentStatus", SibsPaymentStatus.PROCESSED_PAYMENT);

        return queryList(SibsPaymentFileEntry.class, criteria);
    }

}
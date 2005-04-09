/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.gratuity.masterDegree;

import java.util.List;

import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import net.sourceforge.fenixedu.util.gratuity.SibsPaymentType;

import org.apache.ojb.broker.query.Criteria;

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
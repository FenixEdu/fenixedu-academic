/*
 * Created on Apr 27, 2004
 *
 */
package ServidorPersistente.gratuity.masterDegree;

import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.gratuity.SibsPaymentStatus;
import Util.gratuity.SibsPaymentType;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public interface IPersistentSibsPaymentFileEntry extends IPersistentObject {

    public List readByYearAndStudentNumberAndPaymentType(Integer year, Integer studentNumber,
            SibsPaymentType paymentType) throws ExcepcaoPersistencia;

    public List readByPaymentStatus(SibsPaymentStatus paymentStatus) throws ExcepcaoPersistencia;

    public List readNonProcessed() throws ExcepcaoPersistencia;
}
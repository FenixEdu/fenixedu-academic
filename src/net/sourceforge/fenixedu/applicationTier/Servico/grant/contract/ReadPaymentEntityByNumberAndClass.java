/*
 * Created on Feb 17, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author pica
 * @author barbosa
 */
public class ReadPaymentEntityByNumberAndClass implements IService {

    public ReadPaymentEntityByNumberAndClass() {
    }

    public InfoGrantPaymentEntity run(String paymentEntityNumber, String className)
            throws FenixServiceException {
        IGrantPaymentEntity grantPaymentEntity = null;
        InfoGrantPaymentEntity result = null;
        IPersistentGrantPaymentEntity pgpe = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            pgpe = sp.getIPersistentGrantPaymentEntity();
            grantPaymentEntity = pgpe.readByNumberAndClass(paymentEntityNumber, className);
            result = InfoGrantPaymentEntity.newInfoFromDomain(grantPaymentEntity);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
        return result;
    }
}
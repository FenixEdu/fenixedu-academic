/*
 * Created on Feb 17, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author pica
 * @author barbosa
 */
public class ReadPaymentEntityByNumberAndClass extends Service {

	public InfoGrantPaymentEntity run(String paymentEntityNumber, String className)
			throws FenixServiceException, ExcepcaoPersistencia {
		GrantPaymentEntity grantPaymentEntity = null;
		InfoGrantPaymentEntity result = null;
		IPersistentGrantPaymentEntity pgpe = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		pgpe = sp.getIPersistentGrantPaymentEntity();
		grantPaymentEntity = pgpe.readByNumberAndClass(paymentEntityNumber, className);
		result = InfoGrantPaymentEntity.newInfoFromDomain(grantPaymentEntity);

		return result;
	}
}
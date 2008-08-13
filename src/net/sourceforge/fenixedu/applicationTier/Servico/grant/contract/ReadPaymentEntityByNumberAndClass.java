/*
 * Created on Feb 17, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author pica
 * @author barbosa
 */
public class ReadPaymentEntityByNumberAndClass extends Service {

	public InfoGrantPaymentEntity run(String paymentEntityNumber, String className)
			throws FenixServiceException{
		final GrantPaymentEntity grantPaymentEntity = GrantPaymentEntity.findGrantPaymentEntityByNumberAndConcreteClass(paymentEntityNumber, className);
		return InfoGrantPaymentEntity.newInfoFromDomain(grantPaymentEntity);
	}

}
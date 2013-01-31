/*
 * Created on Feb 17, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author pica
 * @author barbosa
 */
public class ReadPaymentEntityByNumberAndClass extends FenixService {

	@Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
	@Service
	public static InfoGrantPaymentEntity run(String paymentEntityNumber, String className) throws FenixServiceException {
		final GrantPaymentEntity grantPaymentEntity =
				GrantPaymentEntity.findGrantPaymentEntityByNumberAndConcreteClass(paymentEntityNumber, className);
		return InfoGrantPaymentEntity.newInfoFromDomain(grantPaymentEntity);
	}

}
/*
 * Created on 23/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadAllGrantPartsByGrantSubsidy implements IService {

	public List run(Integer grantSubsidyId) throws FenixServiceException, ExcepcaoPersistencia {
		List result = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentGrantPart pgp = sp.getIPersistentGrantPart();
		List grantParts = pgp.readAllGrantPartsByGrantSubsidy(grantSubsidyId);

		if (grantParts != null) {
			result = (List) CollectionUtils.collect(grantParts, new Transformer() {
				public Object transform(Object o) {
					GrantPart grantPart = (GrantPart) o;
					return InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity
							.newInfoFromDomain(grantPart);
				}
			});
		}

		return result;
	}
}
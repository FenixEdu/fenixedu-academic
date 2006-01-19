/*
 * Created on 3/Jul/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractMovementWithContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractMovement;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadAllGrantMovementsByContract extends Service {

	public List run(Integer grantContractId) throws FenixServiceException, ExcepcaoPersistencia {
		List result = null;
		IPersistentGrantContractMovement pgcm = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		pgcm = sp.getIPersistentGrantContractMovement();
		List grantMovements = pgcm.readAllMovementsByContract(grantContractId);

		if (grantMovements != null) {
			result = (List) CollectionUtils.collect(grantMovements, new Transformer() {
				public Object transform(Object o) {
					GrantContractMovement grantMovement = (GrantContractMovement) o;
					return InfoGrantContractMovementWithContract.newInfoFromDomain(grantMovement);
				}
			});
		}

		return result;
	}
}
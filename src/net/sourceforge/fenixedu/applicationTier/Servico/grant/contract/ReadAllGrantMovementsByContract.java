/*
 * Created on 3/Jul/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractMovementWithContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractMovement;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantMovementsByContract implements IService {
    public ReadAllGrantMovementsByContract() {
    }

    public List run(Integer grantContractId) throws FenixServiceException {
        List result = null;
        IPersistentGrantContractMovement pgcm = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            pgcm = sp.getIPersistentGrantContractMovement();
            List grantMovements = pgcm.readAllMovementsByContract(grantContractId);

            if (grantMovements != null) {
                result = (List) CollectionUtils.collect(grantMovements, new Transformer() {
                    public Object transform(Object o) {
                        IGrantContractMovement grantMovement = (IGrantContractMovement) o;
                        return InfoGrantContractMovementWithContract.newInfoFromDomain(grantMovement);
                    }
                });
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }

        return result;
    }
}
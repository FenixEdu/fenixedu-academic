/*
 * Created on Jul 6, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.stat;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.stat.InfoStatGrantOwner;
import net.sourceforge.fenixedu.dataTransferObject.grant.stat.InfoStatResultGrantOwner;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;

/**
 * @author Pica
 * @author Barbosa
 */
public class CalculateStateGrantOwnerByCriteria extends Service {

    public Object[] run(InfoStatGrantOwner infoStatGrantOwner) throws FenixServiceException,
            ExcepcaoPersistencia {
        IPersistentGrantContract persistentGrantContract = persistentSupport
                .getIPersistentGrantContract();

        // Queries count
        GrantType granttype = rootDomainObject.readGrantTypeByOID(infoStatGrantOwner.getGrantType());
        Integer totalNumberOfGrantOwners = rootDomainObject.getGrantOwners().size();
        Integer numberOfGrantOwnersByCriteria = GrantOwner.countAllByCriteria(infoStatGrantOwner
                .getJustActiveContracts(), infoStatGrantOwner.getJustInactiveContracts(),
                infoStatGrantOwner.getDateBeginContract(), infoStatGrantOwner.getDateEndContract(),
                granttype);
        Integer totalNumberOfGrantContracts = rootDomainObject.getGrantContractsCount();
        Integer numberOfGrantContractsByCriteria = persistentGrantContract.countAllByCriteria(
                infoStatGrantOwner.getJustActiveContracts(), infoStatGrantOwner
                        .getJustInactiveContracts(), infoStatGrantOwner.getDateBeginContract(),
                infoStatGrantOwner.getDateEndContract(), infoStatGrantOwner.getGrantType());
        // Set the result info
        InfoStatResultGrantOwner infoStatResultGrantOwner = new InfoStatResultGrantOwner();
        infoStatResultGrantOwner.setNumberOfGrantContractsByCriteria(numberOfGrantContractsByCriteria);
        infoStatResultGrantOwner.setNumberOfGrantOwnerByCriteria(numberOfGrantOwnersByCriteria);
        infoStatResultGrantOwner.setTotalNumberOfGrantContracts(totalNumberOfGrantContracts);
        infoStatResultGrantOwner.setTotalNumberOfGrantOwners(totalNumberOfGrantOwners);

        if (infoStatGrantOwner.getGrantType() != null) {
            // Read the sigla for presentation reasons
            infoStatGrantOwner.setGrantTypeSigla(granttype.getSigla());
        }

        Object[] result = { infoStatResultGrantOwner, infoStatGrantOwner };
        return result;
    }
}
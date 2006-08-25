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

/**
 * @author Pica
 * @author Barbosa
 */
public class CalculateStateGrantOwnerByCriteria extends Service {

    public Object[] run(InfoStatGrantOwner infoStatGrantOwner) throws FenixServiceException,
            ExcepcaoPersistencia {

        final GrantType grantType = rootDomainObject.readGrantTypeByOID(infoStatGrantOwner
                .getGrantType());
        Integer totalNumberOfGrantOwners = rootDomainObject.getGrantOwners().size();
        Integer numberOfGrantOwnersByCriteria = GrantOwner.countAllByCriteria(infoStatGrantOwner
                .getJustActiveContracts(), infoStatGrantOwner.getJustInactiveContracts(),
                infoStatGrantOwner.getDateBeginContract(), infoStatGrantOwner.getDateEndContract(),
                grantType);

        Integer totalNumberOfGrantContracts = rootDomainObject.getGrantContractsCount();

        Boolean activeContracts = null;
        if (infoStatGrantOwner.getJustActiveContracts() != null) {
            activeContracts = infoStatGrantOwner.getJustActiveContracts();
        } else if (infoStatGrantOwner.getJustInactiveContracts() != null) {
            activeContracts = !infoStatGrantOwner.getJustInactiveContracts();
        }
        Integer numberOfGrantContractsByCriteria = (grantType == null) ? 0 : grantType.countGrantContractsByActiveAndDate(
                activeContracts, infoStatGrantOwner.getDateBeginContract(),
                infoStatGrantOwner.getDateEndContract());

        // Set the result info
        InfoStatResultGrantOwner infoStatResultGrantOwner = new InfoStatResultGrantOwner();
        infoStatResultGrantOwner.setNumberOfGrantContractsByCriteria(numberOfGrantContractsByCriteria);
        infoStatResultGrantOwner.setNumberOfGrantOwnerByCriteria(numberOfGrantOwnersByCriteria);
        infoStatResultGrantOwner.setTotalNumberOfGrantContracts(totalNumberOfGrantContracts);
        infoStatResultGrantOwner.setTotalNumberOfGrantOwners(totalNumberOfGrantOwners);

        if (infoStatGrantOwner.getGrantType() != null) {
            // Read the sigla for presentation reasons
            infoStatGrantOwner.setGrantTypeSigla(grantType.getSigla());
        }

        Object[] result = { infoStatResultGrantOwner, infoStatGrantOwner };
        return result;
    }
}
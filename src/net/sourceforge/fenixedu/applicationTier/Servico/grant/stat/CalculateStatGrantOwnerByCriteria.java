/*
 * Created on Jul 6, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.stat;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.stat.InfoStatGrantOwner;
import net.sourceforge.fenixedu.dataTransferObject.grant.stat.InfoStatResultGrantOwner;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Pica
 * @author Barbosa
 */
public class CalculateStatGrantOwnerByCriteria implements IService {

    public CalculateStatGrantOwnerByCriteria() {
    }

    public Object[] run(InfoStatGrantOwner infoStatGrantOwner) throws FenixServiceException {
        try {
            ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentGrantContract persistentGrantContract = suportePersistente
                    .getIPersistentGrantContract();
            IPersistentGrantOwner persistentGrantOwner = suportePersistente.getIPersistentGrantOwner();

            //Queries count
            Integer totalNumberOfGrantOwners = persistentGrantOwner.countAll();
            Integer numberOfGrantOwnersByCriteria = persistentGrantOwner.countAllByCriteria(
                    infoStatGrantOwner.getJustActiveContracts(), infoStatGrantOwner
                            .getJustInactiveContracts(), infoStatGrantOwner.getDateBeginContract(),
                    infoStatGrantOwner.getDateEndContract(), infoStatGrantOwner.getGrantType());
            Integer totalNumberOfGrantContracts = persistentGrantContract.countAll();
            Integer numberOfGrantContractsByCriteria = persistentGrantContract.countAllByCriteria(
                    infoStatGrantOwner.getJustActiveContracts(), infoStatGrantOwner
                            .getJustInactiveContracts(), infoStatGrantOwner.getDateBeginContract(),
                    infoStatGrantOwner.getDateEndContract(), infoStatGrantOwner.getGrantType());
            //Set the result info
            InfoStatResultGrantOwner infoStatResultGrantOwner = new InfoStatResultGrantOwner();
            infoStatResultGrantOwner
                    .setNumberOfGrantContractsByCriteria(numberOfGrantContractsByCriteria);
            infoStatResultGrantOwner.setNumberOfGrantOwnerByCriteria(numberOfGrantOwnersByCriteria);
            infoStatResultGrantOwner.setTotalNumberOfGrantContracts(totalNumberOfGrantContracts);
            infoStatResultGrantOwner.setTotalNumberOfGrantOwners(totalNumberOfGrantOwners);

            if (infoStatGrantOwner.getGrantType() != null) {
                //Read the sigla for presentation reasons
                IPersistentGrantType persistentGrantType = suportePersistente.getIPersistentGrantType();
                IGrantType granttype = (IGrantType) persistentGrantType.readByOID(GrantType.class,
                        infoStatGrantOwner.getGrantType());
                infoStatGrantOwner.setGrantTypeSigla(granttype.getSigla());
            }

            Object[] result = { infoStatResultGrantOwner, infoStatGrantOwner };
            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}
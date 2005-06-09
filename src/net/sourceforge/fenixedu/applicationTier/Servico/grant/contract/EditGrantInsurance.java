/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantInsurance implements IService {

    public void run(InfoGrantInsurance infoGrantInsurance)
            throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentGrantInsurance persistentGrantInsurance = persistentSupport
                .getIPersistentGrantInsurance();
        final IPersistentGrantContract persistentGrantContract = persistentSupport
                .getIPersistentGrantContract();
        final IPersistentGrantContractRegime persistentGrantContractRegime = persistentSupport
                .getIPersistentGrantContractRegime();
        final IPersistentGrantPaymentEntity persistentGrantPaymentEntity = persistentSupport.getIPersistentGrantPaymentEntity();

        IGrantInsurance grantInsurance = (IGrantInsurance) persistentGrantInsurance
                .readByOID(GrantInsurance.class, infoGrantInsurance
                        .getIdInternal());
        if (grantInsurance == null)
            grantInsurance = new GrantInsurance();        
        persistentGrantInsurance.simpleLockWrite(grantInsurance);

        grantInsurance.setDateBeginInsurance(infoGrantInsurance.getDateBeginInsurance());
        if (infoGrantInsurance.getDateEndInsurance() == null) {
            final List grantContractRegimeList = persistentGrantContractRegime
                    .readGrantContractRegimeByGrantContractAndState(
                            infoGrantInsurance.getInfoGrantContract()
                                    .getIdInternal(), InfoGrantContractRegime
                                    .getActiveState());
            final IGrantContractRegime grantContractRegime = (IGrantContractRegime) grantContractRegimeList
                    .get(0);
            grantInsurance.setDateEndInsurance(grantContractRegime.getDateEndContract());
        } else {
            grantInsurance.setDateEndInsurance(infoGrantInsurance.getDateEndInsurance());
        }

        final IGrantContract grantContract = (IGrantContract) persistentGrantContract.readByOID(GrantContract.class, infoGrantInsurance.getInfoGrantContract().getIdInternal());
        grantInsurance.setGrantContract(grantContract);

        final IGrantPaymentEntity grantPaymentEntity = (IGrantPaymentEntity) persistentGrantPaymentEntity.readByOID(GrantPaymentEntity.class, infoGrantInsurance.getInfoGrantPaymentEntity().getIdInternal());
        grantInsurance.setGrantPaymentEntity(grantPaymentEntity);

        grantInsurance.setTotalValue(InfoGrantInsurance
                .calculateTotalValue(
                        grantInsurance.getDateBeginInsurance(),
                        grantInsurance.getDateEndInsurance()));
    }

}
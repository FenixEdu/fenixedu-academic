package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditGrantInsurance implements IService {

    public void run(InfoGrantInsurance infoGrantInsurance) throws FenixServiceException,
            ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentGrantContract persistentGrantContract = persistentSupport
                .getIPersistentGrantContract();
        final IPersistentGrantContractRegime persistentGrantContractRegime = persistentSupport
                .getIPersistentGrantContractRegime();
        final IPersistentGrantPaymentEntity persistentGrantPaymentEntity = persistentSupport
                .getIPersistentGrantPaymentEntity();

        GrantInsurance grantInsurance = (GrantInsurance) persistentSupport.getIPersistentObject()
                .readByOID(GrantInsurance.class, infoGrantInsurance.getIdInternal());
        if (grantInsurance == null) {
            grantInsurance = DomainFactory.makeGrantInsurance();
        }

        grantInsurance.setDateBeginInsurance(infoGrantInsurance.getDateBeginInsurance());
        if (infoGrantInsurance.getDateEndInsurance() == null) {
            final List grantContractRegimeList = persistentGrantContractRegime
                    .readGrantContractRegimeByGrantContractAndState(infoGrantInsurance
                            .getInfoGrantContract().getIdInternal(), InfoGrantContractRegime
                            .getActiveState());
            final GrantContractRegime grantContractRegime = (GrantContractRegime) grantContractRegimeList
                    .get(0);
            grantInsurance.setDateEndInsurance(grantContractRegime.getDateEndContract());
        } else {
            grantInsurance.setDateEndInsurance(infoGrantInsurance.getDateEndInsurance());
        }

        final GrantContract grantContract = (GrantContract) persistentGrantContract.readByOID(
                GrantContract.class, infoGrantInsurance.getInfoGrantContract().getIdInternal());
        grantInsurance.setGrantContract(grantContract);

        final GrantPaymentEntity grantPaymentEntity = (GrantPaymentEntity) persistentGrantPaymentEntity
                .readByOID(GrantPaymentEntity.class, infoGrantInsurance.getInfoGrantPaymentEntity()
                        .getIdInternal());
        grantInsurance.setGrantPaymentEntity(grantPaymentEntity);

        grantInsurance.setTotalValue(InfoGrantInsurance.calculateTotalValue(grantInsurance
                .getDateBeginInsurance(), grantInsurance.getDateEndInsurance()));
    }

}
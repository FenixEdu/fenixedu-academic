package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;

public class EditGrantInsurance extends Service {

    public void run(InfoGrantInsurance infoGrantInsurance) throws FenixServiceException,
            ExcepcaoPersistencia {
        final IPersistentGrantContractRegime persistentGrantContractRegime = persistentSupport
                .getIPersistentGrantContractRegime();

        GrantInsurance grantInsurance = (GrantInsurance) persistentObject
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

        final GrantContract grantContract = (GrantContract) persistentObject.readByOID(
                GrantContract.class, infoGrantInsurance.getInfoGrantContract().getIdInternal());
        grantInsurance.setGrantContract(grantContract);

        final GrantPaymentEntity grantPaymentEntity = (GrantPaymentEntity) persistentObject
                .readByOID(GrantPaymentEntity.class, infoGrantInsurance.getInfoGrantPaymentEntity()
                        .getIdInternal());
        grantInsurance.setGrantPaymentEntity(grantPaymentEntity);

        grantInsurance.setTotalValue(InfoGrantInsurance.calculateTotalValue(grantInsurance
                .getDateBeginInsurance(), grantInsurance.getDateEndInsurance()));
    }

}
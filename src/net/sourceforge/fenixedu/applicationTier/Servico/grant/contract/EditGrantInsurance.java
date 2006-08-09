package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditGrantInsurance extends Service {

    public void run(InfoGrantInsurance infoGrantInsurance) throws FenixServiceException,
            ExcepcaoPersistencia {
     
        GrantInsurance grantInsurance = rootDomainObject.readGrantInsuranceByOID(infoGrantInsurance
                .getIdInternal());
        if (grantInsurance == null) {
            grantInsurance = new GrantInsurance();
        }

        final GrantContract grantContract = rootDomainObject.readGrantContractByOID(infoGrantInsurance
                .getInfoGrantContract().getIdInternal());

        grantInsurance.setDateBeginInsurance(infoGrantInsurance.getDateBeginInsurance());
        if (infoGrantInsurance.getDateEndInsurance() == null) {
            final List grantContractRegimeList = grantContract
                    .readGrantContractRegimeByGrantContractAndState(InfoGrantContractRegime
                            .getActiveState());
            final GrantContractRegime grantContractRegime = (GrantContractRegime) grantContractRegimeList
                    .get(0);
            grantInsurance.setDateEndInsurance(grantContractRegime.getDateEndContract());
        } else {
            grantInsurance.setDateEndInsurance(infoGrantInsurance.getDateEndInsurance());
        }

        grantInsurance.setGrantContract(grantContract);

        final GrantPaymentEntity grantPaymentEntity = rootDomainObject
                .readGrantPaymentEntityByOID(infoGrantInsurance.getInfoGrantPaymentEntity()
                        .getIdInternal());
        grantInsurance.setGrantPaymentEntity(grantPaymentEntity);

        grantInsurance.setTotalValue(InfoGrantInsurance.calculateTotalValue(grantInsurance
                .getDateBeginInsurance(), grantInsurance.getDateEndInsurance()));
    }

}
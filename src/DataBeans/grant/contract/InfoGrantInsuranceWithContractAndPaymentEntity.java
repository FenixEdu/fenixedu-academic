/*
 * Created on Jun 26, 2004
 */
package DataBeans.grant.contract;

import Dominio.grant.contract.GrantInsurance;
import Dominio.grant.contract.IGrantInsurance;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantInsuranceWithContractAndPaymentEntity extends InfoGrantInsurance {

    public void copyFromDomain(IGrantInsurance grantInsurance) {
        super.copyFromDomain(grantInsurance);
        if (grantInsurance != null) {
            setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain(grantInsurance.getGrantContract()));
            setInfoGrantPaymentEntity(InfoGrantPaymentEntity.newInfoFromDomain(grantInsurance
                    .getGrantPaymentEntity()));
        }
    }

    public static InfoGrantInsurance newInfoFromDomain(IGrantInsurance grantInsurance) {
        InfoGrantInsuranceWithContractAndPaymentEntity infoGrantInsuranceWithContract = null;
        if (grantInsurance != null) {
            infoGrantInsuranceWithContract = new InfoGrantInsuranceWithContractAndPaymentEntity();
            infoGrantInsuranceWithContract.copyFromDomain(grantInsurance);
        }
        return infoGrantInsuranceWithContract;
    }

    public void copyToDomain(InfoGrantInsurance infoGrantInsurance, IGrantInsurance grantInsurance) {
        super.copyToDomain(infoGrantInsurance, grantInsurance);

        grantInsurance.setGrantContract(InfoGrantContract.newDomainFromInfo(infoGrantInsurance
                .getInfoGrantContract()));
        grantInsurance.setGrantPaymentEntity(InfoGrantPaymentEntity.newDomainFromInfo(infoGrantInsurance
                .getInfoGrantPaymentEntity()));
    }

    public static IGrantInsurance newDomainFromInfo(InfoGrantInsurance infoGrantInsurance) {
        IGrantInsurance grantInsurance = null;
        InfoGrantInsuranceWithContractAndPaymentEntity infoGrantInsuranceWithContractAndPaymentEntity = null;
        if (infoGrantInsurance != null) {
            grantInsurance = new GrantInsurance();
            infoGrantInsuranceWithContractAndPaymentEntity = new InfoGrantInsuranceWithContractAndPaymentEntity();
            infoGrantInsuranceWithContractAndPaymentEntity.copyToDomain(infoGrantInsurance,
                    grantInsurance);
        }
        return grantInsurance;
    }
}
/*
 * Created on Jun 25, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoTeacherWithPerson;
import Dominio.grant.contract.IGrantContractRegime;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractRegimeWithTeacherAndContract extends
        InfoGrantContractRegime {

    public void copyFromDomain(IGrantContractRegime grantContractRegime) {
        super.copyFromDomain(grantContractRegime);
        if (grantContractRegime != null) {
            setInfoTeacher(InfoTeacherWithPerson
                    .newInfoFromDomain(grantContractRegime.getTeacher()));

            if (grantContractRegime.getGrantContract() != null) {
                setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                        .newInfoFromDomain(grantContractRegime
                                .getGrantContract()));
            }
        }
    }

    public static InfoGrantContractRegime newInfoFromDomain(
            IGrantContractRegime grantContractRegime) {
        InfoGrantContractRegimeWithTeacherAndContract infoGrantContractRegime = null;
        if (grantContractRegime != null) {
            infoGrantContractRegime = new InfoGrantContractRegimeWithTeacherAndContract();
            infoGrantContractRegime.copyFromDomain(grantContractRegime);
        }
        return infoGrantContractRegime;
    }

}

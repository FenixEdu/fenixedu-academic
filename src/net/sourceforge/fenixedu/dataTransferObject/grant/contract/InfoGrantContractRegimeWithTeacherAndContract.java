/*
 * Created on Jun 25, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractRegimeWithTeacherAndContract extends InfoGrantContractRegime {

    public void copyFromDomain(GrantContractRegime grantContractRegime) {
        super.copyFromDomain(grantContractRegime);
        if (grantContractRegime != null) {
        	
            setInfoTeacher(InfoTeacher.newInfoFromDomain(grantContractRegime.getTeacher()));
            if (grantContractRegime.getGrantContract().getGrantCostCenter()!=null)
                setGrantCostCenterInfo(InfoGrantCostCenter.newInfoFromDomain(grantContractRegime.getGrantContract().getGrantCostCenter()));
           
            if (grantContractRegime.getGrantContract() != null) {
                setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                        .newInfoFromDomain(grantContractRegime.getGrantContract()));
            }
        }
    }

    public static InfoGrantContractRegime newInfoFromDomain(GrantContractRegime grantContractRegime) {
        InfoGrantContractRegimeWithTeacherAndContract infoGrantContractRegime = null;
        if (grantContractRegime != null) {	
            infoGrantContractRegime = new InfoGrantContractRegimeWithTeacherAndContract();
            infoGrantContractRegime.copyFromDomain(grantContractRegime);
        }  
        return infoGrantContractRegime;
    }

}

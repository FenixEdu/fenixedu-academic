/*
 * Created on Jun 25, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractRegimeWithTeacherAndContract extends InfoGrantContractRegime {

    public void copyFromDomain(IGrantContractRegime grantContractRegime) {
        super.copyFromDomain(grantContractRegime);
        if (grantContractRegime != null) {
        	
            setInfoTeacher(InfoTeacherWithPerson.newInfoFromDomain(grantContractRegime.getTeacher()));
            if (grantContractRegime.getGrantContract().getGrantCostCenter()!=null)
                setGrantCostCenterInfo(InfoGrantCostCenter.newInfoFromDomain(grantContractRegime.getGrantContract().getGrantCostCenter()));
           
            if (grantContractRegime.getGrantContract() != null) {
                setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                        .newInfoFromDomain(grantContractRegime.getGrantContract()));
            }
        }
    }

    public static InfoGrantContractRegime newInfoFromDomain(IGrantContractRegime grantContractRegime) {
        InfoGrantContractRegimeWithTeacherAndContract infoGrantContractRegime = null;
        if (grantContractRegime != null) {	
            infoGrantContractRegime = new InfoGrantContractRegimeWithTeacherAndContract();
            infoGrantContractRegime.copyFromDomain(grantContractRegime);
        }  
        return infoGrantContractRegime;
    }

}

/*
 * Created on Jun 25, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoTeacherWithPerson;
import Dominio.grant.contract.GrantContractRegime;
import Dominio.grant.contract.IGrantContractRegime;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractRegimeWithTeacherAndContract extends InfoGrantContractRegime 
{

    public void copyFromDomain(IGrantContractRegime grantContractRegime) 
    {
        super.copyFromDomain(grantContractRegime);
        if (grantContractRegime != null) {
            setInfoTeacher(InfoTeacherWithPerson.newInfoFromDomain(grantContractRegime.getTeacher()));

            if (grantContractRegime.getGrantContract() != null) {
                setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                        .newInfoFromDomain(grantContractRegime.getGrantContract()));
            }
        }
    }

    public static InfoGrantContractRegime newInfoFromDomain(IGrantContractRegime grantContractRegime) 
    {
        InfoGrantContractRegimeWithTeacherAndContract infoGrantContractRegime = null;
        if (grantContractRegime != null) {
            infoGrantContractRegime = new InfoGrantContractRegimeWithTeacherAndContract();
            infoGrantContractRegime.copyFromDomain(grantContractRegime);
        }
        return infoGrantContractRegime;
    }

    public void copyToDomain(InfoGrantContractRegime infoGrantContractRegime,
            IGrantContractRegime grantContractRegime) 
    {
        super.copyToDomain(infoGrantContractRegime, grantContractRegime);

        grantContractRegime.setGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                .newDomainFromInfo(infoGrantContractRegime.getInfoGrantContract()));
        grantContractRegime.setTeacher(InfoTeacherWithPerson.newDomainFromInfo(infoGrantContractRegime
                .getInfoTeacher()));
    }

    public static IGrantContractRegime newDomainFromInfo(InfoGrantContractRegime infoGrantContractRegime) 
    {
        IGrantContractRegime grantContractRegime = null;
        InfoGrantContractRegimeWithTeacherAndContract infoGrantContractRegimeWithTeacherAndContract = null;
        if (infoGrantContractRegime != null) {
            grantContractRegime = new GrantContractRegime();
            infoGrantContractRegimeWithTeacherAndContract = new InfoGrantContractRegimeWithTeacherAndContract();
            infoGrantContractRegimeWithTeacherAndContract.copyToDomain(infoGrantContractRegime,
                    grantContractRegime);
        }
        return grantContractRegime;
    }
}
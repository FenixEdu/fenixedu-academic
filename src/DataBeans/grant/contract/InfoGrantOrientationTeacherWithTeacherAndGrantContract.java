/*
 * Created on Jun 18, 2004
 *
 */
package DataBeans.grant.contract;

import DataBeans.InfoTeacherWithPerson;
import Dominio.grant.contract.GrantOrientationTeacher;
import Dominio.grant.contract.IGrantOrientationTeacher;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantOrientationTeacherWithTeacherAndGrantContract extends
        InfoGrantOrientationTeacher {

    public void copyFromDomain(IGrantOrientationTeacher grantOrientationTeacher) {
        super.copyFromDomain(grantOrientationTeacher);
        if (grantOrientationTeacher != null) {

            setGrantContractInfo(InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain(grantOrientationTeacher
                            .getGrantContract()));
            setOrientationTeacherInfo(InfoTeacherWithPerson
                    .newInfoFromDomain(grantOrientationTeacher
                            .getOrientationTeacher()));
        }
    }

    public static InfoGrantOrientationTeacher newInfoFromDomain(
            IGrantOrientationTeacher grantOrientationTeacher) {
        InfoGrantOrientationTeacherWithTeacherAndGrantContract infoGrantOrientationTeacherWithTeacherAndGrantContract = null;
        if (grantOrientationTeacher != null) {
            infoGrantOrientationTeacherWithTeacherAndGrantContract = new InfoGrantOrientationTeacherWithTeacherAndGrantContract();
            infoGrantOrientationTeacherWithTeacherAndGrantContract
                    .copyFromDomain(grantOrientationTeacher);
        }
        return infoGrantOrientationTeacherWithTeacherAndGrantContract;
    }

    public void copyToDomain(InfoGrantOrientationTeacher infoGrantOrientationTeacher,
            IGrantOrientationTeacher grantOrientationTeacher) 
    {
        super.copyToDomain(infoGrantOrientationTeacher, grantOrientationTeacher);

        grantOrientationTeacher.setOrientationTeacher(InfoTeacherWithPerson
                .newDomainFromInfo(infoGrantOrientationTeacher.getOrientationTeacherInfo()));
        grantOrientationTeacher.setGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                .newDomainFromInfo(infoGrantOrientationTeacher.getGrantContractInfo()));
    }

    public static IGrantOrientationTeacher newDomainFromInfo(
            InfoGrantOrientationTeacher infoGrantOrientationTeacher)
    {
        IGrantOrientationTeacher grantOrientationTeacher = null;
        InfoGrantOrientationTeacherWithTeacherAndGrantContract infoGrantOrientationTeacherWithTeacherAndGrantContract = null;
        if (infoGrantOrientationTeacher != null) {
            grantOrientationTeacher = new GrantOrientationTeacher();
            infoGrantOrientationTeacherWithTeacherAndGrantContract = new InfoGrantOrientationTeacherWithTeacherAndGrantContract();
            infoGrantOrientationTeacherWithTeacherAndGrantContract.copyToDomain(
                    infoGrantOrientationTeacher, grantOrientationTeacher);
        }
        return grantOrientationTeacher;
    }
}

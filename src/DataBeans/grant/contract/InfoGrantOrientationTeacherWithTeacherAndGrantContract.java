/*
 * Created on Jun 18, 2004
 *
 */
package DataBeans.grant.contract;

import DataBeans.InfoTeacherWithPerson;
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

}

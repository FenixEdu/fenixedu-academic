/*
 * Created on Jun 18, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantOrientationTeacherWithTeacherAndGrantContract extends InfoGrantOrientationTeacher {

    public void copyFromDomain(GrantOrientationTeacher grantOrientationTeacher) {
        super.copyFromDomain(grantOrientationTeacher);
        if (grantOrientationTeacher != null) {

            setGrantContractInfo(InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain(grantOrientationTeacher.getGrantContract()));
            setOrientationTeacherInfo(InfoTeacher.newInfoFromDomain(grantOrientationTeacher
                    .getOrientationTeacher()));
        }
    }

    public static InfoGrantOrientationTeacher newInfoFromDomain(
            GrantOrientationTeacher grantOrientationTeacher) {
        InfoGrantOrientationTeacherWithTeacherAndGrantContract infoGrantOrientationTeacherWithTeacherAndGrantContract = null;
        if (grantOrientationTeacher != null) {
            infoGrantOrientationTeacherWithTeacherAndGrantContract = new InfoGrantOrientationTeacherWithTeacherAndGrantContract();
            infoGrantOrientationTeacherWithTeacherAndGrantContract
                    .copyFromDomain(grantOrientationTeacher);
        }
        return infoGrantOrientationTeacherWithTeacherAndGrantContract;
    }

}

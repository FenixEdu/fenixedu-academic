/*
 * Created on Jun 18, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.owner;

import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;

public class InfoGrantOwnerWithPerson extends InfoGrantOwner {

    public void copyFromDomain(GrantOwner grantOwner) {
        super.copyFromDomain(grantOwner);
        if (grantOwner != null) {
            setPersonInfo(InfoPersonWithInfoCountry.newInfoFromDomain(grantOwner.getPerson()));
        }
    }

    public static InfoGrantOwner newInfoFromDomain(GrantOwner grantOwner) {
        InfoGrantOwnerWithPerson infoGrantOwner = null;
        if (grantOwner != null) {
            infoGrantOwner = new InfoGrantOwnerWithPerson();
            infoGrantOwner.copyFromDomain(grantOwner);
        }
        return infoGrantOwner;
    }
}

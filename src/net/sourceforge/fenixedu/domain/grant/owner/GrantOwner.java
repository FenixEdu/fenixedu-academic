/*
 * Created on 27/Out/2003
 * 
 */
package net.sourceforge.fenixedu.domain.grant.owner;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantOwner extends GrantOwner_Base {

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IGrantOwner) {
            IGrantOwner grantOwner = (IGrantOwner) obj;
            result = (((getNumber().equals(grantOwner.getNumber())) && (getPerson().equals(grantOwner
                    .getPerson()))));
        }
        return result;
    }

}

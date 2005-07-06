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
        if (obj instanceof IGrantOwner) {
            final IGrantOwner grantOwner = (IGrantOwner) obj;
            return this.getIdInternal().equals(grantOwner.getIdInternal());
        }
        return false;
    }

}

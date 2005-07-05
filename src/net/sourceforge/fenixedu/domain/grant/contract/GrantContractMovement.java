/*
 * Created on 3/Jul/2004
 * 
 */
package net.sourceforge.fenixedu.domain.grant.contract;


/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantContractMovement extends GrantContractMovement_Base {

    public boolean equals(Object obj) {
        if (obj instanceof IGrantContractMovement) {
            final IGrantContractMovement grantContractMovement = (IGrantContractMovement) obj;
            return this.getIdInternal().equals(grantContractMovement.getIdInternal());
        }
        return false;
    }
}
/*
 * Created on 18/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.domain.grant.contract;


/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantContract extends GrantContract_Base {

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IGrantContract) {
            IGrantContract grantContract = (IGrantContract) obj;
            result = (((getContractNumber().equals(grantContract.getContractNumber())) && (getGrantOwner()
                    .equals(grantContract.getGrantOwner()))));
        }
        return result;
    }
	
}
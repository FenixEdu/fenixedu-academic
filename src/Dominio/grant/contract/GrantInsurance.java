/*
 * Created on Jun 26, 2004
 */
package Dominio.grant.contract;

import Dominio.DomainObject;


/**
 * @author Barbosa	
 * @author Pica
 */
public class GrantInsurance extends DomainObject implements IGrantInsurance {

    private Integer state;
    
    private IGrantContract grantContract;
    private Integer keyGrantContract;
    
    public GrantInsurance() {
        super();
    }

    /**
     * @param idInternal
     */
    public GrantInsurance(Integer idInternal) {
        super(idInternal);
    }
    
    
    /**
     * @return Returns the state.
     */
    public Integer getState() {
        return state;
    }
    /**
     * @param state The state to set.
     */
    public void setState(Integer state) {
        this.state = state;
    }
    /**
     * @return Returns the grantContract.
     */
    public IGrantContract getGrantContract() {
        return grantContract;
    }
    /**
     * @param grantContract The grantContract to set.
     */
    public void setGrantContract(IGrantContract grantContract) {
        this.grantContract = grantContract;
    }
    /**
     * @return Returns the keyGrantContract.
     */
    public Integer getKeyGrantContract() {
        return keyGrantContract;
    }
    /**
     * @param keyGrantContract The keyGrantContract to set.
     */
    public void setKeyGrantContract(Integer keyGrantContract) {
        this.keyGrantContract = keyGrantContract;
    }
    
    

}

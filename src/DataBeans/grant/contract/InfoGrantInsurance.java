/*
 * Created on Jun 26, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoObject;
import Dominio.grant.contract.IGrantInsurance;


/**
 * @author Barbosa
 * @author Pica
 */
public class InfoGrantInsurance extends InfoObject {

    private Integer state;
    private InfoGrantContract infoGrantContract;

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
     * @return Returns the infoGrantContract.
     */
    public InfoGrantContract getInfoGrantContract() {
        return infoGrantContract;
    }
    /**
     * @param infoGrantContract The infoGrantContract to set.
     */
    public void setInfoGrantContract(InfoGrantContract infoGrantContract) {
        this.infoGrantContract = infoGrantContract;
    }
    public InfoGrantInsurance() {
        super();
    }
    
    /**
     * @param GrantInsurance
     */
    public void copyFromDomain(IGrantInsurance grantInsurance)
    {
    	super.copyFromDomain(grantInsurance);
    	if (grantInsurance != null)
    	{
    	    setState(grantInsurance.getState());
    		//TODO.. copy properties
    	}
    }
    /**
     * @param GrantInsrance
     * @return
     */
    public static InfoGrantInsurance newInfoFromDomain(IGrantInsurance grantInsurance)
    {
    	InfoGrantInsurance infoGrantInsurance = null;
    	if (grantInsurance != null)
    	{
    		infoGrantInsurance = new InfoGrantInsurance();
    		infoGrantInsurance.copyFromDomain(grantInsurance);
    	}
    	return infoGrantInsurance;
    }
    

}

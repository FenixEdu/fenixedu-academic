/*
 * Created on Jun 27, 2004
 */
package DataBeans.grant.list;

import java.util.Date;


/**
 * @author Barbosa
 * @author Pica
 */
public class InfoSpanByCriteriaListGrantContract extends InfoSpanListGrantOwner {

    private Boolean justActiveContract;
    private Boolean justDesactiveContract;
    private Date beginContract;
    private Date endContract;
    private Integer grantTypeId;
    
    
    /**
     * @return Returns the beginContract.
     */
    public Date getBeginContract() {
        return beginContract;
    }
    /**
     * @param beginContract The beginContract to set.
     */
    public void setBeginContract(Date beginContract) {
        this.beginContract = beginContract;
    }
    /**
     * @return Returns the endContract.
     */
    public Date getEndContract() {
        return endContract;
    }
    /**
     * @param endContract The endContract to set.
     */
    public void setEndContract(Date endContract) {
        this.endContract = endContract;
    }
    /**
     * @return Returns the justActiveContract.
     */
    public Boolean getJustActiveContract() {
        return justActiveContract;
    }
    /**
     * @param justActiveContract The justActiveContract to set.
     */
    public void setJustActiveContract(Boolean justActiveContract) {
        this.justActiveContract = justActiveContract;
    }
    /**
     * @return Returns the justDesactiveContract.
     */
    public Boolean getJustDesactiveContract() {
        return justDesactiveContract;
    }
    /**
     * @param justDesactiveContract The justDesactiveContract to set.
     */
    public void setJustDesactiveContract(Boolean justDesactiveContract) {
        this.justDesactiveContract = justDesactiveContract;
    }
    /**
     * @return Returns the grantTypeId.
     */
    public Integer getGrantTypeId() {
        return grantTypeId;
    }
    /**
     * @param grantTypeId The grantTypeId to set.
     */
    public void setGrantTypeId(Integer grantTypeId) {
        this.grantTypeId = grantTypeId;
    }
}

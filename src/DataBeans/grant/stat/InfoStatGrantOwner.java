/*
 * Created on Jul 5, 2004
 *
 */
package DataBeans.grant.stat;

import java.util.Date;

import DataBeans.InfoObject;

/**
 * @author Barbosa
 * @author Pica
 *
 */
public class InfoStatGrantOwner extends InfoObject {
    private Date dateBeginContract;
    private Date dateEndContract;
    private Boolean justActiveContracts;
    private Boolean justInactiveContracts;
    private Integer grantType;
    private String grantTypeSigla;
    
    
    /**
     * @return Returns the dateBeginContract.
     */
    public Date getDateBeginContract() {
        return dateBeginContract;
    }
    /**
     * @param dateBeginContract The dateBeginContract to set.
     */
    public void setDateBeginContract(Date dateBeginContract) {
        this.dateBeginContract = dateBeginContract;
    }
    /**
     * @return Returns the dateEndContract.
     */
    public Date getDateEndContract() {
        return dateEndContract;
    }
    /**
     * @param dateEndContract The dateEndContract to set.
     */
    public void setDateEndContract(Date dateEndContract) {
        this.dateEndContract = dateEndContract;
    }
    /**
     * @return Returns the grantType.
     */
    public Integer getGrantType() {
        return grantType;
    }
    /**
     * @param grantType The grantType to set.
     */
    public void setGrantType(Integer grantType) {
        this.grantType = grantType;
    }
    /**
     * @return Returns the justActiveContracts.
     */
    public Boolean getJustActiveContracts() {
        return justActiveContracts;
    }
    /**
     * @param justActiveContracts The justActiveContracts to set.
     */
    public void setJustActiveContracts(Boolean justActiveContracts) {
        this.justActiveContracts = justActiveContracts;
    }
    /**
     * @return Returns the justInactiveContracts.
     */
    public Boolean getJustInactiveContracts() {
        return justInactiveContracts;
    }
    /**
     * @param justInactiveContracts The justInactiveContracts to set.
     */
    public void setJustInactiveContracts(Boolean justInactiveContracts) {
        this.justInactiveContracts = justInactiveContracts;
    }
    /**
     * @return Returns the grantTypeSigla.
     */
    public String getGrantTypeSigla() {
        return grantTypeSigla;
    }
    /**
     * @param grantTypeSigla The grantTypeSigla to set.
     */
    public void setGrantTypeSigla(String grantTypeSigla) {
        this.grantTypeSigla = grantTypeSigla;
    }
}

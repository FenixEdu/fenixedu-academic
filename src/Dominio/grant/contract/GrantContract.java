/*
 * Created on 18/Nov/2003
 * 
 */
package Dominio.grant.contract;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.grant.owner.IGrantOwner;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class GrantContract extends DomainObject implements IGrantContract
{
    private Integer contractNumber;
    private Date dateBeginContract;
    private Date dateEndContract;
    private String endContractMotive;
    private IGrantOwner grantOwner;
    private IGrantType grantType;
    private Integer keyGrantOwner;
    private Integer keyGrantType;

    /** 
     * Constructor
     */
    public GrantContract()
    {
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof IGrantContract)
        {
            IGrantContract grantContract = (IGrantContract) obj;
            result =
                (((this.contractNumber.equals(grantContract.getContractNumber()))
                    && (this.grantOwner.equals(grantContract.getGrantOwner()))));
        }
        return result;
    }

    /**
     * @return
     */
    public Integer getContractNumber()
    {
        return contractNumber;
    }

    /**
     * @param contractNumber
     */
    public void setContractNumber(Integer contractNumber)
    {
        this.contractNumber = contractNumber;
    }

    /**
     * @return
     */
    public Date getDateBeginContract()
    {
        return dateBeginContract;
    }

    /**
     * @param dateBeginContract
     */
    public void setDateBeginContract(Date dateBeginContract)
    {
        this.dateBeginContract = dateBeginContract;
    }

    /**
     * @return
     */
    public Date getDateEndContract()
    {
        return dateEndContract;
    }

    /**
     * @param dateEndContract
     */
    public void setDateEndContract(Date dateEndContract)
    {
        this.dateEndContract = dateEndContract;
    }

    /**
     * @return
     */
    public String getEndContractMotive()
    {
        return endContractMotive;
    }

    /**
     * @param endContractMotive
     */
    public void setEndContractMotive(String endContractMotive)
    {
        this.endContractMotive = endContractMotive;
    }

    /**
     * @return
     */
    public IGrantOwner getGrantOwner()
    {
        return grantOwner;
    }

    /**
     * @param grantOwner
     */
    public void setGrantOwner(IGrantOwner grantOwner)
    {
        this.grantOwner = grantOwner;
    }

    /**
     * @return
     */
    public Integer getKeyGrantOwner()
    {
        return keyGrantOwner;
    }

    /**
     * @param keyGrantOwner
     */
    public void setKeyGrantOwner(Integer keyGrantOwner)
    {
        this.keyGrantOwner = keyGrantOwner;
    }
    /**
     * @return
     */
    public Integer getKeyGrantType()
    {
        return keyGrantType;
    }

    /**
     * @param keyGrantType
     */
    public void setKeyGrantType(Integer keyGrantType)
    {
        this.keyGrantType = keyGrantType;
    }

    /**
     * @return
     */
    public IGrantType getGrantType()
    {
        return grantType;
    }

    /**
     * @param grantType
     */
    public void setGrantType(IGrantType grantType)
    {
        this.grantType = grantType;
    }

}
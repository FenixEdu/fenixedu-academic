/*
 * Created on Jun 26, 2004
 */
package DataBeans.grant.contract;

import java.util.Date;

import DataBeans.InfoObject;
import Dominio.grant.contract.IGrantInsurance;


/**
 * @author Barbosa
 * @author Pica
 */
public class InfoGrantInsurance extends InfoObject {
	
	//This is the value of a day of an insurance. It's static for all contracts.
	private static final double dayValueOfInsurance = 63.35;
	
    private Date dateEndInsurance;
    private Date dateBeginInsurance;
    private Double totalValue;
    
    private InfoGrantContract infoGrantContract;
    private InfoGrantPaymentEntity infoGrantPaymentEntity;

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
	 * @return Returns the dateBeginInsurance.
	 */
	public Date getDateBeginInsurance()
	{
		return dateBeginInsurance;
	}
	/**
	 * @param dateBeginInsurance The dateBeginInsurance to set.
	 */
	public void setDateBeginInsurance(Date dateBeginInsurance)
	{
		this.dateBeginInsurance = dateBeginInsurance;
	}
	/**
	 * @return Returns the dateEndInsurance.
	 */
	public Date getDateEndInsurance()
	{
		return dateEndInsurance;
	}
	/**
	 * @param dateEndInsurance The dateEndInsurance to set.
	 */
	public void setDateEndInsurance(Date dateEndInsurance)
	{
		this.dateEndInsurance = dateEndInsurance;
	}
	/**
	 * @return Returns the totalValue.
	 */
	public Double getTotalValue()
	{
		return totalValue;
	}
	/**
	 * @param totalValue The totalValue to set.
	 */
	public void setTotalValue(Double totalValue)
	{
		this.totalValue = totalValue;
	}
	/**
	 * @return Returns the infoGrantPaymentEntity.
	 */
	public InfoGrantPaymentEntity getInfoGrantPaymentEntity()
	{
		return infoGrantPaymentEntity;
	}
	/**
	 * @param infoGrantPaymentEntity The infoGrantPaymentEntity to set.
	 */
	public void setInfoGrantPaymentEntity(InfoGrantPaymentEntity infoGrantPaymentEntity)
	{
		this.infoGrantPaymentEntity = infoGrantPaymentEntity;
	}
	
    /**
     * @param GrantInsurance
     */
    public void copyFromDomain(IGrantInsurance grantInsurance)
    {
    	super.copyFromDomain(grantInsurance);
    	if (grantInsurance != null)
    	{
    		setDateBeginInsurance(grantInsurance.getDateBeginInsurance());
    		setDateEndInsurance(grantInsurance.getDateEndInsurance());
    		setTotalValue(grantInsurance.getTotalValue());
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

	public Double getDayValueOfInsurance()
	{
		return new Double(dayValueOfInsurance);
	}

	/**
	 * If the date begin and date end is set, calculates the number of
	 * days and multiply by the constant value 'dayValueOfInsurance'
	 */
	public Double calculateTotalValue() {
		if(dateBeginInsurance != null && dateEndInsurance != null && dateBeginInsurance.before(dateEndInsurance)) {
			// TODO Fazer as contas para descobrir o numero de dias
			double numberOfDays = 0;
			
			return new Double(numberOfDays * getDayValueOfInsurance().doubleValue());
		}
		return new Double(0);		
	}

	
}

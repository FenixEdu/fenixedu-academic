/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;


import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.projectsManagement.FormatDouble;

/**
 * @author Barbosa
 * @author Pica
 */
public class InfoGrantInsurance extends InfoObject {

    //This is the value of a day of an insurance. It's static for all
    // contracts.
    private static final double dayValueOfInsurance = 63.35;

    private Date dateEndInsurance;

    private Date dateBeginInsurance;

    private Double totalValue;

    private InfoGrantContract infoGrantContract;

    private InfoGrantPaymentEntity infoGrantPaymentEntity;

    public InfoGrantInsurance() {
        super();
    }

    /**
     * @return Returns the infoGrantContract.
     */
    public InfoGrantContract getInfoGrantContract() {
        return infoGrantContract;
    }

    /**
     * @param infoGrantContract
     *            The infoGrantContract to set.
     */
    public void setInfoGrantContract(InfoGrantContract infoGrantContract) {
        this.infoGrantContract = infoGrantContract;
    }

    /**
     * @return Returns the dateBeginInsurance.
     */
    public Date getDateBeginInsurance() {
        return dateBeginInsurance;
    }

    /**
     * @param dateBeginInsurance
     *            The dateBeginInsurance to set.
     */
    public void setDateBeginInsurance(Date dateBeginInsurance) {
        this.dateBeginInsurance = dateBeginInsurance;
    }

    /**
     * @return Returns the dateEndInsurance.
     */
    public Date getDateEndInsurance() {
        return dateEndInsurance;
    }

    /**
     * @param dateEndInsurance
     *            The dateEndInsurance to set.
     */
    public void setDateEndInsurance(Date dateEndInsurance) {
        this.dateEndInsurance = dateEndInsurance;
    }

    /**
     * @return Returns the totalValue.
     */
    public Double getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue
     *            The totalValue to set.
     */
    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    /**
     * @return Returns the infoGrantPaymentEntity.
     */
    public InfoGrantPaymentEntity getInfoGrantPaymentEntity() {
        return infoGrantPaymentEntity;
    }

    /**
     * @param infoGrantPaymentEntity
     *            The infoGrantPaymentEntity to set.
     */
    public void setInfoGrantPaymentEntity(InfoGrantPaymentEntity infoGrantPaymentEntity) {
        this.infoGrantPaymentEntity = infoGrantPaymentEntity;
    }

    public static Double getDayValueOfInsurance() {
        return new Double(dayValueOfInsurance);
    }

    /**
     * If the date begin and date end is set, calculates the number of days and
     * multiply by the constant value 'dayValueOfInsurance'
     */
    public static Double calculateTotalValue(Date dateBegin, Date dateEnd) {
      
        if (dateBegin != null && dateEnd != null && dateBegin.before(dateEnd)) {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            Integer numberOfMonth=0;
            Integer numberOfYear=0;
            Integer value=1;
            c1.setTime(dateBegin);
            c2.setTime(dateEnd);
           
            numberOfYear = 12*(c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR));  
            numberOfMonth = (c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH))+1;
            value=numberOfYear+numberOfMonth;
            return new Double(FormatDouble.round(((getDayValueOfInsurance().doubleValue())/12)*value));
           
          
        }
        return new Double(0);
    }

    public void setTotalValue() {
        totalValue = InfoGrantInsurance.calculateTotalValue(this.dateBeginInsurance,
                this.dateEndInsurance);
    }

    public void copyFromDomain(IGrantInsurance grantInsurance) {
        super.copyFromDomain(grantInsurance);
        if (grantInsurance != null) {
            setDateBeginInsurance(grantInsurance.getDateBeginInsurance());
            setDateEndInsurance(grantInsurance.getDateEndInsurance());
            setTotalValue(grantInsurance.getTotalValue());
        }
    }

    public static InfoGrantInsurance newInfoFromDomain(IGrantInsurance grantInsurance) {
        InfoGrantInsurance infoGrantInsurance = null;
        if (grantInsurance != null) {
            infoGrantInsurance = new InfoGrantInsurance();
            infoGrantInsurance.copyFromDomain(grantInsurance);
        }
        return infoGrantInsurance;
    }

    public void copyToDomain(InfoGrantInsurance infoGrantInsurance, IGrantInsurance grantInsurance) throws ExcepcaoPersistencia {
        super.copyToDomain(infoGrantInsurance, grantInsurance);

        grantInsurance.setDateBeginInsurance(infoGrantInsurance.getDateBeginInsurance());
        grantInsurance.setDateEndInsurance(infoGrantInsurance.getDateEndInsurance());
        grantInsurance.setTotalValue(infoGrantInsurance.getTotalValue());
    }

    public static IGrantInsurance newDomainFromInfo(InfoGrantInsurance infoGrantInsurance) throws ExcepcaoPersistencia {
        IGrantInsurance grantInsurance = null;
        if (infoGrantInsurance != null) {
            grantInsurance = new GrantInsurance();
            infoGrantInsurance.copyToDomain(infoGrantInsurance, grantInsurance);
        }
        return grantInsurance;
    }
    

}
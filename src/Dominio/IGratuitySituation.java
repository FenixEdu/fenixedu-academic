/*
 * Created on 5/Jan/2004
 *  
 */
package Dominio;

import java.util.Date;
import java.util.List;

import Util.ExemptionGratuityType;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 */
public interface IGratuitySituation extends IDomainObject {

    public Double getRemainingValue();

    public Double getTotalValue();

    public Integer getExemptionPercentage();

    public ExemptionGratuityType getExemptionType();

    public String getExemptionDescription();

    public IGratuityValues getGratuityValues();

    public IStudentCurricularPlan getStudentCurricularPlan();

    public List getTransactionList();

    public IEmployee getEmployee();

    public Date getWhen();

    public void setRemainingValue(Double remainingValue);

    public void setTotalValue(Double totalValue);

    public void setExemptionPercentage(Integer exemptionPercentage);

    public void setExemptionType(ExemptionGratuityType exemptionType);

    public void setExemptionDescription(String exemptionDescription);

    public void setGratuityValues(IGratuityValues gratuity);

    public void setStudentCurricularPlan(IStudentCurricularPlan student);

    public void setTransactionList(List transactionList);

    public void setEmployee(IEmployee funcionario);

    public void setWhen(Date date);

}
/*
 * Created on 9/Jan/2004
 *
 */
package Dominio;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 *  
 */

public interface IGratuityValues extends IDomainObject {
    public Double getAnualValue();

    public Double getScholarShipValue();

    public Double getCourseValue();

    public Double getCreditValue();

    public Double getFinalProofValue();

    public Boolean getProofRequestPayment();

    public Date getStartPayment();

    public Date getEndPayment();

    public ICursoExecucao getExecutionDegree();

    public IEmployee getEmployee();

    public Date getWhen();

    public List getPaymentPhaseList();

    public void setAnualValue(Double anualValue);

    public void setScholarShipValue(Double scholarShipValue);

    public void setCourseValue(Double courseValue);

    public void setCreditValue(Double creditValue);

    public void setFinalProofValue(Double finalProofValue);

    public void setProofRequestPayment(Boolean proofRequestPayment);

    public void setStartPayment(Date startPayment);

    public void setEndPayment(Date endPayment);

    public void setExecutionDegree(ICursoExecucao executionDegree);

    public void setEmployee(IEmployee employee);

    public void setWhen(Date date);

    public void setPaymentPhaseList(List paymentPhaseList);

}
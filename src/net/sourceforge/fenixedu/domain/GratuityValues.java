/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 */
public class GratuityValues extends DomainObject implements IGratuityValues {
    private Double anualValue;

    private Double scholarShipValue;

    private Double finalProofValue;

    private Double courseValue;

    private Double creditValue;

    private Boolean proofRequestPayment;

    private Date startPayment;

    private Date endPayment;

    private IExecutionDegree executionDegree;

    private Integer keyExecutionDegree;

    private Date when;

    private IEmployee employee;

    private Integer keyEmployee;

    private List paymentPhaseList;

    /**
     * @return Returns the when.
     */
    public Date getWhen() {
        return when;
    }

    /**
     * @param when
     *            The when to set.
     */
    public void setWhen(Date when) {
        this.when = when;
    }

    /**
     * @return Returns the anualValue.
     */
    public Double getAnualValue() {
        return anualValue;
    }

    /**
     * @param anualValue
     *            The anualValue to set.
     */
    public void setAnualValue(Double anualValue) {
        this.anualValue = anualValue;
    }

    /**
     * @return Returns the courseValue.
     */
    public Double getCourseValue() {
        return courseValue;
    }

    /**
     * @param courseValue
     *            The courseValue to set.
     */
    public void setCourseValue(Double courseValue) {
        this.courseValue = courseValue;
    }

    /**
     * @return Returns the creditValue.
     */
    public Double getCreditValue() {
        return creditValue;
    }

    /**
     * @param creditValue
     *            The creditValue to set.
     */
    public void setCreditValue(Double creditValue) {
        this.creditValue = creditValue;
    }

    /**
     * @return Returns the endPayment.
     */
    public Date getEndPayment() {
        return endPayment;
    }

    /**
     * @param endPayment
     *            The endPayment to set.
     */
    public void setEndPayment(Date endPayment) {
        this.endPayment = endPayment;
    }

    /**
     * @return Returns the finalProofValue.
     */
    public Double getFinalProofValue() {
        return finalProofValue;
    }

    /**
     * @param finalProofValue
     *            The finalProofValue to set.
     */
    public void setFinalProofValue(Double finalProofValue) {
        this.finalProofValue = finalProofValue;
    }

    /**
     * @return Returns the proofRequestPayment.
     */
    public Boolean getProofRequestPayment() {
        return proofRequestPayment;
    }

    /**
     * @param proofRequestPayment
     *            The proofRequestPayment to set.
     */
    public void setProofRequestPayment(Boolean proofRequestPayment) {
        this.proofRequestPayment = proofRequestPayment;
    }

    /**
     * @return Returns the scholarShipValue.
     */
    public Double getScholarShipValue() {
        return scholarShipValue;
    }

    /**
     * @param scholarShipValue
     *            The scholarShipValue to set.
     */
    public void setScholarShipValue(Double scholarShipValue) {
        this.scholarShipValue = scholarShipValue;
    }

    /**
     * @return Returns the startPayment.
     */
    public Date getStartPayment() {
        return startPayment;
    }

    /**
     * @param startPayment
     *            The startPayment to set.
     */
    public void setStartPayment(Date startPayment) {
        this.startPayment = startPayment;
    }

    /**
     * @return Returns the employee.
     */
    public IEmployee getEmployee() {
        return employee;
    }

    /**
     * @param employee
     *            The employee to set.
     */
    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }

    /**
     * @return Returns the executionDegree.
     */
    public IExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    /**
     * @param executionDegree
     *            The executionDegree to set.
     */
    public void setExecutionDegree(IExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    /**
     * @return Returns the keyEmployee.
     */
    public Integer getKeyEmployee() {
        return keyEmployee;
    }

    /**
     * @param keyEmployee
     *            The keyEmployee to set.
     */
    public void setKeyEmployee(Integer keyEmployee) {
        this.keyEmployee = keyEmployee;
    }

    /**
     * @return Returns the keyExecutionDegree.
     */
    public Integer getKeyExecutionDegree() {
        return keyExecutionDegree;
    }

    /**
     * @param keyExecutionDegree
     *            The keyExecutionDegree to set.
     */
    public void setKeyExecutionDegree(Integer keyExecutionDegree) {
        this.keyExecutionDegree = keyExecutionDegree;
    }

    /**
     * @return Returns the paymentPhaseList.
     */
    public List getPaymentPhaseList() {
        return paymentPhaseList;
    }

    /**
     * @param paymentPhaseList
     *            The paymentPhaseList to set.
     */
    public void setPaymentPhaseList(List paymentPhaseList) {
        this.paymentPhaseList = paymentPhaseList;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result = result.append("[GratuityValues: \n").append("idInternal= ").append(getIdInternal())
                .append("\nanualValue= ").append(getAnualValue()).append("\nscholarShipPart= ").append(
                        getScholarShipValue()).append("\nfinalProofValue= ")
                .append(getFinalProofValue()).append("\ncourseValue= ").append(getCourseValue()).append(
                        "\ncreditValue= ").append(getCreditValue()).append("\nproofRequestPayment= ")
                .append(getProofRequestPayment()).append("\nstartPayment= ").append(getStartPayment())
                .append("\nendPayment= ").append(getEndPayment()).append("]");
        return result.toString();
    }

    public boolean equals(Object object) {
        //TODO: to make
        return true;
    }
}
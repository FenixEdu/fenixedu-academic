/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.GratuityValues;

/**
 * @author Fernanda Quitério 10/Jan/2004
 * 
 */
public class InfoGratuityValues extends InfoObject {
    private Double anualValue;

    private Double scholarShipValue;

    private Double finalProofValue;

    private Double courseValue;

    private Double creditValue;

    private Boolean proofRequestPayment;

    private Date startPayment;

    private Date endPayment;

    private Boolean registrationPayment;

    private InfoExecutionDegree infoExecutionDegree;

    private InfoEmployee infoEmployee;

    private Date when;

    private List<InfoPaymentPhase> infoPaymentPhases;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result =
                result.append("[InfoGratuityValues: \n").append("externalId= ").append(getExternalId()).append("\nanualValue= ")
                        .append(getAnualValue()).append("\nscholarShipPart= ").append(getScholarShipValue())
                        .append("\nfinalProofValue= ").append(getFinalProofValue()).append("\ncourseValue= ")
                        .append(getCourseValue()).append("\ncreditValue= ").append(getCreditValue())
                        .append("\nproofRequestPayment= ").append(getProofRequestPayment()).append("\nstartPayment= ")
                        .append(getStartPayment()).append("\nendPayment= ").append(getEndPayment())
                        .append("\nregistrationPayment= ").append(getRegistrationPayment()).append("]");
        return result.toString();
    }

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
     * @return Returns the registrationPayment.
     */
    public Boolean getRegistrationPayment() {
        return registrationPayment;
    }

    /**
     * @param registrationPayment
     *            The registrationPayment to set.
     */
    public void setRegistrationPayment(Boolean registrationPayment) {
        this.registrationPayment = registrationPayment;
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
     * @return Returns the infoEmployee.
     */
    public InfoEmployee getInfoEmployee() {
        return infoEmployee;
    }

    /**
     * @param infoEmployee
     *            The infoEmployee to set.
     */
    public void setInfoEmployee(InfoEmployee infoEmployee) {
        this.infoEmployee = infoEmployee;
    }

    /**
     * @return Returns the infoExecutionDegree.
     */
    public InfoExecutionDegree getInfoExecutionDegree() {
        return infoExecutionDegree;
    }

    /**
     * @param infoExecutionDegree
     *            The infoExecutionDegree to set.
     */
    public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
        this.infoExecutionDegree = infoExecutionDegree;
    }

    /**
     * @return Returns the infoPaymentPhases.
     */
    public List<InfoPaymentPhase> getInfoPaymentPhases() {
        return infoPaymentPhases;
    }

    /**
     * @param infoPaymentPhases
     *            The infoPaymentPhases to set.
     */
    public void setInfoPaymentPhases(List<InfoPaymentPhase> infoPaymentPhases) {
        this.infoPaymentPhases = infoPaymentPhases;
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

    public void copyFromDomain(GratuityValues gratuityValues) {
        super.copyFromDomain(gratuityValues);
        if (gratuityValues != null) {
            setAnualValue(gratuityValues.getAnualValue());
            setScholarShipValue(gratuityValues.getScholarShipValue());
            setFinalProofValue(gratuityValues.getFinalProofValue());
            setCourseValue(gratuityValues.getCourseValue());
            setCreditValue(gratuityValues.getCreditValue());
            setProofRequestPayment(gratuityValues.getProofRequestPayment());
            setStartPayment(gratuityValues.getStartPayment());
            setEndPayment(gratuityValues.getEndPayment());
            setWhen(gratuityValues.getWhen());
        }
    }

    public static InfoGratuityValues newInfoFromDomain(GratuityValues gratuityValues) {
        InfoGratuityValues infoGratuityValues = null;
        if (gratuityValues != null) {
            infoGratuityValues = new InfoGratuityValues();
            infoGratuityValues.copyFromDomain(gratuityValues);
        }
        return infoGratuityValues;
    }
}
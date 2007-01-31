/*
 * Created on 10/Jan/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.gratuity.ExemptionGratuityType;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoGratuitySituation extends InfoObject {

    private Integer exemptionPercentage;

    private Double exemptionValue;

    private ExemptionGratuityType exemptionType;

    private String exemptionDescription;

    private InfoGratuityValues infoGratuityValues;

    private InfoStudentCurricularPlan infoStudentCurricularPlan;

    private InfoEmployee infoEmployee;

    private Date when;

    private List transactionList;

    private Double payedValue;// attributes auxiliaries for calculations

    private Double remainingValue;

    private Double totalValue;

    private GratuitySituationType situationType; // attributes
                                                        // auxiliaries for

    // calculations

    private String insurancePayed = SessionConstants.NOT_PAYED_INSURANCE; // attributes

    // auxiliaries
    // for
    // calculations

    /**
         * @return Returns the insurancePayed.
         */
    public String getInsurancePayed() {
	return insurancePayed;
    }

    /**
         * @param insurancePayed
         *                The insurancePayed to set.
         */
    public void setInsurancePayed(String insurancePayed) {
	this.insurancePayed = insurancePayed;
    }

    /**
         * @return Returns the situationType.
         */
    public GratuitySituationType getSituationType() {
	return situationType;
    }

    /**
         * @param situationType
         *                The situationType to set.
         */
    public void setSituationType(GratuitySituationType situationType) {
	this.situationType = situationType;
    }

    /**
         * @return Returns the infoEmployee.
         */
    public InfoEmployee getInfoEmployee() {
	return infoEmployee;
    }

    /**
         * @param infoEmployee
         *                The infoEmployee to set.
         */
    public void setInfoEmployee(InfoEmployee infoEmployee) {
	this.infoEmployee = infoEmployee;
    }

    /**
         * @return Returns the when.
         */
    public Date getWhen() {
	return when;
    }

    /**
         * @param when
         *                The when to set.
         */
    public void setWhen(Date when) {
	this.when = when;
    }

    /**
         * @return Returns the exemptionDescription.
         */
    public String getExemptionDescription() {
	return exemptionDescription;
    }

    /**
         * @param exemptionDescription
         *                The exemptionDescription to set.
         */
    public void setExemptionDescription(String exemptionDescription) {
	this.exemptionDescription = exemptionDescription;
    }

    /**
         * @return Returns the exemptionPercentage.
         */
    public Integer getExemptionPercentage() {
	return exemptionPercentage;
    }

    /**
         * @param exemptionPercentage
         *                The exemptionPercentage to set.
         */
    public void setExemptionPercentage(Integer exemptionPercentage) {
	this.exemptionPercentage = exemptionPercentage;
    }

    /**
         * @return Returns the exemptionType.
         */
    public ExemptionGratuityType getExemptionType() {
	return exemptionType;
    }

    /**
         * @param exemptionType
         *                The exemptionType to set.
         */
    public void setExemptionType(ExemptionGratuityType exemptionType) {
	this.exemptionType = exemptionType;
    }

    /**
         * @return Returns the gratuity.
         */
    public InfoGratuityValues getInfoGratuityValues() {
	return infoGratuityValues;
    }

    /**
         * @param gratuity
         *                The gratuity to set.
         */
    public void setInfoGratuityValues(InfoGratuityValues gratuity) {
	this.infoGratuityValues = gratuity;
    }

    /**
         * @return Returns the payedValue.
         */
    public Double getPayedValue() {
	return payedValue;
    }

    /**
         * @param payedValue
         *                The payedValue to set.
         */
    public void setPayedValue(Double payedValue) {
	this.payedValue = payedValue;
    }

    /**
         * @return Returns the remainingValue.
         */
    public Double getRemainingValue() {
	return remainingValue;
    }

    /**
         * @param remainingValue
         *                The remainingValue to set.
         */
    public void setRemainingValue(Double remainingValue) {
	this.remainingValue = remainingValue;
    }

    /**
         * @return Returns the student.
         */
    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
	return infoStudentCurricularPlan;
    }

    /**
         * @param student
         *                The student to set.
         */
    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan studentCurricularPlan) {
	this.infoStudentCurricularPlan = studentCurricularPlan;
    }

    /**
         * @return Returns the transactionList.
         */
    public List getTransactionList() {
	return transactionList;
    }

    /**
         * @param transactionList
         *                The transactionList to set.
         */
    public void setTransactionList(List transactionList) {
	this.transactionList = transactionList;
    }

    /**
         * @return Returns the exemptionValue.
         */
    public Double getExemptionValue() {
	return exemptionValue;
    }

    /**
         * @param exemptionValue
         *                The exemptionValue to set.
         */
    public void setExemptionValue(Double exemptionValue) {
	this.exemptionValue = exemptionValue;
    }

    public String toString() {
	String result = new String();
	result += "[InfoGratuitySituation: exemptionPercentage" + this.exemptionPercentage;
	result += "\nexemptionType: " + this.exemptionType;
	result += "\nexemptionDescription: " + this.exemptionDescription;
	result += "\npayedValue: " + this.payedValue;
	result += "\nremainingValue: " + this.remainingValue;
	result += "\ninfoGratuityValues: " + this.infoGratuityValues;
	result += "\ninfoStudentCurricularPlan: " + this.infoStudentCurricularPlan;
	return result;
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.DomainObject)
         */
    public void copyFromDomain(GratuitySituation gratuitySituation) {
	super.copyFromDomain(gratuitySituation);
	if (gratuitySituation != null) {
	    setExemptionPercentage(gratuitySituation.getExemptionPercentage());
	    setExemptionValue(gratuitySituation.getExemptionValue());
	    setExemptionDescription(gratuitySituation.getExemptionDescription());
	    setExemptionType(gratuitySituation.getExemptionType());
	    // setPayedValue(gratuitySituation.getPayedValue());
	    if (gratuitySituation.getRemainingValue() == null) {
		setRemainingValue(new Double(0));
	    } else {
		setRemainingValue(gratuitySituation.getRemainingValue());
	    }
	    setWhen(gratuitySituation.getWhen());
	}
    }

    public static InfoGratuitySituation newInfoFromDomain(GratuitySituation gratuitySituation) {
	InfoGratuitySituation infoGratuitySituation = null;
	if (gratuitySituation != null) {
	    infoGratuitySituation = new InfoGratuitySituation();
	    infoGratuitySituation.copyFromDomain(gratuitySituation);
	}
	return infoGratuitySituation;
    }

}
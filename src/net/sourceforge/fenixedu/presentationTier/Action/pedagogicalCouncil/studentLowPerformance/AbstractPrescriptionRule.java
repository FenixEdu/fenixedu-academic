package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance.PrescriptionBean.PrescriptionEnum;

public abstract class AbstractPrescriptionRule {

    private ExecutionYear registrationStart;
    private BigDecimal minimumEcts;
    private int numberOfEntriesStudentInSecretary;
    private PrescriptionBean.PrescriptionEnum prescriptionEnum;

    public PrescriptionBean.PrescriptionEnum getPrescriptionEnum() {
	return prescriptionEnum;
    }

    public void setPrescriptionEnum(PrescriptionBean.PrescriptionEnum prescriptionEnum) {
	this.prescriptionEnum = prescriptionEnum;
    }

    public AbstractPrescriptionRule() {

    }

    public AbstractPrescriptionRule(ExecutionYear registrationStart, BigDecimal minimumEcts, int numberOfEntriesStudentInSecretary) {
	this.registrationStart = registrationStart;
	this.minimumEcts = minimumEcts;
	this.numberOfEntriesStudentInSecretary = numberOfEntriesStudentInSecretary;
    }

    public void setRegistrationStart(ExecutionYear registrationStart) {
	this.registrationStart = registrationStart;
    }

    public ExecutionYear getRegistrationStart() {
	return registrationStart;
    }

    public void setMinimumEcts(BigDecimal minimumEcts) {
	this.minimumEcts = minimumEcts;
    }

    public BigDecimal getMinimumEcts() {
	return minimumEcts;
    }

    public int getNumberOfEntriesStudentInSecretary() {
	return numberOfEntriesStudentInSecretary;
    }

    public void setNumberOfEntriesStudentInSecretary(int numberOfEntriesStudentInSecretary) {
	this.numberOfEntriesStudentInSecretary = numberOfEntriesStudentInSecretary;
    }

    public boolean isOcursInMonth() {
	return true;
    }

    public boolean isContains(PrescriptionEnum p) {
	return p.equals(getPrescriptionEnum());
    }

    public boolean isPrescript(BigDecimal ects, int numberOfEntriesStudentInSecretary) {
	return ects.compareTo(getMinimumEcts()) < 0
		&& numberOfEntriesStudentInSecretary == getNumberOfEntriesStudentInSecretary();
    }

    public static List<AbstractPrescriptionRule> readPrescriptionRules(PrescriptionBean prescriptionBean) {
	List<AbstractPrescriptionRule> abstractPrescriptionRules = new LinkedList<AbstractPrescriptionRule>();
	for (AbstractPrescriptionRule abstractPrescriptionRule : getPrescriptionRules()) {
	    if (abstractPrescriptionRule.isContains(prescriptionBean.getSelectedPrescriptionEnum())) {
		abstractPrescriptionRules.add(abstractPrescriptionRule);
	    }
	}
	return abstractPrescriptionRules;
    }

    private static AbstractPrescriptionRule[] getPrescriptionRules() {
	return new AbstractPrescriptionRule[] { new PrescriptionRuleMomentOne(), new PrescriptionRuleMomentTwo(),
		new PrescriptionRuleMomentTree(), new PrescriptionRuleTreeEntries(), new PrescriptionRuleFourEntries(),
		new PrescriptionRuleFiveEntries() };
    }

    public static List<AbstractPrescriptionRule> readProviderPrescriptionRules() {
	List<AbstractPrescriptionRule> abstractPrescriptionRules = new LinkedList<AbstractPrescriptionRule>();
	abstractPrescriptionRules.add(new PrescriptionRuleMomentOne());
	abstractPrescriptionRules.add(new PrescriptionRuleMomentTwo());
	abstractPrescriptionRules.add(new PrescriptionRuleMomentTree());
	abstractPrescriptionRules.add(new PrescriptionRuleGeneric());
	return abstractPrescriptionRules;
    }
}

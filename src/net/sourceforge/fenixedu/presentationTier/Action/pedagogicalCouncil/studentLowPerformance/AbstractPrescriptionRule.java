package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;
import net.sourceforge.fenixedu.domain.student.Registration;

public abstract class AbstractPrescriptionRule {

    private ExecutionYear registrationStart;
    private BigDecimal minimumEcts;
    private int numberOfEntriesStudentInSecretary;
    private PrescriptionEnum prescriptionEnum;

    public PrescriptionEnum getPrescriptionEnum() {
	return prescriptionEnum;
    }

    public void setPrescriptionEnum(PrescriptionEnum prescriptionEnum) {
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

    public boolean isOccurs() {
	return true;
    }

    public boolean contains(PrescriptionEnum prescriptionEnum) {
	return prescriptionEnum.equals(getPrescriptionEnum());
    }

    public boolean isPrescript(Registration registration, BigDecimal ects, int numberOfEntriesStudentInSecretary) {
	return ects.compareTo(getMinimumEcts()) < 0
		&& numberOfEntriesStudentInSecretary == getNumberOfEntriesStudentInSecretary();
    }

    public static List<AbstractPrescriptionRule> readPrescriptionRules(PrescriptionEnum prescriptionEnum) {
	List<AbstractPrescriptionRule> abstractPrescriptionRules = new LinkedList<AbstractPrescriptionRule>();
	for (AbstractPrescriptionRule abstractPrescriptionRule : getPrescriptionRules()) {
	    if (abstractPrescriptionRule.contains(prescriptionEnum)) {
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

    // valid until 2009_2010
    public static List<AbstractPrescriptionRule> readPrescriptionRulesUntil2009_2010(PrescriptionEnum prescriptionEnum) {
	List<AbstractPrescriptionRule> abstractPrescriptionRules = new LinkedList<AbstractPrescriptionRule>();
	for (AbstractPrescriptionRule abstractPrescriptionRule : getPrescriptionRulesUntil2009_2010()) {
	    if (abstractPrescriptionRule.contains(prescriptionEnum)) {
		abstractPrescriptionRules.add(abstractPrescriptionRule);
	    }
	}
	return abstractPrescriptionRules;
    }

    // valid until 2009_2010
    private static AbstractPrescriptionRule[] getPrescriptionRulesUntil2009_2010() {
	return new AbstractPrescriptionRule[] { new PrescriptionRuleMomentOne(), new PrescriptionRuleMomentTwo(),
		new PrescriptionRuleMomentTree(), new PrescriptionRuleTreeEntries(), new PrescriptionRuleFourEntries() };
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

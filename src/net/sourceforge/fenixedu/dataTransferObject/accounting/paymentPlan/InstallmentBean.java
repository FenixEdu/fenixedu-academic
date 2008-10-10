package net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

public class InstallmentBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -103744109361724129L;

    private boolean selected;

    private Money amount;

    private BigDecimal ectsForAmount;

    private List<DomainReference<ExecutionSemester>> executionSemesters;

    private YearMonthDay startDate;

    private YearMonthDay endDate;

    private boolean penaltyAppliable;

    private BigDecimal montlyPenaltyPercentage;

    private YearMonthDay whenToStartApplyPenalty;

    private Integer maxMonthsToApplyPenalty;

    private PaymentPlanBean paymentPlanBean;
    

    public InstallmentBean(PaymentPlanBean paymentPlanBean) {
	setExecutionSemesters(new ArrayList<ExecutionSemester>());
	setPaymentPlanBean(paymentPlanBean);
    }

    public PaymentPlanBean getPaymentPlanBean() {
	return paymentPlanBean;
    }

    public void setPaymentPlanBean(PaymentPlanBean paymentPlanBean) {
	this.paymentPlanBean = paymentPlanBean;
    }

    public Money getAmount() {
	return amount;
    }

    public void setAmount(Money amount) {
	this.amount = amount;
    }

    public YearMonthDay getStartDate() {
	return startDate;
    }

    public void setStartDate(YearMonthDay startDate) {
	this.startDate = startDate;
    }

    public YearMonthDay getEndDate() {
	return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
	this.endDate = endDate;
    }

    public BigDecimal getMontlyPenaltyPercentage() {
	return montlyPenaltyPercentage;
    }

    public void setMontlyPenaltyPercentage(BigDecimal montlyPenaltyPercentage) {
	this.montlyPenaltyPercentage = montlyPenaltyPercentage;
    }

    public YearMonthDay getWhenToStartApplyPenalty() {
	return whenToStartApplyPenalty;
    }

    public void setWhenToStartApplyPenalty(YearMonthDay whenToStartApplyPenalty) {
	this.whenToStartApplyPenalty = whenToStartApplyPenalty;
    }

    public boolean isSelected() {
	return selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    public Integer getMaxMonthsToApplyPenalty() {
	return maxMonthsToApplyPenalty;
    }

    public void setMaxMonthsToApplyPenalty(Integer maxMonthsToApplyPenalty) {
	this.maxMonthsToApplyPenalty = maxMonthsToApplyPenalty;
    }

    public boolean hasRequiredInformation() {
	boolean result = this.amount != null && this.startDate != null && this.endDate != null;

	if (!isPenaltyAppliable()) {
	    return result;
	}

	return result && this.montlyPenaltyPercentage != null && this.whenToStartApplyPenalty != null
		&& this.maxMonthsToApplyPenalty != null;

    }

    public boolean isPenaltyAppliable() {
	return penaltyAppliable;
    }

    public void setPenaltyAppliable(boolean penaltyAppliable) {
	this.penaltyAppliable = penaltyAppliable;
    }

    public BigDecimal getEctsForAmount() {
	return ectsForAmount;
    }

    public void setEctsForAmount(BigDecimal ectsForAmount) {
	this.ectsForAmount = ectsForAmount;
    }

    public List<ExecutionSemester> getExecutionSemesters() {
	final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
	for (final DomainReference<ExecutionSemester> each : this.executionSemesters) {
	    result.add(each.getObject());
	}

	return result;
    }

    public void setExecutionSemesters(List<ExecutionSemester> executionSemesters) {
	final List<DomainReference<ExecutionSemester>> result = new ArrayList<DomainReference<ExecutionSemester>>();
	for (final ExecutionSemester each : executionSemesters) {
	    result.add(new DomainReference<ExecutionSemester>(each));
	}

	this.executionSemesters = result;
    }

}

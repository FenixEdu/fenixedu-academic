package net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class PaymentPlanBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -103744109361724129L;

    private List<InstallmentBean> installments;

    private boolean forStudentEnroledOnSecondSemesterOnly;

    private boolean main;

    private boolean forPartialRegime;

    private List<DomainReference<DegreeCurricularPlan>> degreeCurricularPlans;

    private DomainReference<ExecutionYear> executionYear;

    public PaymentPlanBean(ExecutionYear executionYear) {
	this.installments = new ArrayList<InstallmentBean>();
	this.degreeCurricularPlans = new ArrayList<DomainReference<DegreeCurricularPlan>>();
	setExecutionYear(executionYear);
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlans() {
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (final DomainReference<DegreeCurricularPlan> each : this.degreeCurricularPlans) {
	    result.add(each.getObject());
	}

	return result;
    }

    public void setDegreeCurricularPlans(List<DegreeCurricularPlan> degreeCurricularPlans) {
	final List<DomainReference<DegreeCurricularPlan>> result = new ArrayList<DomainReference<DegreeCurricularPlan>>();
	for (final DegreeCurricularPlan each : degreeCurricularPlans) {
	    result.add(new DomainReference<DegreeCurricularPlan>(each));
	}

	this.degreeCurricularPlans = result;
    }

    public void addInstallment(final InstallmentBean installment) {
	this.installments.add(installment);
    }

    public List<InstallmentBean> getInstallments() {
	return Collections.unmodifiableList(installments);
    }

    public boolean isForStudentEnroledOnSecondSemesterOnly() {
	return forStudentEnroledOnSecondSemesterOnly;
    }

    public void setForStudentEnroledOnSecondSemesterOnly(boolean forStudentEnroledOnSecondSemesterOnly) {
	this.forStudentEnroledOnSecondSemesterOnly = forStudentEnroledOnSecondSemesterOnly;
    }

    public boolean isMain() {
	return main;
    }

    public void setMain(boolean main) {
	this.main = main;
    }

    public boolean isForPartialRegime() {
	return forPartialRegime;
    }

    public void setForPartialRegime(boolean forPartialRegime) {
	this.forPartialRegime = forPartialRegime;
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

    public List<InstallmentBean> getSelectedInstallments() {

	final List<InstallmentBean> result = new ArrayList<InstallmentBean>();

	for (final InstallmentBean installmentBean : getInstallments()) {
	    if (installmentBean.isSelected()) {
		result.add(installmentBean);
	    }
	}

	return result;
    }

    public void removeSelectedInstallments() {
	for (final InstallmentBean each : getSelectedInstallments()) {
	    this.installments.remove(each);
	}

    }

}

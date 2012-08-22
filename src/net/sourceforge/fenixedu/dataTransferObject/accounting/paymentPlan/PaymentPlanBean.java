package net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class PaymentPlanBean implements Serializable, PaymentsBean {

    static private final long serialVersionUID = -103744109361724129L;

    private List<InstallmentBean> installments;

    private boolean forStudentEnroledOnSecondSemesterOnly;

    private boolean main;

    private boolean forPartialRegime;

    private boolean forSecondCurricularYear;

    private boolean forFirstTimeInstitutionStudents;

    private List<DegreeCurricularPlan> degreeCurricularPlans;

    private ExecutionYear executionYear;

    public PaymentPlanBean(ExecutionYear executionYear) {
	this.installments = new ArrayList<InstallmentBean>();
	this.degreeCurricularPlans = new ArrayList<DegreeCurricularPlan>();
	setExecutionYear(executionYear);
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlans() {
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (final DegreeCurricularPlan each : this.degreeCurricularPlans) {
	    result.add(each);
	}

	return result;
    }

    public void setDegreeCurricularPlans(List<DegreeCurricularPlan> degreeCurricularPlans) {
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (final DegreeCurricularPlan each : degreeCurricularPlans) {
	    result.add(each);
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

    public boolean isForFirstTimeInstitutionStudents() {
	return forFirstTimeInstitutionStudents;
    }

    public void setForFirstTimeInstitutionStudents(boolean forFirstTimeInstitutionStudents) {
	this.forFirstTimeInstitutionStudents = forFirstTimeInstitutionStudents;
    }

    public ExecutionYear getExecutionYear() {
	return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public boolean isForSecondCurricularYear() {
	return forSecondCurricularYear;
    }

    public void setForSecondCurricularYear(boolean forSecondCurricularYear) {
	this.forSecondCurricularYear = forSecondCurricularYear;
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

package net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.util.PartialList;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.Month;

import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public class AnualBonusInstallmentFactory implements Serializable, FactoryExecutor {
    private Integer year;

    private Integer installmentsNumber;

    private List<AnualBonusInstallmentBean> anualBonusInstallmentBeanList;

    public static Integer firstYear = 2007;

    public AnualBonusInstallmentFactory() {
    }

    public AnualBonusInstallmentFactory(Integer year, int installmentsNumber) {
	setYear(year);
	setInstallmentsNumber(installmentsNumber);
    }

    public Object execute() {
	List<ActionMessage> errors = new ArrayList<ActionMessage>();

	if (getInstallmentsNumber().intValue() > Month.values().length || getInstallmentsNumber().intValue() <= 0) {
	    return errors.add(new ActionMessage("error.invalidInstallmentsNumber"));
	}
	List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment.readByYear(getYear());
	if (!getCanEditAnualBonusInstallment(anualBonusInstallmentList)) {
	    errors.add(new ActionMessage("error.cantEditAnualBonusInstallment"));
	}

	List<Month> paymentMonths = new ArrayList<Month>();
	List<Partial> partialList = new ArrayList<Partial>();
	for (AnualBonusInstallmentBean anualBonusInstallmentBean : anualBonusInstallmentBeanList) {
	    if (partialList.removeAll(anualBonusInstallmentBean.getPartials())) {
		errors.add(new ActionMessage("error.installmentsCantReferToSameMonths"));
	    }
	    partialList.addAll(anualBonusInstallmentBean.getPartials());
	    if (anualBonusInstallmentBean.getPartials().isEmpty()) {
		errors.add(new ActionMessage("error.installmentsCantBeEmpty", Month.values()[anualBonusInstallmentBean
			.getPaymentPartial().get(DateTimeFieldType.monthOfYear()) - 1]));
	    }
	    if (paymentMonths.contains(Month.values()[anualBonusInstallmentBean.getPaymentPartial().get(
		    DateTimeFieldType.monthOfYear()) - 1])) {
		errors.add(new ActionMessage("error.diferentInstallmentsCantBePayedAtSameTime"));
	    } else {
		paymentMonths.add(Month.values()[anualBonusInstallmentBean.getPaymentPartial().get(
			DateTimeFieldType.monthOfYear()) - 1]);
	    }
	}

	if (errors.isEmpty()) {
	    for (AnualBonusInstallment anualBonusInstallment : anualBonusInstallmentList) {
		anualBonusInstallment.delete();
	    }
	    for (AnualBonusInstallmentBean anualBonusInstallmentBean : anualBonusInstallmentBeanList) {
		new AnualBonusInstallment(getYear(), anualBonusInstallmentBean.getPaymentPartial(), new PartialList(
			anualBonusInstallmentBean.getPartials()));
	    }
	}
	return errors;
    }

    public List<AnualBonusInstallmentBean> getAnualBonusInstallmentBeanList() {
	return anualBonusInstallmentBeanList;
    }

    public void setAnualBonusInstallmentBeanList(List<AnualBonusInstallmentBean> anualBonusInstallmentBean) {
	this.anualBonusInstallmentBeanList = anualBonusInstallmentBean;
    }

    public void addAnualBonusInstallmentBeanList(AnualBonusInstallmentBean anualBonusInstallmentBean) {
	if (this.anualBonusInstallmentBeanList == null) {
	    this.anualBonusInstallmentBeanList = new ArrayList<AnualBonusInstallmentBean>();
	}
	this.anualBonusInstallmentBeanList.add(anualBonusInstallmentBean);
    }

    public Integer getInstallmentsNumber() {
	return installmentsNumber;
    }

    public void setInstallmentsNumber(Integer installmentsNumber) {
	this.installmentsNumber = installmentsNumber;
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public ActionMessage updateAnualBonusInstallment() {
	if (isValidYear()) {
	    List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment.readByYear(getYear());
	    if (getInstallmentsNumber() == null) {
		if (anualBonusInstallmentList.size() != 0) {
		    setInstallmentsNumber(anualBonusInstallmentList.size());
		    if (!getCanEditAnualBonusInstallment(anualBonusInstallmentList)) {
			updateAnualBonusInstallmentBeanList(anualBonusInstallmentList);
		    }
		}
	    } else if (getInstallmentsNumber() != null) {
		if (anualBonusInstallmentList.size() != 0
			&& getInstallmentsNumber().intValue() == anualBonusInstallmentList.size()) {
		    updateAnualBonusInstallmentBeanList(anualBonusInstallmentList);
		} else {
		    setDefaultAnualBonusInstallmentBeanList();
		}
	    }
	} else {
	    setYear(null);
	    return new ActionMessage("error.installmentsCantBeBeforeYear");
	}
	return null;
    }

    private boolean isValidYear() {
	return getYear() != null && getYear().intValue() >= firstYear;
    }

    private void updateAnualBonusInstallmentBeanList(List<AnualBonusInstallment> anualBonusInstallmentList) {
	for (AnualBonusInstallment anualBonusInstallment : anualBonusInstallmentList) {
	    AnualBonusInstallmentBean anualBonusInstallmentBean = new AnualBonusInstallmentBean(anualBonusInstallment.getYear(),
		    anualBonusInstallment.getAssiduousnessPartials(), anualBonusInstallment.getPaymentPartialDate());
	    addAnualBonusInstallmentBeanList(anualBonusInstallmentBean);
	}
    }

    private void setDefaultAnualBonusInstallmentBeanList() {
	int monthsPerInstallment = Month.values().length / getInstallmentsNumber();
	int rest = Month.values().length % getInstallmentsNumber();
	for (int installmentNumber = 1; installmentNumber <= getInstallmentsNumber(); installmentNumber++) {
	    PartialList partialList = getInstallmentMonths(getInstallmentsNumber(), installmentNumber, monthsPerInstallment, rest);
	    int paymentMonthIndex = partialList.getPartials().get(partialList.getPartials().size() - 1).get(
		    DateTimeFieldType.monthOfYear());
	    Partial paymentPartial = new Partial().with(DateTimeFieldType.monthOfYear(), paymentMonthIndex + 1).with(
		    DateTimeFieldType.year(), getYear());
	    AnualBonusInstallmentBean anualBonusInstallmentBean = new AnualBonusInstallmentBean(getYear(), partialList,
		    paymentPartial);
	    addAnualBonusInstallmentBeanList(anualBonusInstallmentBean);
	}
    }

    private PartialList getInstallmentMonths(int totalInstallmentsNumber, int installmentNumber, int monthsPerInstallment,
	    int rest) {
	PartialList partialList = new PartialList();
	int firstMonth = ((installmentNumber - 1) * monthsPerInstallment);
	if (installmentNumber > (totalInstallmentsNumber - rest)) {
	    firstMonth += ((installmentNumber - 1) - (totalInstallmentsNumber - rest));
	}
	int lastMonth = firstMonth + monthsPerInstallment;
	if (installmentNumber > (totalInstallmentsNumber - rest)) {
	    lastMonth++;
	}

	for (int index = firstMonth; index < lastMonth; index++) {
	    if (index == 0) {
		Partial partial = new Partial().with(DateTimeFieldType.monthOfYear(), 12).with(DateTimeFieldType.year(),
			new Integer(getYear() - 1));
		partialList.getPartials().add(partial);
	    } else {
		Partial partial = new Partial().with(DateTimeFieldType.monthOfYear(), index).with(DateTimeFieldType.year(),
			getYear());
		partialList.getPartials().add(partial);
	    }
	}
	return partialList;
    }

    public boolean getCanEditAnualBonusInstallment(List<AnualBonusInstallment> anualBonusInstallmentList) {
	for (AnualBonusInstallment anualBonusInstallment : anualBonusInstallmentList) {
	    if (anualBonusInstallment.getEmployeeBonusInstallmentsCount() != 0) {
		return false;
	    }
	}
	return true;
    }

    public boolean getCanEditAnualBonusInstallment() {
	List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment.readByYear(getYear());
	return getCanEditAnualBonusInstallment(anualBonusInstallmentList);
    }
}
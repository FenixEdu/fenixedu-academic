package net.sourceforge.fenixedu.dataTransferObject.grant.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class GrantSearch implements Serializable {

    private YearMonth grantYearMonth;

    private String responsibleName;

    private DomainReference<Teacher> responsible;

    private DomainListReference<GrantContractRegime> search;

    private Boolean active;

    private Boolean inactive;

    //datas do subsidio
    private String subsidyCostCenterOrProject;

    private Boolean isSubsidyCostCenter;

    //  datas do seguro
    private String insuranceCostCenterOrProject;

    private Boolean isInsuranceCostCenter;

    public GrantSearch() {
	super();
	setActive(true);
	setInactive(false);
	setGrantYearMonth(new YearMonth());
	search = new DomainListReference<GrantContractRegime>();
	setIsSubsidyCostCenter(true);
	setIsInsuranceCostCenter(true);
    }

    public YearMonth getGrantYearMonth() {
	return grantYearMonth;
    }

    public Partial getGrantYearMonthPartial() {
	if (grantYearMonth.getYear() != null || grantYearMonth.getMonth() != null) {
	    YearMonthDay now = new YearMonthDay();
	    if (grantYearMonth.getYear() == null) {
		getGrantYearMonth().setYear(now.getYear());
	    }
	    if (grantYearMonth.getMonth() == null) {
		getGrantYearMonth().setMonth(Month.values()[now.getMonthOfYear() - 1]);
	    }
	    return grantYearMonth.getPartial();
	}
	return null;
    }

    public void setGrantYearMonth(YearMonth grantYearMonth) {
	this.grantYearMonth = grantYearMonth;
    }

    public Teacher getResponsible() {
	return responsible == null ? null : responsible.getObject();
    }

    public void setResponsible(Teacher responsible) {
	this.responsible = new DomainReference<Teacher>(responsible);
    }

    public String getResponsibleName() {
	return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
	this.responsibleName = responsibleName;
    }

    public List<GrantContractRegime> getSearch() {
	return search;
    }

    public Boolean getActive() {
	return active;
    }

    public void setActive(Boolean active) {
	this.active = active;
    }

    public Boolean getInactive() {
	return inactive;
    }

    public void setInactive(Boolean inactive) {
	this.inactive = inactive;
    }

    public String getInsuranceCostCenterOrProject() {
	return insuranceCostCenterOrProject;
    }

    public void setInsuranceCostCenterOrProject(String insuranceCostCenterOrProject) {
	this.insuranceCostCenterOrProject = insuranceCostCenterOrProject;
    }

    public Boolean getIsInsuranceCostCenter() {
	return isInsuranceCostCenter;
    }

    public void setIsInsuranceCostCenter(Boolean isInsuranceCostCenter) {
	this.isInsuranceCostCenter = isInsuranceCostCenter;
    }

    public Boolean getIsSubsidyCostCenter() {
	return isSubsidyCostCenter;
    }

    public void setIsSubsidyCostCenter(Boolean isSubsidyCostCenter) {
	this.isSubsidyCostCenter = isSubsidyCostCenter;
    }

    public String getSubsidyCostCenterOrProject() {
	return subsidyCostCenterOrProject;
    }

    public void setSubsidyCostCenterOrProject(String subsidyCostCenterOrProject) {
	this.subsidyCostCenterOrProject = subsidyCostCenterOrProject;
    }

    public void setSearch() {
	List<GrantContractRegime> result = new ArrayList<GrantContractRegime>();
	for (GrantContractRegime grantContractRegime : RootDomainObject.getInstance()
		.getGrantContractRegimes()) {
	    if (satisfiedSubsidyCostCenter(grantContractRegime)
		    && satisfiedInsuranceCostCenter(grantContractRegime)
		    && satisfiedPeriod(grantContractRegime) && satisfiedResponsible(grantContractRegime)
		    && satisfiedActive(grantContractRegime)) {
		result.add(grantContractRegime);
	    }

	}
	ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("grantContract.grantOwner.number"));
	comparatorChain.addComparator(new BeanComparator("grantContract.contractNumber"));
	Collections.sort(result, comparatorChain);
	search = new DomainListReference<GrantContractRegime>(result);
    }

    private boolean satisfiedInsuranceCostCenter(GrantContractRegime grantContractRegime) {
	if (getInsuranceCostCenterOrProject() != null && getInsuranceCostCenterOrProject().length() != 0) {
	    GrantInsurance grantInsurance = grantContractRegime.getGrantContract().getGrantInsurance();
	    if (grantInsurance != null
		    && grantInsurance.getGrantPaymentEntity().getNumber().equalsIgnoreCase(
			    getInsuranceCostCenterOrProject())
		    && (grantInsurance.getGrantPaymentEntity() instanceof GrantProject
			    && !getIsInsuranceCostCenter() || grantInsurance.getGrantPaymentEntity() instanceof GrantCostCenter
			    && getIsInsuranceCostCenter())) {
		return true;
	    }
	    return false;
	}
	return true;
    }

    private boolean satisfiedSubsidyCostCenter(GrantContractRegime grantContractRegime) {
	if (getSubsidyCostCenterOrProject() != null && getSubsidyCostCenterOrProject().length() != 0) {
	    GrantSubsidy grantSubsidy = grantContractRegime.getGrantSubsidy();
	    if (grantSubsidy != null) {
		for (GrantPart grantPart : grantSubsidy.getAssociatedGrantParts()) {
		    if (grantPart.getGrantPaymentEntity() != null
			    && grantPart.getGrantPaymentEntity().getNumber().equalsIgnoreCase(
				    getSubsidyCostCenterOrProject())
			    && ((grantPart.getGrantPaymentEntity() instanceof GrantProject && (!getIsSubsidyCostCenter())) || (grantPart
				    .getGrantPaymentEntity() instanceof GrantCostCenter && getIsSubsidyCostCenter()))) {
			return true;
		    }
		}
	    }
	    return false;
	}
	return true;
    }

    private boolean satisfiedActive(GrantContractRegime grantContractRegime) {
	if (getActive() || getInactive()) {
	    Partial partial = getGrantYearMonthPartial();
	    if (partial == null) {
		YearMonthDay now = new YearMonthDay();
		partial = new Partial().with(DateTimeFieldType.monthOfYear(), now.getMonthOfYear())
			.with(DateTimeFieldType.year(), now.getYear());
	    }
	    if ((getActive() && belongsToPeriod(grantContractRegime, partial) && StringUtils
		    .isEmpty(grantContractRegime.getGrantContract().getEndContractMotive()))
		    || (getInactive() && !belongsToPeriod(grantContractRegime, partial) && (!StringUtils
			    .isEmpty(grantContractRegime.getGrantContract().getEndContractMotive())))) {
		return true;
	    }
	    return false;
	}
	return false;
    }

    private boolean satisfiedResponsible(GrantContractRegime grantContractRegime) {
	if (getResponsible() != null) {
	    return (grantContractRegime.getTeacher() != null && grantContractRegime.getTeacher().equals(
		    getResponsible())) ? true : false;
	}
	return true;
    }

    private boolean satisfiedPeriod(GrantContractRegime grantContractRegime) {
	return (getGrantYearMonthPartial() == null || (grantContractRegime != null && belongsToPeriod(
		grantContractRegime, getGrantYearMonthPartial()))) ? true : false;
    }

    public boolean belongsToPeriod(GrantContractRegime contractRegime, Partial partialDate) {
	Partial begin = new Partial().with(DateTimeFieldType.monthOfYear(),
		contractRegime.getDateBeginContractYearMonthDay().getMonthOfYear()).with(
		DateTimeFieldType.year(), contractRegime.getDateBeginContractYearMonthDay().getYear());
	Partial end = new Partial().with(DateTimeFieldType.monthOfYear(),
		contractRegime.getDateEndContractYearMonthDay().getMonthOfYear()).with(
		DateTimeFieldType.year(), contractRegime.getDateEndContractYearMonthDay().getYear());
	return !(begin).isAfter(partialDate) && !(end).isBefore(partialDate);
    }

}

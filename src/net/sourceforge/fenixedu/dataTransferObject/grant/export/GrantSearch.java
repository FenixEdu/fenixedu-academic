package net.sourceforge.fenixedu.dataTransferObject.grant.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class GrantSearch implements Serializable {

    public enum DatesTypeChoice {
        DATE, DATE_INTERVAL;
    }

    public enum ActivityChoice {
        ACTIVITY, TERM;
    }

    public enum CostCenterOrProjectChoice {
        ANY, PROJECT, COST_CENTER;
    }

    private DatesTypeChoice datesTypeChoice;

    private LocalDate beginDate;

    private LocalDate endDate;

    private String responsibleName;

    private Teacher responsible;

    private List<GrantContractRegime> search;

    private ActivityChoice activityChoice;

    private Boolean active;

    private Boolean inactive;

    private Boolean withRenewals;

    private Boolean withoutRenewals;

    // datas do subsidio

    private CostCenterOrProjectChoice subsidyCostCenterOrProjectChoice;

    private String subsidyCostCenterOrProject;

    // datas do seguro
    private CostCenterOrProjectChoice insuranceCostCenterOrProjectChoice;

    private String insuranceCostCenterOrProject;

    public GrantSearch() {
        super();
        setDatesTypeChoice(DatesTypeChoice.DATE_INTERVAL);
        setActive(true);
        setInactive(false);
        LocalDate today = new LocalDate();
        setBeginDate(new LocalDate(today.getYear(), today.getMonthOfYear(), 1));
        setEndDate(new LocalDate(today.getYear(), today.getMonthOfYear(), today.dayOfMonth().getMaximumValue()));
        search = new ArrayList<GrantContractRegime>();
        setSubsidyCostCenterOrProjectChoice(CostCenterOrProjectChoice.ANY);
        setInsuranceCostCenterOrProjectChoice(CostCenterOrProjectChoice.ANY);
        setActivityChoice(ActivityChoice.ACTIVITY);
        setWithRenewals(false);
        setWithoutRenewals(true);
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Teacher getResponsible() {
        return responsible;
    }

    public void setResponsible(Teacher responsible) {
        this.responsible = responsible;
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

    public CostCenterOrProjectChoice getInsuranceCostCenterOrProjectChoice() {
        return insuranceCostCenterOrProjectChoice;
    }

    public void setInsuranceCostCenterOrProjectChoice(CostCenterOrProjectChoice insuranceCostCenterOrProjectChoice) {
        this.insuranceCostCenterOrProjectChoice = insuranceCostCenterOrProjectChoice;
    }

    public CostCenterOrProjectChoice getSubsidyCostCenterOrProjectChoice() {
        return subsidyCostCenterOrProjectChoice;
    }

    public void setSubsidyCostCenterOrProjectChoice(CostCenterOrProjectChoice subsidyCostCenterOrProjectChoice) {
        this.subsidyCostCenterOrProjectChoice = subsidyCostCenterOrProjectChoice;
    }

    public String getSubsidyCostCenterOrProject() {
        return subsidyCostCenterOrProject;
    }

    public void setSubsidyCostCenterOrProject(String subsidyCostCenterOrProject) {
        this.subsidyCostCenterOrProject = subsidyCostCenterOrProject;
    }

    public void setSearch() {
        List<GrantContractRegime> result = new ArrayList<GrantContractRegime>();
        for (GrantContractRegime grantContractRegime : RootDomainObject.getInstance().getGrantContractRegimes()) {
            if (grantContractRegime.getGrantContract() != null && satisfiedSubsidyCostCenter(grantContractRegime)
                    && satisfiedInsuranceCostCenter(grantContractRegime) // &&
                    // satisfiedPeriod(grantContractRegime)
                    && satisfiedResponsible(grantContractRegime) && satisfiedActive(grantContractRegime)) {
                result.add(grantContractRegime);
            }

        }
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("grantContract.grantOwner.number"));
        comparatorChain.addComparator(new BeanComparator("grantContract.contractNumber"));
        Collections.sort(result, comparatorChain);
        search = new ArrayList<GrantContractRegime>(result);
    }

    private boolean satisfiedInsuranceCostCenter(GrantContractRegime grantContractRegime) {
        if (getInsuranceCostCenterOrProject() != null && getInsuranceCostCenterOrProject().length() != 0) {
            GrantInsurance grantInsurance = grantContractRegime.getGrantContract().getGrantInsurance();
            if (grantInsurance != null
                    && grantInsurance.getGrantPaymentEntity().getNumber().equalsIgnoreCase(getInsuranceCostCenterOrProject())
                    && ((grantInsurance.getGrantPaymentEntity() instanceof GrantProject && getInsuranceCostCenterOrProjectChoice() == CostCenterOrProjectChoice.PROJECT) || (grantInsurance
                            .getGrantPaymentEntity() instanceof GrantCostCenter && getInsuranceCostCenterOrProjectChoice() == CostCenterOrProjectChoice.COST_CENTER))) {
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
                            && grantPart.getGrantPaymentEntity().getNumber().equalsIgnoreCase(getSubsidyCostCenterOrProject())
                            && ((grantPart.getGrantPaymentEntity() instanceof GrantProject && getSubsidyCostCenterOrProjectChoice() == CostCenterOrProjectChoice.PROJECT) || (grantPart
                                    .getGrantPaymentEntity() instanceof GrantCostCenter && getSubsidyCostCenterOrProjectChoice() == CostCenterOrProjectChoice.COST_CENTER))) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private boolean satisfiedActive(GrantContractRegime grantContractRegime) {
        if (getActive() || getInactive() || getActivityChoice() == ActivityChoice.TERM) {
            LocalDate endDate = grantContractRegime.getEndDateOrRescissionDate();
            if ((getActivityChoice() == ActivityChoice.ACTIVITY
                    && (getActive() && belongsToPeriod(new LocalDate(grantContractRegime.getDateBeginContractYearMonthDay()),
                            endDate)) || (getInactive() && !belongsToPeriod(
                    new LocalDate(grantContractRegime.getDateBeginContractYearMonthDay()), endDate)))
                    || (getActivityChoice() == ActivityChoice.TERM && belongsToPeriod(endDate) && ((getWithRenewals() && hasRenewedContract(grantContractRegime)) || (getWithoutRenewals() && !hasRenewedContract(grantContractRegime))))) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean hasRenewedContract(GrantContractRegime grantContractRegime) {
        List<GrantContractRegime> contracts =
                new ArrayList<GrantContractRegime>(grantContractRegime.getGrantContract().getContractRegimes());
        Collections.sort(contracts, new BeanComparator("endDateOrRescissionDate"));
        int thisContractIndex = contracts.indexOf(grantContractRegime) + 1;
        return contracts.size() > thisContractIndex;
    }

    private boolean satisfiedResponsible(GrantContractRegime grantContractRegime) {
        if (getResponsible() != null) {
            return (grantContractRegime.getTeacher() != null && grantContractRegime.getTeacher().equals(getResponsible())) ? true : false;
        }
        return true;
    }

    public boolean belongsToPeriod(LocalDate contractBeginDate, LocalDate contractEndDate) {
        if (getDatesTypeChoice() == DatesTypeChoice.DATE) {
            Interval interval =
                    new Interval(contractBeginDate.toDateTimeAtStartOfDay(), contractEndDate.toDateTimeAtStartOfDay());
            return interval.contains(getBeginDate().toDateTimeAtStartOfDay());
        }
        return (!contractBeginDate.isAfter(getEndDate())) && (!contractEndDate.isBefore(getBeginDate()));
    }

    public boolean belongsToPeriod(LocalDate date) {
        if (getDatesTypeChoice() == DatesTypeChoice.DATE) {
            return date.equals(getBeginDate());
        }
        Interval interval = new Interval(getBeginDate().toDateTimeAtStartOfDay(), getEndDate().toDateTimeAtStartOfDay());
        return interval.contains(date.toDateTimeAtStartOfDay());
    }

    public DatesTypeChoice getDatesTypeChoice() {
        return datesTypeChoice;
    }

    public void setDatesTypeChoice(DatesTypeChoice datesTypeChoice) {
        this.datesTypeChoice = datesTypeChoice;
    }

    public ActivityChoice getActivityChoice() {
        return activityChoice;
    }

    public void setActivityChoice(ActivityChoice activityChoice) {
        this.activityChoice = activityChoice;
    }

    public Boolean getWithRenewals() {
        return withRenewals;
    }

    public void setWithRenewals(Boolean withRenewals) {
        this.withRenewals = withRenewals;
    }

    public Boolean getWithoutRenewals() {
        return withoutRenewals;
    }

    public void setWithoutRenewals(Boolean withoutRenewals) {
        this.withoutRenewals = withoutRenewals;
    }

}

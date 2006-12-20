package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkAuthorization;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class ExtraWorkAuthorizationSearchBean implements Serializable {

    YearMonthDay beginDate;

    YearMonthDay endDate;

    Integer workingCostCenterCode;

    Integer payingCostCenterCode;

    List<ExtraWorkAuthorizationFactory> extraWorkAuthorizations = new ArrayList<ExtraWorkAuthorizationFactory>();

    public ExtraWorkAuthorizationSearchBean() {
    }

    public YearMonthDay getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(YearMonthDay beginDate) {
        this.beginDate = beginDate;
    }

    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }

    public Integer getPayingCostCenterCode() {
        return payingCostCenterCode;
    }

    public void setPayingCostCenterCode(Integer payingCostCenterCode) {
        this.payingCostCenterCode = payingCostCenterCode;
    }

    public Integer getWorkingCostCenterCode() {
        return workingCostCenterCode;
    }

    public void setWorkingCostCenterCode(Integer workingCostCenterCode) {
        this.workingCostCenterCode = workingCostCenterCode;
    }

    public void doSearch() {
        getExtraWorkAuthorizations().clear();
        for (ExtraWorkAuthorization extraWorkAuthorization : RootDomainObject.getInstance()
                .getExtraWorkAuthorizations()) {
            if (satisfiedDates(extraWorkAuthorization)
                    && satisfiedWorkingUnit(extraWorkAuthorization)
                    && satisfiedPayingUnit(extraWorkAuthorization)) {
                getExtraWorkAuthorizations().add(new ExtraWorkAuthorizationFactory(extraWorkAuthorization));
            }
        }
        ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator("extraWorkAuthorization.beginDate"));
        comparatorChain.addComparator(new BeanComparator("extraWorkAuthorization.endDate"));
        Collections.sort(getExtraWorkAuthorizations(), comparatorChain);
    }

    private boolean satisfiedPayingUnit(ExtraWorkAuthorization extraWorkAuthorization) {
        if (getPayingCostCenterCode() == null
                || getPayingCostCenterCode().equals(
                        extraWorkAuthorization.getPayingUnit().getCostCenterCode())) {
            return true;
        }
        return false;
    }

    private boolean satisfiedWorkingUnit(ExtraWorkAuthorization extraWorkAuthorization) {
        if (getWorkingCostCenterCode() == null
                || getWorkingCostCenterCode().equals(
                        extraWorkAuthorization.getWorkingUnit().getCostCenterCode())) {
            return true;
        }
        return false;
    }

    private boolean satisfiedEndDate(ExtraWorkAuthorization extraWorkAuthorization) {
        if(getEndDate() == null || getEndDate().isEqual(extraWorkAuthorization.getEndDate())){
            return true;
        }
        return false;
    }

    private boolean satisfiedDates(ExtraWorkAuthorization extraWorkAuthorization) {
        return extraWorkAuthorization.existsBetweenDates(getBeginDate(),getEndDate());
    }

    public List<ExtraWorkAuthorizationFactory> getExtraWorkAuthorizations() {
        return extraWorkAuthorizations;
    }
}

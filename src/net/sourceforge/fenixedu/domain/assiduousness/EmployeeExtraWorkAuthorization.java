package net.sourceforge.fenixedu.domain.assiduousness;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeExtraWorkAuthorizationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class EmployeeExtraWorkAuthorization extends EmployeeExtraWorkAuthorization_Base {

    public EmployeeExtraWorkAuthorization(ExtraWorkAuthorization extraWorkAuthorization,
            EmployeeExtraWorkAuthorizationBean employeeExtraWorkAuthorizationBean) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setExtraWorkAuthorization(extraWorkAuthorization);
        setAssiduousness(employeeExtraWorkAuthorizationBean.getEmployee().getAssiduousness());
        setModifiedBy(employeeExtraWorkAuthorizationBean.getModifiedBy());
        if (!employeeExtraWorkAuthorizationBean.getAuxiliarPersonel()
                && !employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel()
                && !employeeExtraWorkAuthorizationBean.getNightExtraWork()
                && !employeeExtraWorkAuthorizationBean.getNormalExtraWork()
                && !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusOneHundredHours()
                && !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusTwoHours()
                && !employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork()) {
            throw new DomainException("error.extraWorkAuthorization.options");
        }
        setAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getAuxiliarPersonel());
        setExecutiveAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel());
        setNightExtraWork(employeeExtraWorkAuthorizationBean.getNightExtraWork());
        setNormalExtraWork(employeeExtraWorkAuthorizationBean.getNormalExtraWork());
        setNormalExtraWorkPlusOneHundredHours(employeeExtraWorkAuthorizationBean
                .getNormalExtraWorkPlusOneHundredHours());
        setNormalExtraWorkPlusTwoHours(employeeExtraWorkAuthorizationBean
                .getNormalExtraWorkPlusTwoHours());
        setWeeklyRestExtraWork(employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork());
        setLastModifiedDate(new DateTime());
    }

    public void edit(EmployeeExtraWorkAuthorizationBean employeeExtraWorkAuthorizationBean) {
        if (!employeeExtraWorkAuthorizationBean.getAuxiliarPersonel()
                && !employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel()
                && !employeeExtraWorkAuthorizationBean.getNightExtraWork()
                && !employeeExtraWorkAuthorizationBean.getNormalExtraWork()
                && !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusOneHundredHours()
                && !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusTwoHours()
                && !employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork()) {
            throw new DomainException("error.extraWorkAuthorization.options");
        }
        setModifiedBy(employeeExtraWorkAuthorizationBean.getModifiedBy());
        setAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getAuxiliarPersonel());
        setExecutiveAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel());
        setNightExtraWork(employeeExtraWorkAuthorizationBean.getNightExtraWork());
        setNormalExtraWork(employeeExtraWorkAuthorizationBean.getNormalExtraWork());
        setNormalExtraWorkPlusOneHundredHours(employeeExtraWorkAuthorizationBean
                .getNormalExtraWorkPlusOneHundredHours());
        setNormalExtraWorkPlusTwoHours(employeeExtraWorkAuthorizationBean
                .getNormalExtraWorkPlusTwoHours());
        setWeeklyRestExtraWork(employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork());
        setLastModifiedDate(new DateTime());
    }

    public void delete() {
        removeRootDomainObject();
        removeAssiduousness();
        removeExtraWorkAuthorization();
        removeModifiedBy();
        deleteDomainObject();
    }
}

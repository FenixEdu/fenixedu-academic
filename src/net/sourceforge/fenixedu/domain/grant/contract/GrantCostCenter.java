/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author pica
 * @author barbosa
 */
public class GrantCostCenter extends GrantCostCenter_Base {

    public GrantCostCenter(String number, String designation, Teacher responsibleTeacher) {
        for (final GrantPaymentEntity grantPaymentEntity : RootDomainObject.getInstance().getGrantPaymentEntitys()) {
            if (grantPaymentEntity.isCostCenter() && !((GrantCostCenter) grantPaymentEntity).equals(this)
                    && grantPaymentEntity.getNumber().equalsIgnoreCase(number)) {
                throw new DomainException("errors.grant.costcenter.duplicateEntry", number);
            }
        }
        init(number, designation, responsibleTeacher);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    protected void init(String number, String designation, Teacher responsibleTeacher) {
        check(responsibleTeacher, "message.grant.paymentEntity.emptyTeacher");
        checkNumber(number);
        setNumber(number);
        setDesignation(designation);
        setResponsibleTeacher(responsibleTeacher);
    }

    private void checkNumber(String number) {
        check(number, "message.grant.paymentEntity.emptyCostCenter");
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new DomainException("message.grant.paymentEntity.invalidCostCenter");
        }
    }

    public static GrantCostCenter readGrantCostCenterByNumber(String number) {
        for (GrantPaymentEntity grantPaymentEntity : RootDomainObject.getInstance().getGrantPaymentEntitys()) {
            if (grantPaymentEntity instanceof GrantCostCenter && grantPaymentEntity.getNumber().equals(number)) {
                return (GrantCostCenter) grantPaymentEntity;
            }
        }
        return null;
    }

    @Override
    public boolean isCostCenter() {
        return true;
    }

    @Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
    @Service
    public void editGrantCostCenter(String number, String designation, Teacher responsibleTeacher) {
        for (final GrantPaymentEntity grantPaymentEntity : RootDomainObject.getInstance().getGrantPaymentEntitys()) {
            if (grantPaymentEntity.isCostCenter() && grantPaymentEntity.getNumber().equalsIgnoreCase(number)
                    && !((GrantCostCenter) grantPaymentEntity).equals(this)) {
                throw new DomainException("errors.grant.costcenter.duplicateEntry", number);
            }
        }
        init(number, designation, responsibleTeacher);
    }

    public Unit getUnit() {
        if (StringUtils.isNotEmpty(getNumber())) {
            return Unit.readByCostCenterCode(Integer.valueOf(getNumber()));
        }
        return null;
    }
}
/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author pica
 * @author barbosa
 */
public class GrantProject extends GrantProject_Base {

	public GrantProject(String number, String designation, Teacher responsibleTeacher, GrantCostCenter grantCostCenter) {
		for (final GrantPaymentEntity grantPaymentEntity : RootDomainObject.getInstance().getGrantPaymentEntitys()) {
			if (grantPaymentEntity.isGrantProject() && !((GrantProject) grantPaymentEntity).equals(this)
					&& grantPaymentEntity.getNumber().equalsIgnoreCase(number)) {
				throw new DomainException("message.grant.project.alreadyExistsNumber");
			}
		}
		init(number, designation, responsibleTeacher, grantCostCenter);
		setRootDomainObject(RootDomainObject.getInstance());
	}

	protected void init(String number, String designation, Teacher responsibleTeacher, GrantCostCenter grantCostCenter) {
		check(responsibleTeacher, "message.grant.paymentEntity.emptyTeacher");
		check(grantCostCenter, "message.grant.paymentEntity.emptyCostCenter");
		setNumber(number);
		setDesignation(designation);
		setResponsibleTeacher(responsibleTeacher);
		setGrantCostCenter(grantCostCenter);
	}

	@Override
	public boolean isGrantProject() {
		return true;
	}

	@Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
	@Service
	public void editGrantProject(String number, String designation, Teacher responsibleTeacher, GrantCostCenter grantCostCenter) {
		for (final GrantPaymentEntity grantPaymentEntity : RootDomainObject.getInstance().getGrantPaymentEntitys()) {
			if (grantPaymentEntity.isGrantProject() && grantPaymentEntity.getNumber().equalsIgnoreCase(number)
					&& !((GrantProject) grantPaymentEntity).equals(this)) {
				throw new DomainException("message.grant.project.alreadyExistsNumber");
			}
		}
		init(number, designation, responsibleTeacher, grantCostCenter);
	}

}
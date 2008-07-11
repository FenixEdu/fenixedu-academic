package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class PostingRulePredicates {

    public static final AccessControlPredicate<PostingRule> editPredicate = new AccessControlPredicate<PostingRule>() {

	public boolean evaluate(PostingRule postingRule) {

	    if (AccessControl.getUserView().hasRoleType(RoleType.MANAGER)) {
		return true;
	    }

	    return hasValidRole() && postingRuleBelongsToAdministrativeOffice(postingRule);

	}

	private boolean postingRuleBelongsToAdministrativeOffice(PostingRule postingRule) {
	    return AccessControl.getPerson().getEmployee().getCurrentWorkingPlace().getAdministrativeOffice()
		    .getServiceAgreementTemplate() == postingRule.getServiceAgreementTemplate();
	}

	private boolean hasValidRole() {
	    final IUserView userView = AccessControl.getUserView();
	    return (userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		    || userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
		    || userView.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE) || userView
		    .hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER));
	}

    };

}

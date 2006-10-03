package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.AccessControlPredicate;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class PostingRulePredicates {

    public static final AccessControlPredicate<PostingRule> editPredicate = new AccessControlPredicate<PostingRule>() {

	public boolean evaluate(PostingRule postingRule) {

	    return hasValidRole() && postingRuleBelongsToAdministrativeOffice(postingRule);

	}

	private boolean postingRuleBelongsToAdministrativeOffice(PostingRule postingRule) {
	    return AccessControl.getUserView().getPerson().getEmployee().getCurrentWorkingPlace()
		    .getAdministrativeOffice().getServiceAgreementTemplate() == postingRule
		    .getServiceAgreementTemplate();
	}

	private boolean hasValidRole() {
	    return (AccessControl.getUserView().hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
		    || AccessControl.getUserView().hasRoleType(
			    RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE) || AccessControl.getUserView()
		    .hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER));
	}

    };

}

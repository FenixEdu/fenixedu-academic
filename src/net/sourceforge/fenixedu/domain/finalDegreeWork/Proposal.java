package net.sourceforge.fenixedu.domain.finalDegreeWork;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Proposal extends Proposal_Base {

    public Proposal() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeRootDomainObject();
	deleteDomainObject();
    }

    public boolean isProposalConfirmedByTeacherAndStudents(final FinalDegreeWorkGroup group) {
	return getGroupAttributedByTeacher() == group && group.isConfirmedByStudents(this);
    }

}

package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class PrintRegistrationDeclarationOperation extends CandidacyOperation {

    public PrintRegistrationDeclarationOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
	super(roleTypes, candidacy);
    }

    @Override
    protected void internalExecute() {
	// nothing to be done

    }

    @Override
    public CandidacyOperationType getType() {
	return CandidacyOperationType.PRINT_REGISTRATION_DECLARATION;
    }

    @Override
    public boolean isInput() {
	return false;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

    @Override
    public boolean isAuthorized(Person person) {
	return super.isAuthorized(person) && person == getCandidacy().getPerson();
    }
}
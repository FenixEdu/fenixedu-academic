package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class PrintAllDocumentsOperation extends CandidacyOperation {

	static private final long serialVersionUID = 1L;

	public PrintAllDocumentsOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
		super(roleTypes, candidacy);
	}

	public PrintAllDocumentsOperation(final RoleType roleType, final Candidacy candidacy) {
		this(Collections.singleton(roleType), candidacy);
	}

	@Override
	protected void internalExecute() {
		// TODO Auto-generated method stub
	}

	@Override
	public CandidacyOperationType getType() {
		return CandidacyOperationType.PRINT_ALL_DOCUMENTS;
	}

	@Override
	public boolean isInput() {
		return false;
	}

	@Override
	public StudentCandidacy getCandidacy() {
		return (StudentCandidacy) super.getCandidacy();
	}

	@Override
	public boolean isVisible() {
		return getCandidacy().getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear());
	}

	@Override
	public boolean isAuthorized(Person person) {
		return super.isAuthorized(person) && person == getCandidacy().getPerson();
	}
}
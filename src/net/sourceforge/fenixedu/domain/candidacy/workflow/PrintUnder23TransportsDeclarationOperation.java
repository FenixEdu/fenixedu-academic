package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.LocalDate;
import org.joda.time.Period;

public class PrintUnder23TransportsDeclarationOperation extends CandidacyOperation {

	static private final long serialVersionUID = 1L;

	public PrintUnder23TransportsDeclarationOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
		super(roleTypes, candidacy);
	}

	public PrintUnder23TransportsDeclarationOperation(final RoleType roleType, final Candidacy candidacy) {
		this(Collections.singleton(roleType), candidacy);
	}

	@Override
	protected void internalExecute() {
		// nothing to be done
	}

	@Override
	public CandidacyOperationType getType() {
		return CandidacyOperationType.PRINT_UNDER_23_TRANSPORTS_DECLARATION;
	}

	@Override
	public boolean isInput() {
		return false;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public boolean isAuthorized(final Person person) {
		return super.isAuthorized(person) && person == getCandidacy().getPerson() && hasLessOr23Years(getCandidacy().getPerson());
	}

	private boolean hasLessOr23Years(final Person person) {
		return new Period(person.getDateOfBirthYearMonthDay(), new LocalDate()).getYears() <= 23;
	}
}
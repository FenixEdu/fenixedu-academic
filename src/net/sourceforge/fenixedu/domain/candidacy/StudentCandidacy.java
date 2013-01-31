package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PHDProgramCandidacy;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

import org.joda.time.DateTime;

public abstract class StudentCandidacy extends StudentCandidacy_Base {

	public StudentCandidacy() {
		super();
	}

	protected void init(Person person, ExecutionDegree executionDegree) {
		check(executionDegree, "execution degree cannot be null");
		check(person, "person cannot be null");

		if (person.hasStudentCandidacyForExecutionDegree(executionDegree)) {
			StudentCandidacy existentCandidacy = person.getStudentCandidacyForExecutionDegree(executionDegree);
			if (!existentCandidacy.hasRegistration() || existentCandidacy.getRegistration().getActiveStateType().isActive()) {
				throw new DomainException("error.candidacy.already.created");
			}
		}
		setExecutionDegree(executionDegree);
		setPerson(person);
		setPrecedentDegreeInformation(new PrecedentDegreeInformation());
	}

	protected void init(Person person) {
		check(person, "person cannot be null");
		setPerson(person);
		setPrecedentDegreeInformation(new PrecedentDegreeInformation());
	}

	private void checkParameters(final Person person, final ExecutionDegree executionDegree, final Person creator,
			final Double entryGrade, final String contigent, final Ingression ingression, final EntryPhase entryPhase) {
		if (executionDegree == null) {
			throw new DomainException("error.candidacy.DegreeCandidacy.executionDegree.cannot.be.null");
		}

		if (person == null) {
			throw new DomainException("error.candidacy.DegreeCandidacy.person.cannot.be.null");
		}

		if (person.hasDegreeCandidacyForExecutionDegree(executionDegree)) {
			throw new DomainException("error.candidacy.DegreeCandidacy.candidacy.already.created");
		}

		if (creator == null) {
			throw new DomainException("error.candidacy.DegreeCandidacy.creator.cannot.be.null");
		}

		if (entryPhase == null) {
			throw new DomainException("error.candidacy.DegreeCandidacy.entryPhase.cannot.be.null");
		}

	}

	protected void init(final Person person, final ExecutionDegree executionDegree, final Person creator, Double entryGrade,
			String contigent, Ingression ingression, EntryPhase entryPhase, Integer placingOption) {
		checkParameters(person, executionDegree, creator, entryGrade, contigent, ingression, entryPhase);
		super.setExecutionDegree(executionDegree);
		super.setPerson(person);
		super.setPrecedentDegreeInformation(new PrecedentDegreeInformation());
		super.setEntryGrade(entryGrade);
		super.setContigent(contigent);
		super.setIngression(ingression);
		super.setEntryPhase(entryPhase);
		super.setPlacingOption(placingOption);
	}

	public DateTime getCandidacyDate() {
		return Collections.min(getCandidacySituations(), CandidacySituation.DATE_COMPARATOR).getSituationDate();
	}

	public static StudentCandidacy createStudentCandidacy(ExecutionDegree executionDegree, Person studentPerson) {

		switch (executionDegree.getDegree().getDegreeType()) {

		case BOLONHA_DEGREE:
		case DEGREE:
		case EMPTY:
			return new DegreeCandidacy(studentPerson, executionDegree);

		case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
			return new DFACandidacy(studentPerson, executionDegree);

		case BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA:
			// TODO: remove this after PHD Program candidacy is completed and
			// data migrated
			return new PHDProgramCandidacy(studentPerson, executionDegree);

		case BOLONHA_INTEGRATED_MASTER_DEGREE:
			return new IMDCandidacy(studentPerson, executionDegree);

		case BOLONHA_MASTER_DEGREE:
			return new MDCandidacy(studentPerson, executionDegree);

		case BOLONHA_SPECIALIZATION_DEGREE:
			return new SDCandidacy(studentPerson, executionDegree);

		default:
			return null;
		}

	}

	public static Set<StudentCandidacy> readByIds(final List<Integer> studentCandidacyIds) {
		final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();

		for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
			if (candidacy instanceof StudentCandidacy) {
				if (studentCandidacyIds.contains(candidacy.getIdInternal())) {
					result.add((StudentCandidacy) candidacy);
				}
			}
		}

		return result;
	}

	public static Set<StudentCandidacy> readByExecutionYear(final ExecutionYear executionYear) {
		final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
		for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
			if (candidacy instanceof StudentCandidacy) {
				final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
				if (studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear) {
					result.add(studentCandidacy);
				}
			}
		}

		return result;
	}

	public static Set<StudentCandidacy> readNotConcludedBy(final ExecutionDegree executionDegree,
			final ExecutionYear executionYear, final EntryPhase entryPhase) {
		final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
		for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
			if (candidacy instanceof StudentCandidacy) {
				final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
				if (studentCandidacy.hasAnyCandidacySituations() && !studentCandidacy.isConcluded()
						&& studentCandidacy.getExecutionDegree() == executionDegree
						&& studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear
						&& studentCandidacy.getEntryPhase() != null && studentCandidacy.getEntryPhase().equals(entryPhase)) {
					result.add(studentCandidacy);
				}
			}
		}

		return result;
	}

	@Override
	public void delete() {
		removeRegistration();
		removeExecutionDegree();
		removeSchoolTimeDistrictSubDivisionOfResidence();
		removeCountryOfResidence();
		removeDistrictSubdivisionOfResidence();

		if (hasGrantOwnerProvider()) {
			removeGrantOwnerProvider();
		}

		if (hasPrecedentDegreeInformation() && !getPrecedentDegreeInformation().hasStudent()) {
			getPrecedentDegreeInformation().delete();
		}

		super.delete();
	}

	@Override
	public boolean isConcluded() {
		final CandidacySituation activeSituation = getActiveCandidacySituation();
		return activeSituation != null
				&& (activeSituation.getCandidacySituationType() == CandidacySituationType.REGISTERED || getActiveCandidacySituation()
						.getCandidacySituationType() == CandidacySituationType.CANCELLED);
	}

	public boolean cancelCandidacy() {
		if (!isConcluded()) {
			new CancelledCandidacySituation(this, this.getPerson());

			for (PaymentCode paymentCode : getAvailablePaymentCodes()) {
				AccountingEventPaymentCode accountingEventPaymentCode = (AccountingEventPaymentCode) paymentCode;
				if (accountingEventPaymentCode.isNew() && !accountingEventPaymentCode.hasAccountingEvent()) {
					accountingEventPaymentCode.cancel();
				}
			}

			return true;
		}
		return false;
	}

	public DegreeCurricularPlan getDegreeCurricularPlan() {
		return getExecutionDegree().getDegreeCurricularPlan();
	}

	public boolean hasEntryPhase() {
		return getEntryPhase() != null;
	}

	public boolean hasApplyForResidence() {
		return getApplyForResidence() != null;
	}

	public ExecutionYear getExecutionYear() {
		return getExecutionDegree().getExecutionYear();
	}
}
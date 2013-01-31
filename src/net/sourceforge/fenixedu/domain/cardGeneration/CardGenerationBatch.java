package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;

public class CardGenerationBatch extends CardGenerationBatch_Base {

	public static final Comparator<CardGenerationBatch> COMPARATOR_BY_CREATION_DATE = new Comparator<CardGenerationBatch>() {

		@Override
		public int compare(CardGenerationBatch o1, CardGenerationBatch o2) {
			final int c = o1.getCreated().compareTo(o2.getCreated());
			return c == 0 ? DomainObject.COMPARATOR_BY_ID.compare(o1, o2) : c;
		}

	};

	public static class CardGenerationBatchCreator implements FactoryExecutor {

		private final ExecutionYear executionYear;
		private final boolean emptyCardGenerationBatch;

		public CardGenerationBatchCreator(final ExecutionYear executionYear, final boolean emptyCardGenerationBatch) {
			this.executionYear = executionYear;
			this.emptyCardGenerationBatch = emptyCardGenerationBatch;
		}

		@Override
		public Object execute() {
			return new CardGenerationBatch(null, executionYear, emptyCardGenerationBatch);
		}

	}

	public static class CardGenerationBatchDeleter implements FactoryExecutor {

		private final CardGenerationBatch cardGenerationBatch;

		public CardGenerationBatchDeleter(final CardGenerationBatch cardGenerationBatch) {
			this.cardGenerationBatch = cardGenerationBatch;
		}

		@Override
		public Object execute() {
			if (cardGenerationBatch != null) {
				cardGenerationBatch.delete();
			}
			return null;
		}
	}

	public CardGenerationBatch(String description, final ExecutionYear executionYear, boolean createEmptyBatch) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setExecutionYear(executionYear);
		setDescription(description);
		final DateTime dateTime = new DateTime();
		setCreated(dateTime);
		setUpdated(dateTime);
		if (!createEmptyBatch) {
			setPeopleForEntryCreation(getAllPeopleForEntryCreation());
		}
	}

	public void delete() {
		removeExecutionYear();
		removeRootDomainObject();
		for (final CardGenerationProblem cardGenerationProblem : getCardGenerationProblemsSet()) {
			cardGenerationProblem.delete();
		}
		for (final CardGenerationEntry cardGenerationEntry : getCardGenerationEntriesSet()) {
			cardGenerationEntry.delete();
		}
		deleteDomainObject();
	}

	@Override
	@Service
	public void setSent(DateTime dateTime) {
		super.setSent(dateTime);
	}

	protected String getAllPeopleForEntryCreation() {
		return "ALL";
	}

	public void createCardGenerationEntries(final DateTime begin, final DateTime end, final Person person) {
		final Student student = person.getStudent();
		if (student != null) {
			final Set<StudentCurricularPlan> studentCurricularPlans = getStudentCurricularPlans(begin, end, student);

			if (!studentCurricularPlans.isEmpty()) {
				final DegreeType maxDegreeType = findMaxDegreeType(studentCurricularPlans);
				for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
					final DegreeType degreeType = studentCurricularPlan.getDegreeType();
					if (!degreeType.isEmpty()) {
						if (degreeType.compareTo(maxDegreeType) >= 0) {
							// System.out.println("Using: " + degreeType +
							// " same as " + maxDegreeType);
							createCardGenerationEntry(person, studentCurricularPlan);
						} else {
							System.out.println("Not using: " + degreeType + " because of " + maxDegreeType);
						}
					}
				}
			}
		}
	}

	public int getNumberOfIssuedCards() {
		int result = 0;
		for (CardGenerationEntry cardGenerationEntry : getCardGenerationEntries()) {
			result += cardGenerationEntry.getNumberOfCGRsAfterThisCGEAndBeforeTheNextCGE();
		}
		return result;
	}

	public List<String> getSentButNotIssuedCGRs() {
		List<String> lineEntriesSentButNotIssued = new ArrayList<String>();
		for (CardGenerationEntry cardGenerationEntry : getCardGenerationEntries()) {
			if (cardGenerationEntry.getNumberOfCGRsAfterThisCGEAndBeforeTheNextCGE() <= 0) {
				lineEntriesSentButNotIssued.add(cardGenerationEntry.getLine());
			}
		}
		return lineEntriesSentButNotIssued;
	}

	private StudentCurricularPlan findStudentCurricularPlan(final Student student) {
		final ExecutionYear executionYear = this.getExecutionYear();
		final DateTime begin = executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight();
		final DateTime end = executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight();

		final Set<StudentCurricularPlan> studentCurricularPlans = this.getStudentCurricularPlans(begin, end, student);
		if (studentCurricularPlans.size() == 1) {
			return studentCurricularPlans.iterator().next();
		} else if (studentCurricularPlans.size() > 1) {
			final StudentCurricularPlan max = findMaxStudentCurricularPlan(studentCurricularPlans);
			return max;
		}
		return null;
	}

	public static Set<StudentCurricularPlan> getStudentCurricularPlans(final DateTime begin, final DateTime end,
			final Student student) {
		final Set<StudentCurricularPlan> studentCurricularPlans = new HashSet<StudentCurricularPlan>();

		for (final Registration registration : student.getRegistrationsSet()) {
			if (!registration.isActive()) {
				continue;
			}
			final RegistrationAgreement registrationAgreement = registration.getRegistrationAgreement();
			if (registrationAgreement != RegistrationAgreement.NORMAL && registrationAgreement != RegistrationAgreement.TOTAL
					&& registrationAgreement != RegistrationAgreement.ANGOLA_TELECOM
					&& registrationAgreement != RegistrationAgreement.MITP) {
				continue;
			}
			final DegreeType degreeType = registration.getDegreeType();
			if (!degreeType.isBolonhaType()) {
				continue;
			}
			for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
				if (studentCurricularPlan.isActive()) {
					if (degreeType == DegreeType.BOLONHA_DEGREE || degreeType == DegreeType.BOLONHA_MASTER_DEGREE
							|| degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE
							|| degreeType == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) {
						studentCurricularPlans.add(studentCurricularPlan);
					} else {
						final RegistrationState registrationState = registration.getActiveState();
						if (registrationState != null) {
							final DateTime dateTime = registrationState.getStateDate();
							if (!dateTime.isBefore(begin) && !dateTime.isAfter(end)) {
								studentCurricularPlans.add(studentCurricularPlan);
							}
						}
					}
				}
			}
		}
		return studentCurricularPlans;
	}

	public static DegreeType findMaxDegreeType(final Set<StudentCurricularPlan> studentCurricularPlans) {
		return Collections.max(studentCurricularPlans, StudentCurricularPlan.COMPARATOR_BY_DEGREE_TYPE).getDegreeType();
	}

	protected void createCardGenerationEntry(final Person person, final StudentCurricularPlan studentCurricularPlan) {
		if (studentCurricularPlan.getDegreeType() == DegreeType.MASTER_DEGREE) {
			System.out.println("Master degree student curricular plan: "
					+ studentCurricularPlan.getDegreeCurricularPlan().getName() + " - " + studentCurricularPlan.getDegreeType()
					+ " " + person.getUsername());
		}
		final CardGenerationEntry cardGenerationEntry = new CardGenerationEntry();
		cardGenerationEntry.setCreated(getCreated());
		cardGenerationEntry.setCardGenerationBatch(this);
		cardGenerationEntry.setPerson(person);
		cardGenerationEntry.setLine(studentCurricularPlan);

		final ExecutionYear executionYear = getExecutionYear();
		for (final CardGenerationEntry otherCardGenerationEntry : person.getCardGenerationEntriesSet()) {
			if (otherCardGenerationEntry != cardGenerationEntry) {
				final CardGenerationBatch cardGenerationBatch = otherCardGenerationEntry.getCardGenerationBatch();
				if (cardGenerationBatch.getExecutionYear() == executionYear) {
					if (cardGenerationBatch == this) {
						new CardGenerationProblem(this, "message.person.has.multiple.entries.in.same.batch",
								person.getUsername(), person);
					} else {
						new CardGenerationProblem(this, "message.person.has.multiple.entries.in.same.execution.year",
								person.getUsername(), person);
					}
				}
			}
		}
	}

	public boolean contains(final Person person) {
		for (final CardGenerationEntry cardGenerationEntry : person.getCardGenerationEntriesSet()) {
			if (cardGenerationEntry.getCardGenerationBatch() == this) {
				return true;
			}
		}
		return false;
	}

	public CardGenerationEntry createCardGenerationEntries(final String line, String identificationId) {
		CardGenerationEntry entry = createCardGenerationEntries(line);
		entry.setDocumentID(identificationId);
		return entry;
	}

	public CardGenerationEntry createCardGenerationEntries(final String line) {
		final CardGenerationEntry cardGenerationEntry = new CardGenerationEntry();
		cardGenerationEntry.setCreated(getCreated());
		cardGenerationEntry.setCardGenerationBatch(this);
		cardGenerationEntry.setPerson(null);
		cardGenerationEntry.setLine(CardGenerationEntry.normalize(line));
		return cardGenerationEntry;
	}

	public CardGenerationBatch getCardGenerationBatchProblems() {
		final ExecutionYear executionYear = getExecutionYear();
		final String description = "Com Problemas";
		for (final CardGenerationBatch cardGenerationBatch : executionYear.getCardGenerationBatchesSet()) {
			if (description.equals(cardGenerationBatch.getDescription())) {
				return cardGenerationBatch;
			}
		}
		return new CardGenerationBatch(description, executionYear, true);
	}

	private StudentCurricularPlan findStudentCurricularPlan(final Student student, final DateTime begin, final DateTime end) {
		final Set<StudentCurricularPlan> studentCurricularPlans = getStudentCurricularPlans(begin, end, student);
		if (studentCurricularPlans.size() == 1) {
			return studentCurricularPlans.iterator().next();
		} else if (studentCurricularPlans.size() > 1) {
			final StudentCurricularPlan max = findMaxStudentCurricularPlan(studentCurricularPlans);
			return max;
		}
		return null;
	}

	private StudentCurricularPlan findMaxStudentCurricularPlan(final Set<StudentCurricularPlan> studentCurricularPlans) {
		return Collections.max(studentCurricularPlans, new Comparator<StudentCurricularPlan>() {

			@Override
			public int compare(final StudentCurricularPlan o1, final StudentCurricularPlan o2) {
				final DegreeType degreeType1 = o1.getDegreeType();
				final DegreeType degreeType2 = o2.getDegreeType();
				if (degreeType1 == degreeType2) {
					final YearMonthDay yearMonthDay1 = o1.getStartDateYearMonthDay();
					final YearMonthDay yearMonthDay2 = o2.getStartDateYearMonthDay();
					final int c = yearMonthDay1.compareTo(yearMonthDay2);
					return c == 0 ? DomainObject.COMPARATOR_BY_ID.compare(o1, o2) : c;
				} else {
					return degreeType1.compareTo(degreeType2);
				}
			}

		});
	}

	public static SortedSet<CardGenerationBatch> getAvailableBatchesFor() {
		final SortedSet<CardGenerationBatch> cardGenerationBatchs = new TreeSet<CardGenerationBatch>(COMPARATOR_BY_CREATION_DATE);
		for (final CardGenerationBatch cardGenerationBatch : RootDomainObject.getInstance().getCardGenerationBatchesSet()) {
			final ExecutionYear executionYear = cardGenerationBatch.getExecutionYear();
			if (executionYear.isCurrent()) {
				cardGenerationBatchs.add(cardGenerationBatch);
			}
		}
		return cardGenerationBatchs;
	}

	@Service
	public void createCardGenerationEntry(final Student student) {
		final ExecutionYear executionYear = getExecutionYear();
		final DateTime begin = executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight();
		final DateTime end = executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight();;

		final Person person = student.getPerson();
		if (person == null) {
			return;
		}

		final Teacher teacher = person.getTeacher();
		final Employee employee = person.getEmployee();

		if ((teacher == null || !teacher.isActive()) && (employee == null || !employee.isActive())
				&& !student.getActiveRegistrations().isEmpty()) {
			final StudentCurricularPlan studentCurricularPlan = findStudentCurricularPlan(student, begin, end);
			if (studentCurricularPlan != null && studentCurricularPlan.getDegreeType() != DegreeType.EMPTY) {
				final CardGenerationEntry cardGenerationEntry = new CardGenerationEntry();
				cardGenerationEntry.setCreated(new DateTime());
				cardGenerationEntry.setCardGenerationBatch(this);
				cardGenerationEntry.setPerson(person);
				cardGenerationEntry.setLine(studentCurricularPlan);
			}
		}
	}

	private CardGenerationEntry createCardGeneratioEntry(ImportationDataRecord record, Person person, ImportationReport report) {
		if (!person.hasCardGenerationEntryLine(record.getLine())) {
			checkDuplicateLine(person, record, report);
			final CardGenerationEntry cardGenerationEntry =
					createCardGenerationEntries(record.getLine(), record.getIdentificationId());
			cardGenerationEntry.setPerson(person);
			if (person.getCardGenerationEntriesCount() > 1) {
				cardGenerationEntry.incrementVersionNumber();
			}
			return cardGenerationEntry;
		}
		return null;
	}

	private void checkDuplicateLine(final Person person, ImportationDataRecord record, ImportationReport report) {
		final String lineToCompare = record.getComparableLine();
		for (final CardGenerationEntry cardGenerationEntry : person.getCardGenerationEntriesSet()) {
			final String otherLineToCompare = cardGenerationEntry.getComparableLine();
			if (!otherLineToCompare.equals(lineToCompare)) {
				report.addDuplicatedLines(cardGenerationEntry.getNormalizedLine(), record.getLine());
			}
		}
	}

	@Service
	public static CardGenerationBatch findOrCreate(ExecutionYear executionYear, String description) {
		for (final CardGenerationBatch cardGenerationBatch : executionYear.getCardGenerationBatchesSet()) {
			if (description.equals(cardGenerationBatch.getDescription())) {
				return cardGenerationBatch;
			}
		}
		return new CardGenerationBatch(description, executionYear, true);
	}

	@Service
	public ImportationReport importCardIdentificationsFromFile(String contents) {
		ImportationReport report = new ImportationReport();
		CardGenerationBatch cardGenerationBatchWithProblems = this.getCardGenerationBatchProblems();

		for (final String fline : contents.split("\n")) {
			if (fline.isEmpty()) {
				continue;
			}

			ImportationDataRecord record = new ImportationDataRecord(fline);
			Person person = findPerson(record, report);

			if (person == null) {
				createEntryForPersonNotFound(record, cardGenerationBatchWithProblems);
				continue;
			}

			if (person.hasCardGenerationEntryMatchingLine(record.getLine())) {
				continue;
			}

			CardGenerationEntry cardGenerationEntry = this.createCardGeneratioEntry(record, person, report);

			if (cardGenerationEntry == null) {
				continue;
			}

			final Category category = cardGenerationEntry.getCategory();

			if (category == Category.CODE_73 || category == Category.CODE_82 || category == Category.CODE_96) {
				final String studentLine = createNewLineForStudent(person.getStudent());
				if (studentLine == null) {
					cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchWithProblems);
					new CardGenerationProblem(cardGenerationBatchWithProblems, "multiple.user.information.type.not.crossed",
							record.getIdentificationId(), person);
					continue;
				}

				final Category categoryForStudentLine = CardGenerationEntry.readCategory(studentLine);
				final String newLine = merge(record.getLine(), record.getCategory(), studentLine, categoryForStudentLine);
				cardGenerationEntry.setLine(newLine);
				ReportEntry reportEntry = report.findReportEntryByNumbers(record.getNumbers());
				reportEntry.markAsCreated();
			} else if (cardGenerationEntry.getCardGenerationBatch() == this && person != null && person.hasRole(RoleType.STUDENT)) {
				final String studentLine = createNewLineForStudent(person.getStudent());
				if (studentLine == null) {
					continue;
				}

				final Category categoryForStudentLine = CardGenerationEntry.readCategory(studentLine);
				final String newLine = merge(record.getLine(), record.getCategory(), studentLine, categoryForStudentLine);
				cardGenerationEntry.setLine(newLine);
				ReportEntry reportEntry = report.findReportEntryByNumbers(record.getNumbers());
				reportEntry.markAsCreated();
			}
		}

		return report;
	}

	private String createNewLineForStudent(final Student student) {
		if (student == null || student.getActiveRegistrations().isEmpty()) {
			return null;
		}

		final StudentCurricularPlan studentCurricularPlan = findStudentCurricularPlan(student);
		return studentCurricularPlan != null ? CardGenerationEntry.createLine(studentCurricularPlan) : null;
	}

	private void createEntryForPersonNotFound(ImportationDataRecord record, CardGenerationBatch cardGenerationBatchWithProblems) {
		final CardGenerationEntry cardGenerationEntry = this.createCardGenerationEntries(record.getLine());
		cardGenerationEntry.setDocumentID(record.getIdentificationId());
		cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchWithProblems);
		new CardGenerationProblem(cardGenerationBatchWithProblems, "no.person.found", record.getIdentificationId(), null);
	}

	private Person findPerson(ImportationDataRecord record, ImportationReport report) {
		final Collection<Person> peopleForId = Person.findPersonByDocumentID(record.getIdentificationId());

		if (peopleForId.size() == 1) {
			Person matchedPerson = peopleForId.iterator().next();
			report.addMatchById(record.getIdentificationId(), matchedPerson, record.getNumbers());
			return matchedPerson;
		} else if (peopleForId.size() > 1) {
			report.addIdDuplication(record.getIdentificationId(), new java.util.ArrayList<Person>(peopleForId),
					record.getNumbers());
		}

		final Collection<Person> peopleForName = Person.findPerson(record.getName());
		if (peopleForName.size() == 1) {
			Person matchedPerson = peopleForName.iterator().next();
			report.addMatchByName(record.getName(), matchedPerson, record.getNumbers());
			return matchedPerson;
		} else if (peopleForName.size() > 1) {
			report.addNameDuplication(record.getName(), new java.util.ArrayList<Person>(peopleForName), record.getNumbers());
		}

		report.addUnmatchedEntry(record.getIdentificationId(), record.getNumbers());
		return null;
	}

	public static String ERROR_IMPORT_IDENTIFICATION_CARD_DESCRIPTION = "error.import.identification.cards.same.category ";

	public static String merge(final String line1, final String line2) {
		final Category category1 = CardGenerationEntry.readCategory(line1);
		final Category category2 = CardGenerationEntry.readCategory(line2);
		return merge(line1, category1, line2, category2);
	}

	public static String merge(final String line1, final Category category1, final String line2, final Category category2) {
		if (category1 == Category.CODE_71 && category2 == Category.CODE_94) {
			return mergeStudent(line1, line2, Category.CODE_73);
		} else if (category2 == Category.CODE_71 && category1 == Category.CODE_94) {
			return mergeStudent(line2, line1, Category.CODE_73);
		}
		if (category1 == Category.CODE_71 && category2 == Category.CODE_95) {
			return mergeStudent(line1, line2, Category.CODE_73);
		} else if (category2 == Category.CODE_71 && category1 == Category.CODE_95) {
			return mergeStudent(line2, line1, Category.CODE_73);
		}
		if (category1 == Category.CODE_71 && category2 == Category.CODE_92) {
			return mergeStudent(line1, line2, Category.CODE_73);
		} else if (category2 == Category.CODE_71 && category1 == Category.CODE_92) {
			return mergeStudent(line2, line1, Category.CODE_73);
		}
		if (category1 == Category.CODE_71 && category2 == Category.CODE_99) {
			return mergeStudent(line1, line2, Category.CODE_73);
		} else if (category2 == Category.CODE_71 && category1 == Category.CODE_99) {
			return mergeStudent(line2, line1, Category.CODE_73);
		}
		if (category1 == Category.CODE_72 && category2 == Category.CODE_92) {
			return mergeStudent(line1, line2, Category.CODE_73);
		} else if (category2 == Category.CODE_72 && category1 == Category.CODE_92) {
			return mergeStudent(line2, line1, Category.CODE_73);
		}
		if (category1 == Category.CODE_72 && category2 == Category.CODE_94) {
			return mergeStudent(line1, line2, Category.CODE_73);
		} else if (category2 == Category.CODE_72 && category1 == Category.CODE_94) {
			return mergeStudent(line2, line1, Category.CODE_73);
		}
		if (category1 == Category.CODE_72 && category2 == Category.CODE_95) {
			return mergeStudent(line1, line2, Category.CODE_73);
		} else if (category2 == Category.CODE_72 && category1 == Category.CODE_95) {
			return mergeStudent(line2, line1, Category.CODE_73);
		}
		if (category1 == Category.CODE_81 && category2 == Category.CODE_94) {
			return mergeStudent(line1, line2, Category.CODE_82);
		} else if (category2 == Category.CODE_81 && category1 == Category.CODE_94) {
			return mergeStudent(line2, line1, Category.CODE_82);
		}
		if (category1 == Category.CODE_81 && category2 == Category.CODE_95) {
			return mergeStudent(line1, line2, Category.CODE_82);
		} else if (category2 == Category.CODE_81 && category1 == Category.CODE_95) {
			return mergeStudent(line2, line1, Category.CODE_82);
		}
		if (category1 == Category.CODE_81 && category2 == Category.CODE_96) {
			return line1;
		} else if (category2 == Category.CODE_81 && category1 == Category.CODE_96) {
			return line2;
		}
		if (category1 == Category.CODE_81 && category2 == Category.CODE_99) {
			return mergeStudent(line1, line2, Category.CODE_82);
		} else if (category2 == Category.CODE_81 && category1 == Category.CODE_99) {
			return mergeStudent(line2, line1, Category.CODE_82);
		}
		if (category1 == Category.CODE_96 && category2 == Category.CODE_99) {
			return line1;
		} else if (category2 == Category.CODE_96 && category1 == Category.CODE_99) {
			return line2;
		}
		if (category1 == Category.CODE_82 && category2 == Category.CODE_92) {
			return mergeInMergedFormat(line1, line2, Category.CODE_82);
		} else if (category2 == Category.CODE_82 && category1 == Category.CODE_92) {
			return mergeInMergedFormat(line2, line1, Category.CODE_82);
		}
		if (category1 == Category.CODE_82 && category2 == Category.CODE_94) {
			return mergeInMergedFormat(line1, line2, Category.CODE_82);
		} else if (category2 == Category.CODE_82 && category1 == Category.CODE_94) {
			return mergeInMergedFormat(line2, line1, Category.CODE_82);
		}
		if (category1 == Category.CODE_82 && category2 == Category.CODE_95) {
			return mergeInMergedFormat(line1, line2, Category.CODE_82);
		} else if (category2 == Category.CODE_82 && category1 == Category.CODE_95) {
			return mergeInMergedFormat(line2, line1, Category.CODE_82);
		}
		if (category1 == Category.CODE_83 && category2 == Category.CODE_92) {
			return mergeStudent(line1, line2, Category.CODE_82);
		} else if (category2 == Category.CODE_83 && category1 == Category.CODE_92) {
			return mergeStudent(line2, line1, Category.CODE_82);
		}
		if (category1 == Category.CODE_83 && category2 == Category.CODE_95) {
			return mergeStudent(line1, line2, Category.CODE_82);
		} else if (category2 == Category.CODE_83 && category1 == Category.CODE_95) {
			return mergeStudent(line2, line1, Category.CODE_82);
		}
		if (category1 == Category.CODE_83 && category2 == Category.CODE_94) {
			return mergeStudent(line1, line2, Category.CODE_82);
		} else if (category2 == Category.CODE_83 && category1 == Category.CODE_94) {
			return mergeStudent(line2, line1, Category.CODE_82);
		}
		if (category1 == Category.CODE_92 && category2 == Category.CODE_92) {
			throw new DomainException(ERROR_IMPORT_IDENTIFICATION_CARD_DESCRIPTION + "??? 92 + 92");
		} else if (category2 == Category.CODE_92 && category1 == Category.CODE_92) {
			throw new DomainException(ERROR_IMPORT_IDENTIFICATION_CARD_DESCRIPTION + "??? 92 + 92");
		}
		if (category1 == Category.CODE_92 && category2 == Category.CODE_96) {
			return mergeInMergedFormat(line2, line1, Category.CODE_96);
		} else if (category2 == Category.CODE_92 && category1 == Category.CODE_96) {
			return mergeInMergedFormat(line1, line2, Category.CODE_96);
		}
		if (category1 == Category.CODE_94 && category2 == Category.CODE_94) {
			throw new DomainException(ERROR_IMPORT_IDENTIFICATION_CARD_DESCRIPTION + "??? 94 + 94");
		} else if (category2 == Category.CODE_94 && category1 == Category.CODE_94) {
			throw new DomainException(ERROR_IMPORT_IDENTIFICATION_CARD_DESCRIPTION + "??? 94 + 94");
		}
		if (category1 == Category.CODE_94 && category2 == Category.CODE_95) {
			return line2;
		} else if (category2 == Category.CODE_94 && category1 == Category.CODE_95) {
			return line1;
		}
		if (category1 == Category.CODE_94 && category2 == Category.CODE_96) {
			return mergeInMergedFormat(line2, line1, Category.CODE_96);
		} else if (category2 == Category.CODE_94 && category1 == Category.CODE_96) {
			return mergeInMergedFormat(line1, line2, Category.CODE_96);
		}
		if (category1 == Category.CODE_95 && category2 == Category.CODE_95) {
			throw new DomainException(ERROR_IMPORT_IDENTIFICATION_CARD_DESCRIPTION + "??? 95 + 95");
		} else if (category2 == Category.CODE_95 && category1 == Category.CODE_95) {
			throw new DomainException(ERROR_IMPORT_IDENTIFICATION_CARD_DESCRIPTION + "??? 95 + 95");
		}
		if (category1 == Category.CODE_95 && category2 == Category.CODE_96) {
			return mergeInMergedFormat(line2, line1, Category.CODE_96);
		} else if (category2 == Category.CODE_95 && category1 == Category.CODE_96) {
			return mergeInMergedFormat(line1, line2, Category.CODE_96);
		}
		if (category1 == Category.CODE_95 && category2 == Category.CODE_97) {
			return mergeInMergedFormat(line1, line2, Category.CODE_96);
		} else if (category2 == Category.CODE_95 && category1 == Category.CODE_97) {
			return mergeInMergedFormat(line2, line1, Category.CODE_96);
		} else {
			throw new DomainException(ERROR_IMPORT_IDENTIFICATION_CARD_DESCRIPTION + "Unhandled case: " + category1.getCode()
					+ " & " + category2.getCode());
		}
	}

	private static String mergeStudent(final String lineEmployee, final String lineStudent, final Category destinationCategory) {
		final StringBuilder newLine = new StringBuilder();
		newLine.append(lineStudent.substring(0, 11));
		newLine.append(destinationCategory.getCode());
		// newLine.append(lineStudent.substring(13, 34));
		newLine.append(lineEmployee.substring(13, 34));
		newLine.append(lineStudent.substring(11, 21));
		newLine.append(lineStudent.substring(83, 95));
		newLine.append("           ");
		newLine.append(lineStudent.substring(67, 75));
		newLine.append(lineEmployee.substring(67, 75));
		newLine.append(lineStudent.substring(83, 95));
		newLine.append("             ");
		newLine.append(lineEmployee.substring(44, 67));
		newLine.append(lineEmployee.substring(131));
		return newLine.toString();
	}

	private static String mergeInMergedFormat(final String lineEmployee, final String lineStudent,
			final Category destinationCategory) {

		final StringBuilder newLine = new StringBuilder();
		newLine.append(lineStudent.substring(0, 11));
		newLine.append(destinationCategory.getCode());
		newLine.append(lineEmployee.substring(13, 34));
		// newLine.append(lineStudent.substring(13, 34));
		newLine.append(lineStudent.substring(11, 21));
		newLine.append(lineStudent.substring(83, 95));
		newLine.append("           ");
		newLine.append(lineStudent.substring(67, 75));
		newLine.append(lineEmployee.substring(75, 83));
		newLine.append(lineStudent.substring(83, 95));
		newLine.append("             ");
		newLine.append(lineEmployee.substring(108, 131));
		newLine.append(lineEmployee.substring(131));

		return newLine.toString();
	}

	private static class ImportationDataRecord {
		private static Pattern subCategoryPattern = Pattern.compile("[\\./,-]");
		private static Pattern workplacePattern = Pattern.compile("[&,_/:\\.]");
		private static Pattern lineRemainderPattern = Pattern.compile("[,\\.]");

		private String record;
		private String identificationId;
		private String line;
		private String name;

		private Set<Integer> numbers;

		public ImportationDataRecord(String record) {
			this.record = record;
			this.identificationId = this.record.substring(0, 20).trim();
			this.line = normalize(this.record.substring(20)) + "\r\n";
			this.name = this.line.substring(178).trim();
		}

		public String getIdentificationId() {
			return this.identificationId;
		}

		public String getLine() {
			return this.line;
		}

		public String getName() {
			return this.name;
		}

		public String getComparableLine() {
			return CardGenerationEntry.makeComparableLine(this.getLine());
		}

		public Category getCategory() {
			return CardGenerationEntry.readCategory(this.getLine());
		}

		public Set<Integer> getNumbers() {
			if (numbers == null) {
				readNumbers();
			}

			return this.numbers;
		}

		private void readNumbers() {
			final Set<Integer> numbers = new TreeSet<Integer>();
			numbers.add(Integer.valueOf(this.line.substring(13, 21).trim()));

			final Integer i2 = Integer.valueOf(this.line.substring(36, 44).trim());
			if (i2.intValue() > 0) {
				numbers.add(i2);
			}

			final String si3 = this.line.substring(62, 67).trim();
			if (si3.length() > 0) {
				numbers.add(Integer.valueOf(si3));
			}

			final String si4 = this.line.substring(67, 75).trim();
			if (si4.length() > 0) {
				numbers.add(Integer.valueOf(si4));
			}

			final String si5 = this.line.substring(126, 131).trim();
			if (si5.length() > 0) {
				numbers.add(Integer.valueOf(si5));
			}

			this.numbers = numbers;
		}

		private String normalize(String line) {
			StringBuilder sb = new StringBuilder();
			sb.append(lineRemainderPattern.matcher(line.substring(0, 108)).replaceAll(" "));
			sb.append(subCategoryPattern.matcher(line.substring(108, 131)).replaceAll(" "));
			sb.append(workplacePattern.matcher(line.substring(131, 178)).replaceAll(" "));
			sb.append(lineRemainderPattern.matcher(line.substring(178)).replaceAll(" "));
			return sb.toString();
		}
	}

	public static class ReportEntry implements java.io.Serializable {
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		private String key;
		public java.util.List<Person> duplicatedPersons;
		public Set<Integer> numbers;
		public Boolean created;

		private ReportEntry(String searchKey, java.util.List<Person> duplicatedPersons, Set<Integer> numbers) {
			this.key = searchKey;
			this.duplicatedPersons = duplicatedPersons;
			this.numbers = numbers;
			this.created = Boolean.FALSE;
		}

		private ReportEntry(String searchKey, Set<Integer> numbers) {
			this(searchKey, null, numbers);
		}

		public static ReportEntry createDuplicationEntry(String key, java.util.List<Person> duplicatedPersons,
				Set<Integer> numbers) {
			return new ReportEntry(key, duplicatedPersons, numbers);
		}

		public static ReportEntry createUnmatchedEntry(String key, Set<Integer> numbers) {
			return new ReportEntry(key, numbers);
		}

		public static ReportEntry createMatchedEntry(String key, Person person, Set<Integer> numbers) {
			java.util.List<Person> personList = new java.util.ArrayList<Person>();
			personList.add(person);
			return new ReportEntry(key, personList, numbers);
		}

		public String getKey() {
			return this.key;
		}

		public Boolean getCreated() {
			return this.created;
		}

		public void markAsCreated() {
			this.created = Boolean.TRUE;
		}

		public String getFormattedNumbers() {
			StringBuilder sb = new StringBuilder();
			for (Integer number : this.numbers) {
				sb.append(number).append(" ");
			}

			return sb.toString();
		}

		public java.util.List<Person> getDuplicatedPersons() {
			return this.duplicatedPersons;
		}

		public Boolean equals(Set<Integer> numbers) {
			return numbers.equals(this.numbers);
		}
	}

	public static class ImportationReport implements java.io.Serializable {
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		private int matches;
		private java.util.List<ReportEntry> duplicatedPersonsByName;
		private java.util.List<ReportEntry> duplicatedPersonsById;
		private java.util.List<ReportEntry> unmatchedEntries;
		private StringBuilder duplicatedLinesBuilder;
		private java.util.List<ReportEntry> matchedEntriesWithId;
		private java.util.List<ReportEntry> matchedEntriesWithName;

		public ImportationReport() {
			this.duplicatedPersonsById = new java.util.ArrayList<ReportEntry>();
			this.duplicatedPersonsByName = new java.util.ArrayList<ReportEntry>();
			this.unmatchedEntries = new java.util.ArrayList<ReportEntry>();
			this.duplicatedLinesBuilder = new StringBuilder();
			this.matchedEntriesWithId = new java.util.ArrayList<ReportEntry>();
			this.matchedEntriesWithName = new java.util.ArrayList<ReportEntry>();
		}

		@Deprecated
		public void incrementMatches() {
			this.matches++;
		}

		public void addMatchById(String key, Person matchedPerson, Set<Integer> numbers) {
			this.matchedEntriesWithId.add(ReportEntry.createMatchedEntry(key, matchedPerson, numbers));
		}

		public void addMatchByName(String key, Person matchedPerson, Set<Integer> numbers) {
			this.matchedEntriesWithName.add(ReportEntry.createMatchedEntry(key, matchedPerson, numbers));
		}

		public void addNameDuplication(String name, java.util.List<Person> duplicatedPersons, Set<Integer> numbers) {
			this.duplicatedPersonsByName.add(ReportEntry.createDuplicationEntry(name, duplicatedPersons, numbers));
		}

		public void addIdDuplication(String id, java.util.List<Person> duplicatedPersons, Set<Integer> numbers) {
			this.duplicatedPersonsById.add(ReportEntry.createDuplicationEntry(id, duplicatedPersons, numbers));
		}

		public void addUnmatchedEntry(String id, Set<Integer> numbers) {
			this.unmatchedEntries.add(ReportEntry.createUnmatchedEntry(id, numbers));
		}

		public void addDuplicatedLines(String cardGenerationLine, String line) {
			this.duplicatedLinesBuilder.append("1: ").append(cardGenerationLine);
			this.duplicatedLinesBuilder.append("2: ").append(line);
		}

		public java.util.List<ReportEntry> getUnmatchedEntries() {
			return this.unmatchedEntries;
		}

		public java.util.List<ReportEntry> getDuplicatedPersonsById() {
			return this.duplicatedPersonsById;
		}

		public java.util.List<ReportEntry> getDuplicatedPersonsByName() {
			return this.duplicatedPersonsByName;
		}

		public java.util.List<ReportEntry> getMatchedPersonsById() {
			return this.matchedEntriesWithId;
		}

		public java.util.List<ReportEntry> getMatchedPersonsByName() {
			return this.matchedEntriesWithName;
		}

		public ReportEntry findReportEntryByNumbers(final Set<Integer> numbers) {
			ReportEntry entry = (ReportEntry) CollectionUtils.find(this.matchedEntriesWithId, new Predicate() {

				@Override
				public boolean evaluate(Object arg0) {
					return ((ReportEntry) arg0).equals(numbers);
				}
			});

			if (entry != null) {
				return entry;
			}

			entry = (ReportEntry) CollectionUtils.find(this.matchedEntriesWithName, new Predicate() {

				@Override
				public boolean evaluate(Object arg0) {
					return ((ReportEntry) arg0).equals(numbers);
				}
			});

			return entry;
		}

		public String getDuplicatedLines() {
			return this.duplicatedLinesBuilder.toString();
		}

		public Integer getMatches() {
			return this.matchedEntriesWithId.size() + this.matchedEntriesWithName.size();
		}
	}

}

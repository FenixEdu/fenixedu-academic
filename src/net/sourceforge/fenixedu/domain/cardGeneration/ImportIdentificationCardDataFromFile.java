package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.IdDocument;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;

public class ImportIdentificationCardDataFromFile {

	public class PersonEntry {
		private final String name;
		private final Set<Integer> numbers;
		private final String identificationId;
		private Person person;

		public PersonEntry(final String name, final Set<Integer> numbers, final String identificationId, final Person person) {
			this.name = name;
			this.numbers = numbers;
			this.identificationId = identificationId;
			this.person = person;
		}
	}

	public class PersonEntryMap {
		private final Map<String, Set<PersonEntry>> byName = new HashMap<String, Set<PersonEntry>>();
		private final Map<String, Set<PersonEntry>> byIdentificationId = new HashMap<String, Set<PersonEntry>>();
		private final Map<Integer, Set<PersonEntry>> byNumber = new HashMap<Integer, Set<PersonEntry>>();

		public void add(final String identificationId, final String name, final Set<Integer> numbers, final Person person) {
			final PersonEntry personEntry = new PersonEntry(name, numbers, identificationId, person);
			add(byName, personEntry, name);
			add(byIdentificationId, personEntry, identificationId);
			for (final Integer number : numbers) {
				add(byNumber, personEntry, number);
			}
		}

		protected void add(final Map map, final PersonEntry personEntry, final Object key) {
			Set<PersonEntry> personEntries = (Set<PersonEntry>) map.get(key);
			if (personEntries == null) {
				personEntries = new HashSet<PersonEntry>();
				map.put(key, personEntries);
			}
			personEntries.add(personEntry);
		}

		public PersonEntry get(final String nameFromLine, final Set<Integer> numbers) {
			final Set<PersonEntry> personEntries = byName.get(nameFromLine);
			if (personEntries != null) {
				for (final PersonEntry personEntry : personEntries) {
					for (final Integer number : numbers) {
						if (personEntry.numbers.contains(number)) {
							return personEntry;
						}
					}
				}
			}
			return null;
		}

	}

	private final Set<Person> peopleWithBadNames = new HashSet<Person>();

	private Map<Integer, Set<Person>> peopleByNumber = new HashMap<Integer, Set<Person>>();
	private Map<String, Set<Person>> peopleByName = new HashMap<String, Set<Person>>();

	private int matched = 0;
	private int unmatched = 0;
	private int multipleIdMatches = 0;
	private int multipleNameMatches = 0;
	private int matchedLines = 0;
	private int newLines = 0;

	private StringBuilder result = new StringBuilder();

	protected String run(final String description, final ExecutionYear executionYear, final String contents) throws Exception {

		final CardGenerationBatch cardGenerationBatch = findOrCreate(executionYear, description);
		final CardGenerationBatch cardGenerationBatchWithProblems = new CardGenerationBatch("Com Problemas", executionYear, true);
		final CardGenerationBatch cardGenerationBatchWithDuplicates = new CardGenerationBatch("Duplicados", executionYear, true);

		for (final String fline : contents.split("\n")) {
			if (!fline.isEmpty()) {
				final String identificationId = StringUtils.trim(fline.substring(0, 20).trim()).replace(" ", "");
				final String line = normalize(fline.substring(20)) + "\r\n";

				final Person person = findPerson(identificationId, line);
				final CardGenerationEntry cardGenerationEntry;
				if (person == null) {
					cardGenerationEntry = createEntry(cardGenerationBatch, identificationId, line);
					cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchWithProblems);
					new CardGenerationProblem(cardGenerationBatchWithProblems, "no.person.found", identificationId, null);
				} else {
//		    if (!hasMatchingLine(person, line)) {
					cardGenerationEntry = createEntry(cardGenerationBatch, identificationId, line, person);

					if (cardGenerationEntry != null) {
						final Category category = cardGenerationEntry.getCategory();
						if (category == Category.CODE_73 || category == Category.CODE_82 || category == Category.CODE_96) {
							final Category categoryForLine = CardGenerationEntry.readCategory(line);
							final String studentLine = createNewLine(person, cardGenerationBatch);
							if (studentLine == null) {
								if (category != Category.CODE_96) {
									cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchWithProblems);
									new CardGenerationProblem(cardGenerationBatchWithProblems,
											"multiple.user.information.type.not.crossed", identificationId, person);
								} else if (hasMatchingLine(person, line, cardGenerationEntry)) {
									cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchWithDuplicates);
									new CardGenerationProblem(cardGenerationBatchWithDuplicates,
											"duplicate.line.from.previous.batch", identificationId, person);
								}
							} else {
								final Category categoryForStudentLine = CardGenerationEntry.readCategory(studentLine);
								final String newLine = merge(line, categoryForLine, studentLine, categoryForStudentLine);
								if (hasMatchingLine(person, newLine, cardGenerationEntry)) {
									cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchWithDuplicates);
									new CardGenerationProblem(cardGenerationBatchWithDuplicates,
											"duplicate.line.from.previous.batch", identificationId, person);
								}
								cardGenerationEntry.setLine(newLine);
							}
						} else if (cardGenerationEntry.getCardGenerationBatch() == cardGenerationBatch && person != null
								&& person.hasRole(RoleType.STUDENT)) {
							final Category categoryForLine = CardGenerationEntry.readCategory(line);
							final String studentLine = createNewLine(person, cardGenerationBatch);
							if (studentLine == null) {
								if (hasMatchingLine(person, line, cardGenerationEntry)) {
									cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchWithDuplicates);
									new CardGenerationProblem(cardGenerationBatchWithDuplicates,
											"duplicate.line.from.previous.batch", identificationId, person);
								}
//			    		cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchUnmatched);
//			    		new CardGenerationProblem(cardGenerationBatchUnmatched, "person.has.student.role.but.cannot.generate.line", identificationId, person);
							} else {
								final Category categoryForStudentLine = CardGenerationEntry.readCategory(studentLine);
								final String newLine = merge(line, categoryForLine, studentLine, categoryForStudentLine);
								if (hasMatchingLine(person, newLine, cardGenerationEntry)) {
									cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchWithDuplicates);
									new CardGenerationProblem(cardGenerationBatchWithDuplicates,
											"duplicate.line.from.previous.batch", identificationId, person);
								}
								cardGenerationEntry.setLine(newLine);
							}
						} else {
							if (hasMatchingLine(person, line, cardGenerationEntry)) {
								cardGenerationEntry.setCardGenerationBatch(cardGenerationBatchWithDuplicates);
								new CardGenerationProblem(cardGenerationBatchWithDuplicates,
										"duplicate.line.from.previous.batch", identificationId, person);
							}
						}
					}
//		    }
				}
			}
		}

		result.append("Matched " + matched + "\n");
		result.append("Multiple id matches " + multipleIdMatches + "\n");
		result.append("Multiple name matches " + multipleNameMatches + "\n");
		result.append("Unmatched " + unmatched + "\n");
		result.append("Matched lines " + matchedLines + "\n");
		result.append("New lines " + newLines + "\n");

		System.out.flush();
		System.err.flush();

		return result.toString();
	}

	private CardGenerationBatch findOrCreate(final ExecutionYear executionYear, final String description) {
		for (final CardGenerationBatch cardGenerationBatch : executionYear.getCardGenerationBatchesSet()) {
			if (cardGenerationBatch.getDescription() != null && cardGenerationBatch.getDescription().equals(description)) {
				return cardGenerationBatch;
			}
		}
		return new CardGenerationBatch(description, executionYear, true);
	}

	private boolean hasMatchingLine(final Person person, final String line, final CardGenerationEntry currentEntry) {
		for (final CardGenerationEntry cardGenerationEntry : person.getCardGenerationEntriesSet()) {
			if (currentEntry != cardGenerationEntry && cardGenerationEntry.matches(line)
					&& isInValidTimeFrame(cardGenerationEntry)) {
				return true;
			}
		}
		return false;
	}

	private String normalize(final String line) {
		final StringBuilder sb = new StringBuilder();
		sb.append(line.substring(0, 108));

		final String subCategory = line.substring(108, 131).replace('.', ' ').replace('-', ' ').replace('/', ' ');
		sb.append(subCategory);

		final String workPlace = line.substring(131, 178).replace('&', ' ').replace('_', ' ').replace('/', ' ').replace(':', ' ');
		sb.append(workPlace);

		sb.append(line.substring(178));

		return CardGenerationEntry.normalize(sb.toString().replace('.', ' ').replace(',', ' '));
	}

	private CardGenerationEntry createEntry(final CardGenerationBatch cardGenerationBatch, final String identificationId,
			final String line) {
		final CardGenerationEntry cardGenerationEntry = cardGenerationBatch.createCardGenerationEntries(line);
		cardGenerationEntry.setDocumentID(identificationId);
		return cardGenerationEntry;
	}

	private CardGenerationEntry createEntry(final CardGenerationBatch cardGenerationBatch, final String identificationId,
			final String line, final Person person) {
//	if (!alreadyHasLine(person, line)) {
//	    checkDuplicateLine(person, line);
		final CardGenerationEntry cardGenerationEntry = createEntry(cardGenerationBatch, identificationId, line);
		cardGenerationEntry.setPerson(person);
		if (person.getCardGenerationEntriesCount() > 1) {
			incrementVersionNumber(cardGenerationEntry);
		}
		return cardGenerationEntry;
//	}
//	return null;
	}

	private void incrementVersionNumber(final CardGenerationEntry cardGenerationEntry) {
		final String line = cardGenerationEntry.getLine();
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(line.substring(0, 30));
		stringBuilder.append("01");
		stringBuilder.append(line.substring(32));
		cardGenerationEntry.setLine(stringBuilder.toString());
	}

//    private void checkDuplicateLine(final Person person, final String line) {
//	final String lineToCompare = makeComparableLine(line.substring(0, 262));
//	for (final CardGenerationEntry cardGenerationEntry : person.getCardGenerationEntriesSet()) {
//	    final String otherLineToCompare = makeComparableLine(cardGenerationEntry.getLine().replace('.', ' ').substring(0, 262));
//	    if (!otherLineToCompare.equals(lineToCompare)) {
//		printWriter.write("1: " + cardGenerationEntry.getLine().replace('.', ' '));
//		printWriter.write("2: " + line);
//	    }
//	}
//    }

//    private String makeComparableLine(final String line) {
//	final StringBuilder stringBuilder = new StringBuilder();
//	stringBuilder.append(line.substring(0, 21));
//	stringBuilder.append(line.substring(26, 30));
//	stringBuilder.append(line.substring(32, 262));
//	return stringBuilder.toString();
//    }

	private boolean alreadyHasLine(final Person person, final String line) {
		for (final CardGenerationEntry cardGenerationEntry : person.getCardGenerationEntriesSet()) {
			if (isInValidTimeFrame(cardGenerationEntry)) {
				final Category category = cardGenerationEntry.getCategory();
				return category == Category.CODE_73 || category == Category.CODE_83 || category == Category.CODE_96
						|| cardGenerationEntry.getLine().replace('.', ' ').substring(0, 262).equals(line.substring(0, 262));
			}
		}
		return false;
	}

	private boolean isInValidTimeFrame(final CardGenerationEntry cardGenerationEntry) {
		final CardGenerationBatch cardGenerationBatch = cardGenerationEntry.getCardGenerationBatch();
		final ExecutionYear executionYear = cardGenerationBatch.getExecutionYear();
		return isInValidTimeFrame(executionYear);
	}

	private boolean isInValidTimeFrame(final ExecutionYear executionYear) {
		return executionYear.isCurrent() || isInValidTimeFrameLevel1(executionYear.getNextExecutionYear());
	}

	private boolean isInValidTimeFrameLevel1(final ExecutionYear executionYear) {
		return executionYear != null
				&& (executionYear.isCurrent() || isInValidTimeFrameLevel2(executionYear.getNextExecutionYear()));
	}

	private boolean isInValidTimeFrameLevel2(final ExecutionYear executionYear) {
		return executionYear != null && executionYear.isCurrent();
	}

	private Person findPerson(final String identificationId, final String line) {
		//final Collection<Person> peopleForId = Person.findPersonByDocumentID(identificationId);
		final Collection<Person> peopleForId = findPersonByDocumentID(identificationId);
		if (peopleForId.size() == 1) {
			matched++;
			return peopleForId.iterator().next();
		} else if (peopleForId.size() > 1) {
			multipleIdMatches++;
//	    System.out.println("Multiple people for: " + identificationId);
		} else {
//	    System.out.println("No match found for: " + identificationId);
		}

		final String name = getName(line);
		final Collection<Person> peopleForName = Person.findPerson(name);
		if (peopleForName.size() == 1) {
			matched++;
			return peopleForName.iterator().next();
		} else if (peopleForName.size() > 1) {
			multipleNameMatches++;
//	    System.out.println("Multiple people for: " + name);
		} else {
//	    System.out.println("No match found for: " + name);
		}

		final Set<Integer> numbers = getNumbers(line);

		result.append("No match found for: " + identificationId + " " + name);
		for (final Integer integer : numbers) {
			result.append(" " + integer);
		}
		if (peopleForId.size() > 1) {
			result.append(" multiple id's");
		}
		if (peopleForName.size() > 1) {
			result.append(" multiple names's");
		}
		result.append("\n");
		unmatched++;
		return null;
	}

	public static Collection<Person> findPersonByDocumentID(final String documentIDValue) {
		final Collection<Person> people = new ArrayList<Person>();
		if (!StringUtils.isEmpty(documentIDValue)) {
			for (final IdDocument idDocument : RootDomainObject.getInstance().getIdDocumentsSet()) {
				if (StringUtils.trim(idDocument.getValue()).replace(" ", "").equalsIgnoreCase(documentIDValue)) {
					people.add(idDocument.getPerson());
				}
			}
		}
		return people;
	}

	private void loadIndexes() {
		for (final Party party : RootDomainObject.getInstance().getPartysSet()) {
			if (party.isPerson()) {
				final Person person = (Person) party;
				try {
					final String personName = CardGenerationEntry.normalizePersonName(person).trim();
					addPerson(peopleByName, person, personName);

					if (person.hasEmployee()) {
						final Integer number = person.getEmployee().getEmployeeNumber();
						addPerson(peopleByNumber, person, number);
					}
					if (person.hasStudent()) {
						final Integer number = person.getStudent().getNumber();
						addPerson(peopleByNumber, person, number);
					}
				} catch (Error e) {
					// keep going... ignore the person for now.
					peopleWithBadNames.add(person);
				}
			}
		}
	}

	private void addPerson(final Map map, final Person person, final Object key) {
		Set<Person> personByNumber = (Set<Person>) map.get(key);
		if (personByNumber == null) {
			personByNumber = new HashSet<Person>();
			map.put(key, personByNumber);
		}
		personByNumber.add(person);
	}

	private String getName(final String line) {
		return line.substring(178).trim();
	}

	private Set<Integer> getNumbers(final String line) {
		final Set<Integer> numbers = new TreeSet<Integer>();
		numbers.add(Integer.valueOf(line.substring(13, 21).trim()));
		final Integer i2 = Integer.valueOf(line.substring(36, 44).trim());
		if (i2.intValue() > 0) {
			numbers.add(i2);
		}
		final String si3 = line.substring(62, 67).trim();
		if (si3.length() > 0) {
			numbers.add(Integer.valueOf(si3));
		}
		final String si4 = line.substring(67, 75).trim();
		if (si4.length() > 0) {
			numbers.add(Integer.valueOf(si4));
		}
		final String si5 = line.substring(126, 131).trim();
		if (si5.length() > 0) {
			numbers.add(Integer.valueOf(si5));
		}
		return numbers;
	}

	public static String createNewLine(final Person person, final CardGenerationBatch cardGenerationBatch) {
		final Student student = person.getStudent();
		if (student != null && !student.getActiveRegistrations().isEmpty()) {
			final StudentCurricularPlan studentCurricularPlan = findStudentCurricularPlan(cardGenerationBatch, student);
			if (studentCurricularPlan != null) {
				final String line = CardGenerationEntry.createLine(studentCurricularPlan);
				return line;
			}
		}
		return null;
	}

	private static StudentCurricularPlan findStudentCurricularPlan(final CardGenerationBatch cardGenerationBatch,
			final Student student) {
		final ExecutionYear executionYear = cardGenerationBatch.getExecutionYear();
		final DateTime begin = executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight();
		final DateTime end = executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight();;

		final Set<StudentCurricularPlan> studentCurricularPlans =
				cardGenerationBatch.getStudentCurricularPlans(begin, end, student);
		if (studentCurricularPlans.size() == 1) {
			return studentCurricularPlans.iterator().next();
		} else if (studentCurricularPlans.size() > 1) {
			final StudentCurricularPlan max = findMaxStudentCurricularPlan(studentCurricularPlans);
			return max;
		}
		return null;
	}

	private static StudentCurricularPlan findMaxStudentCurricularPlan(final Set<StudentCurricularPlan> studentCurricularPlans) {
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
		if (category1 == Category.CODE_92 && category2 == Category.CODE_92) {
			throw new Error("??? 92 + 92");
		} else if (category2 == Category.CODE_92 && category1 == Category.CODE_92) {
			throw new Error("??? 92 + 92");
		}
		if (category1 == Category.CODE_92 && category2 == Category.CODE_96) {
			return mergeInMergedFormat(line2, line1, Category.CODE_96);
		} else if (category2 == Category.CODE_92 && category1 == Category.CODE_96) {
			return mergeInMergedFormat(line1, line2, Category.CODE_96);
		}
		if (category1 == Category.CODE_94 && category2 == Category.CODE_94) {
			throw new Error("??? 94 + 94");
		} else if (category2 == Category.CODE_94 && category1 == Category.CODE_94) {
			throw new Error("??? 94 + 94");
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
			throw new Error("??? 95 + 95");
		} else if (category2 == Category.CODE_95 && category1 == Category.CODE_95) {
			throw new Error("??? 95 + 95");
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
		}
		if (category1 == Category.CODE_83 && category2 == Category.CODE_95) {
			return mergeStudent(line1, line2, Category.CODE_82);
		} else if (category2 == Category.CODE_83 && category1 == Category.CODE_95) {
			return mergeStudent(line2, line1, Category.CODE_82);
		} else {
			throw new Error("Unhandled case: " + category1.getCode() + " & " + category2.getCode());
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

	@Service
	public static String crossReferenceFile(final String description, final ExecutionYear executionYear, final String contents)
			throws Exception {
		final ImportIdentificationCardDataFromFile importIdentificationCardDataFromFile =
				new ImportIdentificationCardDataFromFile();
		return importIdentificationCardDataFromFile.run(description, executionYear, contents);
	}

}

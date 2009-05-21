package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class CardGenerationEntry extends CardGenerationEntry_Base {

    public static class CardGenerationEntryDeleter implements FactoryExecutor {

	private final CardGenerationEntry cardGenerationEntry;

	public CardGenerationEntryDeleter(final CardGenerationEntry cardGenerationEntry) {
	    this.cardGenerationEntry = cardGenerationEntry;
	}

	public Object execute() {
	    if (cardGenerationEntry != null) {
		cardGenerationEntry.delete();
	    }
	    return null;
	}
    }

    public CardGenerationEntry() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static CardGenerationEntry readByEntityCodeAndCategoryCodeAndMemberNumber(final String subline) {
	for (final CardGenerationEntry cardGenerationEntry : RootDomainObject.getInstance().getCardGenerationEntries()) {
	    if (cardGenerationEntry.getEntityCodeAndCategoryCodeAndMemberNumber().equals(subline)) {
		return cardGenerationEntry;
	    }
	}
	return null;
    }

    public static String createLine(final StudentCurricularPlan studentCurricularPlan) {
	final StringBuilder stringBuilder = new StringBuilder();

	final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
	final Degree degree = degreeCurricularPlan.getDegree();
	final DegreeType degreeType = degree.getDegreeType();
	final Registration registration = studentCurricularPlan.getRegistration();
	final Student student = registration.getStudent();
	final Person person = student.getPerson();

	stringBuilder.append(Campus.getUniversityCode(degreeCurricularPlan.getCurrentCampus()));
	stringBuilder.append(degree.getMinistryCode());
	stringBuilder.append("002");
	stringBuilder.append(translateDegreeType(degreeType));
	stringBuilder.append(fillLeftString(student.getNumber().toString(), '0', 8));
	stringBuilder.append("A");
	stringBuilder.append("1112");
	stringBuilder.append(" ");
	stringBuilder.append(" ");
	stringBuilder.append("00");
	stringBuilder.append("00");

	stringBuilder.append("00");

	stringBuilder.append("00");
	stringBuilder.append("00000000");
	stringBuilder.append(normalizeDegreeType17(degreeType));
	stringBuilder.append(" ");
	stringBuilder.append(fillLeftString(normalizeStudentNumber(student), '0', 5));
	stringBuilder.append(fillString(normalizeStudentNumber(student), ' ', 8));
	stringBuilder.append("        ");
	stringBuilder.append(normalizeDegreeType12(degreeType));
	stringBuilder.append("     "); // Academic year - no longer specified
	// because the cards last for more than
	// one year.
	stringBuilder.append("        ");
	stringBuilder.append("                       ");
	stringBuilder.append(fillString(normalizeDegreeName(degree), ' ', 42));
	stringBuilder.append("     ");
	stringBuilder.append(fillString(normalizePersonName(person), ' ', 84));
	stringBuilder.append("\r\n");

	return stringBuilder.toString();
    }

    public void setLine(final StudentCurricularPlan studentCurricularPlan) {
	final String line = createLine(studentCurricularPlan);

	final StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(line.substring(0, 30));
	stringBuilder.append(getNumberOfCards(studentCurricularPlan));
	stringBuilder.append(line.substring(32));

	setLine(stringBuilder.toString());
    }

    private String getNumberOfCards(final StudentCurricularPlan studentCurricularPlan) {
	final Person person = studentCurricularPlan.getPerson();
	final int numberOfCards;
	if (getPerson() == null) {
	    numberOfCards = person.getCardGenerationEntriesCount();
	} else {
	    numberOfCards = person.getCardGenerationEntriesCount() - 1;
	}
	return numberOfCards > 9 ? Integer.toString(numberOfCards) : "0" + numberOfCards;
    }

    @Override
    public void setLine(final String line) {
	final String name = line.substring(178, 262);
	for (int i = 0; i < name.length(); i++) {
	    char c = name.charAt(i);
	    if (c != '\r' && c != '\n' && c != ' ' && !Character.isLetter(c) && c != '-' && c != '\'') {
		registerProblem("person.has.unallowed.char.in.name");
	    }
	}

	final StringBuilder sb = new StringBuilder();
	sb.append(line.substring(0, 108));

	final String subCategory = line.substring(108, 131).replace('.', ' ').replace('-', ' ').replace('/', ' ');
	sb.append(subCategory);

	final String workPlace = line.substring(131, 178).replace('&', ' ').replace('_', ' ').replace('/', ' ').replace(':', ' ');
	sb.append(workPlace);

	sb.append(line.substring(178));

	if (line.length() != 264 || sb.length() != 264) {
	    registerProblem("line.has.incorrect.length");
	}

	super.setLine(sb.toString());
    }

    protected void registerProblem(final String message) {
	final CardGenerationBatch cardGenerationBatch = getCardGenerationBatch().getCardGenerationBatchProblems();
	setCardGenerationBatch(cardGenerationBatch);
	new CardGenerationProblem(cardGenerationBatch, message, "", getPerson());
    }

    public static int translateDegreeType(final DegreeType degreeType) {
	final String ministryCode = degreeType.getMinistryCode();
	if (StringUtils.isEmpty(ministryCode)) {
	    throw new Error("Unkown degree type: " + degreeType.getName());
	}
	return Integer.parseInt(ministryCode);
    }

    protected static String fillLeftString(final String uppered, final char c, final int fillTo) {
	final StringBuilder stringBuilder = new StringBuilder();
	for (int i = uppered.length(); i < fillTo; i++) {
	    stringBuilder.append(c);
	}
	stringBuilder.append(uppered);
	return stringBuilder.toString();
    }

    protected static String fillString(final String uppered, final char c, final int fillTo) {
	final StringBuilder stringBuilder = new StringBuilder(uppered);
	for (int i = uppered.length(); i < fillTo; i++) {
	    stringBuilder.append(c);
	}
	return stringBuilder.toString();
    }

    protected static String normalizeDegreeType17(final DegreeType degreeType) {
	if (degreeType == DegreeType.DEGREE) {
	    return "LICENCIATURA     ";
	}
	if (degreeType == DegreeType.MASTER_DEGREE) {
	    return "MESTRADO         ";
	}
	if (degreeType == DegreeType.BOLONHA_DEGREE) {
	    return "LICENCIATURA     ";
	}
	if (degreeType == DegreeType.BOLONHA_MASTER_DEGREE) {
	    return "MESTRADO         ";
	}
	if (degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
	    return "MESTR INTEGRADO  ";
	}
	if (degreeType == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) {
	    return "PROGRAMA DOUTORAL";
	}
	if (degreeType == DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) {
	    return "DFA              ";
	}
	if (degreeType == DegreeType.BOLONHA_SPECIALIZATION_DEGREE) {
	    return "ESPECIALIZACAO   ";
	}
	throw new Error("Unknown degree type: " + degreeType);
    }

    protected static String normalizeStudentNumber(final Student student) {
	final Integer number = student.getNumber();
	return number.toString();
    }

    protected static String normalizeDegreeType12(final DegreeType degreeType) {
	if (degreeType == DegreeType.DEGREE) {
	    return "LICENCIATURA";
	}
	if (degreeType == DegreeType.MASTER_DEGREE) {
	    return "MESTRADO    ";
	}
	if (degreeType == DegreeType.BOLONHA_DEGREE) {
	    return "LICENCIATURA";
	}
	if (degreeType == DegreeType.BOLONHA_MASTER_DEGREE) {
	    return "MESTRADO    ";
	}
	if (degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
	    return "MESTR INTEGR";
	}
	if (degreeType == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) {
	    return "PRG DOUTORAL";
	}
	if (degreeType == DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) {
	    return "DFA         ";
	}
	if (degreeType == DegreeType.BOLONHA_SPECIALIZATION_DEGREE) {
	    return "ESPECIALIZ  ";
	}
	throw new Error("Unknown degree type: " + degreeType);
    }

    protected static String normalizeDegreeName(final Degree degree) {
	final String degreeName = normalize(degree.getIdCardName());
	if (degreeName.length() > 42) {
	    throw new Error("Degree name exceeds max length: " + degreeName + " has length: " + degreeName.length());
	}
	return degreeName.replace(',', ' ');
    }

    public static String normalize(final String string) {
	final String normalized = StringNormalizer.normalize(string);
	return StringUtils.upperCase(normalized);
    }

    public static String normalizePersonName(final Person person) {
	final String normalizedName = normalize(person.getName());
	if (normalizedName.length() > 84) {
	    throw new Error("Name exceeds max length of 84: " + normalizedName);
	} else if (normalizedName.length() < 84) {
	    return fillString(normalizedName, ' ', 84);
	}
	return normalizedName;
    }

    public void delete() {
	removeCardGenerationBatch();
	removePerson();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public Category getCategory() {
	final String codeString = getLine().substring(11, 13);
	final int code = Integer.valueOf(codeString);
	return Category.valueOf(code);
    }

    public String getCampusCode() {
	return this.getLine().substring(0, 4);
    }

    public String getCourseCode() {
	return this.getLine().substring(4, 8);
    }

    public String getEntityCode() {
	return this.getLine().substring(8, 11);
    }

    public String getCategoryCode() {
	return this.getLine().substring(11, 13);
    }

    public String getMemberNumber() {
	return this.getLine().substring(13, 21);
    }

    public String getEntityCodeAndCategoryCodeAndMemberNumber() {
	return this.getLine().substring(8, 21);
    }

    public String getRegisterPurpose() {
	return this.getLine().substring(21, 22);
    }

    public String getExpirationDate() {
	return this.getLine().substring(22, 26);
    }

    public String getReservedField1() {
	return this.getLine().substring(26, 27);
    }

    public String getReservedField2() {
	return this.getLine().substring(27, 28);
    }

    public String getSubClassCode() {
	return this.getLine().substring(28, 30);
    }

    public String getCardViaNumber() {
	return this.getLine().substring(30, 32);
    }

    public String getCourseCode2() {
	return this.getLine().substring(32, 34);
    }

    public String getSecondaryCategoryCode() {
	return this.getLine().substring(34, 36);
    }

    public String getSecondaryMemberNumber() {
	return this.getLine().substring(36, 44);
    }

    public String getCourse() {
	return this.getLine().substring(44, 67);
    }

    public String getEditedStudentNumber() {
	return this.getLine().substring(67, 75);
    }

    public String getEditedSecondaryMemberNumber() {
	return this.getLine().substring(75, 83);
    }

    public String getLevelOfEducation() {
	return this.getLine().substring(83, 95);
    }

    public String getRegistrationYear() {
	return this.getLine().substring(95, 100);
    }

    public String getIssueDate() {
	return this.getLine().substring(100, 108);
    }

    public String getSecondaryCategory() {
	return this.getLine().substring(108, 131);
    }

    public String getWorkPlace() {
	return this.getLine().substring(131, 173);
    }

    public String getExtraInformation() {
	return this.getLine().substring(173, 178);
    }

    public String getStudentCompleteName() {
	return this.getLine().substring(178, 262);
    }
}

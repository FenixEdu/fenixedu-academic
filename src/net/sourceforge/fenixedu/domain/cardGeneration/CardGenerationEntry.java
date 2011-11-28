package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class CardGenerationEntry extends CardGenerationEntry_Base {

    public static class CardGenerationEntryDeleter implements FactoryExecutor {

	private final CardGenerationEntry cardGenerationEntry;

	public CardGenerationEntryDeleter(final CardGenerationEntry cardGenerationEntry) {
	    this.cardGenerationEntry = cardGenerationEntry;
	}

	@Override
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

    public static final Comparator<CardGenerationEntry> COMPARATOR_BY_CREATION_DATE = new Comparator<CardGenerationEntry>() {

	@Override
	public int compare(CardGenerationEntry o1, CardGenerationEntry o2) {
	    final int c = o1.getCreated().compareTo(o2.getCreated());
	    return c == 0 ? DomainObject.COMPARATOR_BY_ID.compare(o1, o2) : c;
	}

    };

    public int getNumberOfCGRsAfterThisCGEAndBeforeTheNextCGE() {
	int count = 0;
	DateTime creationDateStart = getCreated();
	DateTime creationDateEnd = null;
	if (getPerson() != null) {
	    List<CardGenerationEntry> cardGenerationEntries = getPerson().getCardGenerationEntries();
	    CardGenerationEntry cardGenerationEntry = getNextCGE(cardGenerationEntries);
	    creationDateEnd = (cardGenerationEntry == null) ? null : cardGenerationEntry.getCreated();
	    for (CardGenerationRegister cardGenerationRegister : getPerson().getCardGenerationRegister()) {
		LocalDate localDate = cardGenerationRegister.getEmission();
		if ((localDate != null) && (localDate.isAfter(creationDateStart.toLocalDate()))) {
		    if ((creationDateEnd == null) || (localDate.isBefore(creationDateEnd.toLocalDate()))) {
			++count;
		    }
		}
	    }
	}
	return count;
    }

    public CardGenerationEntry getNextCGE(List<CardGenerationEntry> cardGenerationEntries) {
	boolean found = false;
	List<CardGenerationEntry> sortedEntries = new ArrayList<CardGenerationEntry>();
	sortedEntries.addAll(cardGenerationEntries);
	Collections.sort(sortedEntries, COMPARATOR_BY_CREATION_DATE);
	CardGenerationEntry nextCardGenerationEntry = null;
	for (CardGenerationEntry cardGenerationEntry : sortedEntries) {
	    if (found) {
		nextCardGenerationEntry = cardGenerationEntry;
		break;
	    } else {
		if (CardGenerationEntry.COMPARATOR_BY_CREATION_DATE.compare(cardGenerationEntry, this) == 0) {
		    found = true;
		}
	    }
	}
	return nextCardGenerationEntry;
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
	stringBuilder.append(getExpirationDateForNewEntry());
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

    public static String createLine(final PhdIndividualProgramProcess phdIndividualProgramProcess) {
	final StringBuilder stringBuilder = new StringBuilder();

	final DegreeCurricularPlan degreeCurricularPlan = phdIndividualProgramProcess.getPhdProgram().getDegree().getLastActiveDegreeCurricularPlan();
	final Degree degree = phdIndividualProgramProcess.getPhdProgram().getDegree();
	final DegreeType degreeType = degree.getDegreeType();
	final Student student = phdIndividualProgramProcess.getStudent();
	final Person person = student.getPerson();

	stringBuilder.append(Campus.getUniversityCode(degreeCurricularPlan.getCurrentCampus()));
	stringBuilder.append(degree.getMinistryCode());
	stringBuilder.append("002");
	stringBuilder.append(translateDegreeType(degreeType));
	stringBuilder.append(fillLeftString(student.getNumber().toString(), '0', 8));
	stringBuilder.append("A");
	stringBuilder.append(getExpirationDateForNewEntry());
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
		registerProblem("person.has.unallowed.char.in.name", line);
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
	    registerProblem("line.has.incorrect.length", line);
	}

	super.setLine(sb.toString());
    }

    protected void registerProblem(final String message, final String args) {
	final CardGenerationBatch cardGenerationBatch = getCardGenerationBatch().getCardGenerationBatchProblems();
	setCardGenerationBatch(cardGenerationBatch);
	new CardGenerationProblem(cardGenerationBatch, message, args, getPerson());
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
	if (degreeType == DegreeType.EMPTY) {
	    return "U C  ISOLADAS    ";
	}
	throw new Error("Unknown degree type: " + degreeType);
    }

    protected static String normalizeStudentNumber(final Student student) {
	final Integer number = student.getNumber();
	return number.toString();
    }

    public static String normalizeDegreeType12(final DegreeType degreeType) {
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
	if (degreeType == DegreeType.EMPTY) {
	    return "U C ISOLADAS";
	}
	throw new Error("Unknown degree type: " + degreeType);
    }

    public static String normalizeDegreeName(final Degree degree) {
	final String degreeName = normalizeAndFlatten(degree.getIdCardName());
	if (degreeName.length() > 42) {
	    throw new Error("Degree name exceeds max length: " + degreeName + " has length: " + degreeName.length());
	}
	return degreeName;
    }

    protected static String normalizeAndFlatten(final String name) {
	final String flatName = normalize(name);
	return flatName.replace(',', ' ');
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
	return readCategory(getLine());
    }

    public static Category readCategory(final String line) {
	final String codeString = line.substring(11, 13);
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

    public boolean matches(final String line) {
	return linesMatch(getLine(), line);
    }

    public static boolean linesMatch(final String line1, final String line2) {
	final String cl1 = makeComparableLine(line1);
	final String cl2 = makeComparableLine(line2);
	return cl1.equals(cl2);
    }

    public static String makeComparableLine(final String line) {
	final StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(line.substring(0, 21));
	stringBuilder.append(line.substring(26, 30));
	stringBuilder.append(line.substring(32, 262));
	return stringBuilder.toString();
    }

    public String getNormalizedLine() {
	return getLine().replaceAll("\\.", " ");
    }

    public void incrementVersionNumber() {
	final String line = this.getLine();
	final StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(line.substring(0, 30));
	stringBuilder.append("01");
	stringBuilder.append(line.substring(32));
	this.setLine(stringBuilder.toString());
    }

    public String getComparableLine() {
	return makeComparableLine(this.getNormalizedLine());
    }

    public String getCgdIdentifier() {
	return getEntityCodeAndCategoryCodeAndMemberNumber();
    }

    public static CardGenerationEntry readCardByCGDIdentifier(String identifier) {
	for (CardGenerationEntry cardEntry : RootDomainObject.getInstance().getCardGenerationEntriesSet()) {
	    if (cardEntry.getCgdIdentifier().equals(identifier)) {
		return cardEntry;
	    }
	}
	return null;
    }

    private static String getExpirationDateForNewEntry() {
	return new DateTime().plusYears(3).toString("YYMM");
    }

    private static String guessWorkingPlaceName(final Unit unit) {
	String name = guessWorkingPlaceName(unit.getName());;
	if (name.length() <= 42) {
	    return normalizeAndFlatten(name);
	}
	final String label = unit.getIdentificationCardLabel();
	if (label == null || label.isEmpty()) {
	    System.out.println("### " + name);
	    throw new DomainException("message.unit.name.too.long: " + name, name);
	}
	return label;

    }

    private static String guessWorkingPlaceName(final String unitName) {
	return unitName
		.replace("Presidência do Departamento ", "Departamento ")
		.replace("Secção de Urbanismo, Transportes, Vias e Sistemas",
			 "Sec Urbanismo Transportes Vias e Sistemas")
		.replace("Biomateriais, Nanotecnologia e Medicina Regenerativa",
			 "Biomateria Nanotecnolo Medici Regenerativa")
		.replace("Secção de Hidráulica e Recursos Hídricos e Ambientais",
			 "Sec Hidraulica Recursos Hidricos Ambientai")
		.replace("Área Científica de Projecto Mecânico e Materiais Estruturais",
			 "Área Cient Proj Mecânico Materiais Estrutu")
		.replace("Área Científica de Sistemas, Decisão e Controlo",
			 "Área Cient Sistemas Decisão e Controlo")
		.replace("Área Científica de Termofluídos e Tecnologias de Conversão de Energia",
			 "Área Cient Termo Tecnol Conv Energ")
		.replace("Área Científica de Mecânica Aplicada e Aeroespacial",
			 "Área Cien Mecânica Aplicada e Aeroespacial")
		.replace("Área Científica de Mecênica Estrutural e Computacional",
			 "Área Cient Mecênica Estrutu Comput")
		.replace("Área Científica de Controlo, Automação e Informática Industrial",
			 "Área Cient Control Autom e Inf Ind")
		.replace("Secção de Matemática Aplicada e Análise Numérica",
			 "Sec Matem Aplicad Análise Numérica")
		.replace("Área Científica de Mecânica Estrutural e Computacional",
			 "Área Cient Mecânic Estrut e Comput")
		.replace("Área Científica de Tecnologia Mecânica e Gestão Industrial",
			 "Área Cient Tecnolo Mecân Gest Indu")
		.replace("Área Científica de Química-Física, Materiais e Nanociências",
			 "Área Cient Quím-Fís Materi Nanociê")
		.replace("Área Científica de Síntese, Estrutura Molecular e Análise Química",
			 "Área Cien Sínt Estr Molec Anál Quí")
		.replace("Área Cientifica de Ciências de Engenharia Química",
			 "Área Cient Ciências Engenh Química")
		.replace("Área Científica de Engenharia de Processos e Projecto",
			 "Área Cient Eng Processos Projecto")
		.replace("Centro de Estudos em Inovação, Tecnologia e Políticas de Desenvolvimento",
			 "Centro Estu Inov Tecn e Políticas Desenv")
		.replace("Gabinete de Comunicação e Relações Públicas",
			 "Gabinete Comunicação e Relações Públicas")
		.replace("Departamento de Engenharia Química e Biológica",
			 "Dep Engenharia Química e Biológica")
		.replace("Departamento de Bioengenharia - Presidência",
			 "Departamento de Bioengenharia")
		.replace("Núcleo de Serviços Médicos e de Apoio e Avaliação Psicológica",
			 "Núcleo Serv Méd e Apoio e Aval Psicológ")
		.replace("Contabilidade Integrada do Dep. Eng. Civil. e Arquit.",
			 "Contab Int do Dep. Eng. Civil e Arquit.")
		.replace("Núcleo de Análises Gerais Aplicadas em Águas Residuais",
			 "Núcleo de Anál Aplic em Águas Residuais")
		.replace("Núcleo de Pós-Graduação e Formação Contínua",
			 "Núcleo Pós-Graduação e Formação Contínua")
		.replace("Centro de Física das Interacções Fundamentais",
			 "Centro de Física das Interacções Fund")
		.replace("Instituto de Engenharia de Estruturas, Território e Construção",
			 "Inst Eng Estruturas Territór Construç")
		.replace("Instituto de Ciência e Engenharia de Materiais e Superfícies",
			 "Inst Ciência e Engenharia Mater Superfí")
		.replace("Centro Eng.Biológica e Química / Instituto de Biotecnologia e Bioengenharia",
			 "Centro Eng.Biol Quí / Inst Biote Bioeng")
		.replace("Centro de Análise Matemática, Geometria e Sistemas Dinâmicos",
			 "Centro Anál Matem Geom e Sist Dinâmicos")
		.replace("Núcleo de Metais e Preparação de Amostras Sólidas",
			 "Núcleo Metais Preparação Amost Sólidas")
		.replace("Núcleo de Projectos de Consultadoria e Serviços",
			 "Núcleo de Projectos Consult e Serviços")
		.replace("Coordenação dos Serviços Administrativos e Financeiros do Complexo Interdisciplinar",
			 "Coord Serv Admin e Fin do Complexo Int")
		.replace("Departamento de Engenharia Electrotécnica e de Computadores - Tagus Park",
			 "Dep Eng Electrotécnica e Comput - TP")
		.replace("Núcleo de Microbiologia-Clássica e Novas Metodologias",
			 "Núcleo Microb-Clássica e Novas Metodol")
		.replace("Centro de Análise e Processamento de Sinais",
			 "Centro Análise e Processamento Sinais")
		.replace("Núcleo de Gestão e Acompanhamento de Contratos",
			 "Núcleo Gestão e Acompanh de Contratos")
		.replace("Núcleo de Análises Gerais Aplicadas em Águas Limpas",
			 "Núcleo Anál Aplicadas em Águas Limpas")
		.replace("Área de Aplicações e Sistemas de Informação",
			 "Área Aplica e Sistemas de Informação")
		.replace("Núcleo de Mobilidade e Cooperação Internacional",
			 "Núcleo Mobil e Cooperação Internacio")
		.replace("Centro para a Inovação em Engenharia Electrotécnica e Energia",
			 "Centro Inov em Eng Electrotéc e Energ")
		.replace("Núcleo de Remunerações, Protecção e Benefícios Sociais",
			 "Núcleo Remun, Protec e Benefíc Sociai")
		.replace("Núcleo de Gestão do Museu e Contro de Congressos",
			 "Núcleo Gestão Museu e Contro Congres")
		.replace("Núcleo de Gestão de Colheitas, Ambiente, Saúde e Segurança",
			 "Núcleo Gest Colhei, Ambi, Saúde e Seg")
		.replace("Departamento de Engenharia Electrotécnica e de Computadores",
			 "Dep Eng Electrotécnica e de Computado")
		.replace("Gestão de Espaços do Complexo Interdisciplinar",
			 "Gest Espaços do Complexo Interdisc")
		.replace("Coordenação da Licenciatura em Engenharia do Ambiente",
			 "Coord Lic em Engenharia do Ambiente")
		.replace("Gestão de Espaços Pavilhão Matemática e Física",
			 "Gest Espaços Pav Matemática e Física")
		;
    }

    public static String createLine(final Teacher teacher) {
	final StringBuilder stringBuilder = new StringBuilder();

	final Person person = teacher.getPerson();

	final Unit workingUnit = teacher.getCurrentWorkingUnit();
	final Campus campus = workingUnit.getCampus();

	final PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
	final GiafProfessionalData giafProfessionalData = personProfessionalData.getGiafProfessionalDataByCategoryType(CategoryType.TEACHER);
	final ProfessionalCategory professionalCategory = giafProfessionalData.getProfessionalCategory();

	final Employee employee = person.getEmployee();
	final Unit currentWorkingPlace = employee.getCurrentWorkingPlace();
	final String workingPlaceName = guessWorkingPlaceName(currentWorkingPlace);

	stringBuilder.append(Campus.getUniversityCode(campus));
	stringBuilder.append("9999");
	stringBuilder.append("002");
	stringBuilder.append("81");
	final Integer employeeNumber = person.getEmployee().getEmployeeNumber();
	//final Integer istNumber = new Integer(person.getUsername().substring(3));
	stringBuilder.append(fillLeftString(employeeNumber.toString(), '0', 8));
	stringBuilder.append("A");
	stringBuilder.append(getExpirationDateForNewEntry());
	stringBuilder.append(" ");
	stringBuilder.append(" ");
	stringBuilder.append("00");
	stringBuilder.append("00");

	stringBuilder.append("00");

	stringBuilder.append("00");
	stringBuilder.append("00000000");
	stringBuilder.append(fillString(professionalCategory.getIdentificationCardLabel(), ' ', 17));
	stringBuilder.append(" ");
	stringBuilder.append(fillLeftString(employeeNumber.toString(), '0', 5));
	stringBuilder.append(fillString(employeeNumber.toString(), ' ', 8));
	stringBuilder.append("        ");
	stringBuilder.append("            ");
	stringBuilder.append("     "); // Academic year - no longer specified because the cards last for more than one year.
	stringBuilder.append("        ");
	stringBuilder.append("                       ");
	stringBuilder.append(fillString(workingPlaceName, ' ', 42));
	stringBuilder.append("     ");
	stringBuilder.append(fillString(normalizePersonName(person), ' ', 84));
	stringBuilder.append("\r\n");

	return stringBuilder.toString();
    }

    public static String createLine(final Employee employee) {
	final StringBuilder stringBuilder = new StringBuilder();

	final Person person = employee.getPerson();

	final Assiduousness assiduousness = employee.getAssiduousness();
	final Campus campus = assiduousness.getCurrentCampus();

	final PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
	final GiafProfessionalData giafProfessionalData = personProfessionalData.getGiafProfessionalDataByCategoryType(CategoryType.EMPLOYEE);
	if (giafProfessionalData == null) {
	    return null;
	}
	final ProfessionalCategory professionalCategory = giafProfessionalData.getProfessionalCategory();
	if (professionalCategory == null) {
	    return null;
	}

	final Unit currentWorkingPlace = employee.getCurrentWorkingPlace();
	if (currentWorkingPlace == null) {
	    return null;
	}
	final String workingPlaceName = guessWorkingPlaceName(currentWorkingPlace);
	if (workingPlaceName == null) {
	    return null;
	}

	stringBuilder.append(Campus.getUniversityCode(campus));
	stringBuilder.append("9999");
	stringBuilder.append("002");
	stringBuilder.append("71");
	final Integer employeeNumber = person.getEmployee().getEmployeeNumber();
	//final Integer istNumber = new Integer(person.getUsername().substring(3));
	stringBuilder.append(fillLeftString(employeeNumber.toString(), '0', 8));
	stringBuilder.append("A");
	stringBuilder.append(getExpirationDateForNewEntry());
	stringBuilder.append(" ");
	stringBuilder.append(" ");
	stringBuilder.append("00");
	stringBuilder.append("00");

	stringBuilder.append("00");

	stringBuilder.append("00");
	stringBuilder.append("00000000");
	stringBuilder.append(fillString(professionalCategory.getIdentificationCardLabel(), ' ', 17));
	stringBuilder.append(" ");
	stringBuilder.append(fillLeftString(employeeNumber.toString(), '0', 5));
	stringBuilder.append(fillString(employeeNumber.toString(), ' ', 8));
	stringBuilder.append("        ");
	stringBuilder.append("            ");
	stringBuilder.append("     "); // Academic year - no longer specified because the cards last for more than one year.
	stringBuilder.append("        ");
	stringBuilder.append("                       ");
	stringBuilder.append(fillString(workingPlaceName, ' ', 42));
	stringBuilder.append("     ");
	stringBuilder.append(fillString(normalizePersonName(person), ' ', 84));
	stringBuilder.append("\r\n");

	return stringBuilder.toString();
    }

    public static String createResearcherLine(final Employee employee) {
	final StringBuilder stringBuilder = new StringBuilder();

	final Person person = employee.getPerson();

	//final Assiduousness assiduousness = employee.getAssiduousness();
	final Campus campus = Campus.readCampusByName("Alameda");

	final PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
	if (personProfessionalData == null) {
	    return null;
	}
	final GiafProfessionalData giafProfessionalData = personProfessionalData.getGiafProfessionalDataByCategoryType(CategoryType.RESEARCHER);
	if (giafProfessionalData == null) {
	    return null;
	}
	final ProfessionalCategory professionalCategory = giafProfessionalData.getProfessionalCategory();
	if (professionalCategory == null) {
	    return null;
	}

	final Unit currentWorkingPlace = employee.getCurrentWorkingPlace();
	final String workingPlaceName = guessWorkingPlaceName(currentWorkingPlace);

	stringBuilder.append(Campus.getUniversityCode(campus));
	stringBuilder.append("9999");
	stringBuilder.append("002");
	stringBuilder.append("83");
	final Integer employeeNumber = person.getEmployee().getEmployeeNumber();
	//final Integer istNumber = new Integer(person.getUsername().substring(3));
	stringBuilder.append(fillLeftString(employeeNumber.toString(), '0', 8));
	stringBuilder.append("A");
	stringBuilder.append(getExpirationDateForNewEntry());
	stringBuilder.append(" ");
	stringBuilder.append(" ");
	stringBuilder.append("00");
	stringBuilder.append("00");

	stringBuilder.append("00");

	stringBuilder.append("00");
	stringBuilder.append("00000000");
	stringBuilder.append(fillString(professionalCategory.getIdentificationCardLabel(), ' ', 17));
	stringBuilder.append(" ");
	stringBuilder.append(fillLeftString(employeeNumber.toString(), '0', 5));
	stringBuilder.append(fillString(employeeNumber.toString(), ' ', 8));
	stringBuilder.append("        ");
	stringBuilder.append("            ");
	stringBuilder.append("     "); // Academic year - no longer specified because the cards last for more than one year.
	stringBuilder.append("        ");
	stringBuilder.append("                       ");
	stringBuilder.append(fillString(workingPlaceName, ' ', 42));
	stringBuilder.append("     ");
	stringBuilder.append(fillString(normalizePersonName(person), ' ', 84));
	stringBuilder.append("\r\n");

	return stringBuilder.toString();
    }

    public static String createLine(final GrantOwner grantOwner) {
	final StringBuilder stringBuilder = new StringBuilder();

	final Person person = grantOwner.getPerson();

	final GrantContract grantContract = grantOwner.getCurrentContract();
	final GrantCostCenter grantCostCenter = grantContract.getGrantCostCenter();
	final Teacher responsibleTeacher = grantCostCenter.getResponsibleTeacher();
	final String costCenterNumber = grantCostCenter.getNumber();
	final Unit unit = Unit.readByCostCenterCode(Integer.valueOf(costCenterNumber));
	final String costCenterDesignation = unit == null ? grantCostCenter.getDesignation() : unit.getName();
	final String workingPlaceName = normalizeAndFlatten(guessWorkingPlaceName(costCenterDesignation));

	final Department department = responsibleTeacher.getCurrentWorkingDepartment();
	final DepartmentUnit departmentUnit = department.getDepartmentUnit();
	final Campus campus = departmentUnit.getCampus();

	stringBuilder.append(Campus.getUniversityCode(campus));
	stringBuilder.append("9999");
	stringBuilder.append("002");
	stringBuilder.append("96");
	final Integer employeeNumber = grantOwner.getNumber();
	//final Integer istNumber = new Integer(person.getUsername().substring(3));
	stringBuilder.append(fillLeftString(employeeNumber.toString(), '0', 8));
	stringBuilder.append("A");
	stringBuilder.append(getExpirationDateForNewEntry());
	stringBuilder.append(" ");
	stringBuilder.append(" ");
	stringBuilder.append("00");
	stringBuilder.append("00");

	stringBuilder.append("00");

	stringBuilder.append("00");
	stringBuilder.append("00000000");
	stringBuilder.append(fillString("BOLSEIRO", ' ', 17));
	stringBuilder.append(" ");
	stringBuilder.append(fillLeftString(employeeNumber.toString(), '0', 5));
	stringBuilder.append(fillString(employeeNumber.toString(), ' ', 8));
	stringBuilder.append("        ");
	stringBuilder.append("            ");
	stringBuilder.append("     "); // Academic year - no longer specified because the cards last for more than one year.
	stringBuilder.append("        ");
	stringBuilder.append("                       ");
	stringBuilder.append(fillString(workingPlaceName, ' ', 42));
	stringBuilder.append("     ");
	stringBuilder.append(fillString(normalizePersonName(person), ' ', 84));
	stringBuilder.append("\r\n");

	return stringBuilder.toString();
    }

}

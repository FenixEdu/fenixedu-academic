package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.lang.StringUtils;

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

    public void setLine(final StudentCurricularPlan studentCurricularPlan) {
	final StringBuilder stringBuilder = new StringBuilder();

	final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
	final Degree degree = degreeCurricularPlan.getDegree();
	final DegreeType degreeType = degree.getDegreeType();
	final Registration registration = studentCurricularPlan.getRegistration();
	final Student student = registration.getStudent();
	final Person person = student.getPerson();

	final String campus = getCampus(degreeCurricularPlan);
	if (campus == null) {
	    stringBuilder.append("9999");
	} else if ("Alameda".equals(campus)) {
	    stringBuilder.append("0807");
	} else {
	    stringBuilder.append("0808");
	}

	stringBuilder.append(degree.getMinistryCode() == null ? "9999" : degree.getMinistryCode());

	stringBuilder.append("002");
	stringBuilder.append(translateDegreeType(degreeType));
	stringBuilder.append(fillLeftString(student.getNumber().toString(), '0', 8));
	stringBuilder.append("A");
	stringBuilder.append("1012");
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
	stringBuilder.append("     "); // Academic year - no longer specified because the cards last for more than one year.
	stringBuilder.append("        ");
	stringBuilder.append("                       ");
	stringBuilder.append(fillString(normalizeDegreeName(degree), ' ', 42));
	stringBuilder.append("     ");
	stringBuilder.append(fillString(normalizePersonName(person), ' ', 84));
	stringBuilder.append("\r\n");

	setLine(stringBuilder.toString());
    }

    @Override
    public void setLine(final String line) {
	final String name = line.substring(178);
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

    protected String getCampus(final DegreeCurricularPlan degreeCurricularPlan) {
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            if (executionYear.getState().equals(PeriodState.CURRENT)) {
                final Campus campus = executionDegree.getCampus();
                return campus.getName();
            }
        }
        return null;
    }

    public static Category getCategoryFromDegreeType(final DegreeType degreeType) {
	for (final Category category : Category.values()) {
	    for (final DegreeType otherDegreeType : category.getDegreeTypes()) {
		if (degreeType == otherDegreeType) {
		    return category;
		}
	    }
	}
	return null;
    }

    public static int translateDegreeType(final DegreeType degreeType) {
	final Category category = getCategoryFromDegreeType(degreeType);
	if (category == null) {
	    throw new Error("Unkown degree type: " + degreeType.getName());
	}
	return category.getCode();
    }

    protected String fillLeftString(final String uppered, final char c, final int fillTo) {
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

    protected String normalizeDegreeType17(final DegreeType degreeType) {
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
	if (degreeType == DegreeType.BOLONHA_PHD_PROGRAM) {
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

    protected String normalizeStudentNumber(final Student student) {
        final Integer number = student.getNumber();
        return number.toString();
    }

    protected String normalizeDegreeType12(final DegreeType degreeType) {
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
	if (degreeType == DegreeType.BOLONHA_PHD_PROGRAM) {
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

    protected String normalizeDegreeName(final Degree degree) {
        final String degreeName = normalize(degree.getIdCardName());
        if (degreeName.length() > 42) {
            throw new Error("Degree name exceeds max length: " + degreeName + " has length: " + degreeName.length());
        }
        return degreeName;
    }

    public static String normalize(final String string) {
        final String normalized = StringNormalizer.normalize(string);
        return StringUtils.upperCase(normalized);
    }

    public static String normalizePersonName(final Person person) {
	final String  normalizedName = normalize(person.getName());
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
}

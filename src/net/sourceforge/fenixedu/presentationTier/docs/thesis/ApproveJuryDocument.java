package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

public class ApproveJuryDocument extends FenixReport {

    private final Thesis thesis;

    public ApproveJuryDocument(Thesis thesis) {
	super();
	this.thesis = thesis;
    }

    @Override
    protected void fillReport() {
	parameters.put("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());

	final Degree degree = thesis.getDegree();
	parameters.put("studentDegreeName", degree.getPresentationName());

	final Student student = thesis.getStudent();
	parameters.put("studentNumber", student.getNumber());

	final Person person = student.getPerson();
	parameters.put("studentName", person.getName());

	final ThesisEvaluationParticipant orientator = thesis.getOrientator();
	parameters.put("orientatorName", orientator.getPerson().getName());
	parameters.put("orientatorCategory", orientator.getCategory());
	parameters.put("orientatorAffiliation", orientator.getAffiliation());

	final ThesisEvaluationParticipant coorientator = thesis.getCoorientator();
	parameters.put("coorientatorName", coorientator.getPerson().getName());
	parameters.put("coorientatorCategory", coorientator.getCategory());
	parameters.put("coorientatorAffiliation", coorientator.getAffiliation());

	parameters.put("thesisTitle", thesis.getTitle().getContent());

	final ThesisEvaluationParticipant juryPresident = thesis.getPresident();
	parameters.put("juryPresidentName", juryPresident.getPerson().getName());
	parameters.put("juryPresidentCategory", juryPresident.getCategory());
	parameters.put("juryPresidentAffiliation", juryPresident.getAffiliation());

	final Set<ThesisEvaluationParticipant> vowels = new TreeSet<ThesisEvaluationParticipant>(
		ThesisEvaluationParticipant.COMPARATOR_BY_PERSON_NAME);
	vowels.addAll(thesis.getVowelsSet());
	int i = 0;
	for (final ThesisEvaluationParticipant vowel : vowels) {
	    final String vowelPrefix = "vowel" + ++i;
	    parameters.put(vowelPrefix + "Name", vowel.getPerson().getName());
	    parameters.put(vowelPrefix + "Category", vowel.getCategory());
	    parameters.put(vowelPrefix + "Affiliation", vowel.getAffiliation());
	}
    }

    @Override
    public String getReportFileName() {
	return "approveJuryFor" + thesis.getStudent().getNumber();
    }

}

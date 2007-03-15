package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
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

	final Person orientator = thesis.getOrientator();
	parameters.put("orientatorName", orientator.getName());
	parameters.put("orientatorCategory", "TODO");
	parameters.put("orientatorAffiliation", "TODO");

	final Person coorientator = thesis.getCoorientator();
	parameters.put("coorientatorName", coorientator.getName());
	parameters.put("coorientatorCategory", "TODO");
	parameters.put("coorientatorAffiliation", "TODO");

	parameters.put("thesisTitle", thesis.getTitle().getContent());

	final Person juryPresident = thesis.getPresident();
	parameters.put("juryPresidentName", juryPresident.getName());
	parameters.put("juryPresidentCategory", "TODO");
	parameters.put("juryPresidentAffiliation", "TODO");

	final Set<Person> vowels = new TreeSet<Person>(Person.COMPARATOR_BY_NAME);
	int i = 0;
	for (final Person vowel : vowels) {
	    final String vowelPrefix = "vowel" + ++i;
	    parameters.put(vowelPrefix + "Name", vowel.getName());
	    parameters.put(vowelPrefix + "Category", "TODO");
	    parameters.put(vowelPrefix + "Affiliation", "TODO");
	}
    }

    @Override
    public String getReportFileName() {
	return "approveJuryFor" + thesis.getStudent().getNumber();
    }

}

package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.CycleConclusionProcess;

import org.joda.time.LocalDate;

public class RegistryDiploma extends AdministrativeOfficeDocument {
    private static final long serialVersionUID = 7788392282506503345L;

    protected RegistryDiploma(DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	RegistryDiplomaRequest request = (RegistryDiplomaRequest) getDocumentRequest();
	Degree degree = request.getDegree();
	DegreeType degreeType = request.getDegreeType();
	CycleType cycle = request.getRequestedCycle();
	Person person = request.getPerson();
	Registration registration = request.getRegistration();
	CycleConclusionProcess conclusion = registration.getLastStudentCurricularPlan().getCycle(cycle).getConclusionProcess();
	addParameter("code", request.getRegistryCode().getCode());

	final UniversityUnit university = UniversityUnit.getInstitutionsUniversityUnit();
	addParameter("institution", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("university", university.getName());
	addParameter("principal", university.getInstitutionsUniversityPrincipal().getValidatedName());

	addParameter("studentName", person.getValidatedName());
	addParameter("idNumber", person.getDocumentIdNumber());
	addParameter("parishOfBirth", person.getParishOfBirth());

	String degreeName = "";
	if (degreeType.isComposite()) {
	    degreeName = getEnumerationBundle().getString(cycle.getQualifiedName()) + " do ";
	}
	addParameter("degreeName", degreeName + "curso de " + degreeType.getFilteredName() + SINGLE_SPACE
		+ getResourceBundle().getString("label.in") + SINGLE_SPACE
		+ degree.getFilteredName(conclusion.getConclusionYear()));
	addParameter("conclusionDay", verboseDate(conclusion.getConclusionDate()));
	addParameter("graduateTitle", getEnumerationBundle().getString(
		degreeType.getQualifiedName() + (degreeType.isComposite() ? "." + cycle.name() : "") + ".graduate.title")
		+ SINGLE_SPACE
		+ getResourceBundle().getString("label.in")
		+ SINGLE_SPACE
		+ degree.getFilteredName(conclusion.getConclusionYear()));
	Integer finalAverage = registration.getFinalAverage(cycle);
	addParameter("finalAverage", getEnumerationBundle().getString(finalAverage.toString()));
	String qualifiedAverageGrade;
	if (finalAverage <= 13) {
	    qualifiedAverageGrade = "sufficient";
	} else if (finalAverage <= 15) {
	    qualifiedAverageGrade = "good";
	} else if (finalAverage <= 17) {
	    qualifiedAverageGrade = "verygood";
	} else {
	    qualifiedAverageGrade = "excelent";
	}
	addParameter("finalAverageQualified", getResourceBundle().getString(
		"diploma.supplement.qualifiedgrade." + qualifiedAverageGrade));

	// DateTimeFormatterBuilder formatter = new
	// DateTimeFormatterBuilder().appendDayOfMonth(2).appendLiteral(" de ")
	// .appendMonthOfYearText().appendLiteral(" de ").appendYear(4, 4);
	addParameter("date", new LocalDate().toString("dd 'de' MMMM 'de' yyyy", new Locale("pt")));
    }

    private String verboseDate(LocalDate date) {
	return "dia " + getEnumerationBundle().getString(String.valueOf(date.getDayOfMonth())) + " do mês de "
		+ date.toString("MMMM", new Locale("pt")) + " de "
		+ getEnumerationBundle().getString(String.valueOf(date.getYear()));
    }
}

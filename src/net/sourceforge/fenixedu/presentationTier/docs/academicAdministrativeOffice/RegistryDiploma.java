package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StringUtils;

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
	CycleType cycle = request.getRequestedCycle();
	Person person = request.getPerson();
	Registration registration = request.getRegistration();
	addParameter("code", request.getRegistryCode().getCode());

	final UniversityUnit university = UniversityUnit.getInstitutionsUniversityUnit();
	addParameter("institution", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("university", university.getName());
	addParameter("principal", university.getInstitutionsUniversityPrincipal().getValidatedName());

	addParameter("studentName", person.getValidatedName());
	addParameter("idHolder", person.getGender() == Gender.MALE ? "portador" : "portadora");
	addParameter("idDocType", getEnumerationBundle().getString(person.getIdDocumentType().getName()));
	addParameter("idNumber", person.getDocumentIdNumber());
	addParameter("parishOfBirth", person.getParishOfBirth());

	addParameter("conclusionDay", verboseDate(getConclusionDate(registration, cycle)));
	addParameter("graduateTitle", registration.getGraduateTitle(cycle, getLocale()));
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
	addParameter("finalAverageQualified",
		getResourceBundle().getString("diploma.supplement.qualifiedgrade." + qualifiedAverageGrade));
	addParameter("date", new LocalDate().toString("dd 'de' MMMM 'de' yyyy", new Locale("pt")));
    }

    @Override
    protected String getDegreeDescription() {
	final StringBuilder res = new StringBuilder();

	RegistryDiplomaRequest request = (RegistryDiplomaRequest) getDocumentRequest();
	CycleType cycle = request.getRequestedCycle();
	Degree degree = request.getDegree();
	final DegreeType degreeType = request.getDegreeType();
	if (degreeType.hasAnyCycleTypes()) {
	    res.append(cycle.getDescription(getLocale()));
	    res.append(StringUtils.SINGLE_SPACE).append(getResourceBundle().getString("label.of.the.male"))
		    .append(StringUtils.SINGLE_SPACE);
	}

	if (!degree.isEmpty()) {
	    res.append(degreeType.getPrefix(getLocale()));
	    res.append(degreeType.getFilteredName(getLocale()));
	    res.append(StringUtils.SINGLE_SPACE).append(getResourceBundle().getString("label.in"))
		    .append(StringUtils.SINGLE_SPACE);
	}

	res.append(degree.getFilteredName(getConclusionYear(request.getRegistration(), cycle), getLocale()));
	return res.toString();
    }

    private LocalDate getConclusionDate(Registration registration, CycleType cycle) {
	if (registration.isBolonha()) {
	    return registration.getLastStudentCurricularPlan().getCycle(cycle).getConclusionProcess().getConclusionDate();
	} else {
	    return registration.getConclusionProcess().getConclusionDate();
	}
    }

    private ExecutionYear getConclusionYear(Registration registration, CycleType cycle) {
	if (registration.isBolonha()) {
	    return registration.getLastStudentCurricularPlan().getCycle(cycle).getConclusionProcess().getConclusionYear();
	} else {
	    return registration.getConclusionProcess().getConclusionYear();
	}
    }

    private String verboseDate(LocalDate date) {
	return "dia " + getEnumerationBundle().getString(String.valueOf(date.getDayOfMonth())) + " do mês de "
		+ date.toString("MMMM", new Locale("pt")) + " de "
		+ getEnumerationBundle().getString(String.valueOf(date.getYear()));
    }
}

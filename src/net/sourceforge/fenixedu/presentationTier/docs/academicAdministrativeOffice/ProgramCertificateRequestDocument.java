package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ProgramCertificateRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.HtmlToTextConverterUtil;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import pt.ist.utl.fenix.utils.NumberToWordsConverter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ProgramCertificateRequestDocument extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 12L;

    protected ProgramCertificateRequestDocument(final ProgramCertificateRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected ProgramCertificateRequest getDocumentRequest() {
	return (ProgramCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
	setPersonFields();
	addParametersInformation();
    }

    private void addParametersInformation() {
	addParameter("studentNumber", getStudentNumber());
	addParameter("numberOfPrograms", calculateNumberOfPrograms());
	addParameter("degreeDescription", getDegreeDescription());

	final Employee employee = AccessControl.getPerson().getEmployee();

	addParameter("administrativeOfficeCoordinatorName", employee.getCurrentWorkingPlace().getActiveUnitCoordinator()
		.getName());
	addParameter("administrativeOfficeName", employee.getCurrentWorkingPlace().getName());
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());
	addParameter("day", new LocalDate().toString("dd 'de' MMMM 'de' yyyy", Language.getLocale()));

	createProgramsList();
    }

    private String getStudentNumber() {
	final Registration registration = getDocumentRequest().getRegistration();
	if (ProgramCertificateRequest.FREE_PAYMENT_AGREEMENTS.contains(registration.getRegistrationAgreement())) {
	    final String agreementInformation = registration.getAgreementInformation();
	    if (!StringUtils.isEmpty(agreementInformation)) {
		return registration.getRegistrationAgreement().toString() + " " + agreementInformation;
	    }
	}
	return registration.getStudent().getNumber().toString();
    }

    public boolean isBolonha() {
	return getDocumentRequest().isBolonha();
    }

    @Override
    protected void setPersonFields() {
	addParameter("name", getDocumentRequest().getPerson().getName());
    }

    private String calculateNumberOfPrograms() {
	return NumberToWordsConverter.convert(getDocumentRequest().getEnrolmentsCount());
    }

    @Override
    protected boolean showPriceFields() {
	return false;
    }

    private void createProgramsList() {
	final List<ProgramInformation> bolonha = new ArrayList<ProgramInformation>();
	final List<ProgramInformation> preBolonha = new ArrayList<ProgramInformation>();

	addParameter("bolonhaList", bolonha);
	addParameter("preBolonhaList", preBolonha);

	for (final Enrolment enrolment : getDocumentRequest().getEnrolmentsSet()) {
	    if (enrolment.isBolonhaDegree()) {
		bolonha.add(new BolonhaProgramInformation(enrolment));
	    } else {
		preBolonha.add(new PreBolonhaProgramInformation(enrolment));
	    }
	}
    }

    static abstract public class ProgramInformation implements Serializable {
	private String degree;
	private String degreeCurricularPlan;
	private String curricularCourse;
	private List<ContextInformation> contexts;

	public ProgramInformation(final Enrolment enrolment) {
	    this.degree = enrolment.getCurricularCourse().getDegree().getName();
	    this.degreeCurricularPlan = enrolment.getCurricularCourse().getDegreeCurricularPlan().getName();
	    this.curricularCourse = buildCurricularCourseName(enrolment.getCurricularCourse());
	    this.contexts = buildContextsInformation(enrolment.getCurricularCourse());
	}

	private List<ContextInformation> buildContextsInformation(final CurricularCourse curricularCourse) {
	    final List<ContextInformation> result = new ArrayList<ContextInformation>();
	    for (final Context context : curricularCourse.getParentContexts()) {
		result.add(new ContextInformation(context));
	    }
	    return result;
	}

	private String buildCurricularCourseName(final CurricularCourse curricularCourse) {
	    return curricularCourse.getName()
		    + (StringUtils.isEmpty(curricularCourse.getAcronym()) ? "" : " (" + curricularCourse.getAcronym() + ")");
	}

	public String getDegree() {
	    return degree;
	}

	public String getDegreeCurricularPlan() {
	    return this.degreeCurricularPlan;
	}

	public String getCurricularCourse() {
	    return this.curricularCourse;
	}

	public List<ContextInformation> getContexts() {
	    return contexts;
	}
    }

    static public class BolonhaProgramInformation extends ProgramInformation {
	private String program;
	private String weigth;
	private String prerequisites;
	private String objectives;
	private String evaluationMethod;
	private List<BibliographicInformation> bibliographics;

	public BolonhaProgramInformation(final Enrolment enrolment) {
	    super(enrolment);

	    final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
	    final CurricularCourse curricularCourse = enrolment.getCurricularCourse();

	    this.program = HtmlToTextConverterUtil.convertToText(curricularCourse.getProgram(executionSemester));
	    this.weigth = curricularCourse.getWeigth().toString();
	    this.prerequisites = curricularCourse.getPrerequisites();
	    this.objectives = HtmlToTextConverterUtil.convertToText(curricularCourse.getObjectives(executionSemester));
	    this.evaluationMethod = HtmlToTextConverterUtil
		    .convertToText(curricularCourse.getEvaluationMethod(executionSemester));

	    this.bibliographics = buildBibliographicInformation(curricularCourse, executionSemester);
	}

	private List<BibliographicInformation> buildBibliographicInformation(final CurricularCourse curricularCourse,
		final ExecutionSemester executionSemester) {
	    final List<BibliographicInformation> result = new ArrayList<BibliographicInformation>();
	    for (final BibliographicReferences.BibliographicReference reference : curricularCourse.getCompetenceCourse()
		    .getAllBibliographicReferences(executionSemester)) {
		result.add(new BibliographicInformation(reference.getAuthors(), reference.getTitle(), reference.getReference(),
			reference.getYear()));
	    }
	    return result;
	}

	public String getProgram() {
	    return program;
	}

	public String getWeigth() {
	    return this.weigth;
	}

	public String getPrerequisites() {
	    return this.prerequisites;
	}

	public String getObjectives() {
	    return objectives;
	}

	public String getEvaluationMethod() {
	    return evaluationMethod;
	}

	public List<BibliographicInformation> getBibliographics() {
	    return this.bibliographics;
	}
    }

    static public class PreBolonhaProgramInformation extends ProgramInformation {
	private String program;
	private String generalObjectives;
	private String operationalObjectives;

	public PreBolonhaProgramInformation(Enrolment enrolment) {
	    super(enrolment);
	    final Curriculum curriculum = enrolment.getCurricularCourse().findLatestCurriculum();
	    if (curriculum != null) {
		this.program = HtmlToTextConverterUtil.convertToText(curriculum.getProgram());
		this.generalObjectives = HtmlToTextConverterUtil.convertToText(curriculum.getGeneralObjectives());
		this.operationalObjectives = HtmlToTextConverterUtil.convertToText(curriculum.getOperacionalObjectives());
	    } else {
		this.program = this.generalObjectives = this.operationalObjectives = StringUtils.EMPTY;
	    }
	}

	public String getProgram() {
	    return program;
	}

	public String getGeneralObjectives() {
	    return generalObjectives;
	}

	public String getOperationalObjectives() {
	    return operationalObjectives;
	}
    }

    static public class ContextInformation {
	private String name;
	private String period;

	public ContextInformation(final Context context) {
	    this.name = context.getParentCourseGroup().getOneFullName();
	    this.period = context.getCurricularPeriod().getFullLabel();
	}

	public String getName() {
	    return name;
	}

	public String getPeriod() {
	    return period;
	}
    }

    static public class BibliographicInformation {
	private String authors;
	private String title;
	private String reference;
	private String year;

	public BibliographicInformation(final String authors, final String title, final String reference, final String year) {
	    this.authors = authors;
	    this.title = title;
	    this.reference = reference;
	    this.year = year;
	}

	public String getAuthors() {
	    return this.authors;
	}

	public String getTitle() {
	    return this.title;
	}

	public String getReference() {
	    return this.reference;
	}

	public String getYear() {
	    return this.year;
	}
    }

}

package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ProgramCertificateRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.HtmlToTextConverterUtil;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import pt.ist.utl.fenix.utils.NumberToWordsConverter;

public class ProgramCertificateRequestDocument extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 12L;

    static final protected String DD = "dd";
    static final protected String MMMM_YYYY = "MMMM yyyy";

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
        addParameter("programsDescription", getProgramsDescription());
        addParameter("degreeDescription", getDegreeDescription());

        AdministrativeOffice administrativeOffice = getAdministrativeOffice();
        Unit adminOfficeUnit = administrativeOffice.getUnit();
        Person activeUnitCoordinator = adminOfficeUnit.getActiveUnitCoordinator();

        addParameter("administrativeOfficeCoordinatorName", activeUnitCoordinator.getName());
        addParameter("administrativeOfficeName", getMLSTextContent(adminOfficeUnit.getPartyName()));
        addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
        addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());

        addParameter("day", new LocalDate().toString(DD_MMMM_YYYY, getLocale()));

        addParameter("coordinatorSignature", coordinatorSignature(administrativeOffice, activeUnitCoordinator));
        addParameter("coordinatorWithoutArticle", coordinatorSignatureWithoutArticle(administrativeOffice, activeUnitCoordinator));
        addParameter("adminOfficeIntroMessage", adminOfficeIntroMessage(administrativeOffice, activeUnitCoordinator));

        createProgramsList();
    }

    private String adminOfficeIntroMessage(AdministrativeOffice administrativeOffice, Person activeUnitCoordinator) {
        String adminOfficeIntroMessage = "message.academicServiceRequest.course.load.admin.office.intro";

        if (administrativeOffice.isMasterDegree()) {
            adminOfficeIntroMessage += ".master.degree";
        } else {
            adminOfficeIntroMessage += ".degree";
        }

        if (activeUnitCoordinator.isMale()) {
            adminOfficeIntroMessage += ".male";
        } else {
            adminOfficeIntroMessage += ".female";
        }

        return ResourceBundle.getBundle("resources.AcademicAdminOffice", getLocale()).getString(adminOfficeIntroMessage);
    }

    private String coordinatorSignatureWithoutArticle(AdministrativeOffice administrativeOffice, Person activeUnitCoordinator) {
        String coordinatorSignature = coordinatorSignature(administrativeOffice, activeUnitCoordinator);
        String withoutArticle = coordinatorSignature.substring(2, coordinatorSignature.length());
        Integer index = withoutArticle.indexOf(" do ");

        return withoutArticle.substring(0, index) + withoutArticle.substring(index, withoutArticle.length()).toUpperCase();
    }

    private String coordinatorSignature(AdministrativeOffice administrativeOffice, Person activeUnitCoordinator) {
        String coordinatorSignatureMessage = "message.academicServiceRequest.course.load.coordinator.signature";

        if (administrativeOffice.isMasterDegree()) {
            coordinatorSignatureMessage += ".master.degree";
        } else {
            coordinatorSignatureMessage += ".degree";
        }

        if (activeUnitCoordinator.isMale()) {
            coordinatorSignatureMessage += ".male";
        } else {
            coordinatorSignatureMessage += ".female";
        }

        return ResourceBundle.getBundle("resources.AcademicAdminOffice", getLocale()).getString(coordinatorSignatureMessage);
    }

    private String getStudentNumber() {
        final Registration registration = getDocumentRequest().getRegistration();
        if (ProgramCertificateRequest.FREE_PAYMENT_AGREEMENTS.contains(registration.getRegistrationAgreement())) {
            final String agreementInformation = registration.getAgreementInformation();
            if (!StringUtils.isEmpty(agreementInformation)) {
                return registration.getRegistrationAgreement().toString() + SINGLE_SPACE + agreementInformation;
            }
        }
        return registration.getStudent().getNumber().toString();
    }

    @Override
    protected String getDegreeDescription() {
        final CycleType requestedCycle = getDocumentRequest().getRequestedCycle();
        if (requestedCycle == null) {
            final Registration registration = getDocumentRequest().getRegistration();
            final DegreeType degreeType = registration.getDegreeType();
            final CycleType cycleType =
                    degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration
                            .getCycleType(getExecutionYear());
            return registration.getDegreeDescription(getExecutionYear(), cycleType, getLocale());
        }
        return getDocumentRequest().getRegistration().getDegreeDescription(getExecutionYear(), requestedCycle, getLocale());
    }

    public boolean isBolonha() {
        return getDocumentRequest().isBolonha();
    }

    @Override
    protected void setPersonFields() {
        addParameter("name", getDocumentRequest().getPerson().getName());
    }

    private String numberOfPrograms() {
        return NumberToWordsConverter.convert(getDocumentRequest().getEnrolmentsCount());
    }

    private String getProgramsDescription() {
        if (getDocumentRequest().getEnrolmentsCount() == 1) {
            return getResourceBundle().getString("label.program.certificate.program");
        } else {
            return MessageFormat.format(getResourceBundle().getString("label.program.certificate.programs"), numberOfPrograms());
        }
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
        private final String degree;
        private final String degreeCurricularPlan;
        private final String curricularCourse;
        private final List<ContextInformation> contexts;

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
                    + (StringUtils.isEmpty(curricularCourse.getAcronym()) ? EMPTY_STR : " (" + curricularCourse.getAcronym()
                            + ")");
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
        private final String program;
        private final String weigth;
        private final String prerequisites;
        private final String objectives;
        private final String evaluationMethod;
        private final List<BibliographicInformation> bibliographics;

        public BolonhaProgramInformation(final Enrolment enrolment) {
            super(enrolment);

            final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
            final CurricularCourse curricularCourse = enrolment.getCurricularCourse();

            this.program = HtmlToTextConverterUtil.convertToText(curricularCourse.getProgram(executionSemester));
            this.weigth = curricularCourse.getWeigth().toString();
            this.prerequisites = curricularCourse.getPrerequisites();
            this.objectives = HtmlToTextConverterUtil.convertToText(curricularCourse.getObjectives(executionSemester));
            this.evaluationMethod =
                    HtmlToTextConverterUtil.convertToText(curricularCourse.getEvaluationMethod(executionSemester));

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
                this.program = this.generalObjectives = this.operationalObjectives = EMPTY_STR;
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
        private final String name;
        private final String period;

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
        private final String authors;
        private final String title;
        private final String reference;
        private final String year;

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

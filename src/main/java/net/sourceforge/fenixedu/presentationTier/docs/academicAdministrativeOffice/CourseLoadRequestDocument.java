package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CourseLoadRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;

public class CourseLoadRequestDocument extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 10L;

    protected CourseLoadRequestDocument(final CourseLoadRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected CourseLoadRequest getDocumentRequest() {
        return (CourseLoadRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {

        addParameter("certification", getResourceBundle().getString("label.certification").toUpperCase());
        addParameter("certificationMessage", getResourceBundle().getString("label.program.certificate.certification"));
        setPersonFields();
        fillEmployeeFields();
        setFooter(getDocumentRequest());
        addParametersInformation();
    }

    private void addParametersInformation() {

        AdministrativeOffice administrativeOffice = getAdministrativeOffice();
        Unit adminOfficeUnit = administrativeOffice.getUnit();
        Person activeUnitCoordinator = adminOfficeUnit.getActiveUnitCoordinator();
        Person student = getDocumentRequest().getPerson();
        final UniversityUnit university = UniversityUnit.getInstitutionsUniversityUnit();

        String coordinatorGender;
        if (activeUnitCoordinator.isMale()) {
            coordinatorGender = getResourceBundle().getString("label.academicDocument.declaration.maleCoordinator");
        } else {
            coordinatorGender = getResourceBundle().getString("label.academicDocument.declaration.femaleCoordinator");
        }

        String labelStudent;
        if (student.isMale()) {
            labelStudent = getResourceBundle().getString("label.of.student.male");
        } else {
            labelStudent = getResourceBundle().getString("label.of.student.female");
        }

        String coordinatorName = activeUnitCoordinator.getName();
        String adminOfficeUnitName = getMLSTextContent(adminOfficeUnit.getPartyName()).toUpperCase();
        String universityName = getMLSTextContent(university.getPartyName()).toUpperCase();

        String institutionName =
                getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName()).toUpperCase();

        String template = getResourceBundle().getString("label.courseLoad.personalData.first");
        String firstPart =
                MessageFormat.format(template, coordinatorName, coordinatorGender, adminOfficeUnitName, institutionName,
                        universityName, labelStudent);
        addParameter("firstPart", firstPart);
        addParameter("secondPart", student.getName());
        addParameter("thirdPart", getResourceBundle().getString("label.with.number"));
        addParameter("fourthPart", getStudentNumber());
        addParameter("fifthPart", getResourceBundle().getString("label.of.male"));
        addParameter("sixthPart", getDegreeDescription());
        addParameter("seventhPart", getResourceBundle().getString("label.courseLoad.endMessage"));

        addLabelsToMultiLanguage();
        createCourseLoadsList();
    }

    private void addLabelsToMultiLanguage() {
        addParameter("enrolment", getResourceBundle().getString("label.serviceRequests.enrolment"));
        addParameter("year", getResourceBundle().getString("label.year"));
        addParameter("autonomousWork", getResourceBundle().getString("label.autonomousWork"));
        addParameter("courseLoad", getResourceBundle().getString("label.courseLoad"));
        addParameter("total", getResourceBundle().getString("label.total.amount"));
        addParameter("tTotal", getResourceBundle().getString("label.total"));
        addParameter("lectures", getResourceBundle().getString("label.lectures"));
        addParameter("practices", getResourceBundle().getString("label.patrice"));
        addParameter("lecturesPractice", getResourceBundle().getString("label.lecturesPractice"));
        addParameter("laboratory", getResourceBundle().getString("label.laboratory"));
        addParameter("dissertations", getResourceBundle().getString("label.courseLoad.dissertations"));
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

    private String getStudentNumber() {
        final Registration registration = getDocumentRequest().getRegistration();
        if (CourseLoadRequest.FREE_PAYMENT_AGREEMENTS.contains(registration.getRegistrationAgreement())) {
            final String agreementInformation = registration.getAgreementInformation();
            if (!StringUtils.isEmpty(agreementInformation)) {
                return registration.getRegistrationAgreement().toString() + SINGLE_SPACE + agreementInformation;
            }
        }
        return registration.getStudent().getNumber().toString();
    }

    private void createCourseLoadsList() {
        final List<CourseLoadEntry> bolonha = new ArrayList<CourseLoadEntry>();
        final List<CourseLoadEntry> preBolonha = new ArrayList<CourseLoadEntry>();
        final List<CourseLoadEntry> dissertations = new ArrayList<CourseLoadEntry>();

        addParameter("bolonhaList", bolonha);
        addParameter("preBolonhaList", preBolonha);
        addParameter("dissertationsList", dissertations);

        for (final Enrolment enrolment : getDocumentRequest().getEnrolmentsSet()) {

            if (enrolment.isBolonhaDegree()) {

                if (enrolment.isDissertation()) {
                    dissertations.add(new BolonhaCourseLoadEntry(enrolment));
                } else {
                    bolonha.add(new BolonhaCourseLoadEntry(enrolment));
                }

            } else {
                preBolonha.add(new PreBolonhaCourseLoadEntry(enrolment));
            }

        }

        Collections.sort(bolonha);
        Collections.sort(preBolonha);
        Collections.sort(dissertations);
    }

    static final protected String DD = "dd";
    static final protected String MMMM_YYYY = "MMMM yyyy";

    @Override
    protected boolean showPriceFields() {
        return false;
    }

    @Override
    protected void setPersonFields() {
        addParameter("name", getDocumentRequest().getPerson().getName());
    }

    abstract public class CourseLoadEntry implements Comparable<CourseLoadEntry> {
        private String curricularCourseName;
        private String year;
        private Double total;

        protected CourseLoadEntry(final String name, final String year) {
            this.curricularCourseName = name;
            this.year = year;
        }

        public String getCurricularCourseName() {
            return curricularCourseName;
        }

        public void setCurricularCourseName(String curricularCourseName) {
            this.curricularCourseName = curricularCourseName;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        @Override
        public int compareTo(CourseLoadEntry other) {
            return getCurricularCourseName().compareTo(other.getCurricularCourseName());
        }

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }

        public Boolean getCourseLoadCorrect() {
            return Boolean.valueOf(total.doubleValue() != 0d);
        }

        CourseLoadEntry create(final Enrolment enrolment) {
            if (enrolment.isBolonhaDegree()) {
                return new BolonhaCourseLoadEntry(enrolment);
            } else {
                return new PreBolonhaCourseLoadEntry(enrolment);
            }
        }
    }

    public class BolonhaCourseLoadEntry extends CourseLoadEntry {
        private Double contactLoad;
        private Double autonomousWork;

        public BolonhaCourseLoadEntry(final Enrolment enrolment) {
            super(enrolment.getCurricularCourse().getName(), enrolment.getExecutionYear().getYear());

            final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            setCurricularCourseName(curricularCourse.getNameI18N(enrolment.getExecutionYear()).getContent(language));
            setContactLoad(curricularCourse.getContactLoad(enrolment.getExecutionPeriod()));
            setAutonomousWork(curricularCourse.getAutonomousWorkHours(enrolment.getExecutionPeriod()));
            setTotal(curricularCourse.getTotalLoad(enrolment.getExecutionPeriod()));
        }

        public Double getAutonomousWork() {
            return autonomousWork;
        }

        public void setAutonomousWork(Double autonomousWork) {
            this.autonomousWork = autonomousWork;
        }

        public Double getContactLoad() {
            return contactLoad;
        }

        public void setContactLoad(Double contactLoad) {
            this.contactLoad = contactLoad;
        }
    }

    public class PreBolonhaCourseLoadEntry extends CourseLoadEntry {
        private Double theoreticalHours;
        private Double praticalHours;
        private Double labHours;
        private Double theoPratHours;

        public PreBolonhaCourseLoadEntry(final Enrolment enrolment) {
            super(enrolment.getCurricularCourse().getName(), enrolment.getExecutionYear().getYear());
            initInformation(enrolment.getCurricularCourse(), enrolment.getExecutionYear());
        }

        private void initInformation(final CurricularCourse curricularCourse, ExecutionYear executionYear) {
            setCurricularCourseName(curricularCourse.getNameI18N(executionYear).getContent(language));
            setTheoreticalHours(curricularCourse.getTheoreticalHours());
            setPraticalHours(curricularCourse.getPraticalHours());
            setLabHours(curricularCourse.getLabHours());
            setTheoPratHours(curricularCourse.getTheoPratHours());
            setTotal(calculateTotal(curricularCourse));
        }

        public Double getLabHours() {
            return labHours;
        }

        public void setLabHours(Double labHours) {
            this.labHours = labHours;
        }

        public Double getPraticalHours() {
            return praticalHours;
        }

        public void setPraticalHours(Double praticalHours) {
            this.praticalHours = praticalHours;
        }

        public Double getTheoPratHours() {
            return theoPratHours;
        }

        public void setTheoPratHours(Double theoPratHours) {
            this.theoPratHours = theoPratHours;
        }

        public Double getTheoreticalHours() {
            return theoreticalHours;
        }

        public void setTheoreticalHours(Double theoreticalHours) {
            this.theoreticalHours = theoreticalHours;
        }

        private Double calculateTotal(final CurricularCourse curricularCourse) {
            double result = 0d;
            result += curricularCourse.getTheoreticalHours();
            result += curricularCourse.getPraticalHours();
            result += curricularCourse.getLabHours();
            result += curricularCourse.getTheoPratHours();
            return Double.valueOf(result);
        }
    }

}

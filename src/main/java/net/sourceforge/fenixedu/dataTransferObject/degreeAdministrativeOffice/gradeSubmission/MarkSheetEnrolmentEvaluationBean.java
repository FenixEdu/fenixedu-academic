package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.curriculum.CurriculumValidationEvaluationPhase;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;

public class MarkSheetEnrolmentEvaluationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources");
    private static final ResourceBundle academicResources = ResourceBundle.getBundle("resources.AcademicAdminOffice");

    private String gradeValue;
    private Date evaluationDate;
    private Enrolment enrolment;
    private String bookReference;
    private String page;

    // used to edit
    private EnrolmentEvaluation enrolmentEvaluation;

    private ExecutionSemester executionSemester;

    private Boolean enrolmentEvaluationSet;

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private CurriculumValidationEvaluationPhase curriculumValidationEvaluationPhase;

    private GradeScale gradeScale;

    private Double weight;

    public MarkSheetEnrolmentEvaluationBean() {
    }

    public MarkSheetEnrolmentEvaluationBean(Enrolment enrolment, Date evaluationDate, Grade grade) {
        setEnrolment(enrolment);
        setEvaluationDate(evaluationDate);
        setGradeValue(grade.getValue());
        setWeight(enrolment.getWeigth());
    }

    public MarkSheetEnrolmentEvaluationBean(Enrolment enrolment, ExecutionSemester executionSemester,
            EnrolmentEvaluationType evaluationType, CurriculumValidationEvaluationPhase evaluationPhase) {
        this.setExecutionSemester(executionSemester);
        this.setEnrolment(enrolment);
        this.setEnrolmentEvaluationType(evaluationType);
        this.setCurriculumValidationEvaluationPhase(evaluationPhase);
        setWeight(enrolment.getWeigth());

        if (this.getHasGrade()) {
            EnrolmentEvaluation enrolmentEvaluation = getLatestEnrolmentEvaluation();
            Grade grade = enrolmentEvaluation.getGrade();

            this.gradeValue = grade.getValue();
            this.evaluationDate = enrolmentEvaluation.getExamDate();
            this.bookReference = enrolmentEvaluation.getBookReference();
            this.page = enrolmentEvaluation.getPage();
            this.gradeScale = enrolmentEvaluation.getAssociatedGradeScale();
        }

        this.enrolmentEvaluationSet = null;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Enrolment getEnrolment() {
        return this.enrolment;
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public String getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(String grade) {
        this.gradeValue = grade;
    }

    public boolean hasAnyGradeValue() {
        return getGradeValue() != null && getGradeValue().length() != 0;
    }

    public EnrolmentEvaluation getEnrolmentEvaluation() {
        return this.enrolmentEvaluation;
    }

    public void setEnrolmentEvaluation(EnrolmentEvaluation enrolmentEvaluation) {
        this.enrolmentEvaluation = enrolmentEvaluation;
        if (this.enrolmentEvaluation != null) {
            setEnrolment(this.enrolmentEvaluation.getEnrolment());
        }
    }

    public String getBookReference() {
        return bookReference;
    }

    public void setBookReference(String bookReference) {
        this.bookReference = bookReference;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester != null ? this.executionSemester : null;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public Boolean getHasGrade() {
        return getLatestEnrolmentEvaluation() != null;
    }

    public Boolean getHasFinalGrade() {
        return getHasGrade() && getLatestEnrolmentEvaluation().isFinal();
    }

    public String getName() {
        return getEnrolment().getName().getContent();
    }

    public EnrolmentEvaluation getLatestEnrolmentEvaluation() {
        EnrolmentEvaluation evaluation =
                getEnrolment().getLatestEnrolmentEvaluationByTypeAndPhase(this.getEnrolmentEvaluationType(),
                        this.getCurriculumValidationEvaluationPhase());

        if (evaluation == null && EnrolmentEvaluationType.NORMAL.equals(this.getEnrolmentEvaluationType())
                && CurriculumValidationEvaluationPhase.FIRST_SEASON.equals(this.getCurriculumValidationEvaluationPhase())) {
            if (getEnrolment().getLatestEnrolmentEvaluationByTypeAndPhase(this.getEnrolmentEvaluationType(),
                    CurriculumValidationEvaluationPhase.SECOND_SEASON) != null) {
                return null;
            }
            return getEnrolment().getLatestEnrolmentEvaluationByTypeAndPhase(this.getEnrolmentEvaluationType(), null);
        }

        return evaluation;
    }

    public String getExecutionYearFullLabel() {
        return getEnrolment().getExecutionPeriod().getExecutionYear().getYear();
    }

    public String getExecutionSemesterFullLabel() {
        return String.format("%s %s", getEnrolment().getExecutionPeriod().getSemester().toString(),
                enumerationResources.getString("SEMESTER.ABBREVIATION"));
    }

    public String getEnrolmentState() {
        return this.getEnrolment().getEnrollmentState().getDescription();
    }

    public String getEnrolmentCondition() {
        return this.getEnrolment().getEnrolmentCondition().getDescription();
    }

    public void setEnrolmentEvaluationSet(Boolean value) {
        this.enrolmentEvaluationSet = value;
    }

    public Boolean getEnrolmentEvaluationSet() {
        return this.enrolmentEvaluationSet;
    }

    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return enrolmentEvaluationType;
    }

    public void setEnrolmentEvaluationType(EnrolmentEvaluationType enrolmentEvaluationType) {
        this.enrolmentEvaluationType = enrolmentEvaluationType;
    }

    public CurriculumValidationEvaluationPhase getCurriculumValidationEvaluationPhase() {
        return curriculumValidationEvaluationPhase;
    }

    public void setCurriculumValidationEvaluationPhase(CurriculumValidationEvaluationPhase curriculumValidationEvaluationPhase) {
        this.curriculumValidationEvaluationPhase = curriculumValidationEvaluationPhase;
    }

    public GradeScale getGradeScale() {
        return this.gradeScale;
    }

    public void setGradeScale(final GradeScale value) {
        this.gradeScale = value;
    }

    private static final String NORMAL_TYPE_FIRST_SEASON_DESCRIPTION =
            "label.curriculum.validation.normal.type.first.season.description";
    private static final String NORMAL_TYPE_SECOND_SEASON_DESCRIPTION =
            "label.curriculum.validation.normal.type.second.season.description";

    public String getEnrolmentEvaluationTypeDescription() {
        if (EnrolmentEvaluationType.NORMAL.equals(this.getEnrolmentEvaluationType())
                && CurriculumValidationEvaluationPhase.FIRST_SEASON.equals(this.getCurriculumValidationEvaluationPhase())) {
            return academicResources.getString(NORMAL_TYPE_FIRST_SEASON_DESCRIPTION);
        } else if (EnrolmentEvaluationType.NORMAL.equals(this.getEnrolmentEvaluationType())
                && CurriculumValidationEvaluationPhase.SECOND_SEASON.equals(this.getCurriculumValidationEvaluationPhase())) {
            return academicResources.getString(NORMAL_TYPE_SECOND_SEASON_DESCRIPTION);
        }

        return this.getEnrolmentEvaluationType().getDescription();
    }

    public boolean isEnrolmentBeMarkedAsEnroled() {
        return !this.getEnrolment().hasAnyEvaluations();
    }

    public boolean isPossibleToUnEnrolEnrolment() {
        return !this.getEnrolment().hasAnyEvaluations()
                && this.getEnrolment().getEnrollmentState().equals(EnrollmentState.ENROLLED);
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}

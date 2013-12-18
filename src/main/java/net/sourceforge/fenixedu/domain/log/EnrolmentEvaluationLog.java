package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

public class EnrolmentEvaluationLog extends EnrolmentEvaluationLog_Base {

    EnrolmentEvaluationLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    EnrolmentEvaluationLog(final EnrolmentEvaluation enrolmentEvaluation) {
        this();

        init(enrolmentEvaluation);
        setWho(getCurrentUser());
    }

    void init(final EnrolmentEvaluation enrolmentEvaluation) {
        setCurricularCourse(String.format("externalId: %s; code: %s; name %s", enrolmentEvaluation.getEnrolment()
                .getCurricularCourse().getExternalId(), enrolmentEvaluation.getEnrolment().getCurricularCourse().getCode(),
                enrolmentEvaluation.getEnrolment().getCurricularCourse().getName()));

        setGradeValue(enrolmentEvaluation.getGradeValue());
        setGradeScale(enrolmentEvaluation.getGradeScale() != null ? enrolmentEvaluation.getGradeScale().getName() : "");
        setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType().getName());
        setEnrolmentEvaluationState(enrolmentEvaluation.getEnrolmentEvaluationState() != null ? enrolmentEvaluation
                .getEnrolmentEvaluationState().getState().toString() : "");
        setExecutionSemester(enrolmentEvaluation.getEnrolment().getExecutionPeriod().getName());
        setExamDate(enrolmentEvaluation.getExamDateYearMonthDay() != null ? enrolmentEvaluation.getExamDateYearMonthDay()
                .toString("dd/MM/yyyy") : "");
        setEnrolmentEvaluationResponsible(enrolmentEvaluation.getPerson() != null ? enrolmentEvaluation.getPerson().getUsername() : "");
        setBook(enrolmentEvaluation.getBookReference());
        setPage(enrolmentEvaluation.getPage());
        setActionDate(new DateTime().toString());
    }

    public static void logEnrolmentEvaluationCreation(final EnrolmentEvaluation enrolmentEvaluation) {
        EnrolmentEvaluationLog log = new EnrolmentEvaluationLog(enrolmentEvaluation);
        log.setAction("create");
    }

    public static void logEnrolmentEvaluationDeletion(final EnrolmentEvaluation enrolmentEvaluation) {
        EnrolmentEvaluationLog log = new EnrolmentEvaluationLog(enrolmentEvaluation);
        log.setAction("delete");
    }

    protected String getCurrentUser() {
        return Authenticate.getUser() != null ? Authenticate.getUser().getUsername() : null;
    }

    @Deprecated
    public boolean hasBook() {
        return getBook() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasGradeScale() {
        return getGradeScale() != null;
    }

    @Deprecated
    public boolean hasExamDate() {
        return getExamDate() != null;
    }

    @Deprecated
    public boolean hasEnrolmentEvaluationType() {
        return getEnrolmentEvaluationType() != null;
    }

    @Deprecated
    public boolean hasActionDate() {
        return getActionDate() != null;
    }

    @Deprecated
    public boolean hasGradeValue() {
        return getGradeValue() != null;
    }

    @Deprecated
    public boolean hasAction() {
        return getAction() != null;
    }

    @Deprecated
    public boolean hasWho() {
        return getWho() != null;
    }

    @Deprecated
    public boolean hasPage() {
        return getPage() != null;
    }

    @Deprecated
    public boolean hasEnrolmentEvaluationState() {
        return getEnrolmentEvaluationState() != null;
    }

    @Deprecated
    public boolean hasEnrolmentEvaluationResponsible() {
        return getEnrolmentEvaluationResponsible() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

}

package net.sourceforge.fenixedu.domain.teacher.evaluation;

import org.fenixedu.bennu.core.domain.Bennu;

public class FacultyEvaluationProcessYear extends FacultyEvaluationProcessYear_Base {

    public FacultyEvaluationProcessYear(final FacultyEvaluationProcess facultyEvaluationProcess, final String year) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setFacultyEvaluationProcess(facultyEvaluationProcess);
        setYear(year);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.evaluation.ApprovedTeacherEvaluationProcessMark> getApprovedTeacherEvaluationProcessMark() {
        return getApprovedTeacherEvaluationProcessMarkSet();
    }

    @Deprecated
    public boolean hasAnyApprovedTeacherEvaluationProcessMark() {
        return !getApprovedTeacherEvaluationProcessMarkSet().isEmpty();
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFacultyEvaluationProcess() {
        return getFacultyEvaluationProcess() != null;
    }

}

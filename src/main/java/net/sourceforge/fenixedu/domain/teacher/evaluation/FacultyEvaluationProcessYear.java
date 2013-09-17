package net.sourceforge.fenixedu.domain.teacher.evaluation;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class FacultyEvaluationProcessYear extends FacultyEvaluationProcessYear_Base {

    public FacultyEvaluationProcessYear(final FacultyEvaluationProcess facultyEvaluationProcess, final String year) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFacultyEvaluationProcess() {
        return getFacultyEvaluationProcess() != null;
    }

}

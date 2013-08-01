package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.Comparator;

import pt.ist.bennu.core.domain.Bennu;

public class ApprovedTeacherEvaluationProcessMark extends ApprovedTeacherEvaluationProcessMark_Base {

    public static final Comparator<ApprovedTeacherEvaluationProcessMark> COMPARATOR_BY_YEAR =
            new Comparator<ApprovedTeacherEvaluationProcessMark>() {
                @Override
                public int compare(final ApprovedTeacherEvaluationProcessMark o1, final ApprovedTeacherEvaluationProcessMark o2) {
                    final String year1 = o1.getFacultyEvaluationProcessYear().getYear();
                    final String year2 = o2.getFacultyEvaluationProcessYear().getYear();
                    return year1.compareTo(year2);
                }
            };

    public ApprovedTeacherEvaluationProcessMark(final FacultyEvaluationProcessYear facultyEvaluationProcessYear,
            final TeacherEvaluationProcess teacherEvaluationProcess) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setFacultyEvaluationProcessYear(facultyEvaluationProcessYear);
        setTeacherEvaluationProces(teacherEvaluationProcess);
    }

    public void delete() {
        setFacultyEvaluationProcessYear(null);
        setTeacherEvaluationProces(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }
    @Deprecated
    public boolean hasTeacherEvaluationProces() {
        return getTeacherEvaluationProces() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFacultyEvaluationProcessYear() {
        return getFacultyEvaluationProcessYear() != null;
    }

    @Deprecated
    public boolean hasApprovedEvaluationMark() {
        return getApprovedEvaluationMark() != null;
    }

}

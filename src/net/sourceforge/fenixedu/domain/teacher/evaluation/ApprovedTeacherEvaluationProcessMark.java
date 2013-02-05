package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

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
        setRootDomainObject(RootDomainObject.getInstance());
        setFacultyEvaluationProcessYear(facultyEvaluationProcessYear);
        setTeacherEvaluationProces(teacherEvaluationProcess);
    }

    public void delete() {
        removeFacultyEvaluationProcessYear();
        removeTeacherEvaluationProces();
        removeRootDomainObject();
        deleteDomainObject();
    }
}

/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.residence;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.QueueJob;
import org.fenixedu.academic.domain.QueueJobResult;
import org.fenixedu.academic.domain.QueueJob_Base;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

public class StudentsPerformanceReport extends StudentsPerformanceReport_Base {

    private static final Logger logger = LoggerFactory.getLogger(StudentsPerformanceReport.class);

    private StudentsPerformanceReport() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRootDomainObjectQueueUndone(Bennu.getInstance());
    }

    private StudentsPerformanceReport(final ExecutionSemester executionSemester, List<Student> studentList) {
        this();

        if (executionSemester == null) {
            throw new DomainException("error.students.performance.report.execution.semester.is.null");
        }

        if (studentList == null || studentList.isEmpty()) {
            throw new DomainException("error.students.performance.report.is.null.or.empty");
        }

        setExecutionSemester(executionSemester);
        getStudentsSet().addAll(studentList);
    }

    @Override
    public QueueJobResult execute() throws Exception {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

        Spreadsheet spreadsheet = createSpreadsheet();

        for (Student student : getStudentsSet()) {
            addInformation(spreadsheet, student);
        }

        spreadsheet.exportToXLSSheet(byteArrayOS);

        final QueueJobResult queueJobResult = new QueueJobResult();
        queueJobResult.setContentType("application/txt");
        queueJobResult.setContent(byteArrayOS.toByteArray());

        logger.info("Job " + getFilename() + " completed");

        return queueJobResult;

    }

    @Override
    public String getFilename() {
        return "Candidatos_Residencia_" + getExecutionSemester().getName() + new DateTime().toString("dd_MM_yyyy") + ".txt";
    }

    public static List<StudentsPerformanceReport> readGeneratedReports(final ExecutionSemester executionSemester) {
        return executionSemester.getStudentsPerformanceReportsSet().stream()
                .filter(QueueJob_Base::getDone)
                .collect(Collectors.toList());
    }

    public static List<StudentsPerformanceReport> readNotGeneratedReports(final ExecutionSemester executionSemester) {
        return executionSemester.getStudentsPerformanceReportsSet().stream()
                .filter(studentsPerformanceReport -> !studentsPerformanceReport.getDone())
                .collect(Collectors.toList());
    }

    public static final Comparator<StudentsPerformanceReport> COMPARE_BY_REQUEST_DATE = Comparator.comparing(QueueJob_Base::getRequestDate);

    public static StudentsPerformanceReport readPendingReport(final ExecutionSemester executionSemester) {
        List<StudentsPerformanceReport> pendingReports =
                executionSemester.getStudentsPerformanceReportsSet().stream()
                        .filter(QueueJob::getIsNotDoneAndNotCancelled)
                        .sorted(Collections.reverseOrder(COMPARE_BY_REQUEST_DATE))
                        .collect(Collectors.toList());

        return pendingReports.isEmpty() ? null : pendingReports.iterator().next();

    }

    public static boolean hasPendingReports(final ExecutionSemester executionSemester) {
        return readPendingReport(executionSemester) != null;
    }

    @Atomic
    public static StudentsPerformanceReport launchJob(final ExecutionSemester executionSemester, List<Student> students) {
        return new StudentsPerformanceReport(executionSemester, students);
    }

    /* STUDY */

    private BigDecimal getApprovedECTS(final Student student) {
        return student
                .getLastActiveRegistration()
                .getCurriculum(getExecutionSemester().getEndDateYearMonthDay().toDateTimeAtCurrentTime(),
                        getExecutionSemester().getExecutionYear(), null).getSumEctsCredits();
    }

    private BigDecimal getEnrolledECTS(final Student student) {
        StudentCurricularPlan scp = getStudentCurricularPlan(student, getExecutionSemester());

        BigDecimal totalECTS = new BigDecimal(0d);

        for (final CurriculumLine curriculumLine : scp.getAllCurriculumLines()) {

            if (curriculumLine.isExtraCurricular()) {
                continue;
            }

            // until given ExecutionSemester
            if (curriculumLine.getExecutionPeriod().isAfter(getExecutionSemester())) {
                continue;
            }

            if (curriculumLine.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumLine;

                totalECTS = totalECTS.add(enrolment.getEctsCreditsForCurriculum());

            } else if (curriculumLine.isDismissal()) {
                final Dismissal dismissal = (Dismissal) curriculumLine;

                if (dismissal.getCredits().isSubstitution()) {
                    for (final IEnrolment enrolment : dismissal.getSourceIEnrolments()) {
                        totalECTS = totalECTS.add(enrolment.getEctsCreditsForCurriculum());
                    }
                } else if (dismissal.getCredits().isEquivalence()) {
                    totalECTS = totalECTS.add(dismissal.getEctsCreditsForCurriculum());
                }
            } else {
                throw new RuntimeException("error.unknown.curriculumLine");
            }
        }

        return totalECTS;
    }

    private int getApprovedGradeValuesSum(final Student student) {
        Collection<ICurriculumEntry> entries =
                student.getLastActiveRegistration()
                        .getCurriculum(getExecutionSemester().getEndDateYearMonthDay().toDateTimeAtCurrentTime(),
                                getExecutionSemester().getExecutionYear(), null).getCurriculumEntries();
        BigDecimal sum = new BigDecimal(0d);

        for (final ICurriculumEntry entry : entries) {
            if (entry.getGrade().isNumeric()) {
                final BigDecimal weigth = entry.getWeigthForCurriculum();
                if (GradeScale.TYPE20 == entry.getGrade().getGradeScale()) {
                    sum = sum.add(entry.getGrade().getNumericValue());
                }
            }
        }

        return sum.intValue();
    }

    private int getNumberOfApprovedCourses(final Student student) {
        Collection<ICurriculumEntry> entries =
                student.getLastActiveRegistration()
                        .getCurriculum(getExecutionSemester().getEndDateYearMonthDay().toDateTimeAtCurrentTime(),
                                getExecutionSemester().getExecutionYear(), null).getCurriculumEntries();

        return entries.size() * 20;
    }

    private BigDecimal getA(final Student student) {
        return BigDecimal.ZERO.equals(getEnrolledECTS(student)) ? BigDecimal.ZERO : getApprovedECTS(student).divide(
                getEnrolledECTS(student), 2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getB(final Student student) {
        return getNumberOfApprovedCourses(student) == 0 ? BigDecimal.ZERO : new BigDecimal(getApprovedGradeValuesSum(student))
                .divide(new BigDecimal(getNumberOfApprovedCourses(student)), 2, RoundingMode.HALF_EVEN);
    }

    private static StudentCurricularPlan getStudentCurricularPlan(final Student student, final ExecutionSemester semester) {
        List<Registration> registrations = student.getActiveRegistrationsIn(semester);
        if (registrations.size() != 1) {
            throw new DomainException("student.has.more.than.one.active.registration");
        }

        Registration registration = registrations.iterator().next();
        final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
        if (!studentCurricularPlan.isBolonhaDegree()) {

            throw new DomainException("student.curricular.plan.is.not.bolonha");
        }

        return studentCurricularPlan;
    }

    private Spreadsheet createSpreadsheet() {
        final Spreadsheet spreadsheet = new Spreadsheet("students");

        spreadsheet.setHeaders("Num Aluno", "Nome", "Tipo Curso", "Curso", "Ciclo", "Ects Aprovados", "Ects Total",
                "Soma classificacoes", "Num Aprovadas * 20", "A", "B", "100 * (A + B)");

        return spreadsheet;
    }

    private void addInformation(final Spreadsheet spreadsheet, final Student student) {
        StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student, getExecutionSemester());

        final Row row = spreadsheet.addRow();
        row.setCell(student.getNumber());
        row.setCell(student.getPerson().getName());
        row.setCell(studentCurricularPlan.getDegreeType().getName().getContent());
        row.setCell(studentCurricularPlan.getName());
        row.setCell(studentCurricularPlan.getRegistration().getCycleType(getExecutionSemester().getExecutionYear()).getDescription());
        row.setCell(getApprovedECTS(student).toPlainString());
        row.setCell(getEnrolledECTS(student).toPlainString());
        row.setCell(getApprovedGradeValuesSum(student));
        row.setCell(getNumberOfApprovedCourses(student));
        row.setCell(getA(student).toPlainString());
        row.setCell(getB(student).toPlainString());
        row.setCell(getA(student).add(getB(student)).multiply(BigDecimal.valueOf(100)).intValue());

    }

}

/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.reports;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class EtiReportFile extends EtiReportFile_Base {

    public EtiReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem para ETI";
    }

    @Override
    protected String getPrefix() {
        return "etiGrades";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
        spreadsheet.setHeader("número aluno");
        setDegreeHeaders(spreadsheet, "aluno");
        spreadsheet.setHeader("semestre");
        spreadsheet.setHeader("ano lectivo");
        spreadsheet.setHeader("nome Disciplina");
        setDegreeHeaders(spreadsheet, "disciplina");
        spreadsheet.setHeader("creditos");
        spreadsheet.setHeader("estado");
        spreadsheet.setHeader("época");
        spreadsheet.setHeader("nota");
        spreadsheet.setHeader("época normal");
        spreadsheet.setHeader("época especial");
        spreadsheet.setHeader("melhoria");
        spreadsheet.setHeader("tipo Aluno");
        spreadsheet.setHeader("número inscricoes anteriores");
        spreadsheet.setHeader("executionCourseId");
        spreadsheet.setHeader("disponível para inquérito");
        spreadsheet.setHeader("OID execucao disciplina");

        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (checkDegreeType(getDegreeType(), degree)) {
                for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
                    if (checkExecutionYear(getExecutionYear(), degreeCurricularPlan)) {
                        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
                            if (checkExecutionYear(getExecutionYear(), curricularCourse)) {

                                for (final CurriculumModule curriculumModule : curricularCourse.getCurriculumModulesSet()) {
                                    if (curriculumModule.isEnrolment()) {
                                        final Enrolment enrolment = (Enrolment) curriculumModule;
                                        if (enrolment.getExecutionYear() == getExecutionYear()) {
                                            final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
                                            if (curricularCourse.isAnual()) {
                                                addEtiRow(spreadsheet, curricularCourse.getDegree(), curricularCourse, enrolment,
                                                        executionSemester, executionSemester);
                                                if (executionSemester.getSemester().intValue() == 1) {
                                                    final ExecutionSemester nextSemester =
                                                            executionSemester.getNextExecutionPeriod();
                                                    addEtiRow(spreadsheet, curricularCourse.getDegree(), curricularCourse,
                                                            enrolment, nextSemester, executionSemester);
                                                }
                                            } else {
                                                addEtiRow(spreadsheet, curricularCourse.getDegree(), curricularCourse, enrolment,
                                                        executionSemester, executionSemester);
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addEtiRow(final Spreadsheet spreadsheet, final Degree degree, final CurricularCourse curricularCourse,
            final Enrolment enrolment, final ExecutionSemester executionSemester,
            final ExecutionSemester executionSemesterForPreviousEnrolmentCount) {
        final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
        final Registration registration = studentCurricularPlan.getRegistration();
        final Student student = registration.getStudent();

        final Row row = spreadsheet.addRow();
        row.setCell(registration.getNumber());
        setDegreeCells(row, registration.getDegree());
        row.setCell(executionSemester.getSemester().toString());
        row.setCell(executionSemester.getExecutionYear().getYear());
        row.setCell(curricularCourse.getName());
        setDegreeCells(row, degree);
        row.setCell(enrolment.getEctsCredits().toString().replace('.', ','));
        row.setCell(enrolment.isApproved() ? EnrollmentState.APROVED.getDescription() : enrolment.getEnrollmentState()
                .getDescription());
        row.setCell(enrolment.getEnrolmentEvaluationType().getDescription());
        row.setCell(enrolment.getGradeValue());

        final EnrolmentEvaluation normal = enrolment.getLatestFinalNormalEnrolmentEvaluation();
        row.setCell(normal == null ? "" : normal.getGradeValue());
        final EnrolmentEvaluation special = enrolment.getLatestFinalSpecialSeasonEnrolmentEvaluation();
        row.setCell(special == null ? "" : special.getGradeValue());
        final EnrolmentEvaluation improvement = enrolment.getLatestFinalImprovementEnrolmentEvaluation();
        row.setCell(improvement == null ? "" : improvement.getGradeValue());

        row.setCell(registration.getRegistrationAgreement().getName());
        row.setCell(countPreviousEnrolments(curricularCourse, executionSemesterForPreviousEnrolmentCount, student));
        Attends attends = null; // enrolment.getAttendsFor(executionSemester);
        for (final Attends a : enrolment.getAttendsSet()) {
            if (a.isFor(executionSemester)) {
                if (attends == null) {
                    attends = a;
                }
            }
        }
        if (attends == null) {
            row.setCell("");
        } else {
            final ExecutionCourse executionCourse = attends.getExecutionCourse();
            row.setCell(executionCourse.getExternalId());
            if (executionCourse.getAvailableForInquiries() != null && executionCourse.getAvailableForInquiries().booleanValue()) {
                row.setCell("sim");
            } else {
                row.setCell("não");
            }
            row.setCell(String.valueOf(executionCourse.getOid()));
        }
    }

    private String countPreviousEnrolments(final CurricularCourse curricularCourse, final ExecutionSemester executionPeriod,
            final Student student) {
        int count = 0;
        for (final CurriculumModule curriculumModule : curricularCourse.getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (executionPeriod.compareTo(enrolment.getExecutionPeriod()) > 0) {
                    if (enrolment.getStudentCurricularPlan().getRegistration().getStudent() == student) {
                        count++;
                    }
                }
            }
        }
        return Integer.toString(count);
    }
}

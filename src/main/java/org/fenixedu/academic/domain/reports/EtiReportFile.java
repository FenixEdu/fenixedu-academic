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
package org.fenixedu.academic.domain.reports;

import java.util.Set;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationConfiguration;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curriculum.EnrollmentState;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

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
        Set<EvaluationSeason> seasons = EvaluationConfiguration.getInstance().getEvaluationSeasonSet();

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
        for (EvaluationSeason season : seasons) {
            spreadsheet.setHeader(season.getName().getContent());
        }
        spreadsheet.setHeader("tipo Aluno");
        spreadsheet.setHeader("número inscricoes anteriores");
        spreadsheet.setHeader("código disciplina execução");

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
                                                        executionSemester, executionSemester, seasons);
                                                if (executionSemester.getSemester().intValue() == 1) {
                                                    final ExecutionSemester nextSemester =
                                                            executionSemester.getNextExecutionPeriod();
                                                    addEtiRow(spreadsheet, curricularCourse.getDegree(), curricularCourse,
                                                            enrolment, nextSemester, executionSemester, seasons);
                                                }
                                            } else {
                                                addEtiRow(spreadsheet, curricularCourse.getDegree(), curricularCourse, enrolment,
                                                        executionSemester, executionSemester, seasons);
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
            final ExecutionSemester executionSemesterForPreviousEnrolmentCount, Set<EvaluationSeason> seasons) {
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
        row.setCell(enrolment.getEvaluationSeason().getName().getContent());
        row.setCell(enrolment.getGradeValue());

        for (EvaluationSeason season : seasons) {
            row.setCell(enrolment.getFinalEnrolmentEvaluationBySeason(season).map(EnrolmentEvaluation::getGradeValue)
                    .orElse(null));
        }

        row.setCell(registration.getRegistrationProtocol().getCode());
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
            row.setCell(GepReportFile.getExecutionCourseCode(executionCourse));
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

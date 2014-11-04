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

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class EctsLabelCurricularCourseReportFile extends EctsLabelCurricularCourseReportFile_Base {

    public EctsLabelCurricularCourseReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem para ECTS LABEL Disciplinas";
    }

    @Override
    protected String getPrefix() {
        return "ectsLabel_Disciplinas";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {

        createEctsLabelCurricularCoursesHeader(spreadsheet);

        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (checkDegreeType(getDegreeType(), degree)) {
                for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
                    if (checkExecutionYear(getExecutionYear(), degreeCurricularPlan)) {
                        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
                            if (checkExecutionYear(getExecutionYear(), curricularCourse)
                                    && !curricularCourse.isOptionalCurricularCourse()) {
                                for (final Context context : curricularCourse
                                        .getParentContextsByExecutionYear(getExecutionYear())) {
                                    addEctsLabelContextRow(spreadsheet, context, getExecutionYear());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createEctsLabelCurricularCoursesHeader(final Spreadsheet spreadsheet) {
        spreadsheet.setHeader("Tipo Curso");
        spreadsheet.setHeader("Nome Curso");
        spreadsheet.setHeader("Sigla Curso");
        spreadsheet.setHeader("Nome Disciplina");
        spreadsheet.setHeader("Nome Disciplina (inglês)");
        spreadsheet.setHeader("Código Disciplina");
        spreadsheet.setHeader("Ano curricular");
        spreadsheet.setHeader("Semestre");
        spreadsheet.setHeader("Duração");
        spreadsheet.setHeader("Tipo");
        spreadsheet.setHeader("Créditos ECTS");
        spreadsheet.setHeader("Idioma");
        spreadsheet.setHeader("Docentes");
        spreadsheet.setHeader("Horas de contacto");
        spreadsheet.setHeader("Objectivos");
        spreadsheet.setHeader("Objectivos (inglês)");
        spreadsheet.setHeader("Programa");
        spreadsheet.setHeader("Programa (inglês)");
        spreadsheet.setHeader("Bibliografia Principal");
        spreadsheet.setHeader("Bibliografia Secundária (inglês)");
        spreadsheet.setHeader("Avaliação");
        spreadsheet.setHeader("Avaliação (inglês)");
        spreadsheet.setHeader("Estimativa total de trabalho");
    }

    private void addEctsLabelContextRow(final Spreadsheet spreadsheet, final Context context, final ExecutionYear executionYear) {

        final Row row = spreadsheet.addRow();
        final ExecutionSemester executionSemester = getExecutionSemester(context, executionYear);
        final CurricularCourse curricular = (CurricularCourse) context.getChildDegreeModule();

        row.setCell(curricular.getDegree().getDegreeType().getLocalizedName());
        row.setCell(curricular.getDegree().getNameFor(executionSemester).getContent());
        row.setCell(curricular.getDegree().getSigla());
        row.setCell(curricular.getName(executionSemester));
        row.setCell(curricular.getNameEn(executionSemester));
        final CompetenceCourse competenceCourse = curricular.getCompetenceCourse();
        if (competenceCourse != null) {
            row.setCell(competenceCourse.getAcronym(executionSemester));
        } else {
            row.setCell("");
        }

        row.setCell(context.getCurricularYear());
        setSemesterAndDuration(row, context);
        row.setCell(curricular.hasCompetenceCourseLevel() ? curricular.getCompetenceCourseLevel().getLocalizedName() : "");
        row.setCell(curricular.getEctsCredits(executionSemester));

        row.setCell(getLanguage(curricular));
        row.setCell(getTeachers(curricular, executionSemester));
        row.setCell(curricular.getContactLoad(context.getCurricularPeriod(), executionSemester));

        row.setCell(normalize(curricular.getObjectives(executionSemester)));
        row.setCell(normalize(curricular.getObjectivesEn(executionSemester)));
        row.setCell(normalize(curricular.getProgram(executionSemester)));
        row.setCell(normalize(curricular.getProgramEn(executionSemester)));

        final BibliographicReferences references = getBibliographicReferences(curricular, executionSemester);
        if (references == null) {
            row.setCell(" ");
            row.setCell(" ");
        } else {
            row.setCell(normalize(getBibliographicReferences(references.getMainBibliographicReferences())));
            row.setCell(normalize(getBibliographicReferences(references.getSecondaryBibliographicReferences())));
        }

        row.setCell(normalize(curricular.getEvaluationMethod(executionSemester)));
        row.setCell(normalize(curricular.getEvaluationMethodEn(executionSemester)));

        row.setCell(curricular.getTotalLoad(context.getCurricularPeriod(), executionSemester));
    }

    private String getLanguage(final CurricularCourse curricularCourse) {
        final DegreeType degreeType = curricularCourse.getDegreeType();
        if (degreeType.hasExactlyOneCycleType() && degreeType.getCycleType() == CycleType.FIRST_CYCLE) {
            return "Português";
        } else {
            return "Português/Inglês";
        }
    }

    private String getTeachers(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        final StringBuilder builder = new StringBuilder();
        for (final ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester)) {
            for (final Professorship professorship : executionCourse.getProfessorshipsSortedAlphabetically()) {
                builder.append(professorship.getPerson().getName()).append("; ");
            }
        }
        return builder.toString();
    }

    private BibliographicReferences getBibliographicReferences(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
        return competenceCourse == null ? null : competenceCourse.getBibliographicReferences(executionSemester);
    }

    private String getBibliographicReferences(final List<BibliographicReference> references) {
        Collections.sort(references);
        final StringBuilder stringBuilder = new StringBuilder();
        for (final BibliographicReference bibliographicReference : references) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("; ");
            }
            stringBuilder.append(bibliographicReference.getTitle());
            stringBuilder.append(", ");
            stringBuilder.append(bibliographicReference.getAuthors());
            stringBuilder.append(", ");
            stringBuilder.append(bibliographicReference.getYear());
            stringBuilder.append(", ");
            stringBuilder.append(bibliographicReference.getReference());
        }
        return stringBuilder.toString();
    }

    private ExecutionSemester getExecutionSemester(final Context context, final ExecutionYear executionYear) {
        final CurricularPeriod curricularPeriod = context.getCurricularPeriod();
        if (curricularPeriod.getAcademicPeriod().getName().equals("SEMESTER")) {
            return (curricularPeriod.getChildOrder().intValue() == 1) ? executionYear.getFirstExecutionPeriod() : executionYear
                    .getLastExecutionPeriod();
        } else {
            return executionYear.getFirstExecutionPeriod();
        }
    }

    private void setSemesterAndDuration(final Row row, final Context context) {
        final CurricularPeriod curricularPeriod = context.getCurricularPeriod();
        if (curricularPeriod.getAcademicPeriod().getName().equals("SEMESTER")) {
            row.setCell(curricularPeriod.getChildOrder());
            row.setCell("Semestral");
        } else {
            row.setCell(" ");
            row.setCell("Anual");
        }
    }

}

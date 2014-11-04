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
package org.fenixedu.academic.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.service.services.credits.ReadAllTeacherCredits;
import org.fenixedu.academic.dto.credits.CreditLineDTO;
import org.fenixedu.academic.domain.accessControl.UnitGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonContractSituation;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.teacher.AdviseType;
import org.fenixedu.academic.domain.teacher.DegreeTeachingService;
import org.fenixedu.academic.domain.teacher.InstitutionWorkTime;
import org.fenixedu.academic.domain.teacher.OtherService;
import org.fenixedu.academic.domain.teacher.TeacherAdviseService;
import org.fenixedu.academic.domain.teacher.TeacherMasterDegreeService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.WeekDay;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class TeacherCreditsDocument extends TeacherCreditsDocument_Base {

    public TeacherCreditsDocument(Teacher teacher, ExecutionSemester executionSemester, TeacherService teacherService) {
        super();
        byte[] content = null;
        try {
            content = getTeacherCreditsFile(teacher, executionSemester, teacherService).getBytes();
        } catch (ParseException e) {
            throw new DomainException("");
        }
        String filename = getFilename(teacher, executionSemester);
        final Set<Group> groups = new HashSet<>();
        final Person person = teacher.getPerson();
        if (person != null) {
            groups.add(UserGroup.of(person.getUser()));
        }
        final Department department = teacher.getLastDepartment(executionSemester.getAcademicInterval());
        if (department != null && department.getDepartmentUnit() != null) {
            groups.add(UnitGroup.get(department.getDepartmentUnit(), FunctionType.PRESIDENT, false));
        }
        groups.add(RoleType.SCIENTIFIC_COUNCIL.actualGroup());
        init(filename, filename, content, UnionGroup.of(groups));
    }

    private String getFilename(Teacher teacher, ExecutionSemester executionSemester) {
        return (teacher.getPerson().getUsername() + "_" + executionSemester.getName() + "_"
                + executionSemester.getExecutionYear().getYear() + ".html").replaceAll(" ", "_").replaceAll("/", "_");
    }

    private String getTeacherCreditsFile(Teacher teacher, ExecutionSemester executionSemester, TeacherService teacherService)
            throws ParseException {
        CreditLineDTO creditLineDTO = ReadAllTeacherCredits.readCreditLineDTO(executionSemester, teacher);
        Unit lastWorkingUnit =
                teacher.getPerson().getEmployee() != null ? teacher
                        .getPerson()
                        .getEmployee()
                        .getLastWorkingPlace(executionSemester.getBeginDateYearMonthDay(),
                                executionSemester.getEndDateYearMonthDay()) : null;

        final StringBuilder htmlText = new StringBuilder();
        htmlText.append("<html><body><div style=\"font-family: Arial, Helvetica, sans-serif;font-size: 100%;\">");
        htmlText.append("<style type=\"text/css\">");
        htmlText.append(".tb01 {width: 100%; border-collapse: collapse;font-size: 65%;}");
        htmlText.append(".tb01 th {background-color: #eee; border: 1pt solid #aaa; height: 1.5em;font-weight: normal; }");
        htmlText.append(".tb01 td { border: 1pt solid #aaa; height: 1.5em;}</style>");

        htmlText.append("<h1 style=\"text-align: center;\">").append(executionSemester.getName()).append(" ")
                .append(executionSemester.getExecutionYear().getYear()).append("</h1>");
        htmlText.append("<table class=\"tb01\"><tr><td><strong>Nome:</strong>").append(teacher.getPerson().getName());
        htmlText.append("</td><td><strong>Username:</strong>").append(teacher.getPerson().getUsername());
        htmlText.append("</td></tr><tr><td><strong>Categoria:</strong>");

        ProfessionalCategory categoryByPeriod = ProfessionalCategory.getCategoryByPeriod(teacher, executionSemester);
        String category = categoryByPeriod != null ? categoryByPeriod.getName().getContent() : null;
        if (category != null) {
            htmlText.append(category);
        }
        htmlText.append("</td><td><strong>Secção:</strong>");
        if (lastWorkingUnit != null) {
            htmlText.append(lastWorkingUnit.getName());
        }
        htmlText.append("</td></tr><tr><td><strong>Departamento:</strong>");
        if (lastWorkingUnit != null && lastWorkingUnit.getDepartmentUnit() != null
                && lastWorkingUnit.getDepartmentUnit().getDepartment() != null) {
            htmlText.append(lastWorkingUnit.getDepartmentUnit().getDepartment().getRealName());
        }
        htmlText.append("</td><td><strong>Centro de Custo:</strong>");
        if (lastWorkingUnit != null) {
            htmlText.append(lastWorkingUnit.getCostCenterCode());
        }
        htmlText.append("</td></tr></table>");

        htmlText.append("<h3>Resumo de créditos</h3>");
        htmlText.append("<ul style=\"list-style: none; font-size: 65%;\"><li><b>")
                .append(creditLineDTO.getTeachingDegreeCredits())
                .append("</b> (A) - Créditos provenientes de aulas de licenciatura</li>");
        htmlText.append("<li><b>").append(creditLineDTO.getSupportLessonHours())
                .append("</b> (D) - Horas semanais de aulas de dúvidas</li>");
        htmlText.append("<li><b>").append(creditLineDTO.getMasterDegreeCredits())
                .append("</b> (M) - Créditos provenientes de aulas de mestrado</li>");
        htmlText.append("<li><b>").append(creditLineDTO.getTfcAdviseCredits())
                .append("</b> (TFC) - Créditos provenientes de alunos de trabalho final de curso</li>");
        htmlText.append("<li><b>").append(creditLineDTO.getThesesCredits())
                .append("</b> (Diss) - Créditos provenientes de Dissertações</li>");
        htmlText.append("<li><b>").append(creditLineDTO.getInstitutionWorkingHours())
                .append("</b> (P) - Horas semanais de permanência no " + Unit.getInstitutionAcronym() + "</li>");
        htmlText.append("<li><b>").append(creditLineDTO.getOtherCredits())
                .append("</b> (O) - Créditos enquadrados na categoria outros</li>");
        htmlText.append("<li><b>").append(creditLineDTO.getManagementCredits())
                .append("</b> (CG) - Créditos provenientes de cargos de gestão</li>");
        htmlText.append("<li><b>").append(creditLineDTO.getServiceExemptionCredits())
                .append("</b> (SNE) - Créditos provenientes de situações em não exercício</li></ul>");

        htmlText.append("<h3>1) Disciplinas Leccionadas em Graduação</h3>");
        List<Professorship> professorships = teacher.getProfessorships(executionSemester);
        if (professorships.isEmpty()) {
            htmlText.append("<p style=\"font-size: 65%;\">O professor não lecciona nenhuma disciplina neste semestre.</p>");
        } else {
            for (Professorship professorship : professorships) {
                htmlText.append("<p style=\"font-size: 65%;\"><strong>").append(professorship.getExecutionCourse().getName())
                        .append(" (").append(professorship.getDegreeSiglas()).append(")</strong></p>");

                htmlText.append("<p style=\"font-size: 65%;\">Aulas:</p>");

                if (TeacherService.getDegreeTeachingServicesOrderedByShift(professorship).isEmpty()) {
                    htmlText.append("<p style=\"font-size: 65%;\">Não foram encontrados registos de aulas.</p>");
                } else {
                    htmlText.append("<table class=\"tb01\"><tr><th>Turno</th><th>Tipo</th><th>Horas</th><th>% que lecciona</th></tr>");
                    for (DegreeTeachingService degreeTeachingService : TeacherService
                            .getDegreeTeachingServicesOrderedByShift(professorship)) {
                        htmlText.append("<tr><td>").append(degreeTeachingService.getShift().getNome()).append("</td>");
                        htmlText.append("<td>").append(degreeTeachingService.getShift().getShiftTypesPrettyPrint())
                                .append("</td>");
                        htmlText.append("<td>").append(degreeTeachingService.getShift().getTotalHours()).append("</td>");
                        htmlText.append("<td>").append(degreeTeachingService.getPercentage()).append("</td></tr>");
                    }
                    htmlText.append("</table>");
                }

                htmlText.append("<p style=\"font-size: 65%;\">Aulas de dúvidas:</p>");

                if (TeacherCredits.getSupportLessonsOrderedByStartTimeAndWeekDay(professorship).isEmpty()) {
                    htmlText.append("<p style=\"font-size: 65%;\">Não foram encontrados registos de aulas de dúvidas.</p>");
                } else {

                    htmlText.append("<table class=\"tb01\"><tr><th>Dia da semana</th><th>Início</th><th>Fim</th><th>Local</th></tr>");
                    for (SupportLesson supportLesson : TeacherCredits
                            .getSupportLessonsOrderedByStartTimeAndWeekDay(professorship)) {
                        htmlText.append("<tr><td>").append(supportLesson.getWeekDay()).append("</td>");
                        htmlText.append("<td>").append(supportLesson.getStartTimeHourMinuteSecond().toString()).append("</td>");
                        htmlText.append("<td>").append(supportLesson.getEndTimeHourMinuteSecond().toString()).append("</td>");
                        htmlText.append("<td>").append(supportLesson.getPlace()).append("</td></tr>");
                    }
                    htmlText.append("</table>");
                }
            }
        }

        htmlText.append("<h3>2) Disciplinas Leccionadas em Pós-Graduação</h3>");

        if (teacherService == null || teacherService.getMasterDegreeServices().isEmpty()) {
            htmlText.append("<p style=\"font-size: 65%;\">Não foram encontrados registos de disciplinas de mestrado.</p>");
        } else {
            htmlText.append("<table class=\"tb01\"><tr><th>Plano(s) curriculare(s)</th><th>Disciplina</th><th>Horas</th><th>Créditos</th></tr>");
            for (TeacherMasterDegreeService teacherMasterDegreeService : teacherService.getMasterDegreeServices()) {
                if (teacherMasterDegreeService.getProfessorship() != null) {
                    htmlText.append("<tr><td>").append(teacherMasterDegreeService.getProfessorship().getDegreePlanNames());
                    htmlText.append("</td><td>").append(
                            teacherMasterDegreeService.getProfessorship().getExecutionCourse().getName());
                } else {
                    htmlText.append("<tr><td></td><td>");
                }
                htmlText.append("</td><td>").append(teacherMasterDegreeService.getHours()).append("</td>");
                htmlText.append("<td>").append(teacherMasterDegreeService.getCredits()).append("</td></tr>");
            }
            htmlText.append("</table>");
        }

        if (teacherService != null && teacherService.getTeacherServiceNotes() != null
                && !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getMasterDegreeTeachingNotes())) {
            htmlText.append("<p style=\"font-size: 65%;\">")
                    .append(teacherService.getTeacherServiceNotes().getMasterDegreeTeachingNotes()
                            .replaceAll("(\r\n)|(\n)", "<br />")).append("</p>");
        }

        htmlText.append("<h3>3) Trabalhos Finais de Curso (até Ano Lectivo de 2006/2007)</h3>");
        List<TeacherAdviseService> teacherAdviseServices = new ArrayList<TeacherAdviseService>();
        if (teacherService != null) {
            teacherAdviseServices.addAll(teacherService.getTeacherAdviseServices());
        }
        List<TeacherAdviseService> tfcAdvises =
                (List<TeacherAdviseService>) CollectionUtils.select(teacherAdviseServices, new Predicate() {
                    @Override
                    public boolean evaluate(Object arg0) {
                        TeacherAdviseService teacherAdviseService = (TeacherAdviseService) arg0;
                        return teacherAdviseService.getAdvise().getAdviseType().equals(AdviseType.FINAL_WORK_DEGREE);
                    }
                });

        if (teacherAdviseServices.isEmpty() || tfcAdvises.isEmpty()) {
            htmlText.append("<p style=\"font-size: 65%;\">Não foram encontrados registos de alunos de trabalho final de curso.</p>");
        } else {
            Collections.sort(tfcAdvises, new BeanComparator("advise.student.number"));

            htmlText.append("<p style=\"font-size: 65%;\"><strong>Alunos:</strong></p><table class=\"tb01\"><tr><th>Número do aluno</th><th>Nome do aluno</th><th>Percentagem</th></tr>");
            for (TeacherAdviseService teacherAdviseService : tfcAdvises) {
                htmlText.append("<tr><td>").append(teacherAdviseService.getAdvise().getStudent().getNumber()).append("</td>");
                htmlText.append("<td>").append(teacherAdviseService.getAdvise().getStudent().getPerson().getName())
                        .append("</td>");
                htmlText.append("<td>").append(teacherAdviseService.getPercentage()).append("</td></tr>");
            }
            htmlText.append("</table>");
        }

        htmlText.append("<h3>4) Dissertações de Mestrado</h3>");

        Collection<ThesisEvaluationParticipant> thesisEvaluationParticipants =
                teacher.getPerson().getThesisEvaluationParticipants(executionSemester);

        if (thesisEvaluationParticipants.isEmpty()) {
            htmlText.append("<p style=\"font-size: 65%;\">Não foram encontradas Dissertações de Mestrado.</p>");
        } else {
            htmlText.append("<p style=\"font-size: 65%;\"><strong>Alunos:</strong></p><table class=\"tb01\"><tbody><tr><th style=\"width: 5em;\">Número</th><th>Nome</th><th style=\"width: 7em;\">Lecciona (%)</th></tr>");
            for (ThesisEvaluationParticipant participant : thesisEvaluationParticipants) {
                htmlText.append("<tr><td>").append(participant.getThesis().getStudent().getNumber());
                htmlText.append("</td><td>").append(participant.getThesis().getStudent().getPerson().getName());
                htmlText.append("</td><td>").append(participant.getCreditsDistribution()).append("</td></tr>");
            }
            htmlText.append("</tbody></table>");
        }

        if (teacherService != null && teacherService.getTeacherServiceNotes() != null
                && !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getThesisNote())) {
            htmlText.append("<p style=\"font-size: 65%;\">")
                    .append(teacherService.getTeacherServiceNotes().getThesisNote().replaceAll("(\r\n)|(\n)", "<br />"))
                    .append("</p>");
        }

        htmlText.append("<h3>5) Permanência no " + Unit.getInstitutionAcronym() + "</h3>");
        ComparatorChain comparatorChain = new ComparatorChain();
        BeanComparator weekDayComparator = new BeanComparator("weekDay");
        BeanComparator startTimeComparator = new BeanComparator("startTime");
        comparatorChain.addComparator(weekDayComparator);
        comparatorChain.addComparator(startTimeComparator);

        List<InstitutionWorkTime> institutionWorkingTimes = new ArrayList<InstitutionWorkTime>();
        if (teacherService != null) {
            institutionWorkingTimes.addAll(teacherService.getInstitutionWorkTimes());
            Collections.sort(institutionWorkingTimes, comparatorChain);
        }

        if (!institutionWorkingTimes.isEmpty()) {
            htmlText.append("<p style=\"font-size: 65%;\">Não foram encontrados registos.</p>");
        } else {
            htmlText.append("<table class=\"tb01\"><tr>");

            for (WeekDay weekDay : WeekDay.values()) {
                if (!weekDay.equals(WeekDay.SUNDAY)) {
                    htmlText.append("<th style=\"width: 16.5em;\">")
                            .append(BundleUtil.getString(Bundle.ENUMERATION, weekDay.name())).append("</th>");
                }
            }
            htmlText.append("</tr><tr>");
            for (WeekDay weekDay : WeekDay.values()) {
                if (!weekDay.equals(WeekDay.SUNDAY)) {
                    htmlText.append("<td style=\"text-align: center;\">");
                    for (InstitutionWorkTime institutionWorkTime : institutionWorkingTimes) {
                        if (institutionWorkTime.getWeekDay().equals(weekDay)) {
                            htmlText.append(institutionWorkTime.getStartTimeHourMinuteSecond().toString()).append(" às ")
                                    .append(institutionWorkTime.getEndTimeHourMinuteSecond().toString()).append("<br/>");
                        }
                    }
                    htmlText.append("</td>");
                }
            }
            htmlText.append("</tr></table>");
        }

        htmlText.append("<h3>6) Acumulação de Funções</h3>");
        if (teacherService != null && teacherService.getTeacherServiceNotes() != null
                && !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getFunctionsAccumulation())) {
            htmlText.append("<p style=\"font-size: 65%;\">")
                    .append(teacherService.getTeacherServiceNotes().getFunctionsAccumulation()
                            .replaceAll("(\r\n)|(\n)", "<br />")).append("</p>");
        } else {
            htmlText.append("<p style=\"font-size: 65%;\">Não foram encontrados registos de acumulação de funções.</p>");
        }

        htmlText.append("<h3>7) Outros</h3>");
        if (teacherService == null || teacherService.getOtherServices().isEmpty()) {
            htmlText.append("<p style=\"font-size: 65%;\">Não foram encontrados registos da categoria outros neste semestre.</p>");
        } else {
            htmlText.append("<table class=\"tb01\"><tr><th>Razão</th><th>Créditos</th></tr>");
            for (OtherService otherService : teacherService.getOtherServices()) {
                htmlText.append("<tr><td>").append(otherService.getReason()).append("</td>");
                htmlText.append("<td>").append(otherService.getCredits()).append("</td></tr>");
            }
            htmlText.append("</table>");
        }

        if (teacherService != null && teacherService.getTeacherServiceNotes() != null
                && !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getOthersNotes())) {
            htmlText.append("<p style=\"font-size: 65%;\">")
                    .append(teacherService.getTeacherServiceNotes().getOthersNotes().replaceAll("(\r\n)|(\n)", "<br />"))
                    .append("</p>");
        }

        htmlText.append("<h3>8) Cargos de Gestão</h3>");
        List<PersonFunction> personFuntions =
                PersonFunction.getPersonFuntions(teacher.getPerson(), executionSemester.getBeginDateYearMonthDay(),
                        executionSemester.getEndDateYearMonthDay());

        if (personFuntions.isEmpty()) {
            htmlText.append("<p style=\"font-size: 65%;\">Não foram encontrados registos de cargos de gestão.</p>");
        } else {
            Collections.sort(personFuntions, new BeanComparator("beginDate"));
            htmlText.append("<table class=\"tb01\"><tr><th>Cargo</th><th>Unidade</th><th>Créditos</th><th>Início</th><th>Fim</th></tr>");
            for (PersonFunction personFunction : personFuntions) {
                htmlText.append("<tr><td>").append(personFunction.getFunction().getName());
                htmlText.append("</td><td>").append(personFunction.getParentParty().getName());
                htmlText.append("</td><td>").append(personFunction.getCredits());
                htmlText.append("</td><td>").append(personFunction.getBeginDate());
                htmlText.append("</td><td>").append(personFunction.getEndDate()).append("</td></tr>");
            }
            htmlText.append("</table>");
        }

        if (teacherService != null && teacherService.getTeacherServiceNotes() != null
                && !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getManagementFunctionNotes())) {
            htmlText.append("<p style=\"font-size: 65%;\">")
                    .append(teacherService.getTeacherServiceNotes().getManagementFunctionNotes()
                            .replaceAll("(\r\n)|(\n)", "<br />")).append("</p>");
        }

        htmlText.append("<h3>9) Situações em Não Exercício</h3>");

        Set<PersonContractSituation> serviceExemptions =
                PersonContractSituation.getValidTeacherServiceExemptions(teacher, executionSemester);

        if (serviceExemptions.isEmpty()) {
            htmlText.append("<p style=\"font-size: 65%;\">Não foram encontrados registos da categoria situações em não exercício.</p>");
        } else {
            htmlText.append("<table class=\"tb01\"><tr><th>Situação</th><th>Organização</th><th>Início</th><th>Fim</th></tr>");
            for (PersonContractSituation teacherServiceExemption : serviceExemptions) {
                htmlText.append("<tr><td>").append(teacherServiceExemption.getContractSituation().getName().getContent());
                htmlText.append("</td><td>").append(teacherServiceExemption.getBeginDate());
                htmlText.append("</td><td>").append(teacherServiceExemption.getServiceExemptionEndDate()).append("</td></tr>");
            }
            htmlText.append("</table>");
        }

        if (teacherService != null && teacherService.getTeacherServiceNotes() != null
                && !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getServiceExemptionNotes())) {
            htmlText.append("<p style=\"font-size: 65%;\">")
                    .append(teacherService.getTeacherServiceNotes().getServiceExemptionNotes()
                            .replaceAll("(\r\n)|(\n)", "<br />")).append("</p>");
        }
        htmlText.append("</div></body></html>");

        return htmlText.toString();
    }

}

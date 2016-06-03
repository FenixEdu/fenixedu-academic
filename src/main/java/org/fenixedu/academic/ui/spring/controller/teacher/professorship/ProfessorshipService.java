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
package org.fenixedu.academic.ui.spring.controller.teacher.professorship;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherAuthorization;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.reports.GepReportFile;
import org.fenixedu.academic.ui.spring.controller.teacher.authorization.AuthorizationService;
import org.fenixedu.academic.ui.spring.controller.teacher.authorization.SearchBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.spreadsheet.SheetData;
import org.fenixedu.commons.spreadsheet.SpreadsheetBuilder;
import org.fenixedu.commons.spreadsheet.WorkbookExportFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@Service
public class ProfessorshipService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AuthorizationService authorizationService;

    /***
     * Helper method to get message from message source easily
     *
     * @param code the key
     * @param args the arguments if necessary
     * @return
     */
    private String message(String code, Object... args) {
        return messageSource.getMessage(code, args, I18N.getLocale());
    }

    public List<Professorship> getProfessorships(User user, ExecutionSemester period) {
        Teacher teacher = user.getPerson().getTeacher();
        if (teacher == null) {
            return ImmutableList.<Professorship> builder().build();
        }
        return teacher.getProfessorships(period).stream().collect(Collectors.toList());
    }

    public String getDegreeAcronyms(Professorship professorship, String separator) {
        return professorship.getExecutionCourse().getExecutionDegrees().stream().map(ExecutionDegree::getDegree)
                .map(Degree::getSigla).distinct().collect(Collectors.joining(separator));
    }

    @Atomic(mode = TxMode.WRITE)
    public Boolean changeResponsibleFor(Professorship professorship, Boolean responsibleFor) {
        if (responsibleFor != null && !professorship.getResponsibleFor().equals(responsibleFor)) {
            professorship.setResponsibleFor(responsibleFor);
        }

        return professorship.getResponsibleFor();
    }

    public List<ExecutionDegree> getDegrees(ExecutionSemester period) {
        if (period == null) {
            return ImmutableList.<ExecutionDegree> builder().build();
        }
        return period.getExecutionYear().getExecutionDegreesSet().stream()
                .sorted(ExecutionDegree.EXECUTION_DEGREE_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME).distinct()
                .collect(Collectors.toList());
    }

    public List<ExecutionCourse> getCourses(ExecutionDegree executionDegree, ExecutionSemester period) {
        if (executionDegree == null) {
            return ImmutableList.<ExecutionCourse> builder().build();
        }
        return executionDegree.getDegreeCurricularPlan().getExecutionCoursesByExecutionPeriod(period).stream().distinct()
                .sorted(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR).collect(Collectors.toList());
    }

    public Professorship create(CreateProfessorshipBean bean) throws DomainException {
        return Professorship.create(bean.getResponsibleFor(), bean.getCourse(), bean.getTeacher());
    }

    @Atomic(mode = TxMode.WRITE)
    public void deleteProfessorship(Professorship professorship) throws DomainException {
        professorship.delete();
    }

    /***
     * Write to outputstream csv file with {@link TeacherAuthorization} specified by the filter bean
     *
     * @param search filter bean
     * @param out the outputstream to dump the CSV to
     * @throws IOException if write to the outputstream fails
     */
    public void dumpCSV(SearchBean search, OutputStream out) throws IOException {

        SpreadsheetBuilder builder = new SpreadsheetBuilder();

        Set<Professorship> professorships = authorizationService.searchAuthorizations
                (search).stream().flatMap(a -> getProfessorships(a.getTeacher().getPerson().getUser(),search.getPeriod()).stream())
                .collect(Collectors.toSet());

        builder.addSheet(getSheetName(search), new SheetData<Professorship>(professorships) {

            @Override
            protected void makeLine(Professorship item) {
                final User user = item.getTeacher().getPerson().getUser();
                final ExecutionCourse course = item.getExecutionCourse();

                addCell(message("teacher.professorships.csv.column.1.username"), user.getUsername());
                addCell(message("teacher.professorships.csv.column.2.name"), user.getProfile().getDisplayName());
                addCell(message("teacher.professorships.csv.column.3.courseCode"), GepReportFile.getExecutionCourseCode(course));
                addCell(message("teacher.professorships.csv.column.4.courseName"), course.getNome());
                addCell(message("teacher.professorships.csv.column.5.semester"), GepReportFile.getExecutionSemesterCode(course
                        .getExecutionPeriod()));
                addCell(message("teacher.authorizations.csv.column.6.responsible"), item.isResponsibleFor() ? "Y" : "N");
            }
        });

        builder.build(WorkbookExportFormat.CSV, out);
    }

    /**
     * get the CSV sheet name when exporting existing teacher professorships
     *
     * @param filter bean
     * @return
     */
    public String getSheetName(SearchBean search) {
        List<String> parts = Lists.newArrayList("teacherProfessorships");
        Department department = search.getDepartment();
        parts.add(department != null? department.getAcronym():message("label.all"));
        ExecutionSemester period = search.getPeriod();
        if(period!= null) {
            parts.add(period.getQualifiedName().replace(" ", "_"));
        }
        return Joiner.on("_").join(parts);
    }

    public String getCsvFilename(SearchBean search) {
        return getSheetName(search);
    }
}

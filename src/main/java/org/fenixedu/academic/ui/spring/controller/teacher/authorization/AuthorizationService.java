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
package org.fenixedu.academic.ui.spring.controller.teacher.authorization;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherAuthorization;
import org.fenixedu.academic.domain.TeacherCategory;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.commons.spreadsheet.SheetData;
import org.fenixedu.commons.spreadsheet.SpreadsheetBuilder;
import org.fenixedu.commons.spreadsheet.WorkbookExportFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/***
 * Teacher authorization and categories service
 * 
 * This service provides methods to manage teacher authorizations and categories.
 * 
 * @author Sérgio Silva (sergio.silva@tecnico.ulisboa.pt)
 *
 */
@Service
public class AuthorizationService {

    @Autowired
    private CsvService csvService;

    @Autowired
    private MessageSource messageSource;

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

    /***
     * Get all active deparments
     * 
     * @return {@link Department}
     */
    public List<Department> getDepartments() {
        return Department.readActiveDepartments();
    }

    /***
     * Get all teacher categories
     * 
     * @return
     */
    public List<TeacherCategory> getCategories() {
        return Bennu.getInstance().getTeacherCategorySet().stream().distinct().sorted().collect(Collectors.toList());
    }

    /***
     * Get all execution periods order by semester and year in descending order (most recent to oldest)
     * 
     * @return
     */
    public List<ExecutionSemester> getExecutionPeriods() {
        return Bennu.getInstance().getExecutionPeriodsSet().stream().distinct()
                .sorted(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR.reversed()).collect(Collectors.toList());
    }

    /***
     * Get all execution periods that the teacher is authorized to
     * 
     * @param teacher
     * @return
     */
    public List<ExecutionSemester> getExecutionPeriods(Teacher teacher) {
        return teacher.getTeacherAuthorizationStream().map(TeacherAuthorization::getExecutionSemester).distinct()
                .sorted(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR.reversed()).collect(Collectors.toList());
    }

    /***
     * Get the current execution period
     * 
     * @return
     */
    public ExecutionSemester getCurrentPeriod() {
        return ExecutionSemester.readActualExecutionSemester();
    }

    /***
     * Create new teacher authorization using the bean
     * 
     * @param bean
     * @return
     */
    public TeacherAuthorization createTeacherAuthorization(FormBean bean) {
        return createTeacherAuthorization(bean.getUser(), bean.getDepartment(), bean.getPeriod(), bean.getCategory(),
                bean.getContracted(), bean.getLessonHours());
    }

    /***
     * Create new teacher authorization in one atomic operation.
     * Creates the teacher if the user doesn't have one
     * 
     * @param user The user
     * @param department The associated deparment
     * @param semester The period
     * @param category The teacher category
     * @param contracted true if the teacher is contracted, false if otherwise
     * @param lessonHours the number of hours the teacher must teacher
     * @return the created {@link TeacherAuthorization} object
     * 
     */

    @Atomic(mode = TxMode.WRITE)
    public TeacherAuthorization createTeacherAuthorization(User user, Department department, ExecutionSemester semester,
            TeacherCategory category, Boolean contracted, Double lessonHours) {

        Teacher teacher;

        if (user.getPerson().getTeacher() == null) {
            teacher = new Teacher(user.getPerson());
        } else {
            teacher = user.getPerson().getTeacher();
        }

        return TeacherAuthorization.createOrUpdate(teacher, department, semester, category, contracted, lessonHours);
    }

    /***
     * Creates a new teacher category.
     * 
     * @param code
     * @param name
     * @param weight The degree of importance (used by the comparator)
     * @return
     */

    @Atomic(mode = TxMode.WRITE)
    public TeacherCategory createTeacherCategory(String code, LocalizedString name, Integer weight) {
        return new TeacherCategory(code, name, weight);
    }

    private Stream<TeacherAuthorization> getAuthorizations(Department department) {

        if (department == null) {
            return Bennu.getInstance().getTeacherAuthorizationSet().stream();
        }

        return department.getTeacherAuthorizationStream();

    }

    /***
     * Get all revoked teacher authorizations ordered descendingly by revoke time.
     * 
     * @return
     */
    public List<TeacherAuthorization> getRevokedAuthorizations() {
        Comparator<TeacherAuthorization> byRevokeTime = (a1, a2) -> {
            return a1.getRevokeTime().compareTo(a2.getRevokeTime());
        };

        return Bennu.getInstance().getRevokedTeacherAuthorizationSet().stream().distinct().sorted(byRevokeTime.reversed())
                .collect(Collectors.toList());
    }

    /**
     * Get all valid teacher authorizations for the specific {@link Department} and {@link ExecutionSemester}
     * 
     * @param search {@link Department} and {@link ExecutionSemester} filter
     * @return
     */
    public List<TeacherAuthorization> searchAuthorizations(final SearchBean search) {
        return getAuthorizations(search.getDepartment()).filter(t -> t.getExecutionSemester().equals(search.getPeriod()))
                .distinct().collect(Collectors.toList());
    }

    /***
     * Revokes the teacher authorization
     * 
     * @param authorization
     */

    @Atomic(mode = TxMode.WRITE)
    public void revoke(TeacherAuthorization authorization) {
        authorization.revoke();
    }

    /***
     * Creates a new {@link TeacherCategory}
     * 
     * @param form
     */
    @Atomic(mode = TxMode.WRITE)
    public void createCategory(CategoryBean form) {
        new TeacherCategory(form.getCode(), form.getName(), form.getWeight());
    }

    /***
     * Edits {@link TeacherCategory} instance
     * 
     * @param category
     * @param form
     */

    @Atomic(mode = TxMode.WRITE)
    public void editCategory(TeacherCategory category, CategoryBean form) {
        category.setCode(form.getCode());
        category.setName(form.getName());
        category.setWeight(form.getWeight());
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

        builder.addSheet(getSheetName(search), new SheetData<TeacherAuthorization>(searchAuthorizations(search)) {

            @Override
            protected void makeLine(TeacherAuthorization item) {
                final User user = item.getTeacher().getPerson().getUser();

                addCell(message("teacher.authorizations.csv.column.1.username"), user.getUsername());
                addCell(message("teacher.authorizations.csv.column.2.categoryCode"), item.getTeacherCategory().getCode());
                addCell(message("teacher.authorizations.csv.column.3.departmentAcronym"), item.getDepartment().getAcronym());
                addCell(message("teacher.authorizations.csv.column.4.lessonHours"), item.getLessonHours());
                addCell(message("teacher.authorizations.csv.column.5.contracted"), item.isContracted() ? "Y" : "N");
                addCell(message("teacher.authorizations.displayname"), user.getProfile().getDisplayName());
                addCell(message("teacher.authorizations.category"), item.getTeacherCategory().getName().getContent());
                addCell(message("teacher.authorizations.department"), item.getDepartment().getNameI18n().getContent());
                addCell(message("teacher.authorizations.period"), item.getExecutionSemester().getQualifiedName());
                addCell(message("teacher.authorizations.authorized"), item.getAuthorizer() == null ? "" : String.format("%s (%s)",
                        item.getAuthorizer().getProfile().getDisplayName(), item.getAuthorizer().getUsername()));
            }
        });

        builder.build(WorkbookExportFormat.CSV, out);
    }

    public String getCsvFilename(SearchBean search) {
        return getSheetName(search) + ".csv";
    };

    /**
     * get the CSV sheet name when exporting existing teacher authorizations
     * 
     * @param search bean
     * @return
     */
    public String getSheetName(SearchBean search) {
        String department = search.getDepartment() == null ? message("label.all") : search.getDepartment().getAcronym();
        String period = search.getPeriod().getQualifiedName().replace(" ", "_");
        return Joiner.on("_").join("teacherAuthorizations", department, period);
    }

    /***
     * Importation of teacher authorizations from a CSV file
     * 
     * @param period the period where to import
     * @param partFile the CSV file
     * @return
     */
    public List<TeacherAuthorization> importCSV(ExecutionSemester period, MultipartFile partFile) {
        try {
            return importAuthorizations(period, csvService.readCsvFile(partFile.getInputStream(), ",", Charsets.UTF_8.toString()));
        } catch (IOException e) {
            throw new RuntimeException(message("teacher.authorizations.upload.parsing.failed"));
        }
    }

    /***
     * Batch import of teacher authorizations
     * Ignores empty lines.
     * 
     * @param period the {@link ExecutionSemester} where to import
     * @param authorizationEntries list where each element is a map of the column and the value
     * @throws RuntimeException if can't convert a specified value for a specific element
     * @return the list of {@link TeacherAuthorization} created objects
     */
    @Atomic(mode = TxMode.WRITE)
    private List<TeacherAuthorization> importAuthorizations(ExecutionSemester period,
            List<Map<String, String>> authorizationEntries) {
        return authorizationEntries
                .stream()
                .map(auth -> {
                    String username = auth.get(message("teacher.authorizations.csv.column.1.username"));
                    String categoryCode = auth.get(message("teacher.authorizations.csv.column.2.categoryCode"));
                    String departmentAcronym = auth.get(message("teacher.authorizations.csv.column.3.departmentAcronym"));
                    String hoursValue = auth.get(message("teacher.authorizations.csv.column.4.lessonHours"));
                    String contractedValue = auth.get(message("teacher.authorizations.csv.column.5.contracted"));

                    if (Strings.isNullOrEmpty(username) && Strings.isNullOrEmpty(categoryCode)
                            && Strings.isNullOrEmpty(departmentAcronym) && Strings.isNullOrEmpty(hoursValue)
                            && Strings.isNullOrEmpty(contractedValue)) {
                        // ignore empty line
                        return Optional.<TeacherAuthorization> empty();
                    }

                    User user = User.findByUsername(username);

                    if (user == null) {
                        throw new RuntimeException(message("teacher.authorizations.csv.column.1.username.error", username));
                    }

                    Optional<TeacherCategory> category = TeacherCategory.findByCode(categoryCode);

                    if (!category.isPresent()) {
                        throw new RuntimeException(
                                message("teacher.authorizations.csv.column.2.categoryCode.error", categoryCode));
                    }

                    Department department = Department.find(departmentAcronym);

                    if (department == null) {
                        throw new RuntimeException(message("teacher.authorizations.csv.column.3.departmentAcronym.error",
                                departmentAcronym));
                    }
                    Double lessonHours;

                    try {
                        lessonHours = Double.parseDouble(hoursValue);
                    } catch (NumberFormatException nfe) {
                        throw new RuntimeException(message("teacher.authorizations.csv.column.4.lessonHours.error", hoursValue));
                    }

                    if (!"Y".equals(contractedValue) && !"N".equals(contractedValue)) {
                        throw new RuntimeException(message("teacher.authorizations.csv.column.5.contracted.error",
                                contractedValue));
                    }

                    Boolean contracted = new Boolean("Y".equals(contractedValue));

                    return Optional.<TeacherAuthorization> of(createTeacherAuthorization(user, department, period,
                            category.get(), contracted, lessonHours));

                }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }
}

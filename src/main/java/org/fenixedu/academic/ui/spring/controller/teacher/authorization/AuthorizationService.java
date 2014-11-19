package org.fenixedu.academic.ui.spring.controller.teacher.authorization;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.Charsets;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherAuthorization;
import org.fenixedu.academic.domain.TeacherCategory;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

@Service
public class AuthorizationService {

    @Autowired
    private CsvService csvService;

    @Autowired
    private MessageSource messageSource;

    private String message(String code, Object... args) {
        return messageSource.getMessage(code, args, I18N.getLocale());
    }

    public List<Department> getDepartments() {
        return Department.readActiveDepartments();
    }

    public List<TeacherCategory> getCategories() {
        return Bennu.getInstance().getTeacherCategorySet().stream().sorted().collect(Collectors.toList());
    }

    public List<ExecutionSemester> getExecutionPeriods() {
        return Bennu.getInstance().getExecutionPeriodsSet().stream()
                .sorted(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR.reversed()).collect(Collectors.toList());
    }

    public TeacherAuthorization createTeacherAuthorization(FormBean bean) {
        return createTeacherAuthorization(bean.getUser(), bean.getDepartment(), bean.getPeriod(), bean.getCategory(),
                bean.getContracted(), bean.getLessonHours());
    }

    @Atomic(mode = TxMode.WRITE)
    public TeacherAuthorization createTeacherAuthorization(User user, Department department, ExecutionSemester semester,
            TeacherCategory category, Boolean contracted, Double lessonHours) {

        Teacher teacher;

        if (user.getPerson().getTeacher() == null) {
            teacher = new Teacher(user.getPerson());
        }

        teacher = user.getPerson().getTeacher();

        return TeacherAuthorization.createOrUpdate(teacher, department, semester, category, contracted, lessonHours);
    }

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

    public List<TeacherAuthorization> getRevokedAuthorizations() {
        Comparator<TeacherAuthorization> byRevokeTime = (a1, a2) -> {
            return a1.getRevokeTime().compareTo(a2.getRevokeTime());
        };

        return Bennu.getInstance().getRevokedTeacherAuthorizationSet().stream().sorted(byRevokeTime.reversed())
                .collect(Collectors.toList());
    }

    public List<TeacherAuthorization> searchAuthorizations(final FormBean search) {
        return getAuthorizations(search.getDepartment()).filter(t -> t.getExecutionSemester().equals(search.getPeriod()))
                .collect(Collectors.toList());
    }

    @Atomic(mode = TxMode.WRITE)
    public void revoke(TeacherAuthorization authorization) {
        authorization.revoke();
    }

    @Atomic(mode = TxMode.WRITE)
    public void createCategory(CategoryBean form) {
        new TeacherCategory(form.getCode(), form.getName(), form.getWeight());
    }

    @Atomic(mode = TxMode.WRITE)
    public void editCategory(TeacherCategory category, CategoryBean form) {
        category.setCode(form.getCode());
        category.setName(form.getName());
        category.setWeight(form.getWeight());
    }

    public void dumpCSV(FormBean search, OutputStream out) throws IOException {

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
                addCell(message("teacher.authorizations.authorized"), String.format("%s (%s)", item.getAuthorizer().getProfile()
                        .getDisplayName(), item.getAuthorizer().getUsername()));
            }
        });

        builder.build(WorkbookExportFormat.CSV, out);
    }

    public String getCsvFilename(FormBean search) {
        return getSheetName(search);
    };

    public String getSheetName(FormBean search) {
        String department =
                search.getDepartment() == null ? message("teacher.authorizations.department.all") : search.getDepartment()
                        .getAcronym();
        String period = search.getPeriod().getQualifiedName().replace(" ", "_");
        return Joiner.on("_").join("teacherAuthorizations", department, period);
    }

    public List<TeacherAuthorization> importCSV(ExecutionSemester period, MultipartFile partFile) {
        try {
            return importAuthorizations(period, csvService.readCsvFile(partFile.getInputStream(), ",", Charsets.UTF_8.toString()));
        } catch (IOException e) {
            throw new RuntimeException(message("teacher.authorizations.upload.parsing.failed"));
        }
    }

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

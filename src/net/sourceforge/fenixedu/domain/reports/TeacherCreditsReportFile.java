package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.credits.util.AnnualTeachingCreditsBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;

import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TeacherCreditsReportFile extends TeacherCreditsReportFile_Base {

    private static final String EMPTY_CELL = "-";

    public TeacherCreditsReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de serviço de docência do IST";
    }

    @Override
    protected String getPrefix() {
        return "Listagem de serviço de docência do IST";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
        ExecutionYear executionYear = getExecutionYear();
        spreadsheet.setName("Docentes do IST " + executionYear.getQualifiedName().replace("/", ""));
        spreadsheet.setHeader("IstId");
        spreadsheet.setHeader("Nº Mec");
        spreadsheet.setHeader("Nome");
        spreadsheet.setHeader("Semestre");
        spreadsheet.setHeader("Categoria");
        spreadsheet.setHeader("Situação");
        spreadsheet.setHeader("Regime");
        spreadsheet.setHeader("Docente de carreira");
        spreadsheet.setHeader("Departamento - último");
        spreadsheet.setHeader("Departamento - dominante");
        spreadsheet.setHeader("CLE");
        spreadsheet.setHeader("CL");
        spreadsheet.setHeader("CG");
        spreadsheet.setHeader("O");
        spreadsheet.setHeader("AD65");
        spreadsheet.setHeader("SNE");
        spreadsheet.setHeader("CLN");
        //1º sem
        spreadsheet.setHeader("COT");
        spreadsheet.setHeader("COD");
        spreadsheet.setHeader("COM");
        //2º sem
        spreadsheet.setHeader("CO");
        spreadsheet.setHeader("CF");
        spreadsheet.setHeader("CLA");

        List<Teacher> teachers = RootDomainObject.getInstance().getTeachers();
        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            Interval semesterInterval =
                    new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                            executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
            for (Teacher teacher : teachers) {
                boolean isContractedTeacher = teacher.isActiveForSemester(executionSemester);
                TeacherAuthorization teacherAuthorization = teacher.getTeacherAuthorization(executionSemester);
                if (isContractedTeacher || teacherAuthorization != null) {
                    final Row row = spreadsheet.addRow();
                    row.setCell(teacher.getPerson().getUsername());
                    row.setCell(teacher.getPerson().getEmployee() != null ? teacher.getPerson().getEmployee().getEmployeeNumber() : null);
                    row.setCell(teacher.getPerson().getName());
                    row.setCell(executionSemester.getName());
                    ProfessionalCategory category = null;
                    PersonContractSituation situation = null;
                    ProfessionalRegime regime = null;
                    if (isContractedTeacher) {
                        category = teacher.getCategoryByPeriod(executionSemester);
                        situation =
                                teacher.getCurrentOrLastTeacherContractSituation(executionSemester.getBeginDateYearMonthDay()
                                        .toLocalDate(), executionSemester.getEndDateYearMonthDay().toLocalDate());
                        regime = getProfessionalRegime(situation, semesterInterval);
                    } else if (teacherAuthorization != null) {
                        category = teacherAuthorization.getProfessionalCategory();
                    }
                    row.setCell(category == null ? null : category.getName().getContent());
                    row.setCell(situation == null ? null : situation.getContractSituation().getName().getContent());

                    row.setCell(regime == null ? null : regime.getName().getContent());
                    row.setCell(teacher.isTeacherProfessorCategory(executionSemester) ? "S" : "N");
                    Department lastWorkingDepartment =
                            teacher.getLastWorkingDepartment(executionSemester.getBeginDateYearMonthDay(),
                                    executionSemester.getEndDateYearMonthDay());
                    row.setCell(lastWorkingDepartment == null ? null : lastWorkingDepartment.getName());
                    Department creditsDepartment = getCreditsDepartment(teacher, executionSemester);
                    row.setCell(creditsDepartment == null ? null : creditsDepartment.getName());

                    TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
                    row.setCell(teacherService == null ? 0 : teacherService.getTeachingDegreeHours());// CLE
                    row.setCell(teacherService == null ? 0 : teacherService.getTeachingDegreeCredits());// CL

                    row.setCell(teacher.getManagementFunctionsCredits(executionSemester)); // CG
                    //CG (desc)
                    row.setCell(teacherService == null ? 0 : teacherService.getOtherServiceCredits());// O
                    //O (desc)
                    Double reductionService =
                            teacherService == null ? 0 : teacherService.getReductionService() == null ? 0 : teacherService
                                    .getReductionService().getCreditsReduction() == null ? 0 : teacherService
                                    .getReductionService().getCreditsReduction().doubleValue();
                    row.setCell(reductionService);// AD65

                    row.setCell(teacher.getServiceExemptionCredits(executionSemester)); //SNE
                    //SNE Desc
                    row.setCell(teacher.getMandatoryLessonHours(executionSemester)); //CLN

                    AnnualTeachingCreditsBean annualTeachingCreditsBean =
                            new AnnualTeachingCreditsBean(executionYear, teacher, RoleType.SCIENTIFIC_COUNCIL);
                    annualTeachingCreditsBean.calculateCredits();
                    if (executionSemester.getSemester() == 1) {
                        row.setCell(annualTeachingCreditsBean.getProjectsTutorialsCredits());//COT
                        row.setCell(annualTeachingCreditsBean.getPhdDegreeThesesCredits());//COD
                        row.setCell(annualTeachingCreditsBean.getMasterDegreeThesesCredits());//COM
                        row.setCell(EMPTY_CELL);//CO
                        row.setCell(EMPTY_CELL);//CF
                        row.setCell(EMPTY_CELL);//CLA
                    } else {
                        row.setCell(EMPTY_CELL);//COT
                        row.setCell(EMPTY_CELL);//COD
                        row.setCell(EMPTY_CELL);//COM
                        row.setCell(annualTeachingCreditsBean.getYearCredits());//CO
                        row.setCell(annualTeachingCreditsBean.getFinalCredits());//CF
                        row.setCell(annualTeachingCreditsBean.getAccumulatedCredits());//CLA			
                    }

                    row.setCell(getMasterDegreeThesesNumber(executionYear, teacher));

                }
            }
        }

        spreadsheet.exportToXLSSheet(new File("Serviço_Docência_ " + executionYear.getQualifiedName().replace("/", "") + ".xls"));
    }

    public int getMasterDegreeThesesNumber(ExecutionYear executionYear, Teacher teacher) {
        int total = 0;
        if (!executionYear.getYear().equals("2011/2012")) {
            for (ThesisEvaluationParticipant participant : teacher.getPerson().getThesisEvaluationParticipants()) {
                Thesis thesis = participant.getThesis();
                if (thesis.isEvaluated()
                        && thesis.hasFinalEnrolmentEvaluation()
                        && thesis.getEvaluation().getYear() == executionYear.getBeginCivilYear()
                        && (participant.getType() == ThesisParticipationType.ORIENTATOR || participant.getType() == ThesisParticipationType.COORIENTATOR)) {
                    total++;
                }
            }
        }
        return total;
    }

    private ProfessionalRegime getProfessionalRegime(PersonContractSituation teacherContractSituation, Interval interval) {
        GiafProfessionalData giafProfessionalData =
                teacherContractSituation != null ? teacherContractSituation.getGiafProfessionalData() : null;
        PersonProfessionalData personProfessionalData =
                giafProfessionalData != null ? giafProfessionalData.getPersonProfessionalData() : null;
        return personProfessionalData != null ? personProfessionalData.getDominantProfessionalRegime(giafProfessionalData,
                interval, CategoryType.TEACHER) : null;
    }

    private Department getCreditsDepartment(Teacher teacher, ExecutionSemester executionSemester) {

        TeacherAuthorization teacherAuthorization = teacher.getTeacherAuthorization(executionSemester);
        if (teacherAuthorization != null && teacherAuthorization instanceof ExternalTeacherAuthorization) {
            return ((ExternalTeacherAuthorization) teacherAuthorization).getDepartment();
        }

        Employee employee = teacher.getPerson().getEmployee();
        if (employee != null) {
            List<Contract> workingContracts =
                    employee.getWorkingContracts(executionSemester.getBeginDateYearMonthDay(),
                            executionSemester.getEndDateYearMonthDay());

            Contract mostSignificantContract = null;
            int mostSignificantContractDays = 0;
            for (Contract contract : workingContracts) {
                if (contract.getUnit().getDepartmentUnit() != null) {
                    int contractDays = getDaysIn(contract, executionSemester);
                    if (mostSignificantContract == null || contractDays > mostSignificantContractDays) {
                        mostSignificantContractDays = contractDays;
                        mostSignificantContract = contract;
                    }
                }
            }
            return mostSignificantContract != null ? mostSignificantContract.getUnit().getDepartmentUnit().getDepartmentUnit()
                    .getDepartment() : null;
        }
        return null;
    }

    private int getDaysIn(Contract contract, ExecutionSemester executionSemester) {
        YearMonthDay begin =
                contract.getBeginDate().isBefore(executionSemester.getBeginDateYearMonthDay()) ? executionSemester
                        .getBeginDateYearMonthDay() : contract.getBeginDate();
        YearMonthDay end =
                contract.getEndDate() == null || contract.getEndDate().isAfter(executionSemester.getEndDateYearMonthDay()) ? executionSemester
                        .getEndDateYearMonthDay() : contract.getEndDate();
        return new Interval(begin.toLocalDate().toDateTimeAtStartOfDay(), end.toLocalDate().toDateTimeAtStartOfDay()).toPeriod(
                PeriodType.days()).getDays() + 1;
    }

}

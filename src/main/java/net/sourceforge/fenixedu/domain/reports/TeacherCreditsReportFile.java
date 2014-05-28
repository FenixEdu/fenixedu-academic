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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.credits.util.AnnualTeachingCreditsBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;

import org.apache.commons.lang.CharSetUtils;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
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
        return "Listagem de serviço de docência do " + Unit.getInstitutionAcronym();
    }

    @Override
    protected String getPrefix() {
        return "Listagem de serviço de docência do " + Unit.getInstitutionAcronym();
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
        ExecutionYear executionYear = getExecutionYear();
        spreadsheet.setName("Docentes do " + Unit.getInstitutionAcronym() + " "
                + executionYear.getQualifiedName().replace("/", ""));
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
        spreadsheet.setHeader("CLE - correcções");
        spreadsheet.setHeader("CL");
        spreadsheet.setHeader("CG");
        spreadsheet.setHeader("O");
        spreadsheet.setHeader("AD65 requerido");
        spreadsheet.setHeader("AD65 atribuído");
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
        spreadsheet.setHeader("SNE - Descrição");
        spreadsheet.setHeader("O - Descrição");

        Collection<Teacher> teachers = Bennu.getInstance().getTeachersSet();
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

                    row.setCell(teacherService == null ? 0 : teacherService.getTeachingDegreeCorrections());// CLE corrections

                    row.setCell(teacherService == null ? 0 : teacherService.getTeachingDegreeCredits());// CL

                    row.setCell(teacher.getManagementFunctionsCredits(executionSemester)); // CG
                    //CG (desc)
                    row.setCell(teacherService == null ? 0 : teacherService.getOtherServiceCredits());// O
                    Double creditsReductionRequired =
                            teacherService == null ? null : teacherService.getReductionService() == null ? null : teacherService
                                    .getReductionService().getCreditsReduction() == null ? null : teacherService
                                    .getReductionService().getCreditsReduction().doubleValue();

                    Double creditsReductionAttributed =
                            teacherService == null ? null : teacherService.getReductionService() == null ? null : teacherService
                                    .getReductionService().getCreditsReductionAttributed() == null ? null : teacherService
                                    .getReductionService().getCreditsReductionAttributed().doubleValue();
                    row.setCell(creditsReductionRequired);// AD65 requerido
                    row.setCell(creditsReductionAttributed);// AD65 atribuído

                    row.setCell(teacher.getServiceExemptionCredits(executionSemester)); //SNE

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
                    row.setCell(getServiceExemptionDescription(executionSemester, teacher)); //SNE Desc
                    row.setCell(teacherService == null ? EMPTY_CELL : getOthersDesciption(teacherService));//O (desc)
                }
            }
        }
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

    private String getOthersDesciption(TeacherService teacherService) {
        List<String> others = new ArrayList<String>();
        for (OtherService otherService : teacherService.getOtherServices()) {
            others.add(CharSetUtils.delete(otherService.getReason(), "\r\n") + " (" + otherService.getCredits() + " créditos)");
        }
        return StringUtils.join(others, ", ");
    }

    public String getServiceExemptionDescription(ExecutionSemester executionSemester, Teacher teacher) {
        Set<PersonContractSituation> personProfessionalExemptions = teacher.getValidTeacherServiceExemptions(executionSemester);
        List<String> serviceExemption = new ArrayList<String>();

        for (PersonContractSituation personContractSituation : personProfessionalExemptions) {
            serviceExemption.add(personContractSituation.getContractSituation().getName().getContent());
        }

        return StringUtils.join(serviceExemption, ", ");
    }
}

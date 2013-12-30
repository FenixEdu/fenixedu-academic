package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import org.apache.commons.lang.StringUtils;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

@Task(englishTitle = "ImportEmployeeUnitsFromGiaf")
public class ImportEmployeeUnitsFromGiaf extends CronTask {
    public ImportEmployeeUnitsFromGiaf() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportEmployeeUnitsFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<Integer, Employee> employeesMap = getEmployeesMap();
            LocalDate today = new LocalDate();

            String query = getEmployeeWorkingUnitsQuery();
            getLogger().debug(query);
            importIt(oracleConnection, employeesMap, today, query, AccountabilityTypeEnum.WORKING_CONTRACT);

            query = getEmployeeMailingUnitsQuery();
            getLogger().debug(query);
            importIt(oracleConnection, employeesMap, today, query, AccountabilityTypeEnum.MAILING_CONTRACT);

            oracleConnection.closeConnection();
        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportEmployeeUnitsFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportEmployeeUnitsFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private void importIt(PersistentSuportGiaf oracleConnection, Map<Integer, Employee> employeesMap, LocalDate today,
            String query, AccountabilityTypeEnum accountabilityTypeEnum) throws SQLException, ExcepcaoPersistencia {
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            String numberString = result.getString("emp_num");
            Integer employeeNumber = null;
            try {
                employeeNumber = Integer.parseInt(numberString);
            } catch (NumberFormatException ex) {
                getLogger().info(accountabilityTypeEnum.getName() + ". Erro a ler número: " + numberString);
                continue;
            }
            Employee employee = employeesMap.get(employeeNumber);
            if (employee == null) {
                getLogger().info(accountabilityTypeEnum.getName() + ". Não existe funcionário. Número: " + employeeNumber);
                continue;
            }
            Integer costCenterCode = 0;
            try {
                costCenterCode = result.getInt("cc");
            } catch (SQLException e) {
                getLogger().info(accountabilityTypeEnum.getName() + ". CC inválido. Número: " + employeeNumber);
                continue;
            }
            Unit unit = costCenterCode.intValue() == 0 ? null : Unit.readByCostCenterCode(costCenterCode);

            String ccDateString = result.getString("cc_date");
            LocalDate beginDate = today;
            if (!StringUtils.isEmpty(ccDateString)) {
                beginDate = new LocalDate(Timestamp.valueOf(ccDateString));
            }

            if (unit != null || costCenterCode.intValue() == 0) {
                if (employee.getPerson().getPersonProfessionalData() != null) {
                    GiafProfessionalData giafProfessionalDataByGiafPersonIdentification =
                            employee.getPerson().getPersonProfessionalData()
                                    .getGiafProfessionalDataByGiafPersonIdentification(numberString);
                    ContractSituation contractSituation = giafProfessionalDataByGiafPersonIdentification.getContractSituation();

                    if (contractSituation != null) {

                        LocalDate endDate = null;
                        if (contractSituation.getEndSituation()) {
                            PersonContractSituation personContractSituation =
                                    getOtherValidPersonContractSituation(giafProfessionalDataByGiafPersonIdentification);
                            if (personContractSituation != null) {
                                contractSituation = personContractSituation.getContractSituation();
                                endDate = personContractSituation.getEndDate();
                            } else {
                                endDate = giafProfessionalDataByGiafPersonIdentification.getContractSituationDate();
                            }
                        }

                        Contract workingContractOnDate =
                                getLastContractByContractType(employee, accountabilityTypeEnum, new YearMonthDay(beginDate),
                                        endDate == null ? null : new YearMonthDay(endDate));
                        Boolean isTeacher = employee.getPerson().hasRole(RoleType.TEACHER);

                        if (workingContractOnDate != null) {
                            if (unit != null && endDate == null) {
                                if (!equalUnitAndCategoryType(workingContractOnDate, unit, isTeacher)) {
                                    createEmployeeContract(employee, new YearMonthDay(beginDate), null, unit,
                                            accountabilityTypeEnum, workingContractOnDate, isTeacher);
                                } else if (!workingContractOnDate.getBeginDate().equals(beginDate)
                                        && !StringUtils.isEmpty(ccDateString)) {
                                    changeEmployeeContractDates(accountabilityTypeEnum, new YearMonthDay(beginDate),
                                            workingContractOnDate);
                                } else if (workingContractOnDate.getEndDate() != null && contractSituation != null
                                        && giafProfessionalDataByGiafPersonIdentification.getContractSituationDate() != null) {
                                    getLogger().info(
                                            accountabilityTypeEnum.getName() + ". Contrato do Funcionário:" + employeeNumber
                                                    + " Voltou a abrir na mesma unidade :"
                                                    + giafProfessionalDataByGiafPersonIdentification.getContractSituationDate());
                                    createEmployeeContract(employee, new YearMonthDay(
                                            giafProfessionalDataByGiafPersonIdentification.getContractSituationDate()), null,
                                            unit, accountabilityTypeEnum, null, isTeacher);
                                } else {
                                    getLogger().info(
                                            accountabilityTypeEnum.getName() + ". Não há alterações para o funcionário:"
                                                    + employeeNumber);
                                }
                            } else if (endDate != null) {
                                // terminou o contrato corrente
                                closeCurrentContract(accountabilityTypeEnum, workingContractOnDate,
                                        new YearMonthDay(endDate.minusDays(1)));
                            }
                        } else {
                            if (unit != null && endDate == null) {
                                // contrato novo
                                createEmployeeContract(employee, new YearMonthDay(beginDate), null, unit, accountabilityTypeEnum,
                                        null, isTeacher);

                            } else {
                                // já tinha terminado o contrato e já
                                // terminou
                                // tb do nosso lado
                                // getLogger().info(accountabilityTypeEnum.getName()
                                // + ". Contrato terminado na unidade "
                                // + unit.getName() + " para o funcionário:"
                                // + employeeNumber + " (não fez nada)");
                            }
                        }
                    } else {
                        getLogger().info("ERRO... não tem situação no GIAF " + employeeNumber);
                        Contract currentWorkingContract = employee.getCurrentContractByContractType(accountabilityTypeEnum);
                        if (currentWorkingContract != null) {
                            LocalDate endDate = today.minusDays(1);
                            closeCurrentContract(accountabilityTypeEnum, currentWorkingContract, new YearMonthDay(endDate));
                            getLogger().info(
                                    accountabilityTypeEnum.getName() + ". Não não tem contrato no GIAF, e terminamos no Fénix: "
                                            + employeeNumber);
                        } else {
                            getLogger().info(
                                    accountabilityTypeEnum.getName() + ". Não não tem contrato no GIAF, nem no fénix: "
                                            + employeeNumber);
                        }
                    }
                } else {
                    getLogger().info("Não tem employeeProfessionalData. Funcionario: " + employeeNumber);
                }
            } else {
                getLogger().info(
                        accountabilityTypeEnum.getName() + ". Não existe unidade: " + costCenterCode + " para o funcionario: "
                                + employeeNumber);
            }
        }
        result.close();
        preparedStatement.close();
    }

    private PersonContractSituation getOtherValidPersonContractSituation(
            GiafProfessionalData giafProfessionalDataByGiafPersonIdentification) {
        for (PersonContractSituation personContractSituation : giafProfessionalDataByGiafPersonIdentification
                .getValidPersonContractSituations()) {
            if (personContractSituation.getBeginDate().equals(
                    giafProfessionalDataByGiafPersonIdentification.getContractSituationDate())) {
                return personContractSituation;
            }
        }
        return null;
    }

    private Contract getLastContractByContractType(Employee employee, AccountabilityTypeEnum contractType, YearMonthDay begin,
            YearMonthDay end) {
        if (end != null && end.isBefore(begin)) {
            end = begin;
        }
        YearMonthDay date = null;
        Contract contractToReturn = null;
        for (Contract contract : employee.getContractsByContractType(contractType, begin, end)) {
            if (date == null || contract.getBeginDate().isAfter(date)) {
                date = contract.getBeginDate();
                contractToReturn = contract;
            }
        }
        return contractToReturn;
    }

    private void changeEmployeeContractDates(AccountabilityTypeEnum accountabilityTypeEnum, YearMonthDay yearMonthDay,
            Contract currentWorkingContract) {
        YearMonthDay yearMonthDayMinusOne = yearMonthDay.minusDays(1);
        for (Contract contract : currentWorkingContract.getEmployee().getContractsByContractType(accountabilityTypeEnum,
                yearMonthDay, null)) {
            if (!currentWorkingContract.equals(contract)) {
                if (contract.getBeginDate().isBefore(yearMonthDay)) {
                    contract.setEndDate(yearMonthDayMinusOne);
                    getLogger().info(
                            accountabilityTypeEnum.getName() + ". Contrato terminado na unidade "
                                    + currentWorkingContract.getUnit().getCostCenterCode() + " para o funcionário:"
                                    + currentWorkingContract.getEmployee().getEmployeeNumber() + " . Mudou data de fim para :"
                                    + yearMonthDayMinusOne);
                } else {
                    contract.delete();
                    getLogger().info(
                            accountabilityTypeEnum.getName() + ". APAGOU para a unidade "
                                    + currentWorkingContract.getUnit().getCostCenterCode() + " para o funcionário:"
                                    + currentWorkingContract.getEmployee().getEmployeeNumber());
                }
            }
        }
        currentWorkingContract.setBeginDate(yearMonthDay);
        getLogger().info(
                accountabilityTypeEnum.getName() + " Mudou data de inicio: "
                        + currentWorkingContract.getUnit().getCostCenterCode() + " para o funcionário:"
                        + currentWorkingContract.getEmployee().getEmployeeNumber() + " . Mudou data de inicio para :"
                        + yearMonthDay);
    }

    private void closeCurrentContract(AccountabilityTypeEnum accountabilityTypeEnum, Contract currentWorkingContract,
            YearMonthDay endDate) {
        if (currentWorkingContract.getEndDate() != null && currentWorkingContract.getEndDate().isEqual(endDate)) {
            getLogger().info(
                    accountabilityTypeEnum.getName() + ". Já está fechado. Não há alterações para o funcionário:"
                            + currentWorkingContract.getEmployee().getEmployeeNumber());
        } else if (currentWorkingContract.getBeginDate().isAfter(endDate)) {
            getLogger().info(
                    accountabilityTypeEnum.getName() + ". APAGOU para a unidade "
                            + currentWorkingContract.getUnit().getCostCenterCode() + " para o funcionário:"
                            + currentWorkingContract.getEmployee().getEmployeeNumber());
            currentWorkingContract.delete();
        } else {
            currentWorkingContract.setEndDate(endDate);
            getLogger().info(
                    accountabilityTypeEnum.getName() + ". Contrato terminado na unidade "
                            + currentWorkingContract.getUnit().getCostCenterCode() + " para o funcionário:"
                            + currentWorkingContract.getEmployee().getEmployeeNumber() + " . Mudou data de fim para :" + endDate);
        }
    }

    private boolean equalUnitAndCategoryType(Contract currentWorkingContract, Unit unit, Boolean isTeacher) {
        if (currentWorkingContract.getUnit().equals(unit)) {
            Boolean teacherContract =
                    ((EmployeeContract) currentWorkingContract).getTeacherContract() == null ? false : ((EmployeeContract) currentWorkingContract)
                            .getTeacherContract();
            if (isTeacher.equals(teacherContract)) {
                return true;
            }
        }
        return false;
    }

    private void createEmployeeContract(Employee employee, YearMonthDay begin, YearMonthDay end, Unit unit,
            AccountabilityTypeEnum accountabilityTypeEnum, Contract currentWorkingContract, Boolean isTeacher) {
        if (currentWorkingContract != null) {
            closeCurrentContract(accountabilityTypeEnum, currentWorkingContract, begin.minusDays(1));
        }
        if (isTeacher) {
            getLogger().info(
                    accountabilityTypeEnum.getName() + ". Novo contrato DOCENTE na unidade " + unit.getName()
                            + " para o funcionário:" + employee.getEmployeeNumber() + " " + begin + " " + end);
            new EmployeeContract(employee.getPerson(), begin, end, unit, accountabilityTypeEnum, true);
        } else {
            getLogger().info(
                    accountabilityTypeEnum.getName() + ". Novo contrato FUNCIONARIO ou RESEARCHER na unidade " + unit.getName()
                            + " para o funcionário:" + employee.getEmployeeNumber() + " " + begin + " " + end);
            new EmployeeContract(employee.getPerson(), begin, end, unit, accountabilityTypeEnum, false);
        }
    }

    private Map<Integer, Employee> getEmployeesMap() {
        Map<Integer, Employee> employees = new HashMap<Integer, Employee>();
        for (Employee employee : Bennu.getInstance().getEmployeesSet()) {
            employees.put(employee.getEmployeeNumber(), employee);
        }
        return employees;
    }

    private String getEmployeeWorkingUnitsQuery() {
        return "SELECT emp.EMP_NUM ,emp.emp_sec_serv as cc, emp.emp_sec_serv_dt as cc_date FROM SLDEMP01 emp";
    }

    private String getEmployeeMailingUnitsQuery() {
        return "SELECT emp.EMP_NUM, emp.emp_sec_serv_ci as cc, null as cc_date FROM SLDEMP03 emp";
    }

}

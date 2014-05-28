package pt.ist.fenix.giafsync;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;

import pt.ist.fenix.giafsync.GiafSync.ImportProcessor;
import pt.ist.fenix.giafsync.GiafSync.Modification;

class ImportEmployeeUnitsFromGiaf extends ImportProcessor {
    @Override
    public List<Modification> processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        List<Modification> modifications = new ArrayList<>();
        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        LocalDate today = new LocalDate();

        modifications.addAll(importIt(oracleConnection, metadata, today, getEmployeeWorkingUnitsQuery(),
                AccountabilityTypeEnum.WORKING_CONTRACT, logger));

        modifications.addAll(importIt(oracleConnection, metadata, today, getEmployeeMailingUnitsQuery(),
                AccountabilityTypeEnum.MAILING_CONTRACT, logger));

        oracleConnection.closeConnection();
        return modifications;
    }

    private List<Modification> importIt(PersistentSuportGiaf oracleConnection, GiafMetadata metadata, LocalDate today,
            String query, AccountabilityTypeEnum accountabilityTypeEnum, Logger logger) throws SQLException {
        List<Modification> modifications = new ArrayList<>();

        PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            String employeeNumber = result.getString("emp_num");
            Employee employee = metadata.getEmployee(employeeNumber, logger);
            if (employee == null) {
                logger.debug(accountabilityTypeEnum.getName() + ". Não existe funcionário. Número: " + employeeNumber);
                continue;
            }
            Integer costCenterCode = 0;
            try {
                costCenterCode = result.getInt("cc");
            } catch (SQLException e) {
                logger.debug(accountabilityTypeEnum.getName() + ". CC inválido. Número: " + employeeNumber);
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
                                    .getGiafProfessionalDataByGiafPersonIdentification(employeeNumber);
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
                                    modifications.add(createEmployeeContract(employee, new YearMonthDay(beginDate), null, unit,
                                            accountabilityTypeEnum, workingContractOnDate, isTeacher, logger));
                                } else if (!workingContractOnDate.getBeginDate().equals(beginDate)
                                        && !StringUtils.isEmpty(ccDateString)) {
                                    modifications.add(changeEmployeeContractDates(accountabilityTypeEnum, new YearMonthDay(
                                            beginDate), workingContractOnDate, logger));
                                } else if (workingContractOnDate.getEndDate() != null && contractSituation != null
                                        && giafProfessionalDataByGiafPersonIdentification.getContractSituationDate() != null) {
                                    logger.debug(accountabilityTypeEnum.getName() + ". Contrato do Funcionário:" + employeeNumber
                                            + " Voltou a abrir na mesma unidade :"
                                            + giafProfessionalDataByGiafPersonIdentification.getContractSituationDate());
                                    modifications.add(createEmployeeContract(employee, new YearMonthDay(
                                            giafProfessionalDataByGiafPersonIdentification.getContractSituationDate()), null,
                                            unit, accountabilityTypeEnum, null, isTeacher, logger));
                                } else {
                                    logger.debug(accountabilityTypeEnum.getName() + ". Não há alterações para o funcionário:"
                                            + employeeNumber);
                                }
                            } else if (endDate != null) {
                                // terminou o contrato corrente
                                modifications.add(closeCurrentContract(accountabilityTypeEnum, workingContractOnDate,
                                        new YearMonthDay(endDate.minusDays(1)), logger));
                            }
                        } else {
                            if (unit != null && endDate == null) {
                                // contrato novo
                                modifications.add(createEmployeeContract(employee, new YearMonthDay(beginDate), null, unit, accountabilityTypeEnum,
                                        null, isTeacher, logger));

                            } else {
                                // já tinha terminado o contrato e já
                                // terminou
                                // tb do nosso lado
                                // logger.debug(accountabilityTypeEnum.getName()
                                // + ". Contrato terminado na unidade "
                                // + unit.getName() + " para o funcionário:"
                                // + employeeNumber + " (não fez nada)");
                            }
                        }
                    } else {
                        logger.debug("ERRO... não tem situação no GIAF " + employeeNumber);
                        Contract currentWorkingContract = employee.getCurrentContractByContractType(accountabilityTypeEnum);
                        if (currentWorkingContract != null) {
                            LocalDate endDate = today.minusDays(1);
                            closeCurrentContract(accountabilityTypeEnum, currentWorkingContract, new YearMonthDay(endDate),
                                    logger);
                            logger.debug(accountabilityTypeEnum.getName()
                                    + ". Não não tem contrato no GIAF, e terminamos no Fénix: " + employeeNumber);
                        } else {
                            logger.debug(accountabilityTypeEnum.getName() + ". Não não tem contrato no GIAF, nem no fénix: "
                                    + employeeNumber);
                        }
                    }
                } else {
                    logger.debug("Não tem employeeProfessionalData. Funcionario: " + employeeNumber);
                }
            } else {
                logger.debug(accountabilityTypeEnum.getName() + ". Não existe unidade: " + costCenterCode
                        + " para o funcionario: " + employeeNumber);
            }
        }
        result.close();
        preparedStatement.close();
        return modifications;
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

    private Modification changeEmployeeContractDates(final AccountabilityTypeEnum accountabilityTypeEnum,
            final YearMonthDay yearMonthDay, final Contract currentWorkingContract, final Logger logger) {
        return new Modification() {
            @Override
            public void execute() {
                YearMonthDay yearMonthDayMinusOne = yearMonthDay.minusDays(1);
                for (Contract contract : currentWorkingContract.getEmployee().getContractsByContractType(accountabilityTypeEnum,
                        yearMonthDay, null)) {
                    if (!currentWorkingContract.equals(contract)) {
                        if (contract.getBeginDate().isBefore(yearMonthDay)) {
                            contract.setEndDate(yearMonthDayMinusOne);
                            logger.debug(accountabilityTypeEnum.getName() + ". Contrato terminado na unidade "
                                    + currentWorkingContract.getUnit().getCostCenterCode() + " para o funcionário:"
                                    + currentWorkingContract.getEmployee().getEmployeeNumber() + " . Mudou data de fim para :"
                                    + yearMonthDayMinusOne);
                        } else {
                            contract.delete();
                            logger.debug(accountabilityTypeEnum.getName() + ". APAGOU para a unidade "
                                    + currentWorkingContract.getUnit().getCostCenterCode() + " para o funcionário:"
                                    + currentWorkingContract.getEmployee().getEmployeeNumber());
                        }
                    }
                }
                currentWorkingContract.setBeginDate(yearMonthDay);
                logger.debug(accountabilityTypeEnum.getName() + " Mudou data de inicio: "
                        + currentWorkingContract.getUnit().getCostCenterCode() + " para o funcionário:"
                        + currentWorkingContract.getEmployee().getEmployeeNumber() + " . Mudou data de inicio para :"
                        + yearMonthDay);
            }
        };
    }

    private Modification closeCurrentContract(final AccountabilityTypeEnum accountabilityTypeEnum,
            final Contract currentWorkingContract, final YearMonthDay endDate, final Logger logger) {
        return new Modification() {
            @Override
            public void execute() {
                if (currentWorkingContract.getEndDate() != null && currentWorkingContract.getEndDate().isEqual(endDate)) {
                    logger.debug(accountabilityTypeEnum.getName() + ". Já está fechado. Não há alterações para o funcionário:"
                            + currentWorkingContract.getEmployee().getEmployeeNumber());
                } else if (currentWorkingContract.getBeginDate().isAfter(endDate)) {
                    logger.debug(accountabilityTypeEnum.getName() + ". APAGOU para a unidade "
                            + currentWorkingContract.getUnit().getCostCenterCode() + " para o funcionário:"
                            + currentWorkingContract.getEmployee().getEmployeeNumber());
                    currentWorkingContract.delete();
                } else {
                    currentWorkingContract.setEndDate(endDate);
                    logger.debug(accountabilityTypeEnum.getName() + ". Contrato terminado na unidade "
                            + currentWorkingContract.getUnit().getCostCenterCode() + " para o funcionário:"
                            + currentWorkingContract.getEmployee().getEmployeeNumber() + " . Mudou data de fim para :" + endDate);
                }
            }
        };
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

    private Modification createEmployeeContract(final Employee employee, final YearMonthDay begin, final YearMonthDay end,
            final Unit unit, final AccountabilityTypeEnum accountabilityTypeEnum, final Contract currentWorkingContract,
            final Boolean isTeacher, final Logger logger) {
        return new Modification() {
            @Override
            public void execute() {
                if (currentWorkingContract != null) {
                    closeCurrentContract(accountabilityTypeEnum, currentWorkingContract, begin.minusDays(1), logger);
                }
                if (isTeacher) {
                    logger.debug(accountabilityTypeEnum.getName() + ". Novo contrato DOCENTE na unidade " + unit.getName()
                            + " para o funcionário:" + employee.getEmployeeNumber() + " " + begin + " " + end);
                    new EmployeeContract(employee.getPerson(), begin, end, unit, accountabilityTypeEnum, true);
                } else {
                    logger.debug(accountabilityTypeEnum.getName() + ". Novo contrato FUNCIONARIO ou RESEARCHER na unidade "
                            + unit.getName() + " para o funcionário:" + employee.getEmployeeNumber() + " " + begin + " " + end);
                    new EmployeeContract(employee.getPerson(), begin, end, unit, accountabilityTypeEnum, false);
                }
            }
        };
    }

    private String getEmployeeWorkingUnitsQuery() {
        return "SELECT emp.EMP_NUM ,emp.emp_sec_serv as cc, emp.emp_sec_serv_dt as cc_date FROM SLDEMP01 emp";
    }

    private String getEmployeeMailingUnitsQuery() {
        return "SELECT emp.EMP_NUM, emp.emp_sec_serv_ci as cc, null as cc_date FROM SLDEMP03 emp";
    }

}

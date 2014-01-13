package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalContract;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import org.apache.commons.lang.StringUtils;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Task(englishTitle = "ImportPersonProfessionalContractsFromGiaf")
public class ImportPersonProfessionalContractsFromGiaf extends ImportFromGiaf {

    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void runTask() {
        getLogger().debug("Start ImportPersonProfessionalContractsFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, ContractSituation> contractSituationsMap = getContractSituationsMap();
            Map<Integer, Employee> employeesMap = getEmployeesMap();
            String query = getQuery();
            getLogger().debug(query);
            PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            int count = 0;
            int news = 0;
            int notImported = 0;
            int dontExist = 0;
            Set<Person> importedButInvalid = new HashSet<Person>();
            while (result.next()) {
                count++;
                String numberString = result.getString("emp_num");
                Person person = getPerson(employeesMap, numberString);
                if (person == null) {
                    getLogger().info("Invalid person with number: " + numberString);
                    dontExist++;
                    continue;
                }

                PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
                if (personProfessionalData == null) {
                    getLogger().info("Empty employeeProfessionalData: " + numberString);
                    dontExist++;
                    continue;
                }
                GiafProfessionalData giafProfessionalData =
                        personProfessionalData.getGiafProfessionalDataByGiafPersonIdentification(numberString);
                if (giafProfessionalData == null) {
                    getLogger().info("Empty giafProfessionalData: " + numberString);
                    dontExist++;
                    continue;
                }
                String contractSituationGiafId = result.getString("COD_CONTRATO");
                ContractSituation contractSituation = contractSituationsMap.get(contractSituationGiafId);
                if (contractSituation == null) {
                    getLogger().info("Empty situation: " + contractSituationGiafId + " for person number: " + numberString);
                    importedButInvalid.add(person);
                }

                String beginDateString = result.getString("dt_inic");

                LocalDate beginDate = null;
                if (!StringUtils.isEmpty(beginDateString)) {
                    beginDate = new LocalDate(Timestamp.valueOf(beginDateString));
                }

                if (beginDate == null) {
                    getLogger().info("Empty beginDate: " + numberString + " Situation: " + contractSituationGiafId);
                    importedButInvalid.add(person);
                }

                String endDateString = result.getString("dt_fim");
                LocalDate endDate = null;
                if (!StringUtils.isEmpty(endDateString)) {
                    endDate = new LocalDate(Timestamp.valueOf(endDateString));
                }
                if (endDate != null) {
                    if (beginDate != null && beginDate.isAfter(endDate)) {
                        getLogger().info(
                                "BeginDate after endDate. Person number:" + numberString + " begin: " + beginDate + " end:"
                                        + endDate);
                        importedButInvalid.add(person);
                    }
                }
                String creationDateString = result.getString("data_criacao");
                if (StringUtils.isEmpty(creationDateString)) {
                    getLogger().info("Empty creationDate: " + numberString + " Situation: " + contractSituationGiafId);
                    notImported++;
                    continue;
                }
                DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

                DateTime modifiedDate = null;
                String modifiedDateString = result.getString("data_alteracao");
                if (!StringUtils.isEmpty(modifiedDateString)) {
                    modifiedDate = new DateTime(Timestamp.valueOf(modifiedDateString));
                }

                if (!hasPersonProfessionalContract(giafProfessionalData, beginDate, endDate, creationDate, modifiedDate,
                        contractSituation, contractSituationGiafId)) {
                    new PersonProfessionalContract(giafProfessionalData, beginDate, endDate, contractSituation,
                            contractSituationGiafId, creationDate, modifiedDate);
                    news++;
                }
            }
            result.close();
            preparedStatement.close();

            int deleted = 0;
            int totalInFenix = 0;
            int repeted = 0;
            for (GiafProfessionalData giafProfessionalData : Bennu.getInstance().getGiafProfessionalDataSet()) {
                for (PersonProfessionalContract personProfessionalContract : giafProfessionalData
                        .getPersonProfessionalContracts()) {
                    if (personProfessionalContract.getAnulationDate() == null) {
                        int countThisPersonProfessionalContractOnGiaf =
                                countThisPersonProfessionalContractOnGiaf(oracleConnection, personProfessionalContract);
                        if (countThisPersonProfessionalContractOnGiaf == 0) {
                            personProfessionalContract.setAnulationDate(new DateTime());
                            deleted++;
                        } else {
                            totalInFenix++;
                            if (countThisPersonProfessionalContractOnGiaf > 1) {
                                repeted += countThisPersonProfessionalContractOnGiaf - 1;
                            }
                        }
                    }
                }
            }

            oracleConnection.closeConnection();
            getLogger().info("Total GIAF: " + count);
            getLogger().info("New: " + news);
            getLogger().info("Deleted: " + deleted);
            getLogger().info("Not imported: " + notImported);
            getLogger().info("Imported with errors: " + importedButInvalid.size());
            getLogger().info("Repeted: " + repeted);
            getLogger().info("Invalid person or situation: " + dontExist);
            getLogger().info("Total Fénix: " + totalInFenix);
            getLogger().info("Total Fénix without errors: " + (totalInFenix - importedButInvalid.size()));
            getLogger().info("Missing in Fénix: " + (count - totalInFenix));

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportPersonProfessionalContractsFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonProfessionalContractsFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private int countThisPersonProfessionalContractOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonProfessionalContract personProfessionalContract) throws ExcepcaoPersistencia, SQLException {
        String query = getPersonProfessionalContractsQuery(personProfessionalContract);
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            preparedStatement = oracleConnection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            if (result.next()) {
                int count = result.getInt("cont");
                if (count > 0) {
                    if (count > 1) {
                        getLogger().info(
                                "Repeted: ---> " + count + " vezes ---> "
                                        + personProfessionalContract.getGiafProfessionalData().getGiafPersonIdentification()
                                        + " Sit: " + personProfessionalContract.getContractSituationGiafId());
                    }
                    return count;
                }
            }
            return 0;
        } catch (ExcepcaoPersistencia e) {
            getLogger().info(
                    "ImportPersonProfessionalContractsFromGiaf -  ERRO ExcepcaoPersistencia hasPersonProfessionalContractOnGiaf");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger()
                    .info("ImportPersonProfessionalContractsFromGiaf -  ERRO SQLException hasPersonProfessionalContractOnGiaf "
                            + query);
            throw new Error(e);
        } finally {
            if (result != null) {
                result.close();
            }
            preparedStatement.close();
        }
    }

    private String getPersonProfessionalContractsQuery(PersonProfessionalContract personProfessionalContract) {

        StringBuilder query = new StringBuilder();
        query.append("select count(*) as cont from sldemp24 where emp_num=");
        query.append(personProfessionalContract.getGiafProfessionalData().getGiafPersonIdentification());
        if (personProfessionalContract.getBeginDate() != null) {
            query.append(" and dt_inic=to_date('");
            query.append(dateFormat.print(personProfessionalContract.getBeginDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and dt_inic is null");
        }
        if (personProfessionalContract.getEndDate() != null) {
            query.append(" and dt_fim=to_date('");
            query.append(dateFormat.print(personProfessionalContract.getEndDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and dt_fim is null");
        }
        if (personProfessionalContract.getContractSituation() != null) {
            query.append(" and COD_CONTRATO=");
            query.append(personProfessionalContract.getContractSituationGiafId());
        } else {
            query.append(" and COD_CONTRATO is null");
        }

        query.append(" and data_criacao=to_date('");
        query.append(dateTimeFormat.print(personProfessionalContract.getCreationDate()));
        query.append("','YYYY-MM-DD HH24:mi:ss')");
        if (personProfessionalContract.getModifiedDate() != null) {
            query.append(" and data_alteracao=to_date('");
            query.append(dateTimeFormat.print(personProfessionalContract.getModifiedDate()));
            query.append("','YYYY-MM-DD HH24:mi:ss')");
        } else {
            query.append("and data_alteracao is null");
        }
        return query.toString();

    }

    private boolean hasPersonProfessionalContract(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
            LocalDate endDate, DateTime creationDate, DateTime modifiedDate, ContractSituation contractSituation,
            String contractSituationGiafId) {
        for (PersonProfessionalContract personProfessionalContract : giafProfessionalData.getPersonProfessionalContracts()) {
            if (personProfessionalContract.getAnulationDate() == null) {
                if (personProfessionalContract.getCreationDate().equals(creationDate)
                        && equal(contractSituation, personProfessionalContract.getContractSituation())
                        && equal(contractSituationGiafId, personProfessionalContract.getContractSituationGiafId())
                        && equal(beginDate, personProfessionalContract.getBeginDate())
                        && equal(endDate, personProfessionalContract.getEndDate())
                        && equal(modifiedDate, personProfessionalContract.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected String getQuery() {
        return "SELECT EMP_NUM, COD_CONTRATO, DT_FIM, DT_INIC, data_criacao, data_alteracao FROM SLDEMP24 where COD_CONTRATO is not null";
    }

}

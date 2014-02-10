package pt.ist.fenix.giafsync;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalContract;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

import pt.ist.fenix.giafsync.GiafSync.ImportProcessor;
import pt.ist.fenix.giafsync.GiafSync.Modification;

class ImportPersonProfessionalContractsFromGiaf extends ImportProcessor {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Modification> processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        List<Modification> modifications = new ArrayList<>();
        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(getQuery());
        ResultSet result = preparedStatement.executeQuery();
        int count = 0;
        int news = 0;
        int notImported = 0;
        int dontExist = 0;
        Set<Person> importedButInvalid = new HashSet<Person>();
        while (result.next()) {
            count++;
            String numberString = result.getString("emp_num");
            Person person = metadata.getPerson(numberString, logger);
            if (person == null) {
                logger.debug("Invalid person with number: " + numberString);
                dontExist++;
                continue;
            }

            PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
            if (personProfessionalData == null) {
                logger.debug("Empty employeeProfessionalData: " + numberString);
                dontExist++;
                continue;
            }
            final GiafProfessionalData giafProfessionalData =
                    personProfessionalData.getGiafProfessionalDataByGiafPersonIdentification(numberString);
            if (giafProfessionalData == null) {
                logger.debug("Empty giafProfessionalData: " + numberString);
                dontExist++;
                continue;
            }
            final String contractSituationGiafId = result.getString("COD_CONTRATO");
            final ContractSituation contractSituation = metadata.situation(contractSituationGiafId);
            if (contractSituation == null) {
                logger.debug("Empty situation: " + contractSituationGiafId + " for person number: " + numberString);
                importedButInvalid.add(person);
            }

            String beginDateString = result.getString("dt_inic");

            final LocalDate beginDate =
                    StringUtils.isEmpty(beginDateString) ? null : new LocalDate(Timestamp.valueOf(beginDateString));

            if (beginDate == null) {
                logger.debug("Empty beginDate: " + numberString + " Situation: " + contractSituationGiafId);
                importedButInvalid.add(person);
            }

            String endDateString = result.getString("dt_fim");
            final LocalDate endDate = StringUtils.isEmpty(endDateString) ? null : new LocalDate(Timestamp.valueOf(endDateString));
            if (endDate != null) {
                if (beginDate != null && beginDate.isAfter(endDate)) {
                    logger.debug("BeginDate after endDate. Person number:" + numberString + " begin: " + beginDate + " end:"
                            + endDate);
                    importedButInvalid.add(person);
                }
            }
            String creationDateString = result.getString("data_criacao");
            if (StringUtils.isEmpty(creationDateString)) {
                logger.debug("Empty creationDate: " + numberString + " Situation: " + contractSituationGiafId);
                notImported++;
                continue;
            }
            final DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

            String modifiedDateString = result.getString("data_alteracao");
            final DateTime modifiedDate =
                    StringUtils.isEmpty(modifiedDateString) ? null : new DateTime(Timestamp.valueOf(modifiedDateString));

            if (!hasPersonProfessionalContract(giafProfessionalData, beginDate, endDate, creationDate, modifiedDate,
                    contractSituation, contractSituationGiafId)) {
                modifications.add(new Modification() {
                    @Override
                    public void execute() {
                        new PersonProfessionalContract(giafProfessionalData, beginDate, endDate, contractSituation,
                                contractSituationGiafId, creationDate, modifiedDate);
                    }
                });
                news++;
            }
        }
        result.close();
        preparedStatement.close();

        int deleted = 0;
        int totalInFenix = 0;
        int repeted = 0;
        for (GiafProfessionalData giafProfessionalData : Bennu.getInstance().getGiafProfessionalDataSet()) {
            for (final PersonProfessionalContract personProfessionalContract : giafProfessionalData
                    .getPersonProfessionalContractsSet()) {
                if (personProfessionalContract.getAnulationDate() == null) {
                    int countThisPersonProfessionalContractOnGiaf =
                            countThisPersonProfessionalContractOnGiaf(oracleConnection, personProfessionalContract, logger);
                    if (countThisPersonProfessionalContractOnGiaf == 0) {
                        modifications.add(new Modification() {
                            @Override
                            public void execute() {
                                personProfessionalContract.setAnulationDate(new DateTime());
                            }
                        });
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
        log.println("-- Professional Contracts --");
        log.println("Total GIAF: " + count);
        log.println("New: " + news);
        log.println("Deleted: " + deleted);
        log.println("Not imported: " + notImported);
        log.println("Imported with errors: " + importedButInvalid.size());
        log.println("Repeted: " + repeted);
        log.println("Invalid persons: " + dontExist);
        log.println("Total Fénix: " + totalInFenix);
        log.println("Total Fénix without errors: " + (totalInFenix - importedButInvalid.size()));
        log.println("Missing in Fénix: " + (count - totalInFenix));
        return modifications;
    }

    private int countThisPersonProfessionalContractOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonProfessionalContract personProfessionalContract, Logger logger) throws SQLException {
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
                        logger.debug("Repeted: ---> " + count + " vezes ---> "
                                + personProfessionalContract.getGiafProfessionalData().getGiafPersonIdentification() + " Sit: "
                                + personProfessionalContract.getContractSituationGiafId());
                    }
                    return count;
                }
            }
            return 0;
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
                        && Objects.equals(contractSituation, personProfessionalContract.getContractSituation())
                        && Objects.equals(contractSituationGiafId, personProfessionalContract.getContractSituationGiafId())
                        && Objects.equals(beginDate, personProfessionalContract.getBeginDate())
                        && Objects.equals(endDate, personProfessionalContract.getEndDate())
                        && Objects.equals(modifiedDate, personProfessionalContract.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getQuery() {
        return "SELECT EMP_NUM, COD_CONTRATO, DT_FIM, DT_INIC, data_criacao, data_alteracao FROM SLDEMP24 where COD_CONTRATO is not null";
    }
}

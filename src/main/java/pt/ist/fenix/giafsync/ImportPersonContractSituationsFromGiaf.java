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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
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

class ImportPersonContractSituationsFromGiaf extends ImportProcessor {
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
        int deleted = 0;
        int totalInFenix = 0;
        int repeted = 0;
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
                logger.debug("Empty personProfessionalData: " + numberString);
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

            final String contractSituationGiafId = result.getString("emp_sit");
            final ContractSituation contractSituation = metadata.situation(contractSituationGiafId);
            if (contractSituation == null) {
                logger.debug("Invalid situation: " + contractSituationGiafId);
                importedButInvalid.add(person);
            }

            final String categoryGiafId = result.getString("emp_cat_func");
            final ProfessionalCategory category = metadata.category(categoryGiafId);
            if (category == null && (!StringUtils.isEmpty(categoryGiafId))) {
                logger.debug("Empty category: " + categoryGiafId + ". Person number: " + numberString);
                importedButInvalid.add(person);
            }
            if (category == null && (contractSituation == null || contractSituation.getEndSituation() == false)) {
                logger.debug("Empty catefory on a non end situation: " + numberString + " . Situation: "
                        + contractSituationGiafId);
                importedButInvalid.add(person);
            }

            String beginDateString = result.getString("dt_inic");
            final LocalDate beginDate =
                    StringUtils.isEmpty(beginDateString) ? null : new LocalDate(Timestamp.valueOf(beginDateString));
            if (beginDate == null) {
                logger.debug("Empty beginDate: " + numberString + " . Situation: " + contractSituationGiafId);
                importedButInvalid.add(person);
            }
            String endDateString = result.getString("dt_fim");
            final LocalDate endDate = StringUtils.isEmpty(endDateString) ? null : new LocalDate(Timestamp.valueOf(endDateString));
            if (endDate != null) {
                if (beginDate != null && beginDate.isAfter(endDate)) {
                    logger.debug("BeginDate after EndDate: " + numberString + " begin: " + beginDate + " end:" + endDate);
                    importedButInvalid.add(person);
                }
            }
            String creationDateString = result.getString("data_criacao");
            if (StringUtils.isEmpty(creationDateString)) {
                logger.debug("Empty creationDate: " + numberString + " . Situation: " + contractSituationGiafId);
                notImported++;
                continue;
            }
            final DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

            String modifiedDateString = result.getString("data_alteracao");
            final DateTime modifiedDate =
                    StringUtils.isEmpty(modifiedDateString) ? null : new DateTime(Timestamp.valueOf(modifiedDateString));

            final String step = result.getString("emp_escalao");

            if (!hasPersonContractSituation(giafProfessionalData, beginDate, endDate, creationDate, modifiedDate, step,
                    contractSituation, contractSituationGiafId, category, categoryGiafId)) {
                modifications.add(new Modification() {
                    @Override
                    public void execute() {
                        new PersonContractSituation(giafProfessionalData, beginDate, endDate, step, contractSituation,
                                contractSituationGiafId, category, categoryGiafId, creationDate, modifiedDate);
                    }
                });
                news++;
            }

        }
        result.close();
        preparedStatement.close();

        for (GiafProfessionalData giafProfessionalData : Bennu.getInstance().getGiafProfessionalDataSet()) {
            for (final PersonContractSituation personContractSituation : giafProfessionalData.getPersonContractSituationsSet()) {
                if (personContractSituation.getAnulationDate() == null) {
                    int countThisPersonContractSituationOnGiaf =
                            countThisPersonContractSituationOnGiaf(oracleConnection, personContractSituation, logger);
                    if (countThisPersonContractSituationOnGiaf == 0) {
                        modifications.add(new Modification() {
                            @Override
                            public void execute() {
                                personContractSituation.setAnulationDate(new DateTime());
                            }
                        });
                        deleted++;
                    } else {
                        totalInFenix++;
                        if (countThisPersonContractSituationOnGiaf > 1) {
                            repeted += countThisPersonContractSituationOnGiaf - 1;
                        }
                    }
                }
            }
        }

        oracleConnection.closeConnection();
        log.println("-- Contract situations --");
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

    private int countThisPersonContractSituationOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonContractSituation personContractSituation, Logger logger) throws SQLException {
        String query = getPersonContractSituationsQuery(personContractSituation);
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            preparedStatement = oracleConnection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            if (result.next()) {
                int count = result.getInt("cont");
                if (count > 0) {
                    if (count > 1) {
                        logger.debug("---> " + count + " ---> "
                                + personContractSituation.getGiafProfessionalData().getGiafPersonIdentification()
                                + " Situation: " + personContractSituation.getContractSituation().getGiafId());
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

    private String getPersonContractSituationsQuery(PersonContractSituation personContractSituation) {
        StringBuilder query = new StringBuilder();
        query.append("select count(*) as cont from sldemp24 emp, sltcatfunc cat, sltsit sit where ");
        query.append(" emp.emp_cat_func = cat.emp_cat_func(+) ");
        query.append("and emp.emp_sit = sit.emp_sit(+) ");
        query.append("and emp.emp_num=");
        query.append(personContractSituation.getGiafProfessionalData().getGiafPersonIdentification());
        if (personContractSituation.getBeginDate() != null) {
            query.append(" and emp.dt_inic=to_date('");
            query.append(dateFormat.print(personContractSituation.getBeginDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and emp.dt_inic is null");
        }
        if (personContractSituation.getEndDate() != null) {
            query.append(" and emp.dt_fim=to_date('");
            query.append(dateFormat.print(personContractSituation.getEndDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and emp.dt_fim is null");
        }
        if (!StringUtils.isEmpty(personContractSituation.getProfessionalCategoryGiafId())) {
            query.append(" and emp.emp_cat_func=");
            query.append(personContractSituation.getProfessionalCategoryGiafId());
            if (personContractSituation.getProfessionalCategory() == null) {
                query.append(" and cat.emp_cat_func is null");
            }
        } else {
            query.append(" and emp.emp_cat_func is null");
        }
        if (!StringUtils.isEmpty(personContractSituation.getContractSituationGiafId())) {
            query.append(" and emp.emp_sit=");
            query.append(personContractSituation.getContractSituationGiafId());
            if (personContractSituation.getContractSituation() == null) {
                query.append(" and sit.emp_sit is null");
            }
        } else {
            query.append(" and emp.emp_sit is null");
        }
        query.append(" and emp.data_criacao=to_date('");
        query.append(dateTimeFormat.print(personContractSituation.getCreationDate()));
        query.append("','YYYY-MM-DD HH24:mi:ss')");
        if (personContractSituation.getModifiedDate() != null) {
            query.append(" and emp.data_alteracao=to_date('");
            query.append(dateTimeFormat.print(personContractSituation.getModifiedDate()));
            query.append("','YYYY-MM-DD HH24:mi:ss')");
        } else {
            query.append("and emp.data_alteracao is null");
        }
        if (personContractSituation.getStep() != null) {
            query.append(" and emp.emp_escalao='");
            query.append(personContractSituation.getStep());
            query.append("'");
        } else {
            query.append(" and emp.emp_escalao is null");
        }
        return query.toString();

    }

    private boolean hasPersonContractSituation(GiafProfessionalData giafProfessionalData, LocalDate beginDate, LocalDate endDate,
            DateTime creationDate, DateTime modifiedDate, String step, ContractSituation contractSituation,
            String contractSituationGiafId, ProfessionalCategory category, String categoryGiafId) {
        for (PersonContractSituation personContractSituation : giafProfessionalData.getPersonContractSituationsSet()) {
            if (personContractSituation.getAnulationDate() == null) {
                if (personContractSituation.getCreationDate().equals(creationDate)
                        && Objects.equals(contractSituation, personContractSituation.getContractSituation())
                        && Objects.equals(contractSituationGiafId, personContractSituation.getContractSituationGiafId())
                        && Objects.equals(beginDate, personContractSituation.getBeginDate())
                        && Objects.equals(endDate, personContractSituation.getEndDate())
                        && Objects.equals(modifiedDate, personContractSituation.getModifiedDate())
                        && Objects.equals(category, personContractSituation.getProfessionalCategory())
                        && Objects.equals(categoryGiafId, personContractSituation.getProfessionalCategoryGiafId())
                        && Objects.equals(step, personContractSituation.getStep())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getQuery() {
        return "select emp_num, dt_inic, dt_fim, emp_cat_func, emp_sit, data_criacao, data_alteracao, emp_escalao from sldemp24 where emp_sit is not null";
    }
}

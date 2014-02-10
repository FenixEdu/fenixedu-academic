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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalExemption;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonSabbatical;
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

class ImportPersonSabbaticalsFromGiaf extends ImportProcessor {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonSabbaticalsFromGiaf() {

    }

    @Override
    public List<Modification> processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        List<Modification> modifications = new ArrayList<>();
        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(getQuery());
        ResultSet result = preparedStatement.executeQuery();
        Set<Person> importedButInvalid = new HashSet<Person>();
        int count = 0;
        int news = 0;
        int notImported = 0;
        int dontExist = 0;
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
            String beginDateString = result.getString("DATA_INICIO");
            final LocalDate beginDate =
                    StringUtils.isEmpty(beginDateString) ? null : new LocalDate(Timestamp.valueOf(beginDateString));
            if (beginDate == null) {
                logger.debug("Empty beginDate. Person number: " + numberString);
                importedButInvalid.add(person);
            }
            String endDateString = result.getString("DATA_FIM");
            final LocalDate endDate = StringUtils.isEmpty(endDateString) ? null : new LocalDate(Timestamp.valueOf(endDateString));
            if (endDate != null) {
                if (beginDate != null && beginDate.isAfter(endDate)) {
                    logger.debug("BeginDate after EndDate. Person number: " + numberString + " begin: " + beginDate + " end: "
                            + endDate);
                    importedButInvalid.add(person);
                }
            }
            String creationDateString = result.getString("data_criacao");
            if (StringUtils.isEmpty(creationDateString)) {
                logger.debug("Empty creationDate. Person number: " + numberString);
                notImported++;
                continue;
            }
            final DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

            String modifiedDateString = result.getString("data_alteracao");
            final DateTime modifiedDate =
                    StringUtils.isEmpty(modifiedDateString) ? null : new DateTime(Timestamp.valueOf(modifiedDateString));

            if (!hasPersonSabbatical(giafProfessionalData, beginDate, endDate, creationDate, modifiedDate)) {
                modifications.add(new Modification() {
                    @Override
                    public void execute() {
                        new PersonSabbatical(giafProfessionalData, beginDate, endDate, creationDate, modifiedDate);
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
            for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData.getPersonProfessionalExemptions()) {
                if (personProfessionalExemption instanceof PersonSabbatical
                        && personProfessionalExemption.getAnulationDate() == null) {
                    final PersonSabbatical personSabbatical = (PersonSabbatical) personProfessionalExemption;
                    int countThisPersonSabbaticalOnGiaf =
                            countThisPersonSabbaticalOnGiaf(oracleConnection, personSabbatical, logger);
                    if (countThisPersonSabbaticalOnGiaf == 0) {
                        modifications.add(new Modification() {
                            @Override
                            public void execute() {
                                personSabbatical.setAnulationDate(new DateTime());
                            }
                        });
                        deleted++;
                    } else {
                        totalInFenix++;
                        if (countThisPersonSabbaticalOnGiaf > 1) {
                            repeted += countThisPersonSabbaticalOnGiaf - 1;
                        }
                    }
                }
            }
        }

        oracleConnection.closeConnection();
        log.println("-- Sabbaticals --");
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

    private int countThisPersonSabbaticalOnGiaf(PersistentSuportGiaf oracleConnection, PersonSabbatical personSabbatical,
            Logger logger) throws SQLException {
        String query = getSabbaticalQuery(personSabbatical);
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
                                + personSabbatical.getGiafProfessionalData().getGiafPersonIdentification());
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

    private String getSabbaticalQuery(PersonSabbatical personSabbatical) {
        StringBuilder query = new StringBuilder();

        query.append("select count(*) as cont from SLDSABATICA where emp_num=");
        query.append(personSabbatical.getGiafProfessionalData().getGiafPersonIdentification());
        if (personSabbatical.getBeginDate() != null) {
            query.append(" and DATA_INICIO=to_date('");
            query.append(dateFormat.print(personSabbatical.getBeginDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and DATA_INICIO is null");
        }
        if (personSabbatical.getEndDate() != null) {
            query.append(" and DATA_FIM=to_date('");
            query.append(dateFormat.print(personSabbatical.getEndDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and DATA_FIM is null");
        }

        query.append(" and data_criacao=to_date('");
        query.append(dateTimeFormat.print(personSabbatical.getCreationDate()));
        query.append("','YYYY-MM-DD HH24:mi:ss')");
        if (personSabbatical.getModifiedDate() != null) {
            query.append(" and data_alteracao=to_date('");
            query.append(dateTimeFormat.print(personSabbatical.getModifiedDate()));
            query.append("','YYYY-MM-DD HH24:mi:ss')");
        } else {
            query.append("and data_alteracao is null");
        }
        return query.toString();

    }

    private boolean hasPersonSabbatical(GiafProfessionalData giafProfessionalData, LocalDate beginDate, LocalDate endDate,
            DateTime creationDate, DateTime modifiedDate) {
        for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData.getPersonProfessionalExemptions()) {
            if (personProfessionalExemption.getAnulationDate() == null) {
                if (personProfessionalExemption instanceof PersonSabbatical) {
                    PersonSabbatical personSabbatical = (PersonSabbatical) personProfessionalExemption;
                    if (Objects.equals(beginDate, personSabbatical.getBeginDate())
                            && Objects.equals(endDate, personSabbatical.getEndDate())
                            && Objects.equals(creationDate, personSabbatical.getCreationDate())
                            && Objects.equals(modifiedDate, personSabbatical.getModifiedDate())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected String getQuery() {
        return "SELECT sabatica.DATA_FIM, sabatica.DATA_INICIO, sabatica.EMP_NUM, sabatica.data_criacao, sabatica.data_alteracao FROM SLDSABATICA sabatica";
    }
}

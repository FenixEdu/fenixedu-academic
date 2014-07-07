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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
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

class ImportPersonProfessionalRegimesFromGiaf extends ImportProcessor {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonProfessionalRegimesFromGiaf() {

    }

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
            final String professionalRegimeGiafId = result.getString("emp_regime");
            final ProfessionalRegime professionalRegime = metadata.regime(professionalRegimeGiafId);
            if (professionalRegime == null) {
                logger.debug("Empty regime: " + professionalRegimeGiafId + " person number" + numberString);
                importedButInvalid.add(person);
            }
            String beginDateString = result.getString("emp_regime_dt");
            final LocalDate beginDate =
                    StringUtils.isEmpty(beginDateString) ? null : new LocalDate(Timestamp.valueOf(beginDateString));
            if (beginDate == null) {
                logger.debug("Empty beginDate. Person number: " + numberString + " Regime: " + professionalRegimeGiafId);
                importedButInvalid.add(person);
            }
            String endDateString = result.getString("emp_regime_dt_fim");
            final LocalDate endDate = StringUtils.isEmpty(endDateString) ? null : new LocalDate(Timestamp.valueOf(endDateString));
            if (endDate != null) {
                if (beginDate != null && beginDate.isAfter(endDate)) {
                    logger.debug("BeginDate after EndDate. Person number: " + numberString + " begin: " + beginDate + " end:"
                            + endDate);
                    importedButInvalid.add(person);
                }
            }
            // ,a.data_criacao,a.data_alteracao
            String creationDateString = result.getString("data_criacao");
            if (StringUtils.isEmpty(creationDateString)) {
                logger.debug("Empty creationDate. Person number: " + numberString + " Regime: " + professionalRegimeGiafId);
                notImported++;
                continue;
            }
            final DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

            String modifiedDateString = result.getString("data_alteracao");
            final DateTime modifiedDate =
                    StringUtils.isEmpty(modifiedDateString) ? null : new DateTime(Timestamp.valueOf(modifiedDateString));

            if (!hasPersonProfessionalRegime(giafProfessionalData, beginDate, endDate, professionalRegime,
                    professionalRegimeGiafId, creationDate, modifiedDate)) {
                modifications.add(new Modification() {
                    @Override
                    public void execute() {
                        new PersonProfessionalRegime(giafProfessionalData, beginDate, endDate, professionalRegime,
                                professionalRegimeGiafId, creationDate, modifiedDate);
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
            for (final PersonProfessionalRegime personProfessionalRegime : giafProfessionalData.getPersonProfessionalRegimesSet()) {
                if (personProfessionalRegime.getAnulationDate() == null) {
                    int countThisPersonProfessionalRegimeOnGiaf =
                            countThisPersonProfessionalRegimeOnGiaf(oracleConnection, personProfessionalRegime, logger);
                    if (countThisPersonProfessionalRegimeOnGiaf == 0) {
                        modifications.add(new Modification() {
                            @Override
                            public void execute() {
                                personProfessionalRegime.setAnulationDate(new DateTime());
                            }
                        });
                        deleted++;
                    } else {
                        totalInFenix++;
                        if (countThisPersonProfessionalRegimeOnGiaf > 1) {
                            repeted += countThisPersonProfessionalRegimeOnGiaf - 1;
                        }
                    }
                }
            }
        }

        oracleConnection.closeConnection();
        log.println("-- Professional regimes --");
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

    private int countThisPersonProfessionalRegimeOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonProfessionalRegime personProfessionalRegime, Logger logger) throws SQLException {
        String query = getPersonProfessionalRegimeQuery(personProfessionalRegime);
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
                                + personProfessionalRegime.getGiafProfessionalData().getGiafPersonIdentification() + " Regime: "
                                + personProfessionalRegime.getProfessionalRegime().getGiafId());
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

    private String getPersonProfessionalRegimeQuery(PersonProfessionalRegime personProfessionalRegime) {
        StringBuilder query = new StringBuilder();
        query.append("select count(*) as cont from sldemp26 where emp_num=");
        query.append(personProfessionalRegime.getGiafProfessionalData().getGiafPersonIdentification());
        if (personProfessionalRegime.getBeginDate() != null) {
            query.append(" and emp_regime_dt=to_date('");
            query.append(dateFormat.print(personProfessionalRegime.getBeginDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and emp_regime_dt is null");
        }
        if (personProfessionalRegime.getEndDate() != null) {
            query.append(" and emp_regime_dt_fim=to_date('");
            query.append(dateFormat.print(personProfessionalRegime.getEndDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and emp_regime_dt_fim is null");
        }
        if (personProfessionalRegime.getProfessionalRegime() != null) {
            query.append(" and emp_regime=");
            query.append(personProfessionalRegime.getProfessionalRegimeGiafId());
        } else {
            query.append(" and emp_regime is null");
        }
        query.append(" and data_criacao=to_date('");
        query.append(dateTimeFormat.print(personProfessionalRegime.getCreationDate()));
        query.append("','YYYY-MM-DD HH24:mi:ss')");
        if (personProfessionalRegime.getModifiedDate() != null) {
            query.append(" and data_alteracao=to_date('");
            query.append(dateTimeFormat.print(personProfessionalRegime.getModifiedDate()));
            query.append("','YYYY-MM-DD HH24:mi:ss')");
        } else {
            query.append("and data_alteracao is null");
        }
        return query.toString();

    }

    private boolean hasPersonProfessionalRegime(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
            LocalDate endDate, ProfessionalRegime professionalRegime, String professionalRegimeGiafId, DateTime creationDate,
            DateTime modifiedDate) {
        for (PersonProfessionalRegime personProfessionalRegime : giafProfessionalData.getPersonProfessionalRegimesSet()) {
            if (personProfessionalRegime.getAnulationDate() == null) {
                if (Objects.equals(beginDate, personProfessionalRegime.getBeginDate())
                        && Objects.equals(endDate, personProfessionalRegime.getEndDate())
                        && Objects.equals(professionalRegime, personProfessionalRegime.getProfessionalRegime())
                        && Objects.equals(professionalRegimeGiafId, personProfessionalRegime.getProfessionalRegimeGiafId())
                        && Objects.equals(creationDate, personProfessionalRegime.getCreationDate())
                        && Objects.equals(modifiedDate, personProfessionalRegime.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getQuery() {
        return "SELECT a.emp_num, a.emp_regime, a.emp_regime_dt, a.emp_regime_dt_fim, a.data_criacao, a.data_alteracao FROM sldemp26 a";
    }
}

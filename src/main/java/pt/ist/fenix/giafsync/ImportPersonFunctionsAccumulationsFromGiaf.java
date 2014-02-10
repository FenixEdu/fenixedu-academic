package pt.ist.fenix.giafsync;

import java.io.PrintWriter;
import java.math.BigDecimal;
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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.FunctionsAccumulation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonFunctionsAccumulation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
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

class ImportPersonFunctionsAccumulationsFromGiaf extends ImportProcessor {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonFunctionsAccumulationsFromGiaf() {

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

            final String functionsAccumulationGiafId = result.getString("TIPO");
            final FunctionsAccumulation functionsAccumulation = metadata.accumulation(functionsAccumulationGiafId);
            if (functionsAccumulation == null) {
                logger.debug("Empty FunctionsAccumulation: " + functionsAccumulationGiafId + " for person number: "
                        + numberString);
                importedButInvalid.add(person);
            }
            String beginDateString = result.getString("DATA_INICIO");
            final LocalDate beginDate =
                    StringUtils.isEmpty(beginDateString) ? null : new LocalDate(Timestamp.valueOf(beginDateString));
            if (beginDate == null) {
                logger.debug("Empty beginDate: " + numberString + " FunctionsAccumulation: " + functionsAccumulationGiafId);
                importedButInvalid.add(person);
            }

            String endDateString = result.getString("DATA_FIM");
            final LocalDate endDate = StringUtils.isEmpty(endDateString) ? null : new LocalDate(Timestamp.valueOf(endDateString));
            if (beginDate != null && endDate != null) {
                if (beginDate != null && beginDate.isAfter(endDate)) {
                    logger.debug("BeginDate after EndDate. Person number: " + numberString + ". begin: " + beginDate + " end:"
                            + endDate);
                    importedButInvalid.add(person);
                }
            }
            final BigDecimal hours = result.getBigDecimal("horas_sem");

            final String professionalRegimeGiafId = result.getString("EMP_REGIME");
            final ProfessionalRegime professionalRegime = metadata.regime(professionalRegimeGiafId);
            if (professionalRegime == null) {
                logger.debug("Empty regime: " + numberString);
                importedButInvalid.add(person);
            }
            String creationDateString = result.getString("data_criacao");
            if (StringUtils.isEmpty(creationDateString)) {
                logger.debug("Empty creation Date. Person number: " + numberString + " FunctionsAccumulation: "
                        + functionsAccumulationGiafId);
                notImported++;
                continue;
            }
            final DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

            String modifiedDateString = result.getString("data_alteracao");
            final DateTime modifiedDate =
                    StringUtils.isEmpty(modifiedDateString) ? null : new DateTime(Timestamp.valueOf(modifiedDateString));

            if (!hasPersonFunctionsAccumulations(giafProfessionalData, beginDate, endDate, hours, functionsAccumulation,
                    functionsAccumulationGiafId, professionalRegime, professionalRegimeGiafId, creationDate, modifiedDate)) {
                modifications.add(new Modification() {
                    @Override
                    public void execute() {
                        new PersonFunctionsAccumulation(giafProfessionalData, beginDate, endDate, hours, functionsAccumulation,
                                functionsAccumulationGiafId, professionalRegime, professionalRegimeGiafId, creationDate,
                                modifiedDate);
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
            for (final PersonFunctionsAccumulation personFunctionsAccumulation : giafProfessionalData
                    .getPersonFunctionsAccumulations()) {
                if (personFunctionsAccumulation.getAnulationDate() == null) {
                    int countThisPersonFunctionsAccumulationOnGiaf =
                            countThisPersonFunctionsAccumulationOnGiaf(oracleConnection, personFunctionsAccumulation, logger);
                    if (countThisPersonFunctionsAccumulationOnGiaf == 0) {
                        modifications.add(new Modification() {
                            @Override
                            public void execute() {
                                personFunctionsAccumulation.setAnulationDate(new DateTime());
                            }
                        });
                        deleted++;
                    } else {
                        totalInFenix++;
                        if (countThisPersonFunctionsAccumulationOnGiaf > 1) {
                            repeted += countThisPersonFunctionsAccumulationOnGiaf - 1;
                        }
                    }
                }
            }
        }

        oracleConnection.closeConnection();
        log.println("-- Function accumuations --");
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

    private int countThisPersonFunctionsAccumulationOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonFunctionsAccumulation personFunctionsAccumulation, Logger logger) throws SQLException {
        String query = getFunctionsAccumulationQuery(personFunctionsAccumulation);
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
                                + personFunctionsAccumulation.getGiafProfessionalData().getGiafPersonIdentification() + " FA: "
                                + personFunctionsAccumulation.getFunctionsAccumulation().getGiafId());
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

    private String getFunctionsAccumulationQuery(PersonFunctionsAccumulation personFunctionsAccumulation) {
        StringBuilder query = new StringBuilder();
        query.append("select count(*) as cont from SLDACUMFUNC where emp_num=");
        query.append(personFunctionsAccumulation.getGiafProfessionalData().getGiafPersonIdentification());

        if (personFunctionsAccumulation.getBeginDate() != null) {
            query.append(" and DATA_INICIO=to_date('");
            query.append(dateFormat.print(personFunctionsAccumulation.getBeginDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and DATA_INICIO is null");
        }
        if (personFunctionsAccumulation.getEndDate() != null) {
            query.append(" and DATA_FIM=to_date('");
            query.append(dateFormat.print(personFunctionsAccumulation.getEndDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and DATA_FIM is null");
        }
        if (personFunctionsAccumulation.getFunctionsAccumulation() != null) {
            query.append(" and TIPO=");
            query.append(personFunctionsAccumulation.getFunctionsAccumulationGiafId());
        } else {
            query.append(" and TIPO is null");
        }
        if (personFunctionsAccumulation.getProfessionalRegime() != null) {
            query.append(" and EMP_REGIME=");
            query.append(personFunctionsAccumulation.getProfessionalRegimeGiafId());
        } else {
            query.append(" and EMP_REGIME is null");
        }

        if (personFunctionsAccumulation.getHours() != null) {
            query.append(" and horas_sem=");
            query.append(personFunctionsAccumulation.getHours());
        } else {
            query.append(" and horas_sem is null");
        }

        query.append(" and data_criacao=to_date('");
        query.append(dateTimeFormat.print(personFunctionsAccumulation.getCreationDate()));
        query.append("','YYYY-MM-DD HH24:mi:ss')");
        if (personFunctionsAccumulation.getModifiedDate() != null) {
            query.append(" and data_alteracao=to_date('");
            query.append(dateTimeFormat.print(personFunctionsAccumulation.getModifiedDate()));
            query.append("','YYYY-MM-DD HH24:mi:ss')");
        } else {
            query.append("and data_alteracao is null");
        }
        return query.toString();

    }

    private boolean hasPersonFunctionsAccumulations(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
            LocalDate endDate, BigDecimal hours, FunctionsAccumulation functionsAccumulation, String functionsAccumulationGiafId,
            ProfessionalRegime professionalRegime, String professionalRegimeGiafId, DateTime creationDate, DateTime modifiedDate) {
        for (PersonFunctionsAccumulation personFunctionsAccumulation : giafProfessionalData.getPersonFunctionsAccumulations()) {
            if (personFunctionsAccumulation.getAnulationDate() == null) {
                if (Objects.equals(beginDate, personFunctionsAccumulation.getBeginDate())
                        && Objects.equals(endDate, personFunctionsAccumulation.getEndDate())
                        && Objects.equals(hours, personFunctionsAccumulation.getHours())
                        && Objects.equals(functionsAccumulation, personFunctionsAccumulation.getFunctionsAccumulation())
                        && Objects.equals(functionsAccumulationGiafId,
                                personFunctionsAccumulation.getFunctionsAccumulationGiafId())
                        && Objects.equals(professionalRegime, personFunctionsAccumulation.getProfessionalRegime())
                        && Objects.equals(professionalRegimeGiafId, personFunctionsAccumulation.getProfessionalRegimeGiafId())
                        && Objects.equals(creationDate, personFunctionsAccumulation.getCreationDate())
                        && Objects.equals(modifiedDate, personFunctionsAccumulation.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getQuery() {
        return "SELECT af.DATA_FIM, af.DATA_INICIO, af.EMP_NUM, af.horas_sem,af.EMP_REGIME, af.TIPO, af.data_criacao, af.data_alteracao FROM SLDACUMFUNC af";
    }
}

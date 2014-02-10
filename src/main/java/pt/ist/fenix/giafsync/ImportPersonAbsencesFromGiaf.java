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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.Absence;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonAbsence;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalExemption;
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

import com.google.common.base.Joiner;

class ImportPersonAbsencesFromGiaf extends ImportProcessor {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonAbsencesFromGiaf() {

    }

    @Override
    public List<Modification> processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        List<Modification> modifications = new ArrayList<>();
        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(getQuery(metadata));
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
            final String absenceGiafId = result.getString("flt_tip");
            final Absence absence = metadata.absence(absenceGiafId);
            if (absence == null) {
                logger.debug("Empty absence: " + absenceGiafId + " for person number: " + numberString);
                importedButInvalid.add(person);

            }
            String beginDateString = result.getString("FLT_DT_INIC");
            final LocalDate beginDate =
                    StringUtils.isEmpty(beginDateString) ? null : new LocalDate(Timestamp.valueOf(beginDateString));
            if (beginDate == null) {
                logger.debug("Empty beginDate. Person number: " + numberString + " Absence: " + absenceGiafId);
                importedButInvalid.add(person);
            }
            String endDateString = result.getString("FLT_DT_FIM");
            final LocalDate endDate = StringUtils.isEmpty(endDateString) ? null : new LocalDate(Timestamp.valueOf(endDateString));
            if (beginDate != null && endDate != null) {
                if (beginDate.isAfter(endDate)) {
                    logger.debug("BeginDate after endDate. Person number: " + numberString + " begin: " + beginDate + " end: "
                            + endDate);
                    importedButInvalid.add(person);
                }
            }
            String creationDateString = result.getString("data_criacao");
            if (StringUtils.isEmpty(creationDateString)) {
                logger.debug("Empty creationDate. Person number: " + numberString + " Absence: " + absenceGiafId);
                notImported++;
                continue;
            }
            final DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

            String modifiedDateString = result.getString("data_alteracao");
            final DateTime modifiedDate =
                    StringUtils.isEmpty(modifiedDateString) ? null : new DateTime(Timestamp.valueOf(modifiedDateString));

            if (!hasPersonAbsence(giafProfessionalData, beginDate, endDate, absence, absenceGiafId, creationDate, modifiedDate)) {
                modifications.add(new Modification() {
                    @Override
                    public void execute() {
                        new PersonAbsence(giafProfessionalData, beginDate, endDate, absence, absenceGiafId, creationDate,
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
            for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData.getPersonProfessionalExemptions()) {
                if (personProfessionalExemption instanceof PersonAbsence
                        && personProfessionalExemption.getAnulationDate() == null) {
                    PersonAbsence personAbsence = (PersonAbsence) personProfessionalExemption;
                    int countThisPersonAbsenceOnGiaf = countThisPersonAbsenceOnGiaf(oracleConnection, personAbsence, logger);
                    if (countThisPersonAbsenceOnGiaf == 0) {
                        personAbsence.setAnulationDate(new DateTime());
                        deleted++;
                    } else {
                        totalInFenix++;
                        if (countThisPersonAbsenceOnGiaf > 1) {
                            repeted += countThisPersonAbsenceOnGiaf - 1;
                        }
                    }
                }
            }
        }

        oracleConnection.closeConnection();
        log.println("-- Absences --");
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

    private int countThisPersonAbsenceOnGiaf(PersistentSuportGiaf oracleConnection, PersonAbsence personAbsence, Logger logger)
            throws SQLException {
        String query = getAbsencesQuery(personAbsence);
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
                                + personAbsence.getGiafProfessionalData().getGiafPersonIdentification() + " FA: "
                                + personAbsence.getAbsence().getGiafId());
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

    private String getAbsencesQuery(PersonAbsence personAbsence) {
        StringBuilder query = new StringBuilder();
        query.append("select count(*) as cont from SLHFLTINT where emp_num=");
        query.append(personAbsence.getGiafProfessionalData().getGiafPersonIdentification());
        if (personAbsence.getBeginDate() != null) {
            query.append(" and FLT_DT_INIC=to_date('");
            query.append(dateFormat.print(personAbsence.getBeginDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and FLT_DT_INIC is null");
        }
        if (personAbsence.getEndDate() != null) {
            query.append(" and FLT_DT_FIM=to_date('");
            query.append(dateFormat.print(personAbsence.getEndDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and FLT_DT_FIM is null");
        }
        if (personAbsence.getAbsence() != null) {
            query.append(" and flt_tip='");
            query.append(personAbsence.getAbsenceGiafId());
            query.append("'");
        } else {
            query.append(" and flt_tip is null");
        }
        query.append(" and data_criacao=to_date('");
        query.append(dateTimeFormat.print(personAbsence.getCreationDate()));
        query.append("','YYYY-MM-DD HH24:mi:ss')");
        if (personAbsence.getModifiedDate() != null) {
            query.append(" and data_alteracao=to_date('");
            query.append(dateTimeFormat.print(personAbsence.getModifiedDate()));
            query.append("','YYYY-MM-DD HH24:mi:ss')");
        } else {
            query.append("and data_alteracao is null");
        }
        return query.toString();

    }

    private boolean hasPersonAbsence(GiafProfessionalData giafProfessionalData, LocalDate beginDate, LocalDate endDate,
            Absence absence, String absenceGiafId, DateTime creationDate, DateTime modifiedDate) {
        for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData.getPersonProfessionalExemptions()) {
            if (personProfessionalExemption instanceof PersonAbsence) {
                PersonAbsence personAbsence = (PersonAbsence) personProfessionalExemption;
                if (personAbsence.getAnulationDate() == null && Objects.equals(personAbsence.getBeginDate(), beginDate)
                        && Objects.equals(endDate, personAbsence.getEndDate())
                        && Objects.equals(absence, personAbsence.getAbsence())
                        && Objects.equals(absenceGiafId, personAbsence.getAbsenceGiafId())
                        && Objects.equals(creationDate, personAbsence.getCreationDate())
                        && Objects.equals(modifiedDate, personAbsence.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getQuery(GiafMetadata metadata) {
        StringBuilder query = new StringBuilder();
        query.append("select emp_num, flt_tip, FLT_DT_INIC, FLT_DT_FIM, data_criacao, data_alteracao from SLHFLTINT where FLT_TIP in (");
        query.append(getAbsencesToImport(metadata)).append(")");
        return query.toString();
    }

    private String getAbsencesToImport(GiafMetadata metadata) {
        List<String> absences = new ArrayList<String>();
        for (Absence absence : metadata.absences()) {
            if (absence.getImportAbsence()) {
                absences.add("'" + absence.getGiafId() + "'");
            }
        }

        return absences.size() == 0 ? "null" : Joiner.on(',').join(absences);
    }
}

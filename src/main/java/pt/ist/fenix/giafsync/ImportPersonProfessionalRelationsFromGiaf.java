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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRelation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRelation;
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

class ImportPersonProfessionalRelationsFromGiaf extends ImportProcessor {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonProfessionalRelationsFromGiaf() {

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
            final String professionalRelationGiafId = result.getString("emp_vinculo");
            final ProfessionalRelation professionalRelation = metadata.relation(professionalRelationGiafId);
            if (professionalRelation == null) {
                logger.debug("Empty relation: " + professionalRelationGiafId + " for person number:" + numberString);
                importedButInvalid.add(person);
            }
            String beginDateString = result.getString("emp_dt_inic");
            final LocalDate beginDate =
                    StringUtils.isEmpty(beginDateString) ? null : new LocalDate(Timestamp.valueOf(beginDateString));
            if (beginDate == null) {
                logger.debug("Empty beginDate. Person number: " + numberString + " Relation: " + professionalRelationGiafId);
                importedButInvalid.add(person);
            }
            String endDateString = result.getString("emp_dt_fim");
            final LocalDate endDate = StringUtils.isEmpty(endDateString) ? null : new LocalDate(Timestamp.valueOf(endDateString));
            if (endDate != null) {
                if (beginDate != null && beginDate.isAfter(endDate)) {
                    logger.debug("BeginDate after endDate. Person number: " + numberString + " begin: " + beginDate + " end:"
                            + endDate);
                    importedButInvalid.add(person);
                }
            }

            final String professionalCategoryGiafId = result.getString("emp_cat_func");
            final ProfessionalCategory professionalCategory = metadata.category(professionalCategoryGiafId);
            if ((!StringUtils.isEmpty(professionalCategoryGiafId)) && professionalCategory == null) {
                logger.debug("Empty category " + professionalCategoryGiafId + ". Person number: " + numberString);
                importedButInvalid.add(person);
            }

            String creationDateString = result.getString("data_criacao");
            if (StringUtils.isEmpty(creationDateString)) {
                logger.debug("Empty creationDate. Person number: " + numberString + " Relation: " + professionalRelationGiafId);
                continue;
            }
            final DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

            String modifiedDateString = result.getString("data_alteracao");
            final DateTime modifiedDate =
                    StringUtils.isEmpty(modifiedDateString) ? null : new DateTime(Timestamp.valueOf(modifiedDateString));

            if (!hasPersonProfessionalRelation(giafProfessionalData, beginDate, endDate, professionalRelation,
                    professionalRelationGiafId, professionalCategory, professionalCategoryGiafId, creationDate, modifiedDate)) {
                modifications.add(new Modification() {
                    @Override
                    public void execute() {
                        new PersonProfessionalRelation(giafProfessionalData, beginDate, endDate, professionalRelation,
                                professionalRelationGiafId, professionalCategory, professionalCategoryGiafId, creationDate,
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
            for (final PersonProfessionalRelation personProfessionalRelation : giafProfessionalData
                    .getPersonProfessionalRelationsSet()) {
                if (personProfessionalRelation.getAnulationDate() == null) {
                    int countThisPersonProfessionalRelationOnGiaf =
                            countThisPersonProfessionalRelationOnGiaf(oracleConnection, personProfessionalRelation, logger);
                    if (countThisPersonProfessionalRelationOnGiaf == 0) {
                        modifications.add(new Modification() {
                            @Override
                            public void execute() {
                                personProfessionalRelation.setAnulationDate(new DateTime());
                            }
                        });
                        deleted++;
                    } else {
                        totalInFenix++;
                        if (countThisPersonProfessionalRelationOnGiaf > 1) {
                            repeted += countThisPersonProfessionalRelationOnGiaf - 1;
                        }
                    }
                }
            }
        }

        oracleConnection.closeConnection();
        log.println("-- Professional relations --");
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

    private int countThisPersonProfessionalRelationOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonProfessionalRelation personProfessionalRelation, Logger logger) throws SQLException {
        String query = getPersonProfessionalRelationQuery(personProfessionalRelation);
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
                                + personProfessionalRelation.getGiafProfessionalData().getGiafPersonIdentification()
                                + " Category: " + personProfessionalRelation.getProfessionalRelation().getGiafId());
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

    private String getPersonProfessionalRelationQuery(PersonProfessionalRelation personProfessionalRelation) {
        StringBuilder query = new StringBuilder();
        query.append("select count(*) as cont from sltprog_vinc where emp_num=");
        query.append(personProfessionalRelation.getGiafProfessionalData().getGiafPersonIdentification());
        if (personProfessionalRelation.getBeginDate() != null) {
            query.append(" and emp_dt_inic=to_date('");
            query.append(dateFormat.print(personProfessionalRelation.getBeginDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and emp_dt_inic is null");
        }
        if (personProfessionalRelation.getEndDate() != null) {
            query.append(" and emp_dt_fim=to_date('");
            query.append(dateFormat.print(personProfessionalRelation.getEndDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and emp_dt_fim is null");
        }
        if (personProfessionalRelation.getProfessionalRelation() != null) {
            query.append(" and emp_vinculo=");
            query.append(personProfessionalRelation.getProfessionalRelationGiafId());
        } else {
            query.append(" and emp_vinculo is null");
        }
        if (personProfessionalRelation.getProfessionalCategory() != null) {
            query.append(" and emp_cat_func=");
            query.append(personProfessionalRelation.getProfessionalCategoryGiafId());
        } else {
            query.append(" and emp_cat_func is null");
        }
        query.append(" and data_criacao=to_date('");
        query.append(dateTimeFormat.print(personProfessionalRelation.getCreationDate()));
        query.append("','YYYY-MM-DD HH24:mi:ss')");
        if (personProfessionalRelation.getModifiedDate() != null) {
            query.append(" and data_alteracao=to_date('");
            query.append(dateTimeFormat.print(personProfessionalRelation.getModifiedDate()));
            query.append("','YYYY-MM-DD HH24:mi:ss')");
        } else {
            query.append("and data_alteracao is null");
        }
        return query.toString();

    }

    private boolean hasPersonProfessionalRelation(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
            LocalDate endDate, ProfessionalRelation professionalRelation, String professionalRelationGiafId,
            ProfessionalCategory professionalCategory, String professionalCategoryGiafId, DateTime creationDate,
            DateTime modifiedDate) {
        for (PersonProfessionalRelation personProfessionalRelation : giafProfessionalData.getPersonProfessionalRelationsSet()) {
            if (personProfessionalRelation.getAnulationDate() == null) {
                if (Objects.equals(beginDate, personProfessionalRelation.getBeginDate())
                        && Objects.equals(endDate, personProfessionalRelation.getEndDate())
                        && Objects.equals(professionalRelation, personProfessionalRelation.getProfessionalRelation())
                        && Objects.equals(professionalRelationGiafId, personProfessionalRelation.getProfessionalRelationGiafId())
                        && Objects.equals(professionalCategory, personProfessionalRelation.getProfessionalCategory())
                        && Objects.equals(professionalCategoryGiafId, personProfessionalRelation.getProfessionalCategoryGiafId())
                        && Objects.equals(creationDate, personProfessionalRelation.getCreationDate())
                        && Objects.equals(modifiedDate, personProfessionalRelation.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getQuery() {
        return "SELECT a.emp_num, a.emp_vinculo, a.emp_cat_func, a.emp_dt_inic, a.emp_dt_fim, a.data_criacao, a.data_alteracao FROM sltprog_vinc a";
    }
}

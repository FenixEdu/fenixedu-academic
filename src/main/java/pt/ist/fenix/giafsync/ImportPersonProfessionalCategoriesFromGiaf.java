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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
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

class ImportPersonProfessionalCategoriesFromGiaf extends ImportProcessor {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Modification> processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        List<Modification> modifications = new ArrayList<>();
        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(getQuery());
        ResultSet result = preparedStatement.executeQuery();
        int count = 0;
        int novos = 0;
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
            final String professionalCategoryGiafId = result.getString("emp_cat_func");
            final ProfessionalCategory professionalCategory = metadata.category(professionalCategoryGiafId);
            if (professionalCategory == null) {
                logger.debug("Empty category : " + professionalCategoryGiafId + " for person number: " + numberString);
                importedButInvalid.add(person);
            }
            String beginDateString = result.getString("emp_dt_inicio");
            final LocalDate beginDate =
                    StringUtils.isEmpty(beginDateString) ? null : new LocalDate(Timestamp.valueOf(beginDateString));
            if (beginDate == null) {
                logger.debug("Empty beginDate. Person number: " + numberString + " category: " + professionalCategoryGiafId);
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

            final String step = result.getString("emp_escalao");
            final String level = result.getString("emp_venc_nvl");

            // a.emp_regime, a.emp_vinculo
            final String professionalRegimeGiafId = result.getString("emp_regime");
            final ProfessionalRegime professionalRegime = metadata.regime(professionalRegimeGiafId);
            if (professionalRegime == null && (!StringUtils.isEmpty(professionalRegimeGiafId))) {
                logger.debug("Empty regime: " + professionalRegimeGiafId + ". Person number: " + numberString);
                importedButInvalid.add(person);
            }

            final String professionalRelationGiafId = result.getString("emp_vinculo");
            final ProfessionalRelation professionalRelation = metadata.relation(professionalRelationGiafId);
            if (professionalRelation == null && (!StringUtils.isEmpty(professionalRelationGiafId))) {
                logger.debug("Empty relation: " + professionalRelationGiafId + ". Person number: " + numberString);
                importedButInvalid.add(person);
            }
            // ,a.data_criacao,a.data_alteracao
            String creationDateString = result.getString("data_criacao");
            if (StringUtils.isEmpty(creationDateString)) {
                logger.debug("Empty creationDate. Person number: " + numberString + " Category: " + professionalCategoryGiafId);
                notImported++;
                continue;
            }
            final DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

            String modifiedDateString = result.getString("data_alteracao");
            final DateTime modifiedDate =
                    StringUtils.isEmpty(modifiedDateString) ? null : new DateTime(Timestamp.valueOf(modifiedDateString));

            if (!hasPersonProfessionalCategory(giafProfessionalData, beginDate, endDate, professionalCategory,
                    professionalCategoryGiafId, professionalRegime, professionalRegimeGiafId, professionalRelation,
                    professionalRelationGiafId, step, level, creationDate, modifiedDate)) {
                modifications.add(new Modification() {
                    @Override
                    public void execute() {
                        new PersonProfessionalCategory(giafProfessionalData, beginDate, endDate, professionalCategory,
                                professionalCategoryGiafId, professionalRegime, professionalRegimeGiafId, professionalRelation,
                                professionalRelationGiafId, step, level, creationDate, modifiedDate);
                    }
                });
                novos++;
            }

        }
        result.close();
        preparedStatement.close();

        int deleted = 0;
        int totalInFenix = 0;
        int repeted = 0;
        for (GiafProfessionalData giafProfessionalData : Bennu.getInstance().getGiafProfessionalDataSet()) {
            for (final PersonProfessionalCategory personProfessionalCategory : giafProfessionalData
                    .getPersonProfessionalCategoriesSet()) {
                if (personProfessionalCategory.getAnulationDate() == null) {
                    int countThisPersonProfessionalCategoryOnGiaf =
                            countThisPersonProfessionalCategoryOnGiaf(oracleConnection, personProfessionalCategory, logger);
                    if (countThisPersonProfessionalCategoryOnGiaf == 0) {
                        modifications.add(new Modification() {
                            @Override
                            public void execute() {
                                personProfessionalCategory.setAnulationDate(new DateTime());
                            }
                        });
                        deleted++;
                    } else {
                        if (countThisPersonProfessionalCategoryOnGiaf > 1) {
                            repeted += countThisPersonProfessionalCategoryOnGiaf - 1;
                        }
                        totalInFenix++;
                    }
                }
            }
        }

        oracleConnection.closeConnection();
        log.println("-- Professional categories --");
        log.println("Total GIAF: " + count);
        log.println("New: " + novos);
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

    private int countThisPersonProfessionalCategoryOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonProfessionalCategory personProfessionalCategory, Logger logger) throws SQLException {
        String query = getPersonProfessionalCategoryQuery(personProfessionalCategory);
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
                                + personProfessionalCategory.getGiafProfessionalData().getGiafPersonIdentification() + " Cat: "
                                + personProfessionalCategory.getProfessionalCategory().getGiafId());
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

    private String getPersonProfessionalCategoryQuery(PersonProfessionalCategory personProfessionalCategory) {
        StringBuilder query = new StringBuilder();
        query.append("select count(*) as cont from sldemp25 where emp_num=");
        query.append(personProfessionalCategory.getGiafProfessionalData().getGiafPersonIdentification());
        if (personProfessionalCategory.getBeginDate() != null) {
            query.append(" and emp_dt_inicio=to_date('");
            query.append(dateFormat.print(personProfessionalCategory.getBeginDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and emp_dt_inicio is null");
        }
        if (personProfessionalCategory.getEndDate() != null) {
            query.append(" and emp_dt_fim=to_date('");
            query.append(dateFormat.print(personProfessionalCategory.getEndDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and emp_dt_fim is null");
        }

        if (personProfessionalCategory.getProfessionalCategory() != null) {
            query.append(" and emp_cat_func=");
            query.append(personProfessionalCategory.getProfessionalCategoryGiafId());
        } else {
            query.append(" and emp_cat_func is null");
        }

        if (personProfessionalCategory.getProfessionalRegime() != null) {
            query.append(" and emp_regime=");
            query.append(personProfessionalCategory.getProfessionalRegimeGiafId());
        } else {
            query.append(" and emp_regime is null");
        }
        if (personProfessionalCategory.getProfessionalRelation() != null) {
            query.append(" and emp_vinculo=");
            query.append(personProfessionalCategory.getProfessionalRelationGiafId());
        } else {
            query.append(" and emp_vinculo is null");
        }

        if (personProfessionalCategory.getStep() != null) {
            query.append(" and emp_escalao='");
            query.append(personProfessionalCategory.getStep());
            query.append("'");
        } else {
            query.append(" and emp_escalao is null");
        }
        if (personProfessionalCategory.getLevel() != null) {
            query.append(" and emp_venc_nvl='");
            query.append(personProfessionalCategory.getLevel());
            query.append("'");
        } else {
            query.append(" and emp_venc_nvl is null");
        }
        query.append(" and data_criacao=to_date('");
        query.append(dateTimeFormat.print(personProfessionalCategory.getCreationDate()));
        query.append("','YYYY-MM-DD HH24:mi:ss')");
        if (personProfessionalCategory.getModifiedDate() != null) {
            query.append(" and data_alteracao=to_date('");
            query.append(dateTimeFormat.print(personProfessionalCategory.getModifiedDate()));
            query.append("','YYYY-MM-DD HH24:mi:ss')");
        } else {
            query.append("and data_alteracao is null");
        }
        return query.toString();

    }

    private boolean hasPersonProfessionalCategory(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
            LocalDate endDate, ProfessionalCategory professionalCategory, String professionalCategoryGiafId,
            ProfessionalRegime professionalRegime, String professionalRegimeGiafId, ProfessionalRelation professionalRelation,
            String professionalRelationGiafId, String step, String level, DateTime creationDate, DateTime modifiedDate) {
        for (PersonProfessionalCategory personProfessionalCategory : giafProfessionalData.getPersonProfessionalCategoriesSet()) {
            if (personProfessionalCategory.getAnulationDate() == null) {
                if (Objects.equals(beginDate, personProfessionalCategory.getBeginDate())
                        && Objects.equals(endDate, personProfessionalCategory.getEndDate())
                        && Objects.equals(professionalCategory, personProfessionalCategory.getProfessionalCategory())
                        && Objects.equals(professionalCategoryGiafId, personProfessionalCategory.getProfessionalCategoryGiafId())
                        && Objects.equals(professionalRegime, personProfessionalCategory.getProfessionalRegime())
                        && Objects.equals(professionalRegimeGiafId, personProfessionalCategory.getProfessionalRegimeGiafId())
                        && Objects.equals(professionalRelation, personProfessionalCategory.getProfessionalRelation())
                        && Objects.equals(professionalRelationGiafId, personProfessionalCategory.getProfessionalRelationGiafId())
                        && Objects.equals(step, personProfessionalCategory.getStep())
                        && Objects.equals(level, personProfessionalCategory.getLevel())
                        && Objects.equals(creationDate, personProfessionalCategory.getCreationDate())
                        && Objects.equals(modifiedDate, personProfessionalCategory.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getQuery() {
        return "SELECT a.emp_num, a.emp_dt_inicio, a.emp_dt_fim, a.emp_cat_func, a.emp_escalao, a.emp_venc_nvl, a.emp_regime, a.emp_vinculo, a.data_criacao, a.data_alteracao FROM sldemp25 a";
    }
}

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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRelation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRelation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import org.apache.commons.lang.StringUtils;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Task(englishTitle = "ImportPersonProfessionalRelationsFromGiaf")
public class ImportPersonProfessionalRelationsFromGiaf extends ImportFromGiaf {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonProfessionalRelationsFromGiaf() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportPersonProfessionalRelationsFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, ProfessionalCategory> professionalCategoriesMap = getProfessionalCategoriesMap();
            Map<String, ProfessionalRelation> professionalRelationsMap = getProfessionalRelationsMap();
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
                    getLogger().info("Empty personProfessionalData: " + numberString);
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
                String professionalRelationGiafId = result.getString("emp_vinculo");
                ProfessionalRelation professionalRelation = professionalRelationsMap.get(professionalRelationGiafId);
                if (professionalRelation == null) {
                    getLogger().info("Empty relation: " + professionalRelationGiafId + " for person number:" + numberString);
                    importedButInvalid.add(person);
                }
                String beginDateString = result.getString("emp_dt_inic");
                LocalDate beginDate = null;
                if (!StringUtils.isEmpty(beginDateString)) {
                    beginDate = new LocalDate(Timestamp.valueOf(beginDateString));
                }
                if (beginDate == null) {
                    getLogger().info(
                            "Empty beginDate. Person number: " + numberString + " Relation: " + professionalRelationGiafId);
                    importedButInvalid.add(person);
                }
                String endDateString = result.getString("emp_dt_fim");
                LocalDate endDate = null;
                if (!StringUtils.isEmpty(endDateString)) {
                    endDate = new LocalDate(Timestamp.valueOf(endDateString));
                }
                if (endDate != null) {
                    if (beginDate != null && beginDate.isAfter(endDate)) {
                        getLogger().info(
                                "BeginDate after endDate. Person number: " + numberString + " begin: " + beginDate + " end:"
                                        + endDate);
                        importedButInvalid.add(person);
                    }
                }

                String professionalCategoryGiafId = result.getString("emp_cat_func");
                ProfessionalCategory professionalCategory = professionalCategoriesMap.get(professionalCategoryGiafId);
                if ((!StringUtils.isEmpty(professionalCategoryGiafId)) && professionalCategory == null) {
                    getLogger().info("Empty category " + professionalCategoryGiafId + ". Person number: " + numberString);
                    importedButInvalid.add(person);
                }

                String creationDateString = result.getString("data_criacao");
                if (StringUtils.isEmpty(creationDateString)) {
                    getLogger().info(
                            "Empty creationDate. Person number: " + numberString + " Relation: " + professionalRelationGiafId);
                    continue;
                }
                DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

                DateTime modifiedDate = null;
                String modifiedDateString = result.getString("data_alteracao");
                if (!StringUtils.isEmpty(modifiedDateString)) {
                    modifiedDate = new DateTime(Timestamp.valueOf(modifiedDateString));
                }

                if (!hasPersonProfessionalRelation(giafProfessionalData, beginDate, endDate, professionalRelation,
                        professionalRelationGiafId, professionalCategory, professionalCategoryGiafId, creationDate, modifiedDate)) {
                    new PersonProfessionalRelation(giafProfessionalData, beginDate, endDate, professionalRelation,
                            professionalRelationGiafId, professionalCategory, professionalCategoryGiafId, creationDate,
                            modifiedDate);
                    news++;
                }
            }
            result.close();
            preparedStatement.close();

            int deleted = 0;
            int totalInFenix = 0;
            int repeted = 0;
            for (GiafProfessionalData giafProfessionalData : Bennu.getInstance().getGiafProfessionalDataSet()) {
                for (PersonProfessionalRelation personProfessionalRelation : giafProfessionalData
                        .getPersonProfessionalRelations()) {
                    if (personProfessionalRelation.getAnulationDate() == null) {
                        int countThisPersonProfessionalRelationOnGiaf =
                                countThisPersonProfessionalRelationOnGiaf(oracleConnection, personProfessionalRelation);
                        if (countThisPersonProfessionalRelationOnGiaf == 0) {
                            personProfessionalRelation.setAnulationDate(new DateTime());
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
            getLogger().info("ImportPersonProfessionalRelationsFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonProfessionalRelationsFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private int countThisPersonProfessionalRelationOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonProfessionalRelation personProfessionalRelation) throws ExcepcaoPersistencia, SQLException {
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
                        getLogger().info(
                                "---> " + count + " ---> "
                                        + personProfessionalRelation.getGiafProfessionalData().getGiafPersonIdentification()
                                        + " Category: " + personProfessionalRelation.getProfessionalRelation().getGiafId());
                    }
                    return count;
                }
            }
            getLogger().info(query);
            return 0;
        } catch (ExcepcaoPersistencia e) {
            getLogger().info(
                    "ImportPersonProfessionalRelationsFromGiaf -  ERRO ExcepcaoPersistencia hasPersonProfessionalRelationOnGiaf");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger()
                    .info("ImportPersonProfessionalRelationsFromGiaf -  ERRO SQLException hasPersonProfessionalRelationOnGiaf "
                            + query);
            throw new Error(e);
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
        for (PersonProfessionalRelation personProfessionalRelation : giafProfessionalData.getPersonProfessionalRelations()) {
            if (personProfessionalRelation.getAnulationDate() == null) {
                if (equal(beginDate, personProfessionalRelation.getBeginDate())
                        && equal(endDate, personProfessionalRelation.getEndDate())
                        && equal(professionalRelation, personProfessionalRelation.getProfessionalRelation())
                        && equal(professionalRelationGiafId, personProfessionalRelation.getProfessionalRelationGiafId())
                        && equal(professionalCategory, personProfessionalRelation.getProfessionalCategory())
                        && equal(professionalCategoryGiafId, personProfessionalRelation.getProfessionalCategoryGiafId())
                        && equal(creationDate, personProfessionalRelation.getCreationDate())
                        && equal(modifiedDate, personProfessionalRelation.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected String getQuery() {
        return "SELECT a.emp_num, a.emp_vinculo, a.emp_cat_func, a.emp_dt_inic, a.emp_dt_fim, a.data_criacao, a.data_alteracao FROM sltprog_vinc a";
    }

}

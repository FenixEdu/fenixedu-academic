package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Task(englishTitle = "ImportPersonContractSituationsFromGiaf")
public class ImportPersonContractSituationsFromGiaf extends ImportFromGiaf {

    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void process() {
        getLogger().debug("Start ImportPersonContractSituationsFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, ContractSituation> contractSituationsMap = getContractSituationsMap();
            Map<Integer, Employee> employeesMap = getEmployeesMap();
            Map<String, ProfessionalCategory> contractCategoriesMap = getProfessionalCategoriesMap();
            String query = getQuery();
            getLogger().debug(query);
            PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            int count = 0;
            int news = 0;
            int notImported = 0;
            Set<Person> importedButInvalid = new HashSet<Person>();
            int dontExist = 0;
            int deleted = 0;
            int totalInFenix = 0;
            int repeted = 0;
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

                String contractSituationGiafId = result.getString("emp_sit");
                ContractSituation contractSituation = contractSituationsMap.get(contractSituationGiafId);
                if (contractSituation == null) {
                    getLogger().info("Invalid situation: " + contractSituationGiafId);
                    importedButInvalid.add(person);
                }

                String categoryGiafId = result.getString("emp_cat_func");
                ProfessionalCategory category = contractCategoriesMap.get(categoryGiafId);
                if (category == null && (!StringUtils.isEmpty(categoryGiafId))) {
                    getLogger().info("Empty category: " + categoryGiafId + ". Person number: " + numberString);
                    importedButInvalid.add(person);
                }
                if (category == null && (contractSituation == null || contractSituation.getEndSituation() == false)) {
                    getLogger()
                            .info("Empty catefory on a non end situation: " + numberString + " . Situation: "
                                    + contractSituationGiafId);
                    importedButInvalid.add(person);
                }

                String beginDateString = result.getString("dt_inic");
                LocalDate beginDate = null;
                if (!StringUtils.isEmpty(beginDateString)) {
                    beginDate = new LocalDate(Timestamp.valueOf(beginDateString));
                }
                if (beginDate == null) {
                    getLogger().info("Empty beginDate: " + numberString + " . Situation: " + contractSituationGiafId);
                    importedButInvalid.add(person);
                }
                String endDateString = result.getString("dt_fim");
                LocalDate endDate = null;
                if (!StringUtils.isEmpty(endDateString)) {
                    endDate = new LocalDate(Timestamp.valueOf(endDateString));
                }
                if (endDate != null) {
                    if (beginDate != null && beginDate.isAfter(endDate)) {
                        getLogger().info("BeginDate after EndDate: " + numberString + " begin: " + beginDate + " end:" + endDate);
                        importedButInvalid.add(person);
                    }
                }
                String creationDateString = result.getString("data_criacao");
                if (StringUtils.isEmpty(creationDateString)) {
                    getLogger().info("Empty creationDate: " + numberString + " . Situation: " + contractSituationGiafId);
                    notImported++;
                    continue;
                }
                DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

                DateTime modifiedDate = null;
                String modifiedDateString = result.getString("data_alteracao");
                if (!StringUtils.isEmpty(modifiedDateString)) {
                    modifiedDate = new DateTime(Timestamp.valueOf(modifiedDateString));
                }

                String step = result.getString("emp_escalao");

                if (!hasPersonContractSituation(giafProfessionalData, beginDate, endDate, creationDate, modifiedDate, step,
                        contractSituation, contractSituationGiafId, category, categoryGiafId)) {
                    new PersonContractSituation(giafProfessionalData, beginDate, endDate, step, contractSituation,
                            contractSituationGiafId, category, categoryGiafId, creationDate, modifiedDate);
                    news++;
                }

            }
            result.close();
            preparedStatement.close();

            for (GiafProfessionalData giafProfessionalData : Bennu.getInstance().getGiafProfessionalDataSet()) {
                for (PersonContractSituation personContractSituation : giafProfessionalData.getPersonContractSituations()) {
                    if (personContractSituation.getAnulationDate() == null) {
                        int countThisPersonContractSituationOnGiaf =
                                countThisPersonContractSituationOnGiaf(oracleConnection, personContractSituation);
                        if (countThisPersonContractSituationOnGiaf == 0) {
                            personContractSituation.setAnulationDate(new DateTime());
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
            getLogger().info("ImportPersonContractSituationsFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonContractSituationsFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private int countThisPersonContractSituationOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonContractSituation personContractSituation) throws SQLException, ExcepcaoPersistencia {
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
                        getLogger().info(
                                "---> " + count + " ---> "
                                        + personContractSituation.getGiafProfessionalData().getGiafPersonIdentification()
                                        + " Situation: " + personContractSituation.getContractSituation().getGiafId());
                    }
                    return count;
                }
            }
            // getLogger().info(query);
            return 0;
        } catch (ExcepcaoPersistencia e) {
            getLogger().info(
                    "ImportPersonContractSituationsFromGiaf -  ERRO ExcepcaoPersistencia hasPersonContractSituationOnGiaf");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info(
                    "ImportPersonContractSituationsFromGiaf -  ERRO SQLException hasPersonContractSituationOnGiaf " + query);
            throw new Error(e);
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
        for (PersonContractSituation personContractSituation : giafProfessionalData.getPersonContractSituations()) {
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

    @Override
    protected String getQuery() {
        return "select emp_num, dt_inic, dt_fim, emp_cat_func, emp_sit, data_criacao, data_alteracao, emp_escalao from sldemp24 where emp_sit is not null";
    }

}

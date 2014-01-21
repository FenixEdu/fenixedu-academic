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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Task(englishTitle = "ImportPersonProfessionalRegimesFromGiaf")
public class ImportPersonProfessionalRegimesFromGiaf extends ImportFromGiaf {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonProfessionalRegimesFromGiaf() {

    }

    @Override
    public void process() {
        getLogger().debug("Start ImportPersonProfessionalRegimesFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, ProfessionalRegime> professionalRegimesMap = getProfessionalRegimesMap();
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
                String professionalRegimeGiafId = result.getString("emp_regime");
                ProfessionalRegime professionalRegime = professionalRegimesMap.get(professionalRegimeGiafId);
                if (professionalRegime == null) {
                    getLogger().info("Empty regime: " + professionalRegimeGiafId + " person number" + numberString);
                    importedButInvalid.add(person);
                }
                String beginDateString = result.getString("emp_regime_dt");
                LocalDate beginDate = null;
                if (!StringUtils.isEmpty(beginDateString)) {
                    beginDate = new LocalDate(Timestamp.valueOf(beginDateString));
                }
                if (beginDate == null) {
                    getLogger().info("Empty beginDate. Person number: " + numberString + " Regime: " + professionalRegimeGiafId);
                    importedButInvalid.add(person);
                }
                String endDateString = result.getString("emp_regime_dt_fim");
                LocalDate endDate = null;
                if (!StringUtils.isEmpty(endDateString)) {
                    endDate = new LocalDate(Timestamp.valueOf(endDateString));
                }
                if (endDate != null) {
                    if (beginDate != null && beginDate.isAfter(endDate)) {
                        getLogger().info(
                                "BeginDate after EndDate. Person number: " + numberString + " begin: " + beginDate + " end:"
                                        + endDate);
                        importedButInvalid.add(person);
                    }
                }
                // ,a.data_criacao,a.data_alteracao
                String creationDateString = result.getString("data_criacao");
                if (StringUtils.isEmpty(creationDateString)) {
                    getLogger().info(
                            "Empty creationDate. Person number: " + numberString + " Regime: " + professionalRegimeGiafId);
                    notImported++;
                    continue;
                }
                DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

                DateTime modifiedDate = null;
                String modifiedDateString = result.getString("data_alteracao");
                if (!StringUtils.isEmpty(modifiedDateString)) {
                    modifiedDate = new DateTime(Timestamp.valueOf(modifiedDateString));
                }

                if (!hasPersonProfessionalRegime(giafProfessionalData, beginDate, endDate, professionalRegime,
                        professionalRegimeGiafId, creationDate, modifiedDate)) {
                    new PersonProfessionalRegime(giafProfessionalData, beginDate, endDate, professionalRegime,
                            professionalRegimeGiafId, creationDate, modifiedDate);
                    news++;
                }
            }
            result.close();
            preparedStatement.close();

            int deleted = 0;
            int totalInFenix = 0;
            int repeted = 0;
            for (GiafProfessionalData giafProfessionalData : Bennu.getInstance().getGiafProfessionalDataSet()) {
                for (PersonProfessionalRegime personProfessionalRegime : giafProfessionalData.getPersonProfessionalRegimes()) {
                    if (personProfessionalRegime.getAnulationDate() == null) {
                        int countThisPersonProfessionalRegimeOnGiaf =
                                countThisPersonProfessionalRegimeOnGiaf(oracleConnection, personProfessionalRegime);
                        if (countThisPersonProfessionalRegimeOnGiaf == 0) {
                            personProfessionalRegime.setAnulationDate(new DateTime());
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
            getLogger().info("ImportPersonProfessionalRegimesFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonProfessionalRegimesFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private int countThisPersonProfessionalRegimeOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonProfessionalRegime personProfessionalRegime) throws ExcepcaoPersistencia, SQLException {
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
                        getLogger().info(
                                "---> " + count + " ---> "
                                        + personProfessionalRegime.getGiafProfessionalData().getGiafPersonIdentification()
                                        + " Regime: " + personProfessionalRegime.getProfessionalRegime().getGiafId());
                    }
                    return count;
                }
            }
            return 0;
        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportPersonProfessionalRegimesFromGiaf -  ERRO ExcepcaoPersistencia findPersonsSituationOnGiaf");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonProfessionalRegimesFromGiaf -  ERRO SQLException findPersonSituationOnGiaf " + query);
            throw new Error(e);
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
        for (PersonProfessionalRegime personProfessionalRegime : giafProfessionalData.getPersonProfessionalRegimes()) {
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

    @Override
    protected String getQuery() {
        return "SELECT a.emp_num, a.emp_regime, a.emp_regime_dt, a.emp_regime_dt_fim, a.data_criacao, a.data_alteracao FROM sldemp26 a";
    }

}

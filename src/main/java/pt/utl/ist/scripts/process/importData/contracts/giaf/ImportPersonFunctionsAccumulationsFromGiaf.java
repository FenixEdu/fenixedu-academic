package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.FunctionsAccumulation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonFunctionsAccumulation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.scheduler.annotation.Task;

@Task(englishTitle = "ImportPersonFunctionsAccumulationsFromGiaf")
public class ImportPersonFunctionsAccumulationsFromGiaf extends ImportFromGiaf {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonFunctionsAccumulationsFromGiaf() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportPersonFunctionsAccumulationsFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, ProfessionalRegime> professionalRegimesMap = getProfessionalRegimesMap();
            Map<String, FunctionsAccumulation> functionsAccumulationsMap = getFunctionsAccumulationsMap();
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

                String functionsAccumulationGiafId = result.getString("TIPO");
                FunctionsAccumulation functionsAccumulation = functionsAccumulationsMap.get(functionsAccumulationGiafId);
                if (functionsAccumulation == null) {
                    getLogger()
                            .info("Empty FunctionsAccumulation: " + functionsAccumulationGiafId + " for person number: "
                                    + numberString);
                    importedButInvalid.add(person);
                }
                String beginDateString = result.getString("DATA_INICIO");
                LocalDate beginDate = null;
                if (!StringUtils.isEmpty(beginDateString)) {
                    beginDate = new LocalDate(Timestamp.valueOf(beginDateString));
                }
                if (beginDate == null) {
                    getLogger().info(
                            "Empty beginDate: " + numberString + " FunctionsAccumulation: " + functionsAccumulationGiafId);
                    importedButInvalid.add(person);
                }

                String endDateString = result.getString("DATA_FIM");
                LocalDate endDate = null;
                if (!StringUtils.isEmpty(endDateString)) {
                    endDate = new LocalDate(Timestamp.valueOf(endDateString));
                }
                if (beginDate != null && endDate != null) {
                    if (beginDate != null && beginDate.isAfter(endDate)) {
                        getLogger().info(
                                "BeginDate after EndDate. Person number: " + numberString + ". begin: " + beginDate + " end:"
                                        + endDate);
                        importedButInvalid.add(person);
                    }
                }
                BigDecimal hours = result.getBigDecimal("horas_sem");

                String professionalRegimeGiafId = result.getString("EMP_REGIME");
                ProfessionalRegime professionalRegime = professionalRegimesMap.get(professionalRegimeGiafId);
                if (professionalRegime == null) {
                    getLogger().info("Empty regime: " + numberString);
                    importedButInvalid.add(person);
                }
                String creationDateString = result.getString("data_criacao");
                if (StringUtils.isEmpty(creationDateString)) {
                    getLogger().info(
                            "Empty creation Date. Person number: " + numberString + " FunctionsAccumulation: "
                                    + functionsAccumulationGiafId);
                    notImported++;
                    continue;
                }
                DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

                DateTime modifiedDate = null;
                String modifiedDateString = result.getString("data_alteracao");
                if (!StringUtils.isEmpty(modifiedDateString)) {
                    modifiedDate = new DateTime(Timestamp.valueOf(modifiedDateString));
                }

                if (!hasPersonFunctionsAccumulations(giafProfessionalData, beginDate, endDate, hours, functionsAccumulation,
                        functionsAccumulationGiafId, professionalRegime, professionalRegimeGiafId, creationDate, modifiedDate)) {
                    new PersonFunctionsAccumulation(giafProfessionalData, beginDate, endDate, hours, functionsAccumulation,
                            functionsAccumulationGiafId, professionalRegime, professionalRegimeGiafId, creationDate, modifiedDate);
                    news++;
                }
            }
            result.close();
            preparedStatement.close();

            int deleted = 0;
            int totalInFenix = 0;
            int repeted = 0;

            for (GiafProfessionalData giafProfessionalData : Bennu.getInstance().getGiafProfessionalDataSet()) {
                for (PersonFunctionsAccumulation personFunctionsAccumulation : giafProfessionalData
                        .getPersonFunctionsAccumulations()) {
                    if (personFunctionsAccumulation.getAnulationDate() == null) {
                        int countThisPersonFunctionsAccumulationOnGiaf =
                                countThisPersonFunctionsAccumulationOnGiaf(oracleConnection, personFunctionsAccumulation);
                        if (countThisPersonFunctionsAccumulationOnGiaf == 0) {
                            personFunctionsAccumulation.setAnulationDate(new DateTime());
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
            getLogger().info("Total GIAF: " + count);
            getLogger().info("News: " + news);
            getLogger().info("Deleted: " + deleted);
            getLogger().info("Not imported: " + notImported);
            getLogger().info("Imported with errors: " + importedButInvalid.size());
            getLogger().info("Repeted: " + repeted);
            getLogger().info("Invalid person or functionAccumulation: " + dontExist);
            getLogger().info("Total Fénix: " + totalInFenix);
            getLogger().info("Total Fénix without errors: " + (totalInFenix - importedButInvalid.size()));
            getLogger().info("Missing in Fénix: " + (count - totalInFenix));

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportPersonFunctionsAccumulationsFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonFunctionsAccumulationsFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private int countThisPersonFunctionsAccumulationOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonFunctionsAccumulation personFunctionsAccumulation) throws ExcepcaoPersistencia, SQLException {
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
                        getLogger().info(
                                "---> " + count + " ---> "
                                        + personFunctionsAccumulation.getGiafProfessionalData().getGiafPersonIdentification()
                                        + " FA: " + personFunctionsAccumulation.getFunctionsAccumulation().getGiafId());
                    }
                    return count;
                }
            }
            getLogger().info(query);
            return 0;
        } catch (ExcepcaoPersistencia e) {
            getLogger()
                    .info("ImportPersonFunctionsAccumulationsFromGiaf -  ERRO ExcepcaoPersistencia hasPersonFunctionsAccumulationOnGiaf");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info(
                    "ImportPersonFunctionsAccumulationsFromGiaf -  ERRO SQLException hasPersonFunctionsAccumulationOnGiaf "
                            + query);
            throw new Error(e);
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
                if (equal(beginDate, personFunctionsAccumulation.getBeginDate())
                        && equal(endDate, personFunctionsAccumulation.getEndDate())
                        && equal(hours, personFunctionsAccumulation.getHours())
                        && equal(functionsAccumulation, personFunctionsAccumulation.getFunctionsAccumulation())
                        && equal(functionsAccumulationGiafId, personFunctionsAccumulation.getFunctionsAccumulationGiafId())
                        && equal(professionalRegime, personFunctionsAccumulation.getProfessionalRegime())
                        && equal(professionalRegimeGiafId, personFunctionsAccumulation.getProfessionalRegimeGiafId())
                        && equal(creationDate, personFunctionsAccumulation.getCreationDate())
                        && equal(modifiedDate, personFunctionsAccumulation.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected String getQuery() {
        return "SELECT af.DATA_FIM, af.DATA_INICIO, af.EMP_NUM, af.horas_sem,af.EMP_REGIME, af.TIPO, af.data_criacao, af.data_alteracao FROM SLDACUMFUNC af";
    }

}

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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalExemption;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonServiceExemption;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ServiceExemption;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import net.sourceforge.fenixedu.util.StringUtils;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Task(englishTitle = "ImportPersonServiceExemptionsFromGiaf")
public class ImportPersonServiceExemptionsFromGiaf extends ImportFromGiaf {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonServiceExemptionsFromGiaf() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportPersonServiceExemptionsFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, ServiceExemption> serviceExemptionsMap = getServiceExemptionsMap();
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
                String serviceExemptionGiafId = result.getString("tip_disp");
                ServiceExemption serviceExemption = serviceExemptionsMap.get(serviceExemptionGiafId);
                if (serviceExemption == null) {
                    getLogger().info("Empty serviceExemption: " + serviceExemptionGiafId + " for person number: " + numberString);
                    importedButInvalid.add(person);

                }
                String beginDateString = result.getString("DATA_INICIO");
                LocalDate beginDate = null;
                if (!StringUtils.isEmpty(beginDateString)) {
                    beginDate = new LocalDate(Timestamp.valueOf(beginDateString));
                }
                if (beginDate == null) {
                    getLogger().info(
                            "Empty beginDate. Person number: " + numberString + " ServiceExemption: " + serviceExemptionGiafId);
                    importedButInvalid.add(person);
                }
                String endDateString = result.getString("DATA_FIM");
                LocalDate endDate = null;
                if (!StringUtils.isEmpty(endDateString)) {
                    endDate = new LocalDate(Timestamp.valueOf(endDateString));
                }
                if (beginDate != null && endDate != null) {
                    if (beginDate.isAfter(endDate)) {
                        getLogger().info(
                                "BeginDate after endDate. Person number: " + numberString + " begin: " + beginDate + " end: "
                                        + endDate);
                        importedButInvalid.add(person);
                    }
                }
                String creationDateString = result.getString("data_criacao");
                if (StringUtils.isEmpty(creationDateString)) {
                    getLogger()
                            .info("Empty creationDate. Person number: " + numberString + " ServiceExemption: "
                                    + serviceExemptionGiafId);
                    notImported++;
                    continue;
                }
                DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

                DateTime modifiedDate = null;
                String modifiedDateString = result.getString("data_alteracao");
                if (!StringUtils.isEmpty(modifiedDateString)) {
                    modifiedDate = new DateTime(Timestamp.valueOf(modifiedDateString));
                }

                if (!hasPersonServiceExemption(giafProfessionalData, beginDate, endDate, serviceExemption,
                        serviceExemptionGiafId, creationDate, modifiedDate)) {
                    new PersonServiceExemption(giafProfessionalData, beginDate, endDate, serviceExemption,
                            serviceExemptionGiafId, creationDate, modifiedDate);
                    news++;
                }
            }
            result.close();
            preparedStatement.close();

            int deleted = 0;
            int totalInFenix = 0;
            int repeted = 0;
            for (GiafProfessionalData giafProfessionalData : Bennu.getInstance().getGiafProfessionalDataSet()) {
                for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptions()) {
                    if (personProfessionalExemption instanceof PersonServiceExemption
                            && personProfessionalExemption.getAnulationDate() == null) {
                        PersonServiceExemption personServiceExemption = (PersonServiceExemption) personProfessionalExemption;
                        int countThisPersonServiceExemptionOnGiaf =
                                countThisPersonServiceExemptionOnGiaf(oracleConnection, personServiceExemption);
                        if (countThisPersonServiceExemptionOnGiaf == 0) {
                            personServiceExemption.setAnulationDate(new DateTime());
                            deleted++;
                        } else {
                            totalInFenix++;
                            if (countThisPersonServiceExemptionOnGiaf > 1) {
                                repeted += countThisPersonServiceExemptionOnGiaf - 1;
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
            getLogger().info("Invalid person or ServiceExemption: " + dontExist);
            getLogger().info("Total Fénix: " + totalInFenix);
            getLogger().info("Total Fénix without errors: " + (totalInFenix - importedButInvalid.size()));
            getLogger().info("Missing in Fénix: " + (count - totalInFenix));

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportPersonServiceExemptionsFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonServiceExemptionsFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private int countThisPersonServiceExemptionOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonServiceExemption personServiceExemption) throws ExcepcaoPersistencia, SQLException {
        String query = getServiceExemptionQuery(personServiceExemption);
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
                                        + personServiceExemption.getGiafProfessionalData().getGiafPersonIdentification()
                                        + " FA: " + personServiceExemption.getServiceExemption().getGiafId());
                    }
                    return count;
                }
            }
            return 0;
        } catch (ExcepcaoPersistencia e) {
            getLogger()
                    .info("ImportPersonServiceExemptionsFromGiaf -  ERRO ExcepcaoPersistencia hasPersonServiceExemptionOnGiaf");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info(
                    "ImportPersonServiceExemptionsFromGiaf -  ERRO SQLException hasPersonServiceExemptionOnGiaf " + query);
            throw new Error(e);
        } finally {
            if (result != null) {
                result.close();
            }
            preparedStatement.close();
        }
    }

    private String getServiceExemptionQuery(PersonServiceExemption personServiceExemption) {
        StringBuilder query = new StringBuilder();
        query.append("select count(*) as cont from SLDEQUIDISP where emp_num=");
        query.append(personServiceExemption.getGiafProfessionalData().getGiafPersonIdentification());
        if (personServiceExemption.getBeginDate() != null) {
            query.append(" and DATA_INICIO=to_date('");
            query.append(dateFormat.print(personServiceExemption.getBeginDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and DATA_INICIO is null");
        }
        if (personServiceExemption.getEndDate() != null) {
            query.append(" and DATA_FIM=to_date('");
            query.append(dateFormat.print(personServiceExemption.getEndDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and DATA_FIM is null");
        }
        if (personServiceExemption.getServiceExemption() != null) {
            query.append(" and tip_disp=");
            query.append(personServiceExemption.getServiceExemptionGiafId());
        } else {
            query.append(" and tip_disp is null");
        }

        query.append(" and data_criacao=to_date('");
        query.append(dateTimeFormat.print(personServiceExemption.getCreationDate()));
        query.append("','YYYY-MM-DD HH24:mi:ss')");
        if (personServiceExemption.getModifiedDate() != null) {
            query.append(" and data_alteracao=to_date('");
            query.append(dateTimeFormat.print(personServiceExemption.getModifiedDate()));
            query.append("','YYYY-MM-DD HH24:mi:ss')");
        } else {
            query.append("and data_alteracao is null");
        }
        return query.toString();

    }

    private boolean hasPersonServiceExemption(GiafProfessionalData giafProfessionalData, LocalDate beginDate, LocalDate endDate,
            ServiceExemption serviceExemption, String serviceExemptionGiafId, DateTime creationDate, DateTime modifiedDate) {
        for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData.getPersonProfessionalExemptions()) {
            if (personProfessionalExemption instanceof PersonServiceExemption) {
                PersonServiceExemption personServiceExemption = (PersonServiceExemption) personProfessionalExemption;
                if (personServiceExemption.getAnulationDate() == null && equal(personServiceExemption.getBeginDate(), beginDate)
                        && equal(endDate, personServiceExemption.getEndDate())
                        && equal(serviceExemption, personServiceExemption.getServiceExemption())
                        && equal(serviceExemptionGiafId, personServiceExemption.getServiceExemptionGiafId())
                        && equal(creationDate, personServiceExemption.getCreationDate())
                        && equal(modifiedDate, personServiceExemption.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected String getQuery() {
        return "SELECT dispensas.DATA_FIM, dispensas.DATA_INICIO, dispensas.EMP_NUM, dispensas.tip_disp, dispensas.data_criacao, dispensas.data_alteracao FROM SLDEQUIDISP dispensas WHERE dispensas.TIPO(+) = 'D'";
    }

}

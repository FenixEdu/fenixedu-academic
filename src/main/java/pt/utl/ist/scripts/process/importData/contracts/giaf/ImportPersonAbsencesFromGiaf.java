package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.Absence;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonAbsence;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalExemption;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import net.sourceforge.fenixedu.util.StringUtils;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Task(englishTitle = "ImportPersonAbsencesFromGiaf")
public class ImportPersonAbsencesFromGiaf extends ImportFromGiaf {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonAbsencesFromGiaf() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportPersonAbsencesFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, Absence> absencesMap = getAbsencesMap();
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
                String absenceGiafId = result.getString("flt_tip");
                Absence absence = absencesMap.get(absenceGiafId);
                if (absence == null) {
                    getLogger().info("Empty absence: " + absenceGiafId + " for person number: " + numberString);
                    importedButInvalid.add(person);

                }
                String beginDateString = result.getString("FLT_DT_INIC");
                LocalDate beginDate = null;
                if (!StringUtils.isEmpty(beginDateString)) {
                    beginDate = new LocalDate(Timestamp.valueOf(beginDateString));
                }
                if (beginDate == null) {
                    getLogger().info("Empty beginDate. Person number: " + numberString + " Absence: " + absenceGiafId);
                    importedButInvalid.add(person);
                }
                String endDateString = result.getString("FLT_DT_FIM");
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
                    getLogger().info("Empty creationDate. Person number: " + numberString + " Absence: " + absenceGiafId);
                    notImported++;
                    continue;
                }
                DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

                DateTime modifiedDate = null;
                String modifiedDateString = result.getString("data_alteracao");
                if (!StringUtils.isEmpty(modifiedDateString)) {
                    modifiedDate = new DateTime(Timestamp.valueOf(modifiedDateString));
                }

                if (!hasPersonAbsence(giafProfessionalData, beginDate, endDate, absence, absenceGiafId, creationDate,
                        modifiedDate)) {
                    new PersonAbsence(giafProfessionalData, beginDate, endDate, absence, absenceGiafId, creationDate,
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
                for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptions()) {
                    if (personProfessionalExemption instanceof PersonAbsence
                            && personProfessionalExemption.getAnulationDate() == null) {
                        PersonAbsence personAbsence = (PersonAbsence) personProfessionalExemption;
                        int countThisPersonAbsenceOnGiaf = countThisPersonAbsenceOnGiaf(oracleConnection, personAbsence);
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
            getLogger().info("Total GIAF: " + count);
            getLogger().info("New: " + news);
            getLogger().info("Deleted: " + deleted);
            getLogger().info("Not imported: " + notImported);
            getLogger().info("Imported with errors: " + importedButInvalid.size());
            getLogger().info("Repeted: " + repeted);
            getLogger().info("Invalid person or absence: " + dontExist);
            getLogger().info("Total Fénix: " + totalInFenix);
            getLogger().info("Total Fénix without errors: " + (totalInFenix - importedButInvalid.size()));
            getLogger().info("Missing in Fénix: " + (count - totalInFenix));

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportPersonAbsencesFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonAbsencesFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private int countThisPersonAbsenceOnGiaf(PersistentSuportGiaf oracleConnection, PersonAbsence personAbsence)
            throws SQLException, ExcepcaoPersistencia {
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
                        getLogger().info(
                                "---> " + count + " ---> "
                                        + personAbsence.getGiafProfessionalData().getGiafPersonIdentification() + " FA: "
                                        + personAbsence.getAbsence().getGiafId());
                    }
                    return count;
                }
            }
            return 0;
        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportPersonAbsencesFromGiaf -  ERRO ExcepcaoPersistencia hasPersonAbsencenOnGiaf");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonAbsencesFromGiaf -  ERRO SQLException hasPersonAbsenceOnGiaf " + query);
            throw new Error(e);
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
                if (personAbsence.getAnulationDate() == null && equal(personAbsence.getBeginDate(), beginDate)
                        && equal(endDate, personAbsence.getEndDate()) && equal(absence, personAbsence.getAbsence())
                        && equal(absenceGiafId, personAbsence.getAbsenceGiafId())
                        && equal(creationDate, personAbsence.getCreationDate())
                        && equal(modifiedDate, personAbsence.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected String getQuery() {
        StringBuilder query = new StringBuilder();
        query.append("select emp_num, flt_tip, FLT_DT_INIC, FLT_DT_FIM, data_criacao, data_alteracao from SLHFLTINT where FLT_TIP in (");
        query.append(getAbsencesToImport()).append(")");
        return query.toString();
    }

    private String getAbsencesToImport() {
        List<String> absences = new ArrayList<String>();
        for (Absence absence : getAbsencesMap().values()) {
            if (absence.getImportAbsence()) {
                absences.add("'" + absence.getGiafId() + "'");
            }
        }

        return absences.size() == 0 ? "null" : org.apache.commons.lang.StringUtils.join(absences, ",");
    }

}

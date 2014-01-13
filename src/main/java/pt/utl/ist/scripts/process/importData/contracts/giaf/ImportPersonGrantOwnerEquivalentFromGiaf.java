package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GrantOwnerEquivalent;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonGrantOwnerEquivalent;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalExemption;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Task(englishTitle = "ImportPersonGrantOwnerEquivalentFromGiaf")
public class ImportPersonGrantOwnerEquivalentFromGiaf extends ImportFromGiaf {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonGrantOwnerEquivalentFromGiaf() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportPersonGrantOwnerEquivalentFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, GrantOwnerEquivalent> grantOwnerEquivalencesMap = getGrantOwnerEquivalencesMap();
            Map<Integer, Employee> employeesMap = getEmployeesMap();
            Map<String, Country> countryMap = getCountryMap();
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
                String grantOwnerEquivalentGiafId = result.getString("tipo_equip");
                GrantOwnerEquivalent grantOwnerEquivalent = grantOwnerEquivalencesMap.get(grantOwnerEquivalentGiafId);
                if (grantOwnerEquivalent == null) {
                    getLogger().info(
                            "Empty grantOwnerEquivalent: " + grantOwnerEquivalentGiafId + ". Person number: " + numberString);
                    importedButInvalid.add(person);
                }
                String beginDateString = result.getString("DATA_INICIO");
                LocalDate beginDate = null;
                if (!StringUtils.isEmpty(beginDateString)) {
                    beginDate = new LocalDate(Timestamp.valueOf(beginDateString));
                }
                if (beginDate == null) {
                    getLogger().info(
                            "Empty beginDate. Person number: " + numberString + " ServiceExemption: "
                                    + grantOwnerEquivalentGiafId);
                    importedButInvalid.add(person);
                }
                String endDateString = result.getString("DATA_FIM");
                LocalDate endDate = null;
                if (!StringUtils.isEmpty(endDateString)) {
                    endDate = new LocalDate(Timestamp.valueOf(endDateString));
                }

                if (endDate != null) {
                    if (beginDate.isAfter(endDate)) {
                        getLogger().info(
                                "BeginDate after EndDate. Person number: " + numberString + " beginDate: " + beginDate
                                        + " endDate: " + endDate);
                        importedButInvalid.add(person);
                    }
                }

                String motive = result.getString("motivo");
                String local = result.getString("local");

                String giafCountryName = result.getString("nac_dsc");
                Country country = null;
                if (StringUtils.isEmpty(giafCountryName)) {
                    getLogger().info("Empty country. Person number: " + numberString);
                    importedButInvalid.add(person);
                } else {
                    String giafCountryNameNormalized = StringNormalizer.normalize(giafCountryName);
                    country = countryMap.get(giafCountryNameNormalized);
                    if (country == null) {
                        getLogger().info("Invalid country. Person number: " + numberString + " country:" + giafCountryName);
                        importedButInvalid.add(person);
                    }
                }

                String creationDateString = result.getString("data_criacao");
                if (StringUtils.isEmpty(creationDateString)) {
                    getLogger().info(
                            "Empty creationDate. Person number: " + numberString + " grantOwnerEquivalent: "
                                    + grantOwnerEquivalentGiafId);
                    notImported++;
                    continue;
                }
                DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

                DateTime modifiedDate = null;
                String modifiedDateString = result.getString("data_alteracao");
                if (!StringUtils.isEmpty(modifiedDateString)) {
                    modifiedDate = new DateTime(Timestamp.valueOf(modifiedDateString));
                }

                if (!hasPersonGrantOwnerEquivalent(giafProfessionalData, beginDate, endDate, motive, local, giafCountryName,
                        country, grantOwnerEquivalent, grantOwnerEquivalentGiafId, creationDate, modifiedDate)) {
                    new PersonGrantOwnerEquivalent(giafProfessionalData, beginDate, endDate, motive, local, giafCountryName,
                            country, grantOwnerEquivalent, grantOwnerEquivalentGiafId, creationDate, modifiedDate);
                    news++;
                }
            }
            result.close();
            preparedStatement.close();

            int deleted = 0;
            int totalInFenix = 0;
            int repeted = 0;
            for (GiafProfessionalData giafProfessionalData : Bennu.getInstance().getGiafProfessionalDataSet()) {
                for (PersonProfessionalExemption pesonProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptions()) {
                    if (pesonProfessionalExemption instanceof PersonGrantOwnerEquivalent
                            && pesonProfessionalExemption.getAnulationDate() == null) {
                        PersonGrantOwnerEquivalent personGrantOwnerEquivalent =
                                (PersonGrantOwnerEquivalent) pesonProfessionalExemption;
                        int countThisPersonGrantOwnerEquivalentOnGiaf =
                                countThisPersonGrantOwnerEquivalentOnGiaf(oracleConnection, personGrantOwnerEquivalent,
                                        countryMap);
                        if (countThisPersonGrantOwnerEquivalentOnGiaf == 0) {
                            personGrantOwnerEquivalent.setAnulationDate(new DateTime());
                            deleted++;
                        } else {
                            totalInFenix++;
                            if (countThisPersonGrantOwnerEquivalentOnGiaf > 1) {
                                repeted += countThisPersonGrantOwnerEquivalentOnGiaf - 1;
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
            getLogger().info("Empty employee or GrantOwnerEquivalent: " + dontExist);
            getLogger().info("Total Fénix: " + totalInFenix);
            getLogger().info("Total Fénix without errors: " + (totalInFenix - importedButInvalid.size()));
            getLogger().info("Missing in Fénix: " + (count - totalInFenix));

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportPersonGrantOwnerEquivalentFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonGrantOwnerEquivalentFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private int countThisPersonGrantOwnerEquivalentOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonGrantOwnerEquivalent personGrantOwnerEquivalent, Map<String, Country> countryMap) throws ExcepcaoPersistencia,
            SQLException {
        String query = getPersonGrantOwnerEquivalentQuery(personGrantOwnerEquivalent, countryMap);
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
                                        + personGrantOwnerEquivalent.getGiafProfessionalData().getGiafPersonIdentification()
                                        + " FA: " + personGrantOwnerEquivalent.getGrantOwnerEquivalent().getGiafId());
                    }
                    return count;
                }
                // if (count == 0) {
                // getLogger().info(query);
                // }
            }
            // getLogger().info(query);
            return 0;
        } catch (ExcepcaoPersistencia e) {
            getLogger().info(
                    "ImportPersonGrantOwnerEquivalentFromGiaf -  ERRO ExcepcaoPersistencia hasPersonGrantOwnerEquivalentOnGiaf");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info(
                    "ImportPersonGrantOwnerEquivalentFromGiaf -  ERRO SQLException hasPersonGrantOwnerEquivalentOnGiaf " + query);
            throw new Error(e);
        } finally {
            if (result != null) {
                result.close();
            }
            preparedStatement.close();
        }
    }

    private String getPersonGrantOwnerEquivalentQuery(PersonGrantOwnerEquivalent personGrantOwnerEquivalent,
            Map<String, Country> countryMap) {
        StringBuilder query = new StringBuilder();
        query.append("select count(*) as cont from SLDEQUIDISP where emp_num=");
        query.append(personGrantOwnerEquivalent.getGiafProfessionalData().getGiafPersonIdentification());
        if (personGrantOwnerEquivalent.getBeginDate() != null) {
            query.append(" and DATA_INICIO=to_date('");
            query.append(dateFormat.print(personGrantOwnerEquivalent.getBeginDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and DATA_INICIO is null");
        }
        if (personGrantOwnerEquivalent.getEndDate() != null) {
            query.append(" and DATA_FIM=to_date('");
            query.append(dateFormat.print(personGrantOwnerEquivalent.getEndDate()));
            query.append("','YYYY-MM-DD')");
        } else {
            query.append(" and DATA_FIM is null");
        }
        if (personGrantOwnerEquivalent.getGrantOwnerEquivalent() != null) {
            query.append(" and tipo_equip='");
            query.append(personGrantOwnerEquivalent.getGrantOwnerEquivalentGiafId());
            query.append("'");
        } else {
            query.append(" and tipo_equip is null");
        }

        if (personGrantOwnerEquivalent.getMotive() != null) {
            query.append(" and motivo=(SELECT c.TAB_COD from SLTCODGER c where c.TAB_ID = 'FA' AND c.TAB_NUM = 85 and c.tab_cod_dsc ='");
            query.append(personGrantOwnerEquivalent.getMotive());
            query.append("')");
        } else {
            query.append(" and motivo is null");
        }
        if (personGrantOwnerEquivalent.getLocal() != null) {
            query.append(" and local='");
            query.append(personGrantOwnerEquivalent.getLocal().replaceAll("'", "''"));
            query.append("'");
        } else {
            query.append(" and local is null");
        }
        if (!StringUtils.isEmpty(personGrantOwnerEquivalent.getGiafCountryName())) {
            query.append(" and cod_pais=(SELECT a.emp_nac FROM sltnac a where upper(convert(nac_dsc, 'US7ASCII', 'WE8ISO8859P1'))=upper(convert('");
            query.append(personGrantOwnerEquivalent.getGiafCountryName());
            query.append("', 'US7ASCII', 'WE8ISO8859P1')))");
        } else {
            query.append(" and (cod_pais is null or cod_pais ='");
            query.append(personGrantOwnerEquivalent.getGiafCountryName());
            query.append("')");
        }

        query.append(" and data_criacao=to_date('");
        query.append(dateTimeFormat.print(personGrantOwnerEquivalent.getCreationDate()));
        query.append("','YYYY-MM-DD HH24:mi:ss')");
        if (personGrantOwnerEquivalent.getModifiedDate() != null) {
            query.append(" and data_alteracao=to_date('");
            query.append(dateTimeFormat.print(personGrantOwnerEquivalent.getModifiedDate()));
            query.append("','YYYY-MM-DD HH24:mi:ss')");
        } else {
            query.append("and data_alteracao is null");
        }
        return query.toString();

    }

    private boolean hasPersonGrantOwnerEquivalent(GiafProfessionalData giafProfessionalData, LocalDate beginDate,
            LocalDate endDate, String motive, String local, String giafCountryName, Country country,
            GrantOwnerEquivalent grantOwnerEquivalent, String grantOwnerEquivalentGiafId, DateTime creationDate,
            DateTime modifiedDate) {
        for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData.getPersonProfessionalExemptions()) {
            if (personProfessionalExemption instanceof PersonGrantOwnerEquivalent) {
                PersonGrantOwnerEquivalent personGrantOwnerEquivalent = (PersonGrantOwnerEquivalent) personProfessionalExemption;
                if (personGrantOwnerEquivalent.getAnulationDate() == null
                        && equal(beginDate, personGrantOwnerEquivalent.getBeginDate())
                        && equal(endDate, personGrantOwnerEquivalent.getEndDate())
                        && equal(motive, personGrantOwnerEquivalent.getMotive())
                        && equal(local, personGrantOwnerEquivalent.getLocal())
                        && equal(giafCountryName, personGrantOwnerEquivalent.getGiafCountryName())
                        && equal(country, personGrantOwnerEquivalent.getCountry())
                        && equal(grantOwnerEquivalent, personGrantOwnerEquivalent.getGrantOwnerEquivalent())
                        && equal(grantOwnerEquivalentGiafId, personGrantOwnerEquivalent.getGrantOwnerEquivalentGiafId())
                        && equal(creationDate, personGrantOwnerEquivalent.getCreationDate())
                        && equal(modifiedDate, personGrantOwnerEquivalent.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected String getQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT d.DATA_FIM, d.DATA_INICIO, d.EMP_NUM, d.tipo_equip, c.tab_cod_dsc as motivo, d.local, p.nac_dsc, d.data_criacao, d.data_alteracao");
        query.append(" FROM SLDEQUIDISP d, sltnac p, SLTCODGER c WHERE d.TIPO(+) = 'E' and d.cod_pais= emp_nac(+) and (d.MOTIVO = c.TAB_COD(+) and (c.TAB_COD is null or (c.TAB_ID = 'FA' AND c.TAB_NUM = 85)))");

        return query.toString();
    }

}

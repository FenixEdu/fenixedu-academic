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

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GrantOwnerEquivalent;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonGrantOwnerEquivalent;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalExemption;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

import pt.ist.fenix.giafsync.GiafSync.ImportProcessor;
import pt.ist.fenix.giafsync.GiafSync.Modification;

class ImportPersonGrantOwnerEquivalentFromGiaf extends ImportProcessor {
    final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    final static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public ImportPersonGrantOwnerEquivalentFromGiaf() {

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
            final String grantOwnerEquivalentGiafId = result.getString("tipo_equip");
            final GrantOwnerEquivalent grantOwnerEquivalent = metadata.grantOwnerEquivalent(grantOwnerEquivalentGiafId);
            if (grantOwnerEquivalent == null) {
                logger.debug("Empty grantOwnerEquivalent: " + grantOwnerEquivalentGiafId + ". Person number: " + numberString);
                importedButInvalid.add(person);
            }
            String beginDateString = result.getString("DATA_INICIO");
            final LocalDate beginDate =
                    StringUtils.isEmpty(beginDateString) ? null : new LocalDate(Timestamp.valueOf(beginDateString));
            if (beginDate == null) {
                logger.debug("Empty beginDate. Person number: " + numberString + " ServiceExemption: "
                        + grantOwnerEquivalentGiafId);
                importedButInvalid.add(person);
            }
            String endDateString = result.getString("DATA_FIM");
            final LocalDate endDate = StringUtils.isEmpty(endDateString) ? null : new LocalDate(Timestamp.valueOf(endDateString));

            if (endDate != null) {
                if (beginDate.isAfter(endDate)) {
                    logger.debug("BeginDate after EndDate. Person number: " + numberString + " beginDate: " + beginDate
                            + " endDate: " + endDate);
                    importedButInvalid.add(person);
                }
            }

            final String motive = result.getString("motivo");
            final String local = result.getString("local");

            final String giafCountryName = result.getString("nac_dsc");
            final Country country =
                    StringUtils.isEmpty(giafCountryName) ? null : metadata.country(StringNormalizer.normalize(giafCountryName));
            if (country == null) {
                importedButInvalid.add(person);
            }

            String creationDateString = result.getString("data_criacao");
            if (StringUtils.isEmpty(creationDateString)) {
                logger.debug("Empty creationDate. Person number: " + numberString + " grantOwnerEquivalent: "
                        + grantOwnerEquivalentGiafId);
                notImported++;
                continue;
            }
            final DateTime creationDate = new DateTime(Timestamp.valueOf(creationDateString));

            String modifiedDateString = result.getString("data_alteracao");
            final DateTime modifiedDate =
                    StringUtils.isEmpty(modifiedDateString) ? null : new DateTime(Timestamp.valueOf(modifiedDateString));

            if (!hasPersonGrantOwnerEquivalent(giafProfessionalData, beginDate, endDate, motive, local, giafCountryName, country,
                    grantOwnerEquivalent, grantOwnerEquivalentGiafId, creationDate, modifiedDate)) {
                modifications.add(new Modification() {
                    @Override
                    public void execute() {
                        new PersonGrantOwnerEquivalent(giafProfessionalData, beginDate, endDate, motive, local, giafCountryName,
                                country, grantOwnerEquivalent, grantOwnerEquivalentGiafId, creationDate, modifiedDate);
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
            for (PersonProfessionalExemption pesonProfessionalExemption : giafProfessionalData
                    .getPersonProfessionalExemptionsSet()) {
                if (pesonProfessionalExemption instanceof PersonGrantOwnerEquivalent
                        && pesonProfessionalExemption.getAnulationDate() == null) {
                    final PersonGrantOwnerEquivalent personGrantOwnerEquivalent =
                            (PersonGrantOwnerEquivalent) pesonProfessionalExemption;
                    int countThisPersonGrantOwnerEquivalentOnGiaf =
                            countThisPersonGrantOwnerEquivalentOnGiaf(oracleConnection, personGrantOwnerEquivalent, logger);
                    if (countThisPersonGrantOwnerEquivalentOnGiaf == 0) {
                        modifications.add(new Modification() {
                            @Override
                            public void execute() {
                                personGrantOwnerEquivalent.setAnulationDate(new DateTime());
                            }
                        });
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
        log.println("-- Grant owner equivalences --");
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

    private int countThisPersonGrantOwnerEquivalentOnGiaf(PersistentSuportGiaf oracleConnection,
            PersonGrantOwnerEquivalent personGrantOwnerEquivalent, Logger logger) throws SQLException {
        String query = getPersonGrantOwnerEquivalentQuery(personGrantOwnerEquivalent);
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
                                + personGrantOwnerEquivalent.getGiafProfessionalData().getGiafPersonIdentification() + " FA: "
                                + personGrantOwnerEquivalent.getGrantOwnerEquivalent().getGiafId());
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

    private String getPersonGrantOwnerEquivalentQuery(PersonGrantOwnerEquivalent personGrantOwnerEquivalent) {
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
        for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData.getPersonProfessionalExemptionsSet()) {
            if (personProfessionalExemption instanceof PersonGrantOwnerEquivalent) {
                PersonGrantOwnerEquivalent personGrantOwnerEquivalent = (PersonGrantOwnerEquivalent) personProfessionalExemption;
                if (personGrantOwnerEquivalent.getAnulationDate() == null
                        && Objects.equals(beginDate, personGrantOwnerEquivalent.getBeginDate())
                        && Objects.equals(endDate, personGrantOwnerEquivalent.getEndDate())
                        && Objects.equals(motive, personGrantOwnerEquivalent.getMotive())
                        && Objects.equals(local, personGrantOwnerEquivalent.getLocal())
                        && Objects.equals(giafCountryName, personGrantOwnerEquivalent.getGiafCountryName())
                        && Objects.equals(country, personGrantOwnerEquivalent.getCountry())
                        && Objects.equals(grantOwnerEquivalent, personGrantOwnerEquivalent.getGrantOwnerEquivalent())
                        && Objects.equals(grantOwnerEquivalentGiafId, personGrantOwnerEquivalent.getGrantOwnerEquivalentGiafId())
                        && Objects.equals(creationDate, personGrantOwnerEquivalent.getCreationDate())
                        && Objects.equals(modifiedDate, personGrantOwnerEquivalent.getModifiedDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT d.DATA_FIM, d.DATA_INICIO, d.EMP_NUM, d.tipo_equip, c.tab_cod_dsc as motivo, d.local, p.nac_dsc, d.data_criacao, d.data_alteracao");
        query.append(" FROM SLDEQUIDISP d, sltnac p, SLTCODGER c WHERE d.TIPO(+) = 'E' and d.cod_pais= emp_nac(+) and (d.MOTIVO = c.TAB_COD(+) and (c.TAB_COD is null or (c.TAB_ID = 'FA' AND c.TAB_NUM = 85)))");

        return query.toString();
    }
}

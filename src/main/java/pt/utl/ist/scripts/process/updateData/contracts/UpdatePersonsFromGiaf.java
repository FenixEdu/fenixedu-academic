package pt.utl.ist.scripts.process.updateData.contracts;

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
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import net.sourceforge.fenixedu.util.StringFormatter;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.bennu.scheduler.annotation.Task;
import pt.utl.ist.fenix.tools.util.StringNormalizer;
import pt.utl.ist.scripts.process.importData.contracts.giaf.ImportFromGiaf;
import pt.utl.ist.scripts.process.updateData.fixNames.DBField2Cap;

@Task(englishTitle = "UpdatePersonsFromGiaf")
public class UpdatePersonsFromGiaf extends ImportFromGiaf {

    private static final String SEPARATOR = " - ";

    private static final String MALE = "M";

    private static final String FEMALE = "F";

    public UpdatePersonsFromGiaf() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start UpdatePersonsFromGiaf");
        try {
            Authenticate.mock(pt.ist.bennu.core.domain.User.findByUsername("ist23932"));

            int count = 0, notImported = 0;
            Set<Person> newPersons = new HashSet<Person>();
            Set<Person> editedPersons = new HashSet<Person>();

            try {
                Map<String, IDDocumentType> documentTypeMap = getDocumentTypeMap();
                Map<String, MaritalStatus> maritalStatusMap = getMaritalStatusMap();
                Map<String, Country> countryMap = getCountryMap();
                Map<Integer, Employee> employeesMap = getEmployeesMap();

                PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
                String query = getQuery();
                getLogger().debug(query);
                PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
                ResultSet result = preparedStatement.executeQuery();
                while (result.next()) {
                    count++;
                    String personNumberString = result.getString("EMP_NUM");
                    Person personByNumber = getPerson(employeesMap, personNumberString);
                    if (personByNumber == null) {
                        getLogger().info("Invalid person with number: " + personNumberString);
                        notImported++;
                        continue;
                    }

                    String giafName = result.getString("EMP_NOM");
                    String IdDocTypeString = result.getString("emp_bi_tp");
                    IDDocumentType idDocumentType = documentTypeMap.get(IdDocTypeString);
                    String oldIdDocNumberString = result.getString("emp_bi_num");
                    String idDocNumberString = getValidID(idDocumentType, oldIdDocNumberString);
                    String contributorNumber = result.getString("emp_num_fisc");

                    String countryName = result.getString("nac_dsc");
                    Country nationality = null;
                    if (!StringUtils.isEmpty(countryName)) {
                        nationality = countryMap.get(StringNormalizer.normalize(countryName));
                    }
                    MaritalStatus maritalStatus = maritalStatusMap.get(result.getString("emp_stcivil"));
                    Gender gender = getGender(result.getString("emp_sex"));
                    String validateDocuments =
                            validateDocuments(personNumberString, idDocumentType, oldIdDocNumberString, idDocNumberString,
                                    contributorNumber, countryName, nationality, maritalStatus, gender);
                    if (!StringUtils.isEmpty(validateDocuments)) {
                        getLogger().debug("---------------------------------\n" + validateDocuments);
                        notImported++;
                        continue;
                    }

                    Person personById = Person.readByDocumentIdNumberAndIdDocumentType(idDocNumberString, idDocumentType);
                    if (personById != null && !personByNumber.equals(personById)) {
                        getLogger().debug(
                                personNumberString + "-------------------Confito de pessoas ------------------- "
                                        + personByNumber.getUsername() + " e " + personById.getUsername());
                        notImported++;
                        continue;
                    }
                    // String diffs = getDifferences(personById, personNumberString,
                    // idDocumentType, idDocNumberString, giafName,
                    // contributorNumber);
                    // if (!StringUtils.isEmpty(diffs)) {
                    // getLogger().debug(personNumberString +
                    // "---------------------------------" + diffs);
                    // notImported++;
                    // continue;
                    // }

                    Party party = Person.readByContributorNumber(contributorNumber);
                    if (party != null) {
                        if (party instanceof Person) {
                            Person person2 = (Person) party;
                            if (personByNumber == null || personByNumber != person2) {
                                getLogger().debug(
                                        "---------------------------------\nJá existe pessoa com o número de contribuinte:"
                                                + getDifferences(person2, personNumberString, idDocumentType, idDocNumberString,
                                                        giafName, contributorNumber));
                                notImported++;
                                continue;
                            }
                        } else {
                            getLogger().debug(
                                    "---------------------------------\nJá existe UNIDADE com o número de contribuinte: "
                                            + contributorNumber + " \n\t Unidade Fénix: " + party.getName()
                                            + ". Número Mecanográfico: " + personNumberString);
                            notImported++;
                            continue;
                        }
                    }

                    YearMonthDay idEmissionDate = getLocalDateFromString(result.getString("emp_bi_dt"));
                    if (canEditPersonInfo(personByNumber, idEmissionDate)) {

                        String prettyPrintName = StringFormatter.prettyPrint(giafName);
                        if (!personByNumber.getName().equals(prettyPrintName)) {
                            if (namesCorrectlyPartitioned(personByNumber, prettyPrintName)) {
                                personByNumber.setName(prettyPrintName);
                                if (!newPersons.contains(personByNumber)) {
                                    editedPersons.add(personByNumber);
                                }
                            } else {
                                getLogger().debug(
                                        "\nNão pode alterar nome (tem nomes partidos). Número Mecanográfico: "
                                                + personNumberString + " Nome: " + prettyPrintName);
                            }
                        }

                        YearMonthDay idExpirationDate = getLocalDateFromString(result.getString("emp_bi_val_dt"));
                        String idArquive = result.getString("emp_bi_arq");
                        String fiscalNeighborhood = result.getString("emp_bfiscal");

                        if (!hasEqualPersonalInformation(maritalStatus, gender, idEmissionDate, idExpirationDate, idArquive,
                                idDocumentType, idDocNumberString, contributorNumber, fiscalNeighborhood, nationality,
                                personByNumber)) {
                            setPersonalInformation(maritalStatus, gender, idEmissionDate, idExpirationDate, idArquive,
                                    idDocumentType, idDocNumberString, contributorNumber, fiscalNeighborhood, nationality,
                                    personByNumber);
                            if (!newPersons.contains(personByNumber)) {
                                editedPersons.add(personByNumber);
                            }
                        }

                        YearMonthDay dateOfBirth = getLocalDateFromString(result.getString("emp_nsc_dt"));
                        String parishOfBirth = StringFormatter.prettyPrint(result.getString("emp_nat_frg"));
                        String districtOfBirth = StringFormatter.prettyPrint(result.getString("emp_nat_dst"));
                        String districtSubdivisionOfBirth = StringFormatter.prettyPrint(result.getString("emp_nat_cnc"));
                        String nameOfFatherString = result.getString("father");
                        String nameOfFather = nameOfFatherString == null ? null : DBField2Cap.prettyPrint(nameOfFatherString);
                        String nameOfMotherString = result.getString("mother");
                        String nameOfMother = nameOfMotherString == null ? null : DBField2Cap.prettyPrint(nameOfMotherString);
                        if (!hasEqualBirthInformation(dateOfBirth, parishOfBirth, districtOfBirth, districtSubdivisionOfBirth,
                                nameOfFather, nameOfMother, personByNumber)) {
                            setBirthInformation(dateOfBirth, parishOfBirth, districtOfBirth, districtSubdivisionOfBirth,
                                    nameOfFather, nameOfMother, personByNumber);
                            if (!newPersons.contains(personByNumber)) {
                                editedPersons.add(personByNumber);
                            }
                        }

                    } else {
                        System.out.println("Não actualiza: " + personNumberString + " - " + giafName + " ->é aluno! ");
                    }
                }
                result.close();
                preparedStatement.close();
                oracleConnection.closeConnection();

            } catch (ExcepcaoPersistencia e) {
                getLogger().debug("ImportPersonsFromGiaf -  ERRO ExcepcaoPersistencia");
                throw new Error(e);
            } catch (SQLException e) {
                getLogger().debug("ImportPersonsFromGiaf -  ERRO SQLException");
                throw new Error(e);
            }
            getLogger().debug("---------------------------------");
            getLogger().debug("\nTotal no GIAF: " + count);
            getLogger().debug("\nNão importados: " + notImported);
            getLogger().debug("\nNovos: " + newPersons.size());
            getLogger().debug("\nEditados: " + editedPersons.size());
            getLogger().debug("\nThe end");
        } finally {
            Authenticate.unmock();
        }

    }

    private boolean namesCorrectlyPartitioned(Person personByNumber, String prettyPrintName) {
        if (StringUtils.isEmpty(personByNumber.getGivenNames()) && StringUtils.isEmpty(personByNumber.getFamilyNames())) {
            return true;
        }
        return (personByNumber.getGivenNames() + " " + personByNumber.getFamilyNames()).equals(prettyPrintName);
    }

    private boolean canEditPersonInfo(Person personByNumber, YearMonthDay idEmissionDate) {
        Boolean isStudent = personByNumber.hasRole(RoleType.STUDENT);
        ContractSituation contractSituation = getCurrentContractSituation(personByNumber.getEmployee());
        if (contractSituation != null && contractSituation.getEndSituation() && isStudent) {
            if (idEmissionDate == null || idEmissionDate.isBefore(personByNumber.getEmissionDateOfDocumentIdYearMonthDay())) {
                return false;
            }
        }
        return true;
    }

    private ContractSituation getCurrentContractSituation(Employee employee) {
        PersonContractSituation currentPersonContractSituation = null;
        if (employee != null) {
            PersonProfessionalData personProfessionalData = employee.getPerson().getPersonProfessionalData();
            if (personProfessionalData != null) {
                LocalDate today = new LocalDate();
                for (GiafProfessionalData giafProfessionalData : personProfessionalData.getGiafProfessionalDatas()) {
                    for (final PersonContractSituation situation : giafProfessionalData.getValidPersonContractSituations()) {
                        if (situation.isActive(today)
                                && (currentPersonContractSituation == null || situation.isAfter(currentPersonContractSituation))) {
                            currentPersonContractSituation = situation;
                        }
                    }
                }
            }
        }
        return currentPersonContractSituation != null ? currentPersonContractSituation.getContractSituation() : null;
    }

    private String getDifferences(Person person, String employeeNumberString, IDDocumentType idDocumentType,
            String idDocNumberString, String giafName, String contributorNumber) {
        StringBuilder log = new StringBuilder();
        Integer employeeNumber = null;
        Employee employee = null;
        try {
            employeeNumber = Integer.parseInt(employeeNumberString);
            employee = Employee.readByNumber(employeeNumber);
        } catch (NumberFormatException e) {
        }
        if (person == null) {
            if (employee != null) {
                log.append("\nExiste um funcionário com o número: " + employeeNumber
                        + "\t mas com outra identificação\tFénix ID:" + employee.getPerson().getIdDocumentType().name()
                        + SEPARATOR + employee.getPerson().getDocumentIdNumber() + " !=  Giaf ID:" + idDocumentType + "  - "
                        + idDocNumberString);
            }
        } else {
            if (person.getEmployee() != null) {
                if (!person.getEmployee().getEmployeeNumber().equals(employeeNumber)) {
                    log.append("\nNúmeros diferentes!\t Fénix num:" + person.getEmployee().getEmployeeNumber() + " !=  Giaf num:"
                            + employeeNumberString);
                }
            } else if (employee != null) {
                log.append("\nExiste outro funcionário com o número: " + employeeNumberString + "\tFénix ID:"
                        + employee.getPerson().getIdDocumentType().name() + SEPARATOR
                        + employee.getPerson().getDocumentIdNumber() + " !=  Giaf ID:" + idDocumentType + "  - "
                        + idDocNumberString);
            }
            if ((!person.getDocumentIdNumber().equalsIgnoreCase(idDocNumberString))
                    || (!idDocumentType.equals(person.getIdDocumentType()))) {
                log.append("\nIDs diferentes!\t Fénix ID:" + person.getIdDocumentType().name() + SEPARATOR
                        + person.getDocumentIdNumber() + " !=  Giaf ID:" + idDocumentType + "  - " + idDocNumberString);
            }
            // if (!equalName(giafName, person.getName())) {
            // log.append("\nNomes diferentes!\t Fénix nome: " +
            // StringNormalizer.normalize(person.getName())
            // + " !=  Giaf nome: " + giafName);
            // }

            if (person.getSocialSecurityNumber() != null) {
                if (!person.getSocialSecurityNumber().equals(contributorNumber)) {
                    log.append("\nNIFs diferentes!\t Fénix NIF: " + person.getSocialSecurityNumber() + " !=  Giaf NIF: "
                            + contributorNumber);
                }
            }
        }
        return log.toString();
    }

    private String validateDocuments(String employeeNumber, IDDocumentType idDocumentType, String oldIdDocNumberString,
            String idDocNumberString, String contributorNumber, String countryName, Country nationality,
            MaritalStatus maritalStatus, Gender gender) {
        StringBuilder errors = new StringBuilder();
        if (idDocumentType == null) {
            errors.append("\nVAZIO: Tipo de Documento de Identificação. Número Mecanográfico: " + employeeNumber);
        }
        if (StringUtils.isEmpty(oldIdDocNumberString)) {
            errors.append("\nVAZIO: Número de Documento de Identificação. Número Mecanográfico: " + employeeNumber);
        }

        if (idDocNumberString == null) {
            errors.append("\nINVÁLIDO: Número de Documento de Identificação. Número Mecanográfico: " + employeeNumber
                    + " TIPO DI:" + idDocumentType.toString() + " Número DI:" + oldIdDocNumberString);
        }
        if (StringUtils.isEmpty(contributorNumber)) {
            errors.append("\nVAZIO: Número de contribuinte. Número Mecanográfico: " + employeeNumber);
        }
        if (nationality == null) {
            errors.append("\nINVÁLIDO: Nacionalidade. Número Mecanográfico: " + employeeNumber + " Nacionalidade:" + countryName);
        }
        if (maritalStatus == null) {
            errors.append("\nINVÁLIDO: Estado Civil. Número Mecanográfico: " + employeeNumber);
        }
        if (gender == null) {
            errors.append("\nINVÁLIDO: Sexo. Número Mecanográfico: " + employeeNumber);
        }
        return errors.toString();
    }

    private boolean equalName(String name1, String name2) {
        return StringNormalizer.normalize(name1).equals(StringNormalizer.normalize(name2));
    }

    private boolean hasEqualPersonalInformation(MaritalStatus maritalStatus, Gender gender, YearMonthDay idEmissionDate,
            YearMonthDay idExpirationDate, String idArquive, IDDocumentType idDocumentType, String idDocNumberString,
            String socialSecurityNumber, String fiscalNeighborhood, Country nationality, Person person) {
        return equal(person.getMaritalStatus(), maritalStatus) && equal(person.getGender(), gender)
                && equal(person.getEmissionDateOfDocumentIdYearMonthDay(), idEmissionDate)
                && equal(person.getExpirationDateOfDocumentIdYearMonthDay(), idExpirationDate)
                && equal(person.getEmissionLocationOfDocumentId(), idArquive)
                && equal(person.getDocumentIdNumber(), idDocNumberString) && equal(person.getIdDocumentType(), idDocumentType)
                && equal(person.getSocialSecurityNumber(), socialSecurityNumber)
                && equal(person.getFiscalCode(), fiscalNeighborhood) && equal(person.getCountry(), nationality);
    }

    private void setPersonalInformation(MaritalStatus maritalStatus, Gender gender, YearMonthDay idEmissionDate,
            YearMonthDay idExpirationDate, String idArquive, IDDocumentType idDocumentType, String idDocNumberString,
            String socialSecurityNumber, String fiscalNeighborhood, Country nationality, Person person) {
        person.setMaritalStatus(maritalStatus);
        person.setGender(gender);
        person.setEmissionDateOfDocumentIdYearMonthDay(idEmissionDate);
        person.setExpirationDateOfDocumentIdYearMonthDay(idExpirationDate);
        person.setEmissionLocationOfDocumentId(idArquive);
        person.setIdentification(idDocNumberString, idDocumentType);
        person.setSocialSecurityNumber(socialSecurityNumber); // contribuinte
        person.setFiscalCode(fiscalNeighborhood); // bairro fiscal
        person.setCountry(nationality);
    }

    private boolean hasEqualBirthInformation(YearMonthDay dateOfBirth, String parishOfBirth, String districtOfBirth,
            String districtSubdivisionOfBirth, String nameOfFather, String nameOfMother, Person person) {
        return equal(person.getDateOfBirthYearMonthDay(), dateOfBirth) && equal(person.getParishOfBirth(), parishOfBirth)
                && equal(person.getDistrictOfBirth(), districtOfBirth)
                && equal(person.getDistrictSubdivisionOfBirth(), districtSubdivisionOfBirth)
                && equal(person.getNameOfFather(), nameOfFather) && equal(person.getNameOfMother(), nameOfMother);
    }

    private void setBirthInformation(YearMonthDay dateOfBirth, String parishOfBirth, String districtOfBirth,
            String districtSubdivisionOfBirth, String nameOfFather, String nameOfMother, Person person) {
        person.setDateOfBirthYearMonthDay(dateOfBirth);
        person.setParishOfBirth(parishOfBirth);
        person.setDistrictOfBirth(districtOfBirth);
        person.setDistrictSubdivisionOfBirth(districtSubdivisionOfBirth);
        person.setNameOfFather(nameOfFather);
        person.setNameOfMother(nameOfMother);
    }

    private Gender getGender(String gender) {
        if (!StringUtils.isEmpty(gender)) {
            if (gender.equalsIgnoreCase(FEMALE)) {
                return Gender.FEMALE;
            } else if (gender.equalsIgnoreCase(MALE)) {
                return Gender.MALE;
            }
        }
        return null;
    }

    private YearMonthDay getLocalDateFromString(String dateString) {
        YearMonthDay date = null;
        if (!StringUtils.isEmpty(dateString)) {
            date = new YearMonthDay(Timestamp.valueOf(dateString));
        }
        return date;
    }

    private String getValidID(IDDocumentType idDocumentType, String idDocNumberString) {
        if (idDocumentType != null && idDocumentType.equals(IDDocumentType.IDENTITY_CARD)) {
            String idDocNumberStringTrimmed = idDocNumberString.replaceAll("\\s*", "");
            try {
                Integer.valueOf(idDocNumberStringTrimmed);
                return idDocNumberStringTrimmed;
            } catch (NumberFormatException e) {
                if (idDocNumberStringTrimmed.matches("\\d{7,8}\\d[a-zA-Z][a-zA-Z]\\d")) {
                    String[] split = idDocNumberStringTrimmed.split("\\d[a-zA-Z][a-zA-Z]\\d", 0);
                    return Integer.valueOf(split[0]).toString();
                }
            }
        } else {
            return idDocNumberString;
        }
        return null;
    }

    @Override
    protected String getQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT emp.EMP_NUM, emp.EMP_NOM, info.emp_sex,info.emp_stcivil");
        query.append(",info.emp_bi_tp, info.emp_bi_num, info.emp_bi_arq, info.emp_bi_dt, info.emp_bi_val_dt, info.emp_nsc_dt");
        query.append(",info.emp_num_fisc,emp_bfiscal");
        query.append(",(select emp_nom_famil from sldemp06 where emp_grau_parent = 'P' and emp_sex='M' and rownum=1 and emp.EMP_NUM=emp_num) as father");
        query.append(",(select emp_nom_famil from sldemp06 where emp_grau_parent = 'P' and emp_sex='F' and rownum=1 and emp.EMP_NUM=emp_num) as mother");
        query.append(", nac.nac_dsc, info.emp_nat_loc, info.emp_nat_frg, info.emp_nat_cnc, info.emp_nat_dst");
        query.append(" FROM SLDEMP01 emp, SLDEMP03 info, SLTNAC nac");
        query.append(" WHERE emp.EMP_NUM = info.EMP_NUM and info.emp_nac=nac.emp_nac");
        return query.toString();
    }

}

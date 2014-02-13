package pt.ist.fenix.giafsync;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.Absence;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.FunctionsAccumulation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GrantOwnerEquivalent;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalContractType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRelation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ServiceExemption;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;
import org.slf4j.Logger;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

class GiafMetadata {
    private Map<Integer, Employee> employees = null;

    private Map<String, Country> countries = new HashMap<String, Country>();

    private Map<String, MaritalStatus> maritalStatus = new HashMap<String, MaritalStatus>();

    private Map<String, IDDocumentType> documentTypes = new HashMap<String, IDDocumentType>();

    private Map<String, ContractSituation> contractSituations = new HashMap<String, ContractSituation>();

    private Map<String, ProfessionalCategory> professionalCategories = new HashMap<String, ProfessionalCategory>();

    private Map<String, ProfessionalRelation> professionalRelations = new HashMap<String, ProfessionalRelation>();

    private Map<String, ProfessionalContractType> professionalContractTypes = new HashMap<String, ProfessionalContractType>();

    private Map<String, ProfessionalRegime> professionalRegimes = new HashMap<String, ProfessionalRegime>();

    private Map<String, FunctionsAccumulation> functionsAccumulations = new HashMap<String, FunctionsAccumulation>();

    private Map<String, GrantOwnerEquivalent> grantOwnerEquivalences = new HashMap<String, GrantOwnerEquivalent>();

    private Map<String, ServiceExemption> serviceExemptions = new HashMap<String, ServiceExemption>();

    private Map<String, Absence> absences = new HashMap<String, Absence>();

    public GiafMetadata() {
        for (FunctionsAccumulation functionsAccumulation : Bennu.getInstance().getFunctionsAccumulationsSet()) {
            functionsAccumulations.put(functionsAccumulation.getGiafId(), functionsAccumulation);
        }
        for (GrantOwnerEquivalent grantOwnerEquivalent : Bennu.getInstance().getGrantOwnerEquivalencesSet()) {
            grantOwnerEquivalences.put(grantOwnerEquivalent.getGiafId(), grantOwnerEquivalent);
        }
        for (ServiceExemption serviceExemption : Bennu.getInstance().getServiceExemptionsSet()) {
            serviceExemptions.put(serviceExemption.getGiafId(), serviceExemption);
        }
        for (ProfessionalContractType professionalContractType : Bennu.getInstance().getProfessionalContractTypesSet()) {
            professionalContractTypes.put(professionalContractType.getGiafId(), professionalContractType);
        }
        for (Absence absence : Bennu.getInstance().getAbsencesSet()) {
            absences.put(absence.getGiafId(), absence);
        }
        for (ContractSituation contractSituation : Bennu.getInstance().getContractSituationsSet()) {
            contractSituations.put(contractSituation.getGiafId(), contractSituation);
        }
        for (ProfessionalCategory professionalCategory : Bennu.getInstance().getProfessionalCategoriesSet()) {
            professionalCategories.put(professionalCategory.getGiafId(), professionalCategory);
        }
        for (ProfessionalRegime professionalRegime : Bennu.getInstance().getProfessionalRegimesSet()) {
            professionalRegimes.put(professionalRegime.getGiafId(), professionalRegime);
        }
        for (ProfessionalRelation professionalRelation : Bennu.getInstance().getProfessionalRelationsSet()) {
            professionalRelations.put(professionalRelation.getGiafId(), professionalRelation);
        }
        for (Country country : Country.readDistinctCountries()) {
            countries.put(StringNormalizer.normalize(country.getName()), country);
        }
        countries.put("checa (republica)", Country.readByThreeLetterCode("CZE"));
        countries.put("irao (republica islamica do)", Country.readByThreeLetterCode("IRN"));
        countries.put("coreia (republica da)", Country.readByThreeLetterCode("KOR"));
        countries.put("coreia (rep.pop.dem.da)", Country.readByThreeLetterCode("PRK"));
        countries.put("emiratos arabes unidos", Country.readByThreeLetterCode("ARE"));
        countries.put("zimbabwe", Country.readByThreeLetterCode("ZWE"));
        countries.put("servia-e-montenegro", Country.readByThreeLetterCode("SCG"));
        countries.put("libia (jamahiriya arabe da)", Country.readByThreeLetterCode("LBY"));
        countries.put("mauricias", Country.readByThreeLetterCode("MUS"));
        countries.put("bosnia-e-herzegovina", Country.readByThreeLetterCode("BIH"));
        countries.put("paises baixos", Country.readByThreeLetterCode("NLD"));

        maritalStatus.put("ST", MaritalStatus.SINGLE);
        maritalStatus.put("CS", MaritalStatus.MARRIED);
        maritalStatus.put("VV", MaritalStatus.WIDOWER);
        maritalStatus.put("DV", MaritalStatus.DIVORCED);
        maritalStatus.put("SJ", MaritalStatus.SEPARATED);
        maritalStatus.put("UF", MaritalStatus.CIVIL_UNION);
        maritalStatus.put("D", MaritalStatus.UNKNOWN);

        documentTypes.put("AUT", IDDocumentType.RESIDENCE_AUTHORIZATION);
        documentTypes.put("BI", IDDocumentType.IDENTITY_CARD);
        // CP N.Cedula Pessoal
        documentTypes.put("P", IDDocumentType.PASSPORT);
        // BN Boletim Nascimento
        documentTypes.put("BI-E", IDDocumentType.FOREIGNER_IDENTITY_CARD);
        documentTypes.put("BI-M", IDDocumentType.NAVY_IDENTITY_CARD);
        documentTypes.put("BI-P", IDDocumentType.NATIVE_COUNTRY_IDENTITY_CARD);
        documentTypes.put("BI-F", IDDocumentType.AIR_FORCE_IDENTITY_CARD);
        documentTypes.put("CC", IDDocumentType.IDENTITY_CARD);
    }

    public Absence absence(String giafId) {
        return absences.get(giafId);
    }

    public void registerAbsence(String giafId, final MultiLanguageString name) {
        absences.put(giafId, new Absence(giafId, name));
    }

    public Collection<Absence> absences() {
        return absences.values();
    }

    public FunctionsAccumulation accumulation(String giafId) {
        return functionsAccumulations.get(giafId);
    }

    public void registerAccumulation(String giafId, final MultiLanguageString name) {
        functionsAccumulations.put(giafId, new FunctionsAccumulation(giafId, name));
    }

    public ProfessionalCategory category(String giafId) {
        return professionalCategories.get(giafId);
    }

    public void registerCategory(String giafId, CategoryType categoryType, MultiLanguageString description) {
        professionalCategories.put(giafId, new ProfessionalCategory(giafId, description, categoryType));
    }

    public ProfessionalContractType contractType(String giafId) {
        return professionalContractTypes.get(giafId);
    }

    public void registerContractType(String giafId, final MultiLanguageString name) {
        professionalContractTypes.put(giafId, new ProfessionalContractType(giafId, name));
    }

    public Country country(String name) {
        return countries.get(name);
    }

    public ServiceExemption exemption(String giafId) {
        return serviceExemptions.get(giafId);
    }

    public void registerExemption(String giafId, final MultiLanguageString name) {
        serviceExemptions.put(giafId, new ServiceExemption(giafId, name));
    }

    public Employee getEmployee(String employeeNumberString, Logger logger) {
        if (employees == null) {
            employees = new HashMap<Integer, Employee>();
            for (Employee employee : Bennu.getInstance().getEmployeesSet()) {
                employees.put(employee.getEmployeeNumber(), employee);
            }
        }
        try {
            return employees.get(Integer.parseInt(employeeNumberString));
        } catch (NumberFormatException e) {
            logger.debug("Number problem: " + employeeNumberString);
        }
        return null;
    }

    protected Map<Integer, Employee> getEmployeesMap() {
        Map<Integer, Employee> employees = new HashMap<Integer, Employee>();
        for (Employee employee : Bennu.getInstance().getEmployeesSet()) {
            employees.put(employee.getEmployeeNumber(), employee);
        }
        return employees;
    }

    public Person getPerson(String employeeNumberString, Logger logger) {
        Employee employee = getEmployee(employeeNumberString, logger);
        return employee != null ? employee.getPerson() : null;
    }

    public GrantOwnerEquivalent grantOwnerEquivalent(String giafId) {
        return grantOwnerEquivalences.get(giafId);
    }

    public void registerGrantOwnerEquivalent(String giafId, final MultiLanguageString name) {
        grantOwnerEquivalences.put(giafId, new GrantOwnerEquivalent(giafId, name));
    }

    public ProfessionalRegime regime(String giafId) {
        return professionalRegimes.get(giafId);
    }

    public void registerRegime(String giafId, Integer weighting, BigDecimal fullTimeEquivalent, CategoryType categoryType,
            MultiLanguageString name) {
        professionalRegimes.put(giafId, new ProfessionalRegime(giafId, name, weighting, fullTimeEquivalent, categoryType));
    }

    public ProfessionalRelation relation(String giafId) {
        return professionalRelations.get(giafId);
    }

    public void registerRelation(String giafId, Boolean fullTimeEquivalent, MultiLanguageString name) {
        professionalRelations.put(giafId, new ProfessionalRelation(giafId, name, fullTimeEquivalent));
    }

    public ContractSituation situation(String giafId) {
        return contractSituations.get(giafId);
    }

    public void registerSituation(String giafId, Boolean endSituation, Boolean serviceExemption, MultiLanguageString description) {
        contractSituations.put(giafId, new ContractSituation(giafId, description, endSituation, serviceExemption));
    }

    public MaritalStatus maritalStatus(String key) {
        return maritalStatus.get(key);
    }

    public IDDocumentType documentType(String idDocTypeString) {
        return documentTypes.get(idDocTypeString);
    }
}

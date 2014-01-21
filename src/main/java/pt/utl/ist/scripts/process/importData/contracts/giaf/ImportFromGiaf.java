package pt.utl.ist.scripts.process.importData.contracts.giaf;

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

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.commons.StringNormalizer;

public abstract class ImportFromGiaf extends CronTask {
    abstract protected String getQuery();

    @Override
    public void runTask() throws Exception {
        synchronized (ImportFromGiaf.class) {
            process();
        }
    }

    protected abstract void process() throws Exception;

    protected Map<Integer, Employee> getEmployeesMap() {
        Map<Integer, Employee> employees = new HashMap<Integer, Employee>();
        for (Employee employee : Bennu.getInstance().getEmployeesSet()) {
            employees.put(employee.getEmployeeNumber(), employee);
        }
        return employees;
    }

    protected Integer getEmployeeNumber(String employeeNumberString) {
        try {
            return Integer.parseInt(employeeNumberString);
        } catch (NumberFormatException e) {
            getLogger().info("Number problem: " + employeeNumberString);
        }
        return null;
    }

    protected Person getPerson(Map<Integer, Employee> employees, String employeeNumberString) {
        try {
            Integer employeeNumber = Integer.parseInt(employeeNumberString);
            Employee employee = employees.get(employeeNumber);
            return employee != null ? employee.getPerson() : null;
        } catch (NumberFormatException e) {
            getLogger().info("Number problem: " + employeeNumberString);
        }
        return null;
    }

    protected Map<String, MaritalStatus> getMaritalStatusMap() {
        Map<String, MaritalStatus> maritalStatus = new HashMap<String, MaritalStatus>();
        maritalStatus.put("ST", MaritalStatus.SINGLE);
        maritalStatus.put("CS", MaritalStatus.MARRIED);
        maritalStatus.put("VV", MaritalStatus.WIDOWER);
        maritalStatus.put("DV", MaritalStatus.DIVORCED);
        maritalStatus.put("SJ", MaritalStatus.SEPARATED);
        maritalStatus.put("UF", MaritalStatus.CIVIL_UNION);
        maritalStatus.put("D", MaritalStatus.UNKNOWN);
        return maritalStatus;
    }

    protected Map<String, IDDocumentType> getDocumentTypeMap() {
        Map<String, IDDocumentType> result = new HashMap<String, IDDocumentType>();
        result.put("AUT", IDDocumentType.RESIDENCE_AUTHORIZATION);
        result.put("BI", IDDocumentType.IDENTITY_CARD);
        // CP N.Cedula Pessoal
        result.put("P", IDDocumentType.PASSPORT);
        // BN Boletim Nascimento
        result.put("BI-E", IDDocumentType.FOREIGNER_IDENTITY_CARD);
        result.put("BI-M", IDDocumentType.NAVY_IDENTITY_CARD);
        result.put("BI-P", IDDocumentType.NATIVE_COUNTRY_IDENTITY_CARD);
        result.put("BI-F", IDDocumentType.AIR_FORCE_IDENTITY_CARD);
        result.put("CC", IDDocumentType.IDENTITY_CARD);
        return result;
    }

    protected Map<String, Country> getCountryMap() {
        Map<String, Country> countries = new HashMap<String, Country>();
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

        return countries;
    }

    protected Map<String, ContractSituation> getContractSituationsMap() {
        Map<String, ContractSituation> contractSituations = new HashMap<String, ContractSituation>();
        for (ContractSituation contractSituation : Bennu.getInstance().getContractSituationsSet()) {
            contractSituations.put(contractSituation.getGiafId(), contractSituation);
        }
        return contractSituations;
    }

    protected Map<String, ProfessionalCategory> getProfessionalCategoriesMap() {
        Map<String, ProfessionalCategory> professionalCategories = new HashMap<String, ProfessionalCategory>();
        for (ProfessionalCategory professionalCategory : Bennu.getInstance().getProfessionalCategoriesSet()) {
            professionalCategories.put(professionalCategory.getGiafId(), professionalCategory);
        }
        return professionalCategories;
    }

    protected Map<String, ProfessionalRelation> getProfessionalRelationsMap() {
        Map<String, ProfessionalRelation> professionalRelations = new HashMap<String, ProfessionalRelation>();
        for (ProfessionalRelation professionalRelation : Bennu.getInstance().getProfessionalRelationsSet()) {
            professionalRelations.put(professionalRelation.getGiafId(), professionalRelation);
        }
        return professionalRelations;
    }

    protected Map<String, ProfessionalContractType> getProfessionalContractTypesMap() {
        Map<String, ProfessionalContractType> professionalContractTypes = new HashMap<String, ProfessionalContractType>();
        for (ProfessionalContractType professionalContractType : Bennu.getInstance().getProfessionalContractTypesSet()) {
            professionalContractTypes.put(professionalContractType.getGiafId(), professionalContractType);
        }
        return professionalContractTypes;
    }

    protected Map<String, ProfessionalRegime> getProfessionalRegimesMap() {
        Map<String, ProfessionalRegime> professionalRegimes = new HashMap<String, ProfessionalRegime>();
        for (ProfessionalRegime professionalRegime : Bennu.getInstance().getProfessionalRegimesSet()) {
            professionalRegimes.put(professionalRegime.getGiafId(), professionalRegime);
        }
        return professionalRegimes;
    }

    protected Map<String, FunctionsAccumulation> getFunctionsAccumulationsMap() {
        Map<String, FunctionsAccumulation> functionsAccumulations = new HashMap<String, FunctionsAccumulation>();
        for (FunctionsAccumulation functionsAccumulation : Bennu.getInstance().getFunctionsAccumulationsSet()) {
            functionsAccumulations.put(functionsAccumulation.getGiafId(), functionsAccumulation);
        }
        return functionsAccumulations;
    }

    protected Map<String, GrantOwnerEquivalent> getGrantOwnerEquivalencesMap() {
        Map<String, GrantOwnerEquivalent> grantOwnerEquivalences = new HashMap<String, GrantOwnerEquivalent>();
        for (GrantOwnerEquivalent grantOwnerEquivalent : Bennu.getInstance().getGrantOwnerEquivalencesSet()) {
            grantOwnerEquivalences.put(grantOwnerEquivalent.getGiafId(), grantOwnerEquivalent);
        }
        return grantOwnerEquivalences;
    }

    protected Map<String, ServiceExemption> getServiceExemptionsMap() {
        Map<String, ServiceExemption> serviceExemptions = new HashMap<String, ServiceExemption>();
        for (ServiceExemption serviceExemption : Bennu.getInstance().getServiceExemptionsSet()) {
            serviceExemptions.put(serviceExemption.getGiafId(), serviceExemption);
        }
        return serviceExemptions;
    }

    protected Map<String, Absence> getAbsencesMap() {
        Map<String, Absence> absences = new HashMap<String, Absence>();
        for (Absence absence : Bennu.getInstance().getAbsencesSet()) {
            absences.put(absence.getGiafId(), absence);
        }
        return absences;
    }

}

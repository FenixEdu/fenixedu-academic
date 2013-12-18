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
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalContractType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRelation;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import net.sourceforge.fenixedu.util.StringUtils;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;

@Task(englishTitle = "ImportPersonProfessionalData")
public class ImportPersonProfessionalData extends ImportFromGiaf {

    public ImportPersonProfessionalData() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportPersonProfessionalData");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<Integer, Employee> employeesMap = getEmployeesMap();
            Map<String, ContractSituation> contractSituationsMap = getContractSituationsMap();
            Map<String, ProfessionalCategory> professionalCategoriesMap = getProfessionalCategoriesMap();
            Map<String, ProfessionalRelation> professionalRelationMap = getProfessionalRelationsMap();
            Map<String, ProfessionalContractType> professionalContractTypesMap = getProfessionalContractTypesMap();
            Map<String, ProfessionalRegime> professionalRegimesMap = getProfessionalRegimesMap();
            String query = getQuery();
            getLogger().debug(query);
            PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            int count = 0;
            int novos = 0;
            int notImported = 0;
            Set<Person> importedButInvalid = new HashSet<Person>();
            int dontExist = 0;
            int deleted = 0;
            int totalInFenix = 0;
            int repeted = 0;
            while (result.next()) {
                count++;
                String personNumberString = result.getString("emp_num");
                Person person = getPerson(employeesMap, personNumberString);
                if (person == null) {
                    getLogger().info("Invalid person with number: " + personNumberString);
                    dontExist++;
                    continue;
                }

                Campus campus = getCampus(result.getInt("EMP_LOC_TRAB"));

                String institutionEntryDateString = result.getString("emp_adm_emp_dt");
                LocalDate institutionEntryDate = null;
                if (!StringUtils.isEmpty(institutionEntryDateString)) {
                    institutionEntryDate = new LocalDate(Timestamp.valueOf(institutionEntryDateString));
                }

                String contractSituationGiafId = result.getString("emp_sit");
                ContractSituation contractSituation = contractSituationsMap.get(contractSituationGiafId);
                if (contractSituation == null) {
                    getLogger().info("Empty situation: " + contractSituationGiafId + " for person number: " + personNumberString);
                    importedButInvalid.add(person);
                }
                String contractSituationDateString = result.getString("emp_sit_dt");
                LocalDate contractSituationDate = null;
                if (!StringUtils.isEmpty(contractSituationDateString)) {
                    contractSituationDate = new LocalDate(Timestamp.valueOf(contractSituationDateString));
                }

                String terminationSituationDateString = result.getString("emp_rz_dem_dt");
                if ((!StringUtils.isEmpty(terminationSituationDateString) || !StringUtils.isEmpty(terminationSituationDateString))
                        && (contractSituation == null || !contractSituation.getEndSituation())) {
                    getLogger().info(
                            "Non end situation with endSituationDate: " + contractSituationGiafId + " for person number: "
                                    + personNumberString);
                }
                LocalDate terminationSituationDate = null;
                if (!StringUtils.isEmpty(terminationSituationDateString)) {
                    terminationSituationDate = new LocalDate(Timestamp.valueOf(terminationSituationDateString));
                }

                String professionalRelationGiafId = result.getString("EMP_VINCULO");
                ProfessionalRelation professionalRelation = professionalRelationMap.get(professionalRelationGiafId);
                if (professionalRelation == null) {
                    getLogger().info(
                            "Empty relation: " + professionalRelationGiafId + " for person number: " + personNumberString);
                    importedButInvalid.add(person);
                }
                String professionalRelationDateString = result.getString("EMP_VINCULO_DT");
                LocalDate professionalRelationDate = null;
                if (!StringUtils.isEmpty(professionalRelationDateString)) {
                    professionalRelationDate = new LocalDate(Timestamp.valueOf(professionalRelationDateString));
                }

                String professionalContractTypeGiafId = result.getString("EMP_TP_FUNC");
                ProfessionalContractType professionalContractType =
                        professionalContractTypesMap.get(professionalContractTypeGiafId);

                String professionalCategoryGiafId = result.getString("EMP_CAT_FUNC");
                ProfessionalCategory professionalCategory = professionalCategoriesMap.get(professionalCategoryGiafId);
                if (professionalCategory == null) {
                    getLogger().info(
                            "Empty category: " + professionalCategoryGiafId + " for person number: " + personNumberString);
                    importedButInvalid.add(person);
                }
                String professionalCategoryDateString = result.getString("EMP_CAT_FUNC_DT");
                LocalDate professionalCategoryDate = null;
                if (!StringUtils.isEmpty(professionalCategoryDateString)) {
                    professionalCategoryDate = new LocalDate(Timestamp.valueOf(professionalCategoryDateString));
                }

                String professionalRegimeGiafId = result.getString("EMP_REGIME");
                ProfessionalRegime professionalRegime = professionalRegimesMap.get(professionalRegimeGiafId);
                if (professionalRegime == null) {
                    getLogger().info("Empty regime: " + professionalRegimeGiafId + " for person number: " + personNumberString);
                    importedButInvalid.add(person);
                }
                String professionalRegimeDateString = result.getString("EMP_REGIME_DT");
                LocalDate professionalRegimeDate = null;
                if (!StringUtils.isEmpty(professionalRegimeDateString)) {
                    professionalRegimeDate = new LocalDate(Timestamp.valueOf(professionalRegimeDateString));
                }

                String creationDateString = result.getString("data_criacao");
                DateTime creationDate = null;
                if (!StringUtils.isEmpty(creationDateString)) {
                    creationDate = new DateTime(Timestamp.valueOf(creationDateString));
                }

                DateTime modifiedDate = null;
                String modifiedDateString = result.getString("data_alteracao");
                if (!StringUtils.isEmpty(modifiedDateString)) {
                    modifiedDate = new DateTime(Timestamp.valueOf(modifiedDateString));
                }

                PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
                if (personProfessionalData == null) {
                    personProfessionalData = new PersonProfessionalData(person);
                }

                GiafProfessionalData giafProfessionalData =
                        personProfessionalData.getGiafProfessionalDataByGiafPersonIdentification(personNumberString);

                if (giafProfessionalData == null) {
                    new GiafProfessionalData(personProfessionalData, personNumberString, institutionEntryDate, contractSituation,
                            contractSituationGiafId, contractSituationDate, terminationSituationDate, professionalRelation,
                            professionalRelationGiafId, professionalRelationDate, professionalContractType,
                            professionalContractTypeGiafId, professionalCategory, professionalCategoryGiafId,
                            professionalCategoryDate, professionalRegime, professionalRegimeGiafId, professionalRegimeDate,
                            campus, creationDate, modifiedDate);
                    novos++;
                } else {
                    if (!hasDiferences(giafProfessionalData, institutionEntryDate, contractSituation, contractSituationGiafId,
                            contractSituationDate, terminationSituationDate, professionalRelation, professionalRelationGiafId,
                            professionalRelationDate, professionalContractType, professionalContractTypeGiafId,
                            professionalCategory, professionalCategoryGiafId, professionalCategoryDate, professionalRegime,
                            professionalRegimeGiafId, professionalRegimeDate, campus, creationDate, modifiedDate)) {
                        giafProfessionalData.setInstitutionEntryDate(institutionEntryDate);
                        giafProfessionalData.setContractSituation(contractSituation);
                        giafProfessionalData.setContractSituationGiafId(contractSituationGiafId);
                        giafProfessionalData.setContractSituationDate(contractSituationDate);
                        giafProfessionalData.setTerminationSituationDate(terminationSituationDate);
                        giafProfessionalData.setProfessionalRelation(professionalRelation);
                        giafProfessionalData.setProfessionalRelationGiafId(professionalRelationGiafId);
                        giafProfessionalData.setProfessionalRelationDate(professionalRelationDate);
                        giafProfessionalData.setProfessionalContractType(professionalContractType);
                        giafProfessionalData.setProfessionalContractTypeGiafId(professionalContractTypeGiafId);
                        giafProfessionalData.setProfessionalCategory(professionalCategory);
                        giafProfessionalData.setProfessionalCategoryGiafId(professionalCategoryGiafId);
                        giafProfessionalData.setProfessionalCategoryDate(professionalCategoryDate);
                        giafProfessionalData.setProfessionalRegime(professionalRegime);
                        giafProfessionalData.setProfessionalRegimeGiafId(professionalRegimeGiafId);
                        giafProfessionalData.setProfessionalRegimeDate(professionalRegimeDate);
                        giafProfessionalData.setCampus(campus);
                        giafProfessionalData.setCreationDate(creationDate);
                        giafProfessionalData.setModifiedDate(modifiedDate);
                    }
                }
            }
            result.close();
            preparedStatement.close();

            oracleConnection.closeConnection();
            totalInFenix = Bennu.getInstance().getGiafProfessionalDataSet().size();
            getLogger().info("Total GIAF: " + count);
            getLogger().info("New: " + novos);
            getLogger().info("Deleted: " + deleted);
            getLogger().info("Not imported: " + notImported);
            getLogger().info("Imported with errors: " + importedButInvalid.size());
            getLogger().info("Repeted: " + repeted);
            getLogger().info("Invalid persons: " + dontExist);
            getLogger().info("Total Fénix: " + totalInFenix);
            getLogger().info("Total Fénix without errors: " + (totalInFenix - importedButInvalid.size()));
            getLogger().info("Missing in Fénix: " + (count - totalInFenix));

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportPersonProfessionalData -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportPersonProfessionalData -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private Campus getCampus(int campus) {
        if (campus == 1) {
            return FenixFramework.getDomainObject("2465311230081");//Alameda
        } else if (campus == 2) {
            return FenixFramework.getDomainObject("2465311230082");//Taguspark
        } else if (campus == 3) {
            return FenixFramework.getDomainObject("2465311261622");//ITN
        }
        return null;
    }

    private boolean hasDiferences(GiafProfessionalData giafProfessionalData, LocalDate institutionEntryDate,
            ContractSituation contractSituation, String contractSituationGiafId, LocalDate contractSituationDate,
            LocalDate terminationSituationDate, ProfessionalRelation professionalRelation, String professionalRelationGiafId,
            LocalDate professionalRelationDate, ProfessionalContractType professionalContractType,
            String professionalContractTypeGiafId, ProfessionalCategory professionalCategory, String professionalCategoryGiafId,
            LocalDate professionalCategoryDate, ProfessionalRegime professionalRegime, String professionalRegimeGiafId,
            LocalDate professionalRegimeDate, Campus campus, DateTime creationDate, DateTime modifiedDate) {
        return (equal(giafProfessionalData.getContractSituation(), contractSituation)
                && equal(giafProfessionalData.getInstitutionEntryDate(), institutionEntryDate)
                && equal(giafProfessionalData.getContractSituationGiafId(), contractSituationGiafId)
                && equal(giafProfessionalData.getContractSituationDate(), contractSituationDate)
                && equal(giafProfessionalData.getTerminationSituationDate(), terminationSituationDate)
                && equal(giafProfessionalData.getProfessionalRelation(), professionalRelation)
                && equal(giafProfessionalData.getProfessionalRelationGiafId(), professionalRelationGiafId)
                && equal(giafProfessionalData.getProfessionalRelationDate(), professionalRelationDate)
                && equal(giafProfessionalData.getProfessionalContractType(), professionalContractType)
                && equal(giafProfessionalData.getProfessionalContractTypeGiafId(), professionalContractTypeGiafId)
                && equal(giafProfessionalData.getProfessionalCategory(), professionalCategory)
                && equal(giafProfessionalData.getProfessionalCategoryGiafId(), professionalCategoryGiafId)
                && equal(giafProfessionalData.getProfessionalCategoryDate(), professionalCategoryDate)
                && equal(giafProfessionalData.getProfessionalRegime(), professionalRegime)
                && equal(giafProfessionalData.getProfessionalRegimeGiafId(), professionalRegimeGiafId)
                && equal(giafProfessionalData.getProfessionalRegimeDate(), professionalRegimeDate)
                && equal(giafProfessionalData.getCampus(), campus) && equal(giafProfessionalData.getCreationDate(), creationDate) && equal(
                    giafProfessionalData.getModifiedDate(), modifiedDate));
    }

    @Override
    protected String getQuery() {
        return "SELECT emp.emp_num, EMP_LOC_TRAB, emp_adm_emp_dt, emp.EMP_SIT, emp.emp_sit_dt,emp.emp_rz_dem, emp.emp_rz_dem_dt, emp.EMP_VINCULO, emp.EMP_VINCULO_DT ,emp.EMP_TP_FUNC, emp.EMP_CAT_FUNC, emp.EMP_CAT_FUNC_DT,emp.EMP_REGIME, emp.EMP_REGIME_DT, emp.DATA_ALTERACAO, to_date(to_char(emp.DATA_CRIACAO, 'RR-MM-DD HH24:MI:SS'), 'RR-MM-DD HH24:MI:SS') as DATA_CRIACAO FROM SLDEMP01 emp";
    }
    // -- emp_adm_emp_dt
    // -- univ_adm_dt

    // SELECT emp.UNIV_ADM_DT,emp.EMP_ADM_EMP_DT
    // , emp.EMP_SIT, emp.emp_sit_dt
    // ,emp.EMP_CP_INICIO_DT, emp.EMP_CP_FIM_DT
    // , emp.EMP_VINCULO, emp.EMP_VINCULO_DT
    // ,emp.EMP_TP_FUNC, (
    // DECODE(emp.EMP_TP_FUNC,'PR','Privado','PU','Público','BA','Bancário','Inválido')
    // ) as tipo
    // ,emp.emp_rz_dem, emp.emp_rz_dem_dt
    // , emp.EMP_CAT_INT, emp.emp_cat_int_dt
    // ,emp.EMP_CARREIRA, emp.EMP_CARREIRA_DT
    // , emp.EMP_CAT_FUNC, emp.EMP_CAT_FUNC_DT
    // , emp.EMP_PROFISSAO, emp.EMP_PROFISSAO_DT
    // ,emp.EMP_REGIME, emp.EMP_REGIME_DT
    // , emp.EMP_LOC_TRAB
    // , emp.EMP_SEC_SERV, emp.EMP_CCUSTO, emp.EMP_SCCUSTO
    // , emp.EMP_DIREC_DT, emp.EMP_SEC_SERV_DT
    // , emp.EMP_CLFUNC_DT
    // , emp.DATA_ALTERACAO, emp.DATA_CRIACAO
    // FROM SLDEMP01 emp

}

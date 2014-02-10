package pt.ist.fenix.giafsync;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalContractType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRelation;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;

import pt.ist.fenix.giafsync.GiafSync.ImportProcessor;
import pt.ist.fenix.giafsync.GiafSync.Modification;
import pt.ist.fenixframework.FenixFramework;

class ImportPersonProfessionalData extends ImportProcessor {
    @Override
    public List<Modification> processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        List<Modification> modifications = new ArrayList<>();
        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(getQuery());
        ResultSet result = preparedStatement.executeQuery();
        int count = 0;
        int novos = 0;
        int notImported = 0;
        int dontExist = 0;
        int deleted = 0;
        int totalInFenix = 0;
        int repeted = 0;
        Set<Person> importedButInvalid = new HashSet<Person>();
        while (result.next()) {
            count++;
            final String personNumberString = result.getString("emp_num");
            final Person person = metadata.getPerson(personNumberString, logger);
            if (person == null) {
                logger.debug("Invalid person with number: " + personNumberString);
                dontExist++;
                continue;
            }

            final Campus campus = getCampus(result.getInt("EMP_LOC_TRAB"));

            String institutionEntryDateString = result.getString("emp_adm_emp_dt");
            final LocalDate institutionEntryDate =
                    StringUtils.isEmpty(institutionEntryDateString) ? null : new LocalDate(
                            Timestamp.valueOf(institutionEntryDateString));

            final String contractSituationGiafId = result.getString("emp_sit");
            final ContractSituation contractSituation = metadata.situation(contractSituationGiafId);
            if (contractSituation == null) {
                logger.debug("Empty situation: " + contractSituationGiafId + " for person number: " + personNumberString);
                importedButInvalid.add(person);
            }
            String contractSituationDateString = result.getString("emp_sit_dt");
            final LocalDate contractSituationDate =
                    StringUtils.isEmpty(contractSituationDateString) ? null : new LocalDate(
                            Timestamp.valueOf(contractSituationDateString));

            String terminationSituationDateString = result.getString("emp_rz_dem_dt");
            if ((!StringUtils.isEmpty(terminationSituationDateString) || !StringUtils.isEmpty(terminationSituationDateString))
                    && (contractSituation == null || !contractSituation.getEndSituation())) {
                logger.debug("Non end situation with endSituationDate: " + contractSituationGiafId + " for person number: "
                        + personNumberString);
            }
            final LocalDate terminationSituationDate =
                    StringUtils.isEmpty(terminationSituationDateString) ? null : new LocalDate(
                            Timestamp.valueOf(terminationSituationDateString));

            final String professionalRelationGiafId = result.getString("EMP_VINCULO");
            final ProfessionalRelation professionalRelation = metadata.relation(professionalRelationGiafId);
            if (professionalRelation == null) {
                logger.debug("Empty relation: " + professionalRelationGiafId + " for person number: " + personNumberString);
                importedButInvalid.add(person);
            }
            String professionalRelationDateString = result.getString("EMP_VINCULO_DT");
            final LocalDate professionalRelationDate =
                    StringUtils.isEmpty(professionalRelationDateString) ? null : new LocalDate(
                            Timestamp.valueOf(professionalRelationDateString));

            final String professionalContractTypeGiafId = result.getString("EMP_TP_FUNC");
            final ProfessionalContractType professionalContractType = metadata.contractType(professionalContractTypeGiafId);

            final String professionalCategoryGiafId = result.getString("EMP_CAT_FUNC");
            final ProfessionalCategory professionalCategory = metadata.category(professionalCategoryGiafId);
            if (professionalCategory == null) {
                logger.debug("Empty category: " + professionalCategoryGiafId + " for person number: " + personNumberString);
                importedButInvalid.add(person);
            }
            String professionalCategoryDateString = result.getString("EMP_CAT_FUNC_DT");
            final LocalDate professionalCategoryDate =
                    StringUtils.isEmpty(professionalCategoryDateString) ? null : new LocalDate(
                            Timestamp.valueOf(professionalCategoryDateString));

            final String professionalRegimeGiafId = result.getString("EMP_REGIME");
            final ProfessionalRegime professionalRegime = metadata.regime(professionalRegimeGiafId);
            if (professionalRegime == null) {
                logger.debug("Empty regime: " + professionalRegimeGiafId + " for person number: " + personNumberString);
                importedButInvalid.add(person);
            }
            String professionalRegimeDateString = result.getString("EMP_REGIME_DT");
            final LocalDate professionalRegimeDate =
                    StringUtils.isEmpty(professionalRegimeDateString) ? null : new LocalDate(
                            Timestamp.valueOf(professionalRegimeDateString));

            String creationDateString = result.getString("data_criacao");
            final DateTime creationDate =
                    StringUtils.isEmpty(creationDateString) ? null : new DateTime(Timestamp.valueOf(creationDateString));

            String modifiedDateString = result.getString("data_alteracao");
            final DateTime modifiedDate =
                    StringUtils.isEmpty(modifiedDateString) ? null : new DateTime(Timestamp.valueOf(modifiedDateString));

            final PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
            if (personProfessionalData == null) {
                modifications.add(new Modification() {
                    @Override
                    public void execute() {
                        PersonProfessionalData personProfessionalData = new PersonProfessionalData(person);
                        new GiafProfessionalData(personProfessionalData, personNumberString, institutionEntryDate,
                                contractSituation, contractSituationGiafId, contractSituationDate, terminationSituationDate,
                                professionalRelation, professionalRelationGiafId, professionalRelationDate,
                                professionalContractType, professionalContractTypeGiafId, professionalCategory,
                                professionalCategoryGiafId, professionalCategoryDate, professionalRegime,
                                professionalRegimeGiafId, professionalRegimeDate, campus, creationDate, modifiedDate);
                    }
                });
                novos++;
            } else {
                final GiafProfessionalData giafProfessionalData =
                        personProfessionalData.getGiafProfessionalDataByGiafPersonIdentification(personNumberString);
                if (giafProfessionalData == null) {
                    modifications.add(new Modification() {
                        @Override
                        public void execute() {
                            new GiafProfessionalData(personProfessionalData, personNumberString, institutionEntryDate,
                                    contractSituation, contractSituationGiafId, contractSituationDate, terminationSituationDate,
                                    professionalRelation, professionalRelationGiafId, professionalRelationDate,
                                    professionalContractType, professionalContractTypeGiafId, professionalCategory,
                                    professionalCategoryGiafId, professionalCategoryDate, professionalRegime,
                                    professionalRegimeGiafId, professionalRegimeDate, campus, creationDate, modifiedDate);
                        }
                    });
                    novos++;
                } else {
                    if (!hasDiferences(giafProfessionalData, institutionEntryDate, contractSituation, contractSituationGiafId,
                            contractSituationDate, terminationSituationDate, professionalRelation, professionalRelationGiafId,
                            professionalRelationDate, professionalContractType, professionalContractTypeGiafId,
                            professionalCategory, professionalCategoryGiafId, professionalCategoryDate, professionalRegime,
                            professionalRegimeGiafId, professionalRegimeDate, campus, creationDate, modifiedDate)) {
                        modifications.add(new Modification() {
                            @Override
                            public void execute() {
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
                        });
                    }
                }
            }
        }
        result.close();
        preparedStatement.close();

        oracleConnection.closeConnection();
        totalInFenix = Bennu.getInstance().getGiafProfessionalDataSet().size();
        log.println("-- Professional data --");
        log.println("Total GIAF: " + count);
        log.println("New: " + novos);
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
        return (Objects.equals(giafProfessionalData.getContractSituation(), contractSituation)
                && Objects.equals(giafProfessionalData.getInstitutionEntryDate(), institutionEntryDate)
                && Objects.equals(giafProfessionalData.getContractSituationGiafId(), contractSituationGiafId)
                && Objects.equals(giafProfessionalData.getContractSituationDate(), contractSituationDate)
                && Objects.equals(giafProfessionalData.getTerminationSituationDate(), terminationSituationDate)
                && Objects.equals(giafProfessionalData.getProfessionalRelation(), professionalRelation)
                && Objects.equals(giafProfessionalData.getProfessionalRelationGiafId(), professionalRelationGiafId)
                && Objects.equals(giafProfessionalData.getProfessionalRelationDate(), professionalRelationDate)
                && Objects.equals(giafProfessionalData.getProfessionalContractType(), professionalContractType)
                && Objects.equals(giafProfessionalData.getProfessionalContractTypeGiafId(), professionalContractTypeGiafId)
                && Objects.equals(giafProfessionalData.getProfessionalCategory(), professionalCategory)
                && Objects.equals(giafProfessionalData.getProfessionalCategoryGiafId(), professionalCategoryGiafId)
                && Objects.equals(giafProfessionalData.getProfessionalCategoryDate(), professionalCategoryDate)
                && Objects.equals(giafProfessionalData.getProfessionalRegime(), professionalRegime)
                && Objects.equals(giafProfessionalData.getProfessionalRegimeGiafId(), professionalRegimeGiafId)
                && Objects.equals(giafProfessionalData.getProfessionalRegimeDate(), professionalRegimeDate)
                && Objects.equals(giafProfessionalData.getCampus(), campus)
                && Objects.equals(giafProfessionalData.getCreationDate(), creationDate) && Objects.equals(
                giafProfessionalData.getModifiedDate(), modifiedDate));
    }

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

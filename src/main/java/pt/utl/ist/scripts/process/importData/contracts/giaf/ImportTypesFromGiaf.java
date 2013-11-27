package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import net.sourceforge.fenixedu.domain.personnelSection.contracts.Absence;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.FunctionsAccumulation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GrantOwnerEquivalent;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalContractType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ServiceExemption;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.ist.bennu.scheduler.annotation.Task;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Task(englishTitle = "ImportTypesFromGiaf")
public class ImportTypesFromGiaf extends ImportFromGiaf {

    private static final String SERVICE_EXEMPTION = "71";

    private static final String GRANT_OWNER_EQUIVALENCE = "84";

    private static final String FUNCTIONS_ACCUMULATION = "46.6";

    private static final String CONTRACT_TYPE = "2";

    private static final String ABSENCE_TYPE = "J";

    public ImportTypesFromGiaf() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportTypesFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, FunctionsAccumulation> functionsAccumulationsMap = getFunctionsAccumulationsMap();
            Map<String, GrantOwnerEquivalent> grantOwnerEquivalencesMap = getGrantOwnerEquivalencesMap();
            Map<String, ServiceExemption> serviceExemptionsMap = getServiceExemptionsMap();
            Map<String, Absence> absencesMap = getAbsencesMap();
            Map<String, ProfessionalContractType> professionalContractTypesMap = getProfessionalContractTypesMap();

            getLogger().debug("Start ImportTypesFromGiaf");
            String query = getQuery();
            getLogger().debug(query);
            PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                String type = result.getString("TAB_NUM");
                String giafId = result.getString("tab_cod");
                String nameString = result.getString("tab_cod_alg");
                if (StringUtils.isEmpty(nameString)) {
                    nameString = result.getString("tab_cod_dsc");
                }
                MultiLanguageString name = new MultiLanguageString(pt.utl.ist.fenix.tools.util.i18n.Language.pt, nameString);

                if (type.equalsIgnoreCase(FUNCTIONS_ACCUMULATION)) {
                    FunctionsAccumulation functionsAccumulation = functionsAccumulationsMap.get(giafId);
                    if (functionsAccumulation != null) {
                        if (!functionsAccumulation.getName().equalInAnyLanguage(name)) {
                            functionsAccumulation.edit(name);
                            getLogger().info("Alterar functionsAccumulation: " + giafId + " - " + name);
                        }
                    } else {
                        new FunctionsAccumulation(giafId, name);
                        getLogger().info("Nova functionsAccumulation: " + giafId + " - " + name);
                    }
                } else if (type.equalsIgnoreCase(GRANT_OWNER_EQUIVALENCE)) {
                    GrantOwnerEquivalent grantOwnerEquivalent = grantOwnerEquivalencesMap.get(giafId);
                    if (grantOwnerEquivalent != null) {
                        if (!grantOwnerEquivalent.getName().equalInAnyLanguage(name)) {
                            grantOwnerEquivalent.edit(name);
                            getLogger().info("Alterar grantOwnerEquivalent: " + giafId + " - " + name);
                        }
                    } else {
                        new GrantOwnerEquivalent(giafId, name);
                        getLogger().info("Nova grantOwnerEquivalent: " + giafId + " - " + name);
                    }
                } else if (type.equalsIgnoreCase(SERVICE_EXEMPTION)) {
                    ServiceExemption serviceExemption = serviceExemptionsMap.get(giafId);
                    if (serviceExemption != null) {
                        if (!serviceExemption.getName().equalInAnyLanguage(name)) {
                            serviceExemption.edit(name);
                            getLogger().info("Alterar serviceExemption: " + giafId + " - " + name);
                        }
                    } else {
                        new ServiceExemption(giafId, name);
                        getLogger().info("Nova serviceExemption: " + giafId + " - " + name);
                    }
                } else if (type.equalsIgnoreCase(CONTRACT_TYPE)) {
                    ProfessionalContractType professionalContractType = professionalContractTypesMap.get(giafId);
                    if (professionalContractType != null) {
                        if (!professionalContractType.getName().equalInAnyLanguage(name)) {
                            professionalContractType.edit(name);
                            getLogger().info("Alterar professionalContractType: " + giafId + " - " + name);
                        }
                    } else {
                        new ProfessionalContractType(giafId, name);
                        getLogger().info("Nova professionalContractType: " + giafId + " - " + name);
                    }
                } else if (type.equalsIgnoreCase(ABSENCE_TYPE)) {
                    Absence absence = absencesMap.get(giafId);
                    if (absence != null) {
                        if (!absence.getName().equalInAnyLanguage(name)) {
                            absence.edit(name);
                            getLogger().info("Alterar absenceType: " + giafId + " - " + name);
                        }
                    } else {
                        new Absence(giafId, name);
                        getLogger().info("Nova absenceType: " + giafId + " - " + name);
                    }
                }
            }
            result.close();
            preparedStatement.close();

            oracleConnection.closeConnection();

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportTypesFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportTypesFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    @Override
    protected String getQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT tab_cod, tab_cod_dsc, tab_cod_alg, to_char(TAB_NUM) as TAB_NUM FROM SLTCODGER cod WHERE (cod.TAB_ID = 'FA' AND cod.TAB_NUM in (");
        query.append(SERVICE_EXEMPTION).append(",");
        query.append(GRANT_OWNER_EQUIVALENCE).append(",");
        query.append(FUNCTIONS_ACCUMULATION);
        query.append(")) or (cod.TAB_ID = 'AW' and cod.TAB_NUM=");
        query.append(CONTRACT_TYPE);
        query.append(") union all select flt_tip as tab_cod, flt_dsc as tab_cod_dsc, '' as tab_cod_alg, flt_just as TAB_NUM from SLTFALTAS");
        return query.toString();
    }

}

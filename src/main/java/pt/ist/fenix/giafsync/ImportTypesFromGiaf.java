package pt.ist.fenix.giafsync;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sourceforge.fenixedu.domain.personnelSection.contracts.Absence;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.FunctionsAccumulation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GrantOwnerEquivalent;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalContractType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ServiceExemption;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import pt.ist.fenix.giafsync.GiafSync.MetadataProcessor;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

class ImportTypesFromGiaf implements MetadataProcessor {
    private static final String SERVICE_EXEMPTION = "71";

    private static final String GRANT_OWNER_EQUIVALENCE = "84";

    private static final String FUNCTIONS_ACCUMULATION = "46.6";

    private static final String CONTRACT_TYPE = "2";

    private static final String ABSENCE_TYPE = "J";

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

    @Override
    public void processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        int updatedAccumulations = 0;
        int newAccumulations = 0;
        int updatedGrantOwnerEquivalences = 0;
        int newGrantOwnerEquivalences = 0;
        int updatedExemptions = 0;
        int newExemptions = 0;
        int updatedContractTypes = 0;
        int newContractTypes = 0;
        int updatedAbsences = 0;
        int newAbsences = 0;

        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(getQuery());
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            String type = result.getString("TAB_NUM");
            final String giafId = result.getString("tab_cod");
            String nameString = result.getString("tab_cod_alg");
            if (StringUtils.isEmpty(nameString)) {
                nameString = result.getString("tab_cod_dsc");
            }
            final MultiLanguageString name = new MultiLanguageString(Language.pt, nameString);
            if (type.equalsIgnoreCase(FUNCTIONS_ACCUMULATION)) {
                FunctionsAccumulation accumulation = metadata.accumulation(giafId);
                if (accumulation != null) {
                    if (!accumulation.getName().equalInAnyLanguage(name)) {
                        accumulation.edit(name);
                        updatedAccumulations++;
                    }
                } else {
                    metadata.registerAccumulation(giafId, name);
                    newAccumulations++;
                }
            } else if (type.equalsIgnoreCase(GRANT_OWNER_EQUIVALENCE)) {
                GrantOwnerEquivalent grantOwnerEquivalent = metadata.grantOwnerEquivalent(giafId);
                if (grantOwnerEquivalent != null) {
                    if (!grantOwnerEquivalent.getName().equalInAnyLanguage(name)) {
                        grantOwnerEquivalent.edit(name);
                        updatedGrantOwnerEquivalences++;
                    }
                } else {
                    metadata.registerGrantOwnerEquivalent(giafId, name);
                    newGrantOwnerEquivalences++;
                }
            } else if (type.equalsIgnoreCase(SERVICE_EXEMPTION)) {
                ServiceExemption exemption = metadata.exemption(giafId);
                if (exemption != null) {
                    if (!exemption.getName().equalInAnyLanguage(name)) {
                        exemption.edit(name);
                        updatedExemptions++;
                    }
                } else {
                    metadata.registerExemption(giafId, name);
                    newExemptions++;
                }
            } else if (type.equalsIgnoreCase(CONTRACT_TYPE)) {
                ProfessionalContractType contractType = metadata.contractType(giafId);
                if (contractType != null) {
                    if (!contractType.getName().equalInAnyLanguage(name)) {
                        contractType.edit(name);
                        updatedContractTypes++;
                    }
                } else {
                    metadata.registerContractType(giafId, name);
                    newContractTypes++;
                }
            } else if (type.equalsIgnoreCase(ABSENCE_TYPE)) {
                Absence absence = metadata.absence(giafId);
                if (absence != null) {
                    if (!absence.getName().equalInAnyLanguage(name)) {
                        absence.edit(name);
                        updatedAbsences++;
                    }
                } else {
                    metadata.registerAbsence(giafId, name);
                    newAbsences++;
                }
            }
        }
        result.close();
        preparedStatement.close();

        oracleConnection.closeConnection();

        log.printf("Accumulations: %d updated, %d new\n", updatedAccumulations, newAccumulations);
        log.printf("GrantOwnerEquivalences: %d updated, %d new\n", updatedGrantOwnerEquivalences, newGrantOwnerEquivalences);
        log.printf("Exemptions: %d updated, %d new\n", updatedExemptions, newExemptions);
        log.printf("ContractTypes: %d updated, %d new\n", updatedContractTypes, newContractTypes);
        log.printf("Absences: %d updated, %d new\n", updatedAbsences, newAbsences);
    }

}

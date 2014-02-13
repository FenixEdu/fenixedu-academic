package pt.ist.fenix.giafsync;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRelation;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import pt.ist.fenix.giafsync.GiafSync.MetadataProcessor;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

class ImportProfessionalRelationsFromGiaf implements MetadataProcessor {

    private static final String TRUE_STRING = "S";

    public ImportProfessionalRelationsFromGiaf() {

    }

    @Override
    public void processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        int updatedRelations = 0;
        int newRelations = 0;

        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(getQuery());
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            String giafId = result.getString("tab_cod");
            String nameString = result.getString("tab_cod_alg");
            if (StringUtils.isEmpty(nameString)) {
                nameString = result.getString("tab_cod_dsc");
            }
            Boolean fullTimeEquivalent = getBoolean(result.getString("eti"));

            ProfessionalRelation professionalRelation = metadata.relation(giafId);
            MultiLanguageString name = new MultiLanguageString(Language.pt, nameString);
            if (professionalRelation != null) {
                if (!professionalRelation.getName().equalInAnyLanguage(name)) {
                    professionalRelation.edit(name, fullTimeEquivalent);
                    updatedRelations++;
                }
            } else {
                metadata.registerRelation(giafId, fullTimeEquivalent, name);
                newRelations++;
            }
        }
        result.close();
        preparedStatement.close();
        oracleConnection.closeConnection();
        log.printf("Relations: %d updated, %d new\n", updatedRelations, newRelations);
    }

    private Boolean getBoolean(String booleanString) {
        return ((!StringUtils.isEmpty(booleanString)) && booleanString.equalsIgnoreCase(TRUE_STRING));
    }

    protected String getQuery() {
        return "SELECT tab_cod, tab_cod_dsc, tab_cod_alg, eti FROM SLTCODGER cod WHERE  ( cod.TAB_ID = 'FA' AND cod.TAB_NUM = 46.5 )";
    }
}

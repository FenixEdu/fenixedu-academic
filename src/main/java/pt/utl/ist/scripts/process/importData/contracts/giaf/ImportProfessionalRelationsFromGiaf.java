package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRelation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import org.apache.commons.lang.StringUtils;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Task(englishTitle = "ImportProfessionalRelationsFromGiaf")
public class ImportProfessionalRelationsFromGiaf extends CronTask {

    private static final String TRUE_STRING = "S";

    public ImportProfessionalRelationsFromGiaf() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportProfessionalRelationsFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, ProfessionalRelation> professionalRelationsMap = getProfessionalRelationsMap();
            String query = getProfessionalRelationsQuery();
            getLogger().debug(query);
            PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                String giafId = result.getString("tab_cod");
                String nameString = result.getString("tab_cod_alg");
                if (StringUtils.isEmpty(nameString)) {
                    nameString = result.getString("tab_cod_dsc");
                }
                MultiLanguageString name = new MultiLanguageString(pt.utl.ist.fenix.tools.util.i18n.Language.pt, nameString);
                Boolean fullTimeEquivalent = getBoolean(result.getString("eti"));

                ProfessionalRelation professionalRelation = professionalRelationsMap.get(giafId);
                if (professionalRelation != null) {
                    if (!professionalRelation.getName().equalInAnyLanguage(name)) {
                        professionalRelation.edit(name, fullTimeEquivalent);
                        getLogger().info("Alterar tipo vinculo: " + giafId + " - " + name + ". ETI: " + fullTimeEquivalent);
                    }
                } else {
                    new ProfessionalRelation(giafId, name, fullTimeEquivalent);
                    getLogger().info("Novo tipo vinculo: " + giafId + " - " + name + ". ETI: " + fullTimeEquivalent);
                }
            }
            result.close();
            preparedStatement.close();
            oracleConnection.closeConnection();

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportProfessionalRelationsFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportProfessionalRelationsFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private Boolean getBoolean(String booleanString) {
        return ((!StringUtils.isEmpty(booleanString)) && booleanString.equalsIgnoreCase(TRUE_STRING));
    }

    private String getProfessionalRelationsQuery() {
        return "SELECT tab_cod, tab_cod_dsc, tab_cod_alg, eti FROM SLTCODGER cod WHERE  ( cod.TAB_ID = 'FA' AND cod.TAB_NUM = 46.5 )";
    }

    private Map<String, ProfessionalRelation> getProfessionalRelationsMap() {
        Map<String, ProfessionalRelation> professionalRelations = new HashMap<String, ProfessionalRelation>();
        for (ProfessionalRelation professionalRelation : Bennu.getInstance().getProfessionalRelationsSet()) {
            professionalRelations.put(professionalRelation.getGiafId(), professionalRelation);
        }
        return professionalRelations;
    }

}

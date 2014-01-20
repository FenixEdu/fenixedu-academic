package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Task(englishTitle = "ImportProfessionalRegimesFromGiaf")
public class ImportProfessionalRegimesFromGiaf extends ImportFromGiaf {

    public ImportProfessionalRegimesFromGiaf() {

    }

    @Override
    public void process() {
        getLogger().debug("Start ImportProfessionalRegimesFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, ProfessionalRegime> professionalRegimesMap = getProfessionalRegimesMap();
            String query = getQuery();
            getLogger().debug(query);
            PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                String giafId = result.getString("emp_regime");
                String regimeName = result.getString("regime_dsc");
                MultiLanguageString name = new MultiLanguageString(pt.utl.ist.fenix.tools.util.i18n.Language.pt, regimeName);
                Integer weighting = result.getInt("regime_pond");
                BigDecimal fullTimeEquivalent = result.getBigDecimal("valor_eti");

                CategoryType categoryType = null;
                if (!StringUtils.isBlank(regimeName)) {
                    if (regimeName.contains("Bolseiro")) {
                        categoryType = CategoryType.GRANT_OWNER;
                    } else if (regimeName.contains("Investigador")) {
                        categoryType = CategoryType.RESEARCHER;
                    } else if (regimeName.contains("Pessoal não Docente") || regimeName.contains("Pess. não Doc.")
                            || regimeName.contains("Pessoal Não Docente")) {
                        categoryType = CategoryType.EMPLOYEE;
                    } else if (regimeName.contains("(Docentes)") || regimeName.contains("(Doc)")) {
                        categoryType = CategoryType.TEACHER;
                    }
                }

                ProfessionalRegime professionalRegime = professionalRegimesMap.get(giafId);
                if (professionalRegime != null) {
                    if (!isEqual(professionalRegime, name, weighting, fullTimeEquivalent, categoryType)) {
                        professionalRegime.edit(name, weighting, fullTimeEquivalent, categoryType);
                        getLogger().info(
                                "Alterar regime: " + giafId + " - " + name + ". Peso: " + weighting + " ETI: "
                                        + fullTimeEquivalent + " categoryType: " + categoryType);
                    }
                } else {
                    new ProfessionalRegime(giafId, name, weighting, fullTimeEquivalent, categoryType);
                    getLogger().info(
                            "Novo regime: " + giafId + " - " + name + ". Peso: " + weighting + " ETI: " + fullTimeEquivalent
                                    + " categoryType: " + categoryType);
                }
            }
            result.close();
            preparedStatement.close();
            oracleConnection.closeConnection();

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportProfessionalRegimesFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportProfessionalRegimesFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private boolean isEqual(ProfessionalRegime professionalRegime, MultiLanguageString name, Integer weighting,
            BigDecimal fullTimeEquivalent, CategoryType categoryType) {
        return professionalRegime.getName().getContent().equalsIgnoreCase(name.getContent())
                && Objects.equals(professionalRegime.getFullTimeEquivalent(), fullTimeEquivalent)
                && Objects.equals(professionalRegime.getWeighting(), weighting)
                && Objects.equals(professionalRegime.getCategoryType(), categoryType);
    }

    @Override
    protected String getQuery() {
        return "SELECT a.emp_regime, a.regime_dsc, a.regime_pond, a.valor_eti FROM sltregimes a";
    }
}

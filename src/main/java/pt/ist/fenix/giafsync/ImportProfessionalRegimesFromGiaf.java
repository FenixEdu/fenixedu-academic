package pt.ist.fenix.giafsync;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import pt.ist.fenix.giafsync.GiafSync.MetadataProcessor;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

class ImportProfessionalRegimesFromGiaf implements MetadataProcessor {
    public ImportProfessionalRegimesFromGiaf() {

    }

    @Override
    public void processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        int updatedRegimes = 0;
        int newRegimes = 0;

        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        String query = getQuery();
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            String giafId = result.getString("emp_regime");
            String regimeName = result.getString("regime_dsc");
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

            ProfessionalRegime professionalRegime = metadata.regime(giafId);
            MultiLanguageString name = new MultiLanguageString(Language.pt, regimeName);
            if (professionalRegime != null) {
                if (!isEqual(professionalRegime, name, weighting, fullTimeEquivalent, categoryType)) {
                    professionalRegime.edit(name, weighting, fullTimeEquivalent, categoryType);
                    updatedRegimes++;
                }
            } else {
                metadata.registerRegime(giafId, weighting, fullTimeEquivalent, categoryType, name);
                newRegimes++;
            }
        }
        result.close();
        preparedStatement.close();
        oracleConnection.closeConnection();
        log.printf("Regimes: %d updated, %d new\n", updatedRegimes, newRegimes);
    }

    private boolean isEqual(ProfessionalRegime professionalRegime, MultiLanguageString name, Integer weighting,
            BigDecimal fullTimeEquivalent, CategoryType categoryType) {
        return professionalRegime.getName().getContent().equalsIgnoreCase(name.getContent())
                && Objects.equals(professionalRegime.getFullTimeEquivalent(), fullTimeEquivalent)
                && Objects.equals(professionalRegime.getWeighting(), weighting)
                && Objects.equals(professionalRegime.getCategoryType(), categoryType);
    }

    protected String getQuery() {
        return "SELECT a.emp_regime, a.regime_dsc, a.regime_pond, a.valor_eti FROM sltregimes a";
    }
}

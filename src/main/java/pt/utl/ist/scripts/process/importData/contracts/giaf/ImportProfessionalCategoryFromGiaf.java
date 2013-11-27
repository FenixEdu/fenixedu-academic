package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Task(englishTitle = "ImportProfessionalCategoryFromGiaf")
public class ImportProfessionalCategoryFromGiaf extends CronTask {

    private static final String NDOCENTE = "NDOCENTE";

    private static final String INVEST = "INVEST";

    private static final String DOCENTE = "DOCENTE";

    private static final String BOLSEIRO = "BOLSEIRO";

    private static final String PSERV = "PSERV";

    public ImportProfessionalCategoryFromGiaf() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportProfessionalCategoryFromGiaf");
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, ProfessionalCategory> professionalCategoriesMap = getProfessionalCategoriesMap();
            String query = getProfessionalCategoriesQuery();
            getLogger().debug(query);
            PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                String giafId = result.getString("emp_cat_func");
                MultiLanguageString description =
                        new MultiLanguageString(pt.utl.ist.fenix.tools.util.i18n.Language.pt, result.getString("cat_func_dsc"));
                CategoryType categoryType = null;
                String agrupamento = (result.getString("agrupamento"));
                if (agrupamento != null) {
                    if (agrupamento.equals(BOLSEIRO)) {
                        categoryType = CategoryType.GRANT_OWNER;
                    } else if (agrupamento.equals(DOCENTE)) {
                        categoryType = CategoryType.TEACHER;
                    } else if (agrupamento.equals(INVEST)) {
                        categoryType = CategoryType.RESEARCHER;
                    } else if (agrupamento.equals(NDOCENTE)) {
                        categoryType = CategoryType.EMPLOYEE;
                    } else if (agrupamento.equals(PSERV)) {
                    }
                }
                if (categoryType == null) {
                    getLogger().info(
                            "Erro no agrupamento da categoria. Categoria: " + giafId + " - " + description + ". Agrupamento: "
                                    + agrupamento);
                } else {
                    ProfessionalCategory professionalCategory = professionalCategoriesMap.get(giafId);
                    if (professionalCategory != null) {
                        if ((!professionalCategory.getCategoryType().equals(categoryType))
                                || (!professionalCategory.getName().getContent().equalsIgnoreCase(description.getContent()))) {
                            professionalCategory.edit(description, categoryType);
                            getLogger().info(
                                    "Alterar categoria: " + giafId + " - " + description + ". Tipo: " + categoryType.getName());
                        }
                    } else {
                        professionalCategory = new ProfessionalCategory(giafId, description, categoryType);
                        getLogger().info("Nova categoria: " + giafId + " - " + description + ". Tipo: " + categoryType.getName());
                    }
                }

            }
            result.close();
            preparedStatement.close();
            oracleConnection.closeConnection();

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportProfessionalCategoryFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportProfessionalCategoryFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

    private String getProfessionalCategoriesQuery() {
        return "SELECT a.emp_cat_func, a.cat_func_dsc, a.agrupamento FROM sltcatfunc a where emp_cat_func in (select distinct emp_cat_func from sldemp24 where emp_sit is not null) or emp_cat_func in (select distinct emp_cat_func from sldemp25) or emp_cat_func in (select distinct emp_cat_func from sltprog_vinc) or emp_cat_func in (select distinct emp_cat_func from sldemp01)";
    }

    private Map<String, ProfessionalCategory> getProfessionalCategoriesMap() {
        Map<String, ProfessionalCategory> professionalCategories = new HashMap<String, ProfessionalCategory>();
        for (ProfessionalCategory professionalCategory : Bennu.getInstance().getProfessionalCategoriesSet()) {
            professionalCategories.put(professionalCategory.getGiafId(), professionalCategory);
        }
        return professionalCategories;
    }

}

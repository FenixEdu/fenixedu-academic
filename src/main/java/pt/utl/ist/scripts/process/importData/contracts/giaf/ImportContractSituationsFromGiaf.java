package pt.utl.ist.scripts.process.importData.contracts.giaf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import pt.ist.bennu.scheduler.annotation.Task;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Task(englishTitle = "ImportContractSituationsFromGiaf")
public class ImportContractSituationsFromGiaf extends ImportFromGiaf {

    private List<String> getServiceExemptionList() {
        List<String> result = new ArrayList<String>();
        result.add("RQEX");
        result.add("LSVC");
        return result;
    }

    private List<String> getEndSituationList() {
        List<String> result = new ArrayList<String>();
        result.add("DMTD");
        result.add("FLCD");
        return result;
    }

    @Override
    protected String getQuery() {
        return "select emp_sit, sit_dsc, UPPER(sit_prc) as sit_prc from sltsit";
    }

    @Override
    public void runTask() {
        getLogger().debug("Start ImportContractSituationsFromGiaf");
        List<String> endSituationList = getEndSituationList();
        List<String> serviceExemptionList = getServiceExemptionList();
        try {
            PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
            Map<String, ContractSituation> contractSituationsMap = getContractSituationsMap();
            String query = getQuery();
            getLogger().debug(query);
            PreparedStatement preparedStatement = oracleConnection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                String giafId = result.getString("emp_sit");
                MultiLanguageString description =
                        new MultiLanguageString(pt.utl.ist.fenix.tools.util.i18n.Language.pt, result.getString("sit_dsc"));
                String type = result.getString("sit_prc");
                Boolean endSituation = giafId.equals("290") || giafId.equals("177") ? false : endSituationList.contains(type);
                Boolean serviceExemption = serviceExemptionList.contains(type);
                ContractSituation situation = contractSituationsMap.get(giafId);
                if (situation != null) {
                    if ((!situation.getEndSituation().equals(endSituation))
                            || (!situation.getName().getContent().equalsIgnoreCase(description.getContent()))) {
                        situation.edit(description, endSituation);
                        getLogger().info(
                                "Alterar situação: " + giafId + " - " + description + ". Situação de fim?: " + endSituation);
                    }
                    if (!situation.getServiceExemption().equals(serviceExemption)) {
                        getLogger().info("Alterou no GIAF: Situação: " + giafId + " - Isenção?: " + serviceExemption);
                    }
                } else {
                    situation = new ContractSituation(giafId, description, endSituation, serviceExemption);
                    getLogger().info(
                            "Nova situação: " + giafId + " - " + description + ". Situação de fim?: " + endSituation
                                    + ". Isenção?: " + serviceExemption);
                }
            }
            result.close();
            preparedStatement.close();
            oracleConnection.closeConnection();

        } catch (ExcepcaoPersistencia e) {
            getLogger().info("ImportContractSituationsFromGiaf -  ERRO ExcepcaoPersistencia");
            throw new Error(e);
        } catch (SQLException e) {
            getLogger().info("ImportContractSituationsFromGiaf -  ERRO SQLException");
            throw new Error(e);
        }
        getLogger().debug("The end");
    }

}

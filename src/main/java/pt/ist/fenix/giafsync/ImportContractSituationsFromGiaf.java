package pt.ist.fenix.giafsync;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportGiaf;

import org.slf4j.Logger;

import pt.ist.fenix.giafsync.GiafSync.MetadataProcessor;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

class ImportContractSituationsFromGiaf implements MetadataProcessor {
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

    protected String getQuery() {
        return "select emp_sit, sit_dsc, UPPER(sit_prc) as sit_prc from sltsit";
    }

    @Override
    public void processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        List<String> endSituationList = getEndSituationList();
        List<String> serviceExemptionList = getServiceExemptionList();
        int updatedSituations = 0;
        int changedExemptions = 0;
        int newSituations = 0;

        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(getQuery());
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            String giafId = result.getString("emp_sit");
            MultiLanguageString description = new MultiLanguageString(MultiLanguageString.pt, result.getString("sit_dsc"));
            String type = result.getString("sit_prc");
            Boolean endSituation = giafId.equals("290") || giafId.equals("177") ? false : endSituationList.contains(type);
            Boolean serviceExemption = serviceExemptionList.contains(type);
            ContractSituation situation = metadata.situation(giafId);
            if (situation != null) {
                if ((!situation.getEndSituation().equals(endSituation))
                        || (!situation.getName().getContent().equalsIgnoreCase(description.getContent()))) {
                    situation.edit(description, endSituation);
                    updatedSituations++;
                }
                if (!situation.getServiceExemption().equals(serviceExemption)) {
                    changedExemptions++;
                    //Shouldn't we update on this side?
                    logger.info("Alterou no GIAF: Situação: " + giafId + " - Isenção?: " + serviceExemption);
                }
            } else {
                metadata.registerSituation(giafId, endSituation, serviceExemption, description);
                newSituations++;
            }
        }
        result.close();
        preparedStatement.close();
        oracleConnection.closeConnection();

        log.printf("Situations: %d updated, %d new, %d changed exemptions\n", updatedSituations, newSituations, changedExemptions);
    }
}

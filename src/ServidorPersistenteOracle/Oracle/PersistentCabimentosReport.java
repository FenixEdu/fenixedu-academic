/*
 * Created on Jan 12, 2005
 *
 */
package ServidorPersistenteOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dominio.projectsManagement.CabimentosReportLine;
import Dominio.projectsManagement.ICabimentosReportLine;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistenteOracle.IPersistentReport;
import Util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentCabimentosReport extends PersistentReport implements IPersistentReport {

    public List getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        List result = new ArrayList();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);

            String query = new String("select \"CABIMENTOS\", \"JUST_CABIMENTOS\", \"TOTAL\" from " + tableOrView + " where PROJECTCODE='"
                    + projectCode + "'");
            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                ICabimentosReportLine report = new CabimentosReportLine();
                report.setProjectCode(projectCode);
                report.setCabimentos(new Double(rs.getDouble("CABIMENTOS")));
                report.setJustifications(new Double(rs.getDouble("JUST_CABIMENTOS")));
                report.setTotal(new Double(rs.getDouble("TOTAL")));
                result.add(report);
            }

            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }

        return result;
    }

}

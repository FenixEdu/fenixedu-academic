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

import Dominio.projectsManagement.AdiantamentosReportLine;
import Dominio.projectsManagement.IAdiantamentosReportLine;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistenteOracle.IPersistentReport;
import Util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentAdiantamentosReport extends PersistentReport implements IPersistentReport {

    public List getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        List result = new ArrayList();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);

            String query = new String("select \"ADIANTAMENTOS\", \"JUST_ADIANTAMENTOS\", \"TOTAL\" from " + tableOrView + " where PROJECTCODE='"
                    + projectCode + "'");
            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                IAdiantamentosReportLine report = new AdiantamentosReportLine();
                report.setProjectCode(projectCode);
                report.setAdiantamentos(new Double(rs.getDouble("ADIANTAMENTOS")));
                report.setJustifications(new Double(rs.getDouble("JUST_ADIANTAMENTOS")));
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

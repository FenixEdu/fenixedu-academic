/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentReport {

    protected String getTableOrViewName(PersistentSuportOracle p, ReportType reportType) throws ExcepcaoPersistencia, SQLException {
        String query = "select TABLE_OR_VIEW from web_report where REPORT_TYPE = " + reportType.getReportType();
        PreparedStatement stmt = p.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        String tableOrView = null;
        if (rs.next()) {
            tableOrView = rs.getString(1);
        }
        rs.close();
        return tableOrView;
    }
}

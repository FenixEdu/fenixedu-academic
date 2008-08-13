/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.IOverheadsSummaryReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.OverheadsSummaryReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentOverheadsSummaryReport extends PersistentReport implements IPersistentReport {

    public List<IOverheadsSummaryReportLine> getCompleteReport(ReportType reportType, Integer costCenterCoordinatorId)
	    throws ExcepcaoPersistencia {
	List<IOverheadsSummaryReportLine> result = new ArrayList<IOverheadsSummaryReportLine>();
	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	    p.startTransaction();
	    String tableOrView = getTableOrViewName(p, reportType);
	    StringBuilder stringBuffer = new StringBuilder();
	    stringBuffer
		    .append("select \"ANO\", \"UE\", \"COST_CENTER\", \"REC_OG\", \"OVH_OG\", \"REC_OA\", \"OVH_OA\",\"REC_OO\", \"OVH_OO\", \"REC_OE\", \"OVH_OE\", \"TOTAL_OVH\", \"OVH_TRANSF\", \"SALDO\" from ");
	    stringBuffer.append(tableOrView);
	    stringBuffer.append(" where CC_COORD='");
	    stringBuffer.append(costCenterCoordinatorId);
	    stringBuffer.append("' order by \"ANO\", \"UE\"");
	    PreparedStatement stmt = p.prepareStatement(stringBuffer.toString());
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
		IOverheadsSummaryReportLine report = new OverheadsSummaryReportLine();
		report.setYear(new Integer(rs.getInt("ANO")));
		report.setExplorationUnit(new Integer(rs.getInt("UE")));
		report.setCostCenter(rs.getString("COST_CENTER"));
		report.setOGRevenue(new Double(rs.getDouble("REC_OG")));
		report.setOGOverhead(new Double(rs.getDouble("OVH_OG")));
		report.setOARevenue(new Double(rs.getDouble("REC_OA")));
		report.setOAOverhead(new Double(rs.getDouble("OVH_OA")));
		report.setOORevenue(new Double(rs.getDouble("REC_OO")));
		report.setOOOverhead(new Double(rs.getDouble("OVH_OO")));
		report.setOERevenue(new Double(rs.getDouble("REC_OE")));
		report.setOEOverhead(new Double(rs.getDouble("OVH_OE")));
		report.setTotalOverheads(new Double(rs.getDouble("TOTAL_OVH")));
		report.setTransferedOverheads(new Double(rs.getDouble("OVH_TRANSF")));
		report.setBalance(new Double(rs.getDouble("SALDO")));
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

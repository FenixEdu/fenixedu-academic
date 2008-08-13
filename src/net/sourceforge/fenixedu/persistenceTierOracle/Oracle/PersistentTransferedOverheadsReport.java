/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.ITransferedOverheadsReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.TransferedOverheadsReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentTransferedOverheadsReport extends PersistentReport implements IPersistentReport {

    public List<ITransferedOverheadsReportLine> getCompleteReport(ReportType reportType, Integer costCenterCoordinatorId)
	    throws ExcepcaoPersistencia {
	List<ITransferedOverheadsReportLine> result = new ArrayList<ITransferedOverheadsReportLine>();
	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	    p.startTransaction();
	    String tableOrView = getTableOrViewName(p, reportType);
	    StringBuilder stringBuffer = new StringBuilder();
	    stringBuffer.append("select \"UE\", \"IDMOV\", \"DATE_AUTOR\", \"TIPO\", \"DESCRICAO\", \"VALOR\" from ");
	    stringBuffer.append(tableOrView);
	    stringBuffer.append(" where CC_COORD='");
	    stringBuffer.append(costCenterCoordinatorId);
	    stringBuffer.append("' order by \"DATE_AUTOR\", \"UE\", \"IDMOV\"");
	    PreparedStatement stmt = p.prepareStatement(stringBuffer.toString());
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
		ITransferedOverheadsReportLine report = new TransferedOverheadsReportLine();
		report.setExplorationUnit(new Integer(rs.getInt("UE")));
		report.setMovementId(rs.getString("IDMOV"));
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = rs.getDate("DATE_AUTOR");
		report.setDate(formatter.format(date));
		report.setType(rs.getString("TIPO"));
		report.setDescription(rs.getString("DESCRICAO"));
		report.setOverheadValue(new Double(rs.getDouble("VALOR")));
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

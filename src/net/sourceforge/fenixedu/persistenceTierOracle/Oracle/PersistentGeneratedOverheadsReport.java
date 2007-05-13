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

import net.sourceforge.fenixedu.domain.projectsManagement.GeneratedOverheadsReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IGeneratedOverheadsReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentGeneratedOverheadsReport extends PersistentReport implements IPersistentReport {

    public List<IGeneratedOverheadsReportLine> getCompleteReport(ReportType reportType, Integer costCenterCoordinatorId) throws ExcepcaoPersistencia {
        List<IGeneratedOverheadsReportLine> result = new ArrayList<IGeneratedOverheadsReportLine>();
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer
                    .append("select \"UNID_EXPL\", \"ID_PROJ\", \"ACRONIMO\", \"ID_COORD\", \"NOME\", \"TIPO_OVH\", \"DATE_AUTOR\", \"DESCRICAO\", \"VALOR_RECEITA\", \"PCT_OVH\", \"VALOR_OVH\" from ");
            stringBuffer.append(tableOrView);
            stringBuffer.append(" where cc_coord='");
            stringBuffer.append(costCenterCoordinatorId);
            stringBuffer.append("' order by \"DATE_AUTOR\", \"UNID_EXPL\", \"ID_PROJ\"");
            PreparedStatement stmt = p.prepareStatement(stringBuffer.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                IGeneratedOverheadsReportLine report = new GeneratedOverheadsReportLine();
                report.setExplorationUnit(new Integer(rs.getInt("UNID_EXPL")));
                report.setProjectNumber(new Integer(rs.getInt("ID_PROJ")));
                report.setAcronim(rs.getString("ACRONIMO"));
                report.setCoordinatorName(rs.getString("NOME"));
                report.setCoordinatorNumber(new Integer(rs.getInt("ID_COORD")));
                report.setType(rs.getString("TIPO_OVH"));
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = rs.getDate("DATE_AUTOR");
                report.setDate(formatter.format(date));
                report.setDescription(rs.getString("DESCRICAO"));
                report.setRevenue(new Double(rs.getDouble("VALOR_RECEITA")));
                report.setOverheadPerscentage(new Double(rs.getDouble("PCT_OVH")));
                report.setOverheadValue(new Double(rs.getDouble("VALOR_OVH")));
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

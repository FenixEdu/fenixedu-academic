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

import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReport;
import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.MovementReport;
import net.sourceforge.fenixedu.domain.projectsManagement.MovementReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentMovementReport extends PersistentReport implements IPersistentReport {

    public List getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        List result = new ArrayList();
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer
                    .append("select \"PAI_IDMOV\", \"PAI_IDPROJ\", \"PAI_IDRUB\", \"PAI_TIPO\", \"PAI_DATA\", \"PAI_DESCRICAO\", \"PAI_VALOR_TOTAL\",\"FILHO_IDMOV\", \"FILHO_IDRUB\", \"FILHO_TIPO\", \"FILHO_DATA\", \"FILHO_DESCRICAO\", \"FILHO_VALOR\", \"FILHO_IVA\" from ");
            stringBuffer.append(tableOrView);
            stringBuffer.append(" where PAI_IDPROJ='");
            stringBuffer.append(projectCode);
            stringBuffer.append("' order by \"PAI_IDMOV\", \"FILHO_IDMOV\"");
            String query = stringBuffer.toString();
            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            String lastParentMovementId = "";
            IMovementReport reportParent = null;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                IMovementReportLine report = new MovementReportLine();
                if (!rs.getString("PAI_IDMOV").equals(lastParentMovementId)) {
                    if (reportParent != null)
                        result.add(reportParent);
                    reportParent = new MovementReport();
                    reportParent.setParentMovementId(rs.getString("PAI_IDMOV"));
                    reportParent.setParentProjectCode(new Integer(rs.getInt("PAI_IDPROJ")));
                    reportParent.setParentRubricId(new Integer(rs.getInt("PAI_IDRUB")));
                    reportParent.setParentType(rs.getString("PAI_TIPO"));
                    Date date = rs.getDate("PAI_DATA");
                    if (date != null)
                        reportParent.setParentDate(formatter.format(date));
                    else
                        reportParent.setParentDate("");
                    reportParent.setParentDescription(rs.getString("PAI_DESCRICAO"));
                    reportParent.setParentValue(new Double(rs.getDouble("PAI_VALOR_TOTAL")));
                    reportParent.setMovements(new ArrayList());
                }
                lastParentMovementId = rs.getString("PAI_IDMOV");
                report.setMovementId(rs.getString("FILHO_IDMOV"));
                report.setRubricId(new Integer(rs.getInt("FILHO_IDRUB")));
                report.setType(rs.getString("FILHO_TIPO"));
                Date date = rs.getDate("FILHO_DATA");
                if (date != null)
                    report.setDate(formatter.format(date));
                else
                    report.setDate("");
                report.setDescription(rs.getString("FILHO_DESCRICAO"));
                report.setValue(new Double(rs.getDouble("FILHO_VALOR")));
                report.setTax(new Double(rs.getDouble("FILHO_IVA")));
                reportParent.getMovements().add(report);
            }
            if (reportParent != null)
                result.add(reportParent);
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return result;
    }

}

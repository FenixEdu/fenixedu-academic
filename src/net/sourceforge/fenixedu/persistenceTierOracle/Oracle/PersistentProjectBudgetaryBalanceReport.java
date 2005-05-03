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

import net.sourceforge.fenixedu.domain.projectsManagement.IProjectBudgetaryBalanceReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectBudgetaryBalanceReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentProjectBudgetaryBalanceReport extends PersistentReport implements IPersistentReport {

    public List getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        List result = new ArrayList();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("select \"RUBRICA\", \"DESCRICAORUBRICA\", \"ORÇAMENTADO\", \"EXECUTADO\", \"SALDO\"from ");
            stringBuffer.append(tableOrView);
            stringBuffer.append(" where PROJECTO='");
            stringBuffer.append(projectCode);
            stringBuffer.append("'");
            String query = stringBuffer.toString();

            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                IProjectBudgetaryBalanceReportLine report = new ProjectBudgetaryBalanceReportLine();
                report.setRubric(new Integer(rs.getInt("RUBRICA")));
                report.setRubricDescription(rs.getString("DESCRICAORUBRICA"));
                report.setBudget(new Double(rs.getDouble("ORÇAMENTADO")));
                report.setExecuted(new Double(rs.getDouble("EXECUTADO")));
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

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

import net.sourceforge.fenixedu.domain.projectsManagement.ExpensesReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IExpensesReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentExpensesReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;
import net.sourceforge.fenixedu.util.projectsManagement.RubricType;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentExpensesReport extends PersistentReport implements IPersistentExpensesReport {

    public List getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        List result = new ArrayList();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);

            String query = new String(
                    "select \"idMov\", \"Membro\", \"Rubrica\", \"Tipo\", \"data\", \"Descrição\", \"Valor\", \"Iva\", \"Total\" from " + tableOrView
                            + " where PROJECTCODE='" + projectCode + "' order by \"data\"");
            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                IExpensesReportLine report = new ExpensesReportLine();
                report.setProjectCode(projectCode);
                report.setMovementId(rs.getString("idMov"));
                report.setMember(rs.getString("Membro"));
                report.setRubric(new Integer(rs.getInt("Rubrica")));
                report.setType(rs.getString("Tipo"));
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = rs.getDate("data");
                report.setDate(formatter.format(date));
                report.setDescription(rs.getString("Descrição"));
                report.setValue(new Double(rs.getDouble("Valor")));
                report.setTax(new Double(rs.getDouble("Iva")));
                report.setTotal(new Double(rs.getDouble("Total")));
                result.add(report);
            }

            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }

        return result;
    }

    public List getReportByRubric(ReportType reportType, Integer projectCode, String rubric) throws ExcepcaoPersistencia {
        List result = new ArrayList();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);

            String query = new String(
                    "select \"idMov\", \"Membro\", \"Rubrica\", \"Tipo\", \"data\", \"Descrição\", \"Valor\", \"Iva\", \"Total\" from " + tableOrView
                            + " where PROJECTCODE='" + projectCode + "' and \"Rubrica\"='" + rubric + "'");
            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                IExpensesReportLine report = new ExpensesReportLine();
                report.setProjectCode(projectCode);
                report.setMovementId(rs.getString("idMov"));
                report.setMember(rs.getString("Membro"));
                report.setRubric(new Integer(rs.getInt("Rubrica")));
                report.setType(rs.getString("Tipo"));
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = rs.getDate("data");
                report.setDate(formatter.format(date));
                report.setDescription(rs.getString("Descrição"));
                report.setValue(new Double(rs.getDouble("Valor")));
                report.setTax(new Double(rs.getDouble("Iva")));
                report.setTotal(new Double(rs.getDouble("Total")));
                result.add(report);
            }

            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }

        return result;
    }

    public List getRubricList(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        List rubricList = new ArrayList();
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String query = "select distinct r.COD, r.DESCRICAO from " + RubricType.EXPENSES_RUBRIC_TABLE_NAME + " r, " + getTableOrViewName(p, reportType)
                    + " p where p.PROJECTCODE='" + projectCode + "' and p.\"Rubrica\"=r.COD";
            // String query = "select distinct \"Rubrica\" from " +
            // getTableOrViewName(p, reportType) + " where PROJECTCODE='" +
            // projectCode + "'";
            PreparedStatement stmt = p.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rubricList.add(new LabelValueBean(rs.getString("COD"), rs.getString("DESCRICAO")));
            }

            rs.close();
            p.commitTransaction();

        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return rubricList;
    }
}

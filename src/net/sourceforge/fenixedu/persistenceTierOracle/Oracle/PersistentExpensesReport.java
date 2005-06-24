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

    public List<IExpensesReportLine> getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        List<IExpensesReportLine> result = new ArrayList<IExpensesReportLine>();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);

            StringBuffer query = new StringBuffer();
            query.append("select \"idMov\", \"Membro\", \"Rubrica\", \"Tipo\", \"data\", \"Descrição\", \"Valor\", \"Iva\", \"Total\" from ");
            query.append(tableOrView);
            query.append(" where PROJECTCODE='");
            query.append(projectCode);
            query.append("' order by \"data\", \"idMov\"");
            PreparedStatement stmt = p.prepareStatement(query.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                IExpensesReportLine report = new ExpensesReportLine();
                report.setProjectCode(projectCode);
                report.setMovementId(rs.getString("idMov"));
                report.setMember(rs.getString("Membro"));
                report.setRubric(new Integer(rs.getInt("Rubrica")));
                report.setMovementType(rs.getString("Tipo"));
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

    public List<IExpensesReportLine> getReportByRubric(ReportType reportType, Integer projectCode, String rubric) throws ExcepcaoPersistencia {
        List<IExpensesReportLine> result = new ArrayList<IExpensesReportLine>();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);

            StringBuffer query = new StringBuffer();
            query.append("select \"idMov\", \"Membro\", \"Rubrica\", \"Tipo\", \"data\", \"Descrição\", \"Valor\", \"Iva\", \"Total\" from ");
            query.append(tableOrView);
            query.append(" where PROJECTCODE='");
            query.append(projectCode);
            query.append("' and \"Rubrica\"='");
            query.append(rubric);
            query.append("' order by \"data\", \"idMov\"");
            PreparedStatement stmt = p.prepareStatement(query.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                IExpensesReportLine report = new ExpensesReportLine();
                report.setProjectCode(projectCode);
                report.setMovementId(rs.getString("idMov"));
                report.setMember(rs.getString("Membro"));
                report.setRubric(new Integer(rs.getInt("Rubrica")));
                report.setMovementType(rs.getString("Tipo"));
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

    public List<LabelValueBean> getRubricList(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        List<LabelValueBean> rubricList = new ArrayList<LabelValueBean>();
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            StringBuffer query = new StringBuffer();
            query.append("select distinct r.COD, r.DESCRICAO from ");
            query.append(RubricType.EXPENSES_RUBRIC_TABLE_NAME);
            query.append(" r, ");
            query.append(getTableOrViewName(p, reportType));
            query.append(" p where p.PROJECTCODE='");
            query.append(projectCode);
            query.append("' and p.\"Rubrica\"=r.COD");
            PreparedStatement stmt = p.prepareStatement(query.toString());

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

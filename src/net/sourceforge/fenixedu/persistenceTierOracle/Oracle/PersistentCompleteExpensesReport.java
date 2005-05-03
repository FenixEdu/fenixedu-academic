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
public class PersistentCompleteExpensesReport extends PersistentReport implements IPersistentExpensesReport {

    public List getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        List result = new ArrayList();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer
                    .append("select \"id Mov.\", \"Membro Cons.\", \"Fornecedor\", \"desc Fornecedor\", \"Tipo Doc.\", \"Nº Doc.\",\"Fonte Financ.\", \"Rubrica\", \"Tipo Mov.\", \"Data Doc\", \"Descrição\", \"pct Iva\",\"Valor\", \"IVA\", \"Total\", \"pct imput.\" from ");
            stringBuffer.append(tableOrView);
            stringBuffer.append(" where PROJECTCODE='");
            stringBuffer.append(projectCode);
            stringBuffer.append("' order by \"Data Doc\", \"id Mov.\"");
            String query = stringBuffer.toString();

            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                IExpensesReportLine report = new ExpensesReportLine();
                report.setProjectCode(projectCode);
                report.setMovementId(rs.getString("id Mov."));
                report.setMember(rs.getString("Membro Cons."));
                report.setSupplier(rs.getString("Fornecedor"));
                report.setSupplierDescription(rs.getString("desc Fornecedor"));
                report.setDocumentType(rs.getString("Tipo Doc."));
                report.setDocumentNumber(rs.getString("Nº Doc."));
                report.setFinancingSource(rs.getString("Fonte Financ."));
                report.setRubric(new Integer(rs.getInt("Rubrica")));
                report.setMovementType(rs.getString("Tipo Mov."));
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = rs.getDate("Data Doc");
                report.setDate(formatter.format(date));
                report.setDescription(rs.getString("Descrição"));
                report.setIvaPercentage(new Double(rs.getDouble("pct Iva")));
                report.setValue(new Double(rs.getDouble("Valor")));
                report.setTax(new Double(rs.getDouble("IVA")));
                report.setTotal(new Double(rs.getDouble("Total")));
                report.setImputedPercentage(new Double(rs.getDouble("pct imput.")));
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
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer
                    .append("select \"id Mov.\", \"Membro Cons.\", \"Fornecedor\", \"desc Fornecedor\", \"Tipo Doc.\", \"Nº Doc.\",\"Fonte Financ.\", \"Rubrica\", \"Tipo Mov.\", \"Data Doc\", \"Descrição\", \"pct Iva\",\"Valor\", \"IVA\", \"Total\", \"pct imput.\" from ");
            stringBuffer.append(tableOrView);
            stringBuffer.append(" where PROJECTCODE='");
            stringBuffer.append(projectCode);
            stringBuffer.append("' and \"Rubrica\"='");
            stringBuffer.append(rubric);
            stringBuffer.append("' order by \"Data Doc\", \"id Mov.\"");
            String query = stringBuffer.toString();

            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                IExpensesReportLine report = new ExpensesReportLine();
                report.setProjectCode(projectCode);
                report.setMovementId(rs.getString("id Mov."));
                report.setMember(rs.getString("Membro Cons."));
                report.setSupplier(rs.getString("Fornecedor"));
                report.setSupplierDescription(rs.getString("desc Fornecedor"));
                report.setDocumentType(rs.getString("Tipo Doc."));
                report.setDocumentNumber(rs.getString("Nº Doc."));
                report.setFinancingSource(rs.getString("Fonte Financ."));
                report.setRubric(new Integer(rs.getInt("Rubrica")));
                report.setMovementType(rs.getString("Tipo Mov."));
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = rs.getDate("Data Doc");
                report.setDate(formatter.format(date));
                report.setDescription(rs.getString("Descrição"));
                report.setIvaPercentage(new Double(rs.getDouble("pct Iva")));
                report.setValue(new Double(rs.getDouble("Valor")));
                report.setTax(new Double(rs.getDouble("IVA")));
                report.setTotal(new Double(rs.getDouble("Total")));
                report.setImputedPercentage(new Double(rs.getDouble("pct imput.")));
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

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("select distinct r.COD, r.DESCRICAO from ");
            stringBuffer.append(RubricType.EXPENSES_RUBRIC_TABLE_NAME);
            stringBuffer.append(" r, ");
            stringBuffer.append(getTableOrViewName(p, reportType));
            stringBuffer.append(" p where p.PROJECTCODE='");
            stringBuffer.append(projectCode);
            stringBuffer.append("' and p.\"Rubrica\"=r.COD");
            String query = stringBuffer.toString();

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

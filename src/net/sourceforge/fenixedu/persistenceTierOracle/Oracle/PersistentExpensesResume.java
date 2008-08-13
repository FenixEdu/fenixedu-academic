/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.fenixedu.domain.projectsManagement.AdiantamentosReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.CabimentosReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IAdiantamentosReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ICabimentosReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryEURReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryPTEReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.SummaryEURReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.SummaryPTEReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentExpensesResume;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentExpensesResume extends PersistentReport implements IPersistentExpensesResume {

    public ISummaryPTEReportLine getSummaryPTEReportLine(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
	ISummaryPTEReportLine report = null;
	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	    p.startTransaction();
	    String tableOrView = getTableOrViewName(p, reportType);
	    String query = "select \"RECEITA\", \"DESPESA\", \"IVA\", \"TOTAL\" from " + tableOrView + " where PROJECTCODE='"
		    + projectCode + "'";
	    PreparedStatement stmt = p.prepareStatement(query);
	    ResultSet rs = stmt.executeQuery(query);
	    if (rs.next()) {
		report = new SummaryPTEReportLine();
		report.setProjectCode(projectCode);
		report.setRevenue(new Double(rs.getDouble("RECEITA")));
		report.setExpense(new Double(rs.getDouble("DESPESA")));
		report.setTax(new Double(rs.getDouble("IVA")));
		report.setTotal(new Double(rs.getDouble("TOTAL")));
	    }
	    rs.close();
	    p.commitTransaction();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
	return report;
    }

    public ISummaryEURReportLine getSummaryEURReportLine(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
	ISummaryEURReportLine report = null;
	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	    p.startTransaction();
	    String tableOrView = getTableOrViewName(p, reportType);
	    String query = "select \"RECEITA\", \"DESPESA\", \"IVA\", \"AD_POR_JUST\",  \"TOTAL\" from " + tableOrView
		    + " where PROJECTCODE='" + projectCode + "'";
	    PreparedStatement stmt = p.prepareStatement(query);
	    ResultSet rs = stmt.executeQuery(query);
	    if (rs.next()) {
		report = new SummaryEURReportLine();
		report.setProjectCode(projectCode);
		report.setRevenue(new Double(rs.getDouble("RECEITA")));
		report.setExpense(new Double(rs.getDouble("DESPESA")));
		report.setTax(new Double(rs.getDouble("IVA")));
		report.setAdiantamentosPorJustificar(new Double(rs.getDouble("AD_POR_JUST")));
		report.setTotal(new Double(rs.getDouble("TOTAL")));
	    }
	    rs.close();
	    p.commitTransaction();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
	return report;
    }

    public IAdiantamentosReportLine getAdiantamentosReportLine(ReportType reportType, Integer projectCode)
	    throws ExcepcaoPersistencia {
	IAdiantamentosReportLine report = null;
	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	    p.startTransaction();
	    String tableOrView = getTableOrViewName(p, reportType);
	    StringBuilder stringBuffer = new StringBuilder();
	    stringBuffer.append("select \"ADIANTAMENTOS\", \"JUST_ADIANTAMENTOS\", \"TOTAL\" from ");
	    stringBuffer.append(tableOrView);
	    stringBuffer.append(" where PROJECTCODE='");
	    stringBuffer.append(projectCode);
	    stringBuffer.append("'");
	    String query = stringBuffer.toString();
	    PreparedStatement stmt = p.prepareStatement(query);
	    ResultSet rs = stmt.executeQuery(query);
	    if (rs.next()) {
		report = new AdiantamentosReportLine();
		report.setProjectCode(projectCode);
		report.setAdiantamentos(new Double(rs.getDouble("ADIANTAMENTOS")));
		report.setJustifications(new Double(rs.getDouble("JUST_ADIANTAMENTOS")));
		report.setTotal(new Double(rs.getDouble("TOTAL")));
	    }
	    rs.close();
	    p.commitTransaction();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
	return report;
    }

    public ICabimentosReportLine getCabimentosReportLine(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
	ICabimentosReportLine report = null;
	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	    p.startTransaction();
	    String tableOrView = getTableOrViewName(p, reportType);
	    StringBuilder stringBuffer = new StringBuilder();
	    stringBuffer.append("select \"CABIMENTOS\", \"JUST_CABIMENTOS\", \"TOTAL\" from ");
	    stringBuffer.append(tableOrView);
	    stringBuffer.append(" where PROJECTCODE='");
	    stringBuffer.append(projectCode);
	    stringBuffer.append("'");
	    String query = stringBuffer.toString();
	    PreparedStatement stmt = p.prepareStatement(query);
	    ResultSet rs = stmt.executeQuery(query);
	    if (rs.next()) {
		report = new CabimentosReportLine();
		report.setProjectCode(projectCode);
		report.setCabimentos(new Double(rs.getDouble("CABIMENTOS")));
		report.setJustifications(new Double(rs.getDouble("JUST_CABIMENTOS")));
		report.setTotal(new Double(rs.getDouble("TOTAL")));
	    }
	    rs.close();
	    p.commitTransaction();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
	return report;
    }
}

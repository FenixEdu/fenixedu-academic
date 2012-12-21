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

import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.SummaryReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentSummaryReport extends PersistentReport {

    public List readByCoordinatorCode(ReportType reportType, Integer coordinatorCode, final BackendInstance instance)
	    throws ExcepcaoPersistencia {
	List result = new ArrayList();

	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
	    p.startTransaction();
	    String tableOrView = "V_LST_RESUMOCOORDENADOR";//getTableOrViewName(p, reportType);

	    String query = new String(
		    "select proj, acronimo, proj_ue, proj_tipo, orcbruto, maxfinanc, receita, trf_parceiros, desp_valor, ju_ad_valor , cb_ad_valor from "
			    + tableOrView + " where coord='" + coordinatorCode + "'");
	    PreparedStatement stmt = p.prepareStatement(query);
	    ResultSet rs = stmt.executeQuery(query);
	    while (rs.next()) {
		ISummaryReportLine report = new SummaryReportLine();
		report.setCoordinatorCode(coordinatorCode);
		report.setProjectCode(rs.getString("proj"));
		report.setAcronym(rs.getString("acronimo"));
		report.setExplorationUnit(new Integer(rs.getInt("proj_ue")));
		report.setType(rs.getString("proj_tipo"));
		report.setBudget(new Double(rs.getDouble("orcbruto")));
		Double maxFinance = new Double(rs.getDouble("maxfinanc"));
		report.setMaxFinance(maxFinance);
		Double revenue = new Double(rs.getDouble("receita"));
		report.setRevenue(revenue);
		Double partnersTransfers = new Double(rs.getDouble("trf_parceiros"));
		report.setPartnersTransfers(partnersTransfers);
		Double expense = new Double(rs.getDouble("desp_valor"));
		report.setExpense(expense);
		Double adiantamentosPorJustificar = new Double(rs.getDouble("ju_ad_valor"));
		report.setAdiantamentosPorJustificar(adiantamentosPorJustificar);
		Double cabimentoPorExecutar = new Double(rs.getDouble("cb_ad_valor"));
		report.setCabimentoPorExecutar(cabimentoPorExecutar);
		Double treasuryBalance = revenue - (partnersTransfers + expense + adiantamentosPorJustificar);
		report.setTreasuryBalance(treasuryBalance);
		Double budgetBalance = maxFinance - (expense - cabimentoPorExecutar);
		report.setBudgetBalance(budgetBalance);
		result.add(report);
	    }

	    rs.close();
	    p.commitTransaction();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia(e);
	}
	return result;
    }

    public List readByCoordinatorAndProjectCodes(ReportType reportType, Integer coordinatorCode, List projectCodes,
	    final BackendInstance instance) throws ExcepcaoPersistencia {
	List result = new ArrayList();
	try {
	    PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
	    p.startTransaction();
	    String tableOrView = getTableOrViewName(p, reportType);
	    StringBuilder queryBuffer = new StringBuilder();
	    queryBuffer
		    .append("select distinct \"NºProj\", \"Acrónimo\", \"Unid Expl\", \"Tipo\", \"Orçamento\", \"Máximo Financiável\", \"Receita\", \"Despesa\", \"Adiantamentos por Justificar\" ,\"Saldo Tesouraria\", \"Cabimentos por Executar\", \"Saldo Orçamental\" from ");
	    queryBuffer.append(tableOrView);
	    if (projectCodes != null && projectCodes.size() != 0) {
		queryBuffer.append(" where \"NºProj\" IN (");
		for (int i = 0; i < projectCodes.size(); i++) {
		    if (i != 0)
			queryBuffer.append(", ");
		    queryBuffer.append("'");
		    queryBuffer.append(projectCodes.get(i));
		    queryBuffer.append("'");
		}
		queryBuffer.append(")");
	    } else {
		queryBuffer.append(" where IDCOORD='");
		queryBuffer.append(coordinatorCode);
		queryBuffer.append("'");
	    }
	    queryBuffer.append(" order by \"NºProj\"");
	    String query = queryBuffer.toString();
	    PreparedStatement stmt = p.prepareStatement(query);
	    ResultSet rs = stmt.executeQuery(query);
	    while (rs.next()) {
		ISummaryReportLine report = new SummaryReportLine();
		report.setCoordinatorCode(coordinatorCode);
		report.setProjectCode(rs.getString("NºProj"));
		report.setAcronym(rs.getString("Acrónimo"));
		report.setExplorationUnit(new Integer(rs.getInt("Unid Expl")));
		report.setType(rs.getString("Tipo"));
		report.setBudget(new Double(rs.getDouble("Orçamento")));
		report.setMaxFinance(new Double(rs.getDouble("Máximo Financiável")));
		report.setRevenue(new Double(rs.getDouble("Receita")));
		report.setExpense(new Double(rs.getDouble("Despesa")));
		report.setAdiantamentosPorJustificar(new Double(rs.getDouble("Adiantamentos por Justificar")));
		report.setTreasuryBalance(new Double(rs.getDouble("Saldo Tesouraria")));
		report.setCabimentoPorExecutar(new Double(rs.getDouble("Cabimentos por Executar")));
		report.setBudgetBalance(new Double(rs.getDouble("Saldo Orçamental")));

		result.add(report);
	    }
	    rs.close();
	    p.commitTransaction();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia(e);
	}
	return result;
    }
}

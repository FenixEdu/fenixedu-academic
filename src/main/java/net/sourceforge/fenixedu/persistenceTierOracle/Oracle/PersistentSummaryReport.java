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

    public List<ISummaryReportLine> readByCoordinatorCode(ReportType reportType, Integer coordinatorCode,
            final BackendInstance instance) throws ExcepcaoPersistencia {
        String tableOrView = "V_LST_RESUMOCOORDENADOR";//getTableOrViewName(p, reportType);

        String query =
                new String(
                        "select proj, acronimo, proj_ue, proj_tipo, orcbruto, maxfinanc, receita, trf_parceiros, desp_valor, ju_ad_valor , cb_ad_valor from "
                                + tableOrView + " where coord='" + coordinatorCode + "'");
        List<ISummaryReportLine> result = getQueryResult(coordinatorCode, instance, query);
        return result;
    }

    public List<ISummaryReportLine> readByCoordinatorAndProjectCodes(ReportType reportType, Integer coordinatorCode,
            List<String> projectCodes, final BackendInstance instance) throws ExcepcaoPersistencia {
        String tableOrView = "V_LST_RESUMOCOORDENADOR";//getTableOrViewName(p, reportType);
        StringBuilder queryBuffer = new StringBuilder();
        queryBuffer
                .append("select proj, coord, acronimo, proj_ue, proj_tipo, orcbruto, maxfinanc, receita, trf_parceiros, desp_valor, ju_ad_valor , cb_ad_valor from ");
        queryBuffer.append(tableOrView);
        if (projectCodes != null && projectCodes.size() != 0) {
            queryBuffer.append(" where proj IN (");
            for (int i = 0; i < projectCodes.size(); i++) {
                if (i != 0) {
                    queryBuffer.append(", ");
                }
                queryBuffer.append("'");
                String projectCode = projectCodes.get(i);
//                if (projectCode.matches("[a-zA-Z][a-zA-Z]\\d{1,4}")) {
//                    projectCode = projectCodes.get(i).substring(2);
//                }
                queryBuffer.append(projectCode);
                queryBuffer.append("'");
            }
            queryBuffer.append(")");
        } else {
            queryBuffer.append(" where coord='");
            queryBuffer.append(coordinatorCode);
            queryBuffer.append("'");
        }
        queryBuffer.append(" order by proj");
        String query = queryBuffer.toString();

        List<ISummaryReportLine> result = getQueryResult(coordinatorCode, instance, query);
        return result;
    }

    protected List<ISummaryReportLine> getQueryResult(Integer coordinatorCode, final BackendInstance instance, String query)
            throws ExcepcaoPersistencia {
        List<ISummaryReportLine> result = new ArrayList<ISummaryReportLine>();
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
            p.startTransaction();
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
                Double budgetBalance = maxFinance - (expense + cabimentoPorExecutar);
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
}

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

import net.sourceforge.fenixedu.domain.projectsManagement.IProjectMemberBudget;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectMemberBudget;
import net.sourceforge.fenixedu.domain.projectsManagement.Rubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentProjectMemberBudget extends PersistentReport implements IPersistentReport {

    public List getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        List result = new ArrayList();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);
            String rubricTableOrView = getTableOrViewName(p, ReportType.RUBRIC_BUDGET_MEMBER);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("select \"INSTITUICAO\", \"DESCRICAOINSTITUICAO\", \"TIPO\", \"OVH\", \"GRP\", \"PCTFINANCIAMENTO\" from ");
            stringBuffer.append(tableOrView);
            stringBuffer.append(" where PROJECTO='");
            stringBuffer.append(projectCode);
            stringBuffer.append("'");
            String query = stringBuffer.toString();

            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                IProjectMemberBudget report = new ProjectMemberBudget();
                report.setProjectCode(projectCode);
                report.setInstitutionCode(rs.getString("INSTITUICAO"));
                report.setInstitutionName(rs.getString("DESCRICAOINSTITUICAO"));
                report.setType(rs.getString("TIPO"));
                report.setOverheads(rs.getString("OVH"));
                report.setTransferences(rs.getString("GRP"));
                report.setFinancingPercentage(new Integer(rs.getInt("PCTFINANCIAMENTO")));
                report.setRubricBudget(getRubricBudget(p, rubricTableOrView, projectCode, report.getInstitutionCode()));
                result.add(report);
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }

        return result;
    }

    protected List getRubricBudget(PersistentSuportOracle p, String rubricTableOrView, Integer projectCode, String institutuion)
            throws ExcepcaoPersistencia, SQLException {
        List rubricBudget = new ArrayList();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select \"RUBRICA\", \"DESCRIPTIONRUBRICA\", \"VALOR\" from ");
        stringBuffer.append(rubricTableOrView);
        stringBuffer.append(" where PROJECTO='");
        stringBuffer.append(projectCode);
        stringBuffer.append("' and INSTITUICAO='");
        stringBuffer.append(institutuion);
        stringBuffer.append("'");
        String query = stringBuffer.toString();
        PreparedStatement stmt = p.prepareStatement(query);
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            IRubric rubric = new Rubric();
            rubric.setCode(rs.getString("RUBRICA"));
            rubric.setDescription(rs.getString("DESCRIPTIONRUBRICA"));
            rubric.setValue(new Double(rs.getDouble("VALOR")));
            rubricBudget.add(rubric);
        }
        rs.close();
        return rubricBudget;
    }

}

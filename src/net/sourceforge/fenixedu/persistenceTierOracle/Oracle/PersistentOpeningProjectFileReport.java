/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.IOpeningProjectFileReport;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.OpeningProjectFileReport;
import net.sourceforge.fenixedu.domain.projectsManagement.Rubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentOpeningProjectFileReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentOpeningProjectFileReport extends PersistentReport implements IPersistentOpeningProjectFileReport {

    public IOpeningProjectFileReport getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        IOpeningProjectFileReport report = null;

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer
                    .append("select \"PROJECTO\", \"ACRONIMO\", \"UNIDEXPL\", \"NUMCOORDENADOR\", \"NOMECOORDENADOR\", \"UNIACAD\", \"UNIACADDESCRICAO\", \"CONTACTO\", \"ORIGEM\", \"TIPO\", \"CUSTOS\", \"COORDENACAO\", \"UNIDOPER\", \"UNIDOPERDESCRICAO\", \"MOEDA\", \"NIB\", \"NUMCONTRACTO\", \"OLDPROJ\", \"DG\", \"PROGRAMA\", \"PROGRAMADESCRICAO\", \"INICIO\", \"DURACAO\", \"TITULO\", \"RESUMO\", \"CONTROLOORCAMMEMB\", \"CONTIVA\", \"CONTIVAILEGIVEL\", \"OVHDATA\", \"OVHGESTAO\", \"OVHUNIDEXPL\", \"OVHUNIDACAD\", \"OVHUNIDOPER\", \"OVHCOORD\" from ");
            stringBuffer.append(tableOrView);
            stringBuffer.append(" where PROJECTO='");
            stringBuffer.append(projectCode);
            stringBuffer.append("'");
            String query = stringBuffer.toString();

            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                report = new OpeningProjectFileReport();
                report.setProjectCode(projectCode);
                report.setAcronym(rs.getString("ACRONIMO"));
                report.setExplorationUnit(new Integer(rs.getInt("UNIDEXPL")));
                report.setCoordinatorCode(new Integer(rs.getInt("NUMCOORDENADOR")));
                report.setCoordinatorName(rs.getString("NOMECOORDENADOR"));
                report.setAcademicUnit(rs.getString("UNIACAD"));
                report.setAcademicUnitDescription(rs.getString("UNIACADDESCRICAO"));
                report.setCoordinatorContact(rs.getString("CONTACTO"));
                report.setProjectType(rs.getString("TIPO"));
                report.setProjectOrigin(rs.getString("ORIGEM"));
                report.setCost(rs.getString("CUSTOS"));
                report.setCoordination(rs.getString("COORDENACAO"));
                report.setOperationalUnit(rs.getString("UNIDOPER"));
                report.setOperationalUnitDescription(rs.getString("UNIDOPERDESCRICAO"));
                report.setCurrency(rs.getString("MOEDA"));
                report.setBid(rs.getString("NIB"));
                report.setContractNumber(rs.getString("NUMCONTRACTO"));
                report.setParentProjectCode(rs.getString("OLDPROJ"));
                report.setGeneralDirection(rs.getString("DG"));
                report.setProgram(rs.getString("PROGRAMA"));
                report.setProgramDescription(rs.getString("PROGRAMADESCRICAO"));
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = rs.getDate("INICIO");
                report.setInitialDate(formatter.format(date));
                report.setDuration(new Integer(rs.getInt("DURACAO")));
                report.setTitle(rs.getString("TITULO"));
                report.setSummary(rs.getString("RESUMO"));
                report.setBudgetaryMemberControl(rs.getString("CONTROLOORCAMMEMB"));
                report.setTaxControl(rs.getString("CONTIVA"));
                report.setIlegivelTaxControl(rs.getString("CONTIVAILEGIVEL"));
                date = rs.getDate("OVHDATA");
                report.setOverheadsDate(formatter.format(date));
                report.setManagementUnitOverhead(new Double(rs.getDouble("OVHGESTAO")));
                report.setExplorationUnitOverhead(new Double(rs.getDouble("OVHUNIDEXPL")));
                report.setAcademicUnitOverhead(new Double(rs.getDouble("OVHUNIDACAD")));
                report.setOperationalUnitOverhead(new Double(rs.getDouble("OVHUNIDOPER")));
                report.setCoordinatorOverhead(new Double(rs.getDouble("OVHCOORD")));

            }

            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }

        return report;
    }

    public List getReportRubricList(ReportType reportType, Integer projectCode, boolean getValue) throws ExcepcaoPersistencia {
        List report = null;
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("select \"CODE\", \"DESCRIPTION\"");
            if (getValue)
                stringBuffer.append(", \"VALUE\"");
            stringBuffer.append(" from ");
            stringBuffer.append(tableOrView);
            stringBuffer.append(" where PROJECTCODE='");
            stringBuffer.append(projectCode);
            stringBuffer.append("'");
            String query = stringBuffer.toString();

            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            report = new ArrayList();
            while (rs.next()) {
                IRubric rubric = new Rubric();
                rubric.setCode(rs.getString("CODE"));
                rubric.setDescription(rs.getString("DESCRIPTION"));
                if (getValue)
                    rubric.setValue(new Double(rs.getDouble("VALUE")));
                report.add(rubric);
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }

        return report;
    }
}

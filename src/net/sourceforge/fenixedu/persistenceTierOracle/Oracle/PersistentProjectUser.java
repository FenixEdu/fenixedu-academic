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

import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.Rubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentProjectUser;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentProjectUser implements IPersistentProjectUser {

    public IRubric readProjectCoordinator(Integer userCode) throws ExcepcaoPersistencia {
        StringBuilder queryBuffer = new StringBuilder();
        queryBuffer.append("select NOME, IDCOORD from V_COORD where IDCOORD = '");
        queryBuffer.append(userCode);
        queryBuffer.append("'");
        String query = queryBuffer.toString();
        IRubric result = null;
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();

            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = new Rubric();
                result.setCode(new Integer(rs.getInt("IDCOORD")).toString());
                result.setDescription(rs.getString("NOME"));
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return result;
    }

    public List<IRubric> getInstitucionalProjectCoordId(Integer userCode) throws ExcepcaoPersistencia {
        StringBuilder queryBuffer = new StringBuilder();
        queryBuffer.append("select ID_COORD_CC, CC_NAME from v_responsavel_cc where ID_COORD = '");
        queryBuffer.append(userCode);
        queryBuffer.append("'");
        String query = queryBuffer.toString();
        List<IRubric> result = new ArrayList<IRubric>();
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                IRubric rubric = new Rubric();
                rubric.setCode(new Integer(rs.getInt("ID_COORD_CC")).toString());
                rubric.setDescription(rs.getString("CC_NAME"));
                result.add(rubric);
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return result;
    }

    public List<IRubric> getInstitucionalProjectByCCIDs(List<Integer> ccCodes) throws ExcepcaoPersistencia {
        List<IRubric> result = new ArrayList<IRubric>();

        if (ccCodes != null && ccCodes.size() != 0) {
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append("select distinct(ID_COORD_CC), CC_NAME from v_responsavel_cc where ID_COORD_CC IN (");
            for (int i = 0; i < ccCodes.size(); i++) {
                if (i != 0)
                    stringBuffer.append(", ");
                stringBuffer.append(ccCodes.get(i));
            }
            stringBuffer.append(") order by ID_COORD_CC");
            String query = stringBuffer.toString();

            try {
                PersistentSuportOracle p = PersistentSuportOracle.getInstance();
                p.startTransaction();
                PreparedStatement stmt = p.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    IRubric rubric = new Rubric();
                    rubric.setCode(new Integer(rs.getInt("ID_COORD_CC")).toString());
                    rubric.setDescription(rs.getString("CC_NAME"));
                    result.add(rubric);
                }
                rs.close();
                p.commitTransaction();
            } catch (SQLException e) {
                throw new ExcepcaoPersistencia();
            }
        }
        return result;
    }

    public String getCCNameByCoordinatorAndCC(Integer userNumber, Integer costCenter) throws ExcepcaoPersistencia {
        StringBuilder query = new StringBuilder();
        query.append("select CC_NAME from v_responsavel_cc where ID_COORD_CC = '");
        query.append(costCenter);
        query.append("' and ID_COORD = '");
        query.append(userNumber);
        query.append("'");
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            PreparedStatement stmt = p.prepareStatement(query.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("CC_NAME");
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return null;
    }

    public IRubric getCostCenterByID(Integer costCenter) throws ExcepcaoPersistencia {
        StringBuilder query = new StringBuilder();
        query.append("select CC_NAME from v_responsavel_cc where ID_COORD_CC = '");
        query.append(costCenter);
        query.append("'");
        try {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            p.startTransaction();
            PreparedStatement stmt = p.prepareStatement(query.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                IRubric rubric = new Rubric();
                rubric.setCode(costCenter.toString());
                rubric.setDescription(rs.getString("CC_NAME"));
                return rubric;
            }
            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
        return null;
    }

}
